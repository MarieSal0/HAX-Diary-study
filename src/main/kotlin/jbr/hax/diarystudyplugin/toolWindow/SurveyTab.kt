package jbr.hax.diarystudyplugin.toolWindow

import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBPanel
import com.intellij.ui.content.ContentFactory
import com.intellij.ui.jcef.JBCefBrowser
import jbr.hax.diarystudyplugin.utils.getOrCreateUserId
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.util.*
import javax.swing.JButton
import javax.swing.JPanel

fun openSurveyTab(toolWindow: ToolWindow, url: String) {
    val surveyUrl = createSurveyUrl(url)
    val content = ContentFactory.getInstance().createContent(
        SurveyTab(toolWindow).getContent(surveyUrl), "Survey", false
    )
    toolWindow.contentManager.addContent(content)
    toolWindow.show()
    toolWindow.contentManager.setSelectedContent(content)
}

class SurveyTab(private val toolWindow: ToolWindow) {
    // Tool Window panel
    private var tabPanel: JPanel = JBPanel<JBPanel<*>>(BorderLayout())

    fun getContent(url: String): JPanel {
        addBrowserTab(url)
        return tabPanel
    }

    private fun addBrowserTab(url: String) {
        val browser = JBCefBrowser(url)

        // Tab close button
        val closeButton = JButton("x").apply {
            //isBorderPainted = false
            isOpaque = false
            isContentAreaFilled = false
            background = JBColor.PanelBackground.brighter()
            addActionListener {
                // Remove the tab when clicked
                val content = toolWindow.contentManager.getContent(tabPanel)
                if (content != null) {
                    browser.jbCefClient.dispose()
                    toolWindow.contentManager.removeContent(content, true)
                }
            }
        }

        val rightAlignedPanel = JPanel(FlowLayout(FlowLayout.RIGHT))
        rightAlignedPanel.add(closeButton)
        tabPanel.add(rightAlignedPanel, BorderLayout.NORTH)
        tabPanel.add(browser.component, BorderLayout.CENTER)
    }

}

// Function to generate the survey URL with user ID and cache buster
private fun createSurveyUrl(url: String): String {
    val userId = getOrCreateUserId() // Retrieves a consistent unique ID for the user
    val cacheBuster = UUID.randomUUID().toString() // Generates a unique identifier to bypass caching
    return "${url}?user_id=$userId&cache_buster=$cacheBuster"
}