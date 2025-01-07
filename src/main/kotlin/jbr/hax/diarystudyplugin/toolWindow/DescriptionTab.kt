package jbr.hax.diarystudyplugin.toolWindow

import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.content.ContentFactory
import com.intellij.util.ui.FormBuilder
import jbr.hax.diarystudyplugin.utils.*
import java.awt.Font
import javax.swing.*

/**
 * This class stores the main panel and the UI of the "Parameters" tool window tab.
 */
class DescriptionTab(private val toolWindow: ToolWindow) {

    private val panelTitle = JPanel()
    /*@JvmField
    val pluginIcon = IconLoader.getIcon("/META-INF/pluginIcon.svg", javaClass)
    private val iconTitle = JLabel(IconLoader.getIcon("/META-INF/pluginIcon.svg", javaClass))*/
    private val textTitle = JLabel(PLUGIN_NAME)

    private val diaryStudyDescription = createDescriptionPane()

    private val baseSurveyDescription = createDescriptionPane()

    private val dayEndSurveyDescription = createDescriptionPane()

    private val settingsDescription = createDescriptionPane()

    // Link to base survey
    private val baseSurveyButton = JButton("During the day Survey")
    // Link to day end survey
    private val dayEndSurveyButton = JButton("End of the day Survey")
    // Link to settings
    private val settingsButton = JButton("Settings")


    // Tool Window panel
    private var toolWindowPanel: JPanel = JPanel()
    private var tabbedPane: JBTabbedPane = JBTabbedPane()

    init {
        textTitle.font = Font("Monochrome", Font.BOLD, 20)

        panelTitle.setLayout(BoxLayout(panelTitle, BoxLayout.X_AXIS))
        //panelTitle.add(iconTitle)
        panelTitle.add(textTitle)

        // Create the main panel and set the font of the title
        toolWindowPanel = createToolWindowPanel()

        // base survey button setup
        baseSurveyButton.isOpaque = false
        baseSurveyButton.isContentAreaFilled = false
        baseSurveyButton.addActionListener {
            openSurveyTab(toolWindow, BASE_SURVEY_URL)
        }

        // day end survey button setup
        dayEndSurveyButton.isOpaque = false
        dayEndSurveyButton.isContentAreaFilled = false
        dayEndSurveyButton.addActionListener {
            openSurveyTab(toolWindow, DAY_END_SURVEY_URL)
        }

        // settings button setup
        settingsButton.isOpaque = false
        settingsButton.isContentAreaFilled = false
        settingsButton.addActionListener {
            val content = ContentFactory.getInstance().createContent(
                SettingsTab(toolWindow).getContent(), "Settings", false
            )
            toolWindow.contentManager.addContent(content)
            toolWindow.show()
            toolWindow.contentManager.setSelectedContent(content)
        }

        // plugin description setup
        diaryStudyDescription.isOpaque = false
        diaryStudyDescription.text = getCommonDescriptionText(getContent().preferredSize.width)

        // base survey description setup
        baseSurveyDescription.isOpaque = false
        baseSurveyDescription.text = getBaseSurveyDescriptionText(getContent().preferredSize.width)

        // day end survey description setup
        dayEndSurveyDescription.isOpaque = false
        dayEndSurveyDescription.text = getDayEndSurveyDescriptionText(getContent().preferredSize.width)

        // settings description setup
        settingsDescription.isOpaque = false
        settingsDescription.text = getSettingsDescriptionText(getContent().preferredSize.width)
    }

    /**
     * Creates the entire tool window panel.
     */
    private fun createToolWindowPanel() = FormBuilder.createFormBuilder()
        // Add indentations from the left border and between the lines, and add title
        .setFormLeftIndent(30)
        .addVerticalGap(25)
        .addComponent(panelTitle)
        .addComponent(tabbedPane)
        .addComponent(diaryStudyDescription, 10)
        .addComponent(settingsDescription, 10)
        .addComponent(settingsButton, 10)
        .addComponent(baseSurveyDescription, 20)
        .addComponent(baseSurveyButton, 10)
        .addComponent(dayEndSurveyDescription, 20)
        .addComponent(dayEndSurveyButton, 10)
        // Add the main panel
        .addComponentFillVertically(JPanel(), 20)
        .panel

    /**
     * Gets the panel that is the main wrapper component of the tool window.
     * The panel is put into a scroll pane so that all the parameters can fit.
     *
     * @return the created tool window pane wrapped into a scroll pane
     */
    fun getContent(): JComponent {
        return JBScrollPane(toolWindowPanel)
    }

    /**
     * Returns the common description text for DiaryStudy plugin.
     *
     * @param width The width used to set the style of the HTML body.
     * @return The common description text formatted as HTML.
     */
    private fun getCommonDescriptionText(width: Int): String {
        return "<html><body style='width: ${(1.25 * width).toInt()} px;'><font face=Monochrome>" +
                "Welcome and thank you for participating in our study!<br><br></font></body></html>"
    }

    /**
     * Returns the description text for the base survey.
     *
     * @param width The width of the text container.
     * @return The formatted HTML description text for LLM-based test generation.
     */
    private fun getBaseSurveyDescriptionText(width: Int): String {
        return "<html><body style='width: ${(0.65 * width).toInt()} px;'><font face=Monochrome>" +
                "<strong>During the day survey</strong><br><br>" +
                "The plugin will remind you to fill in the survey 3 times during the day.<br>" +
                "However, you can take it in any time you prefer by clicking the button below.</font></body></html>"
    }

    /**
     * Returns the description text for the day end survey.
     *
     * @param width The width of the text container.
     * @return The formatted HTML description text for LLM-based test generation.
     */
    private fun getDayEndSurveyDescriptionText(width: Int): String {
        return "<html><body style='width: ${(0.65 * width).toInt()} px;'><font face=Monochrome>" +
                "<strong>End of the day survey</strong><br><br>" +
                "The plugin will remind you to fill in the end of the day survey at the set time.<br>" +
                "However, you can take it any time you prefer by clicking the button below.</font></body></html>"
    }

    /**
     * Returns the description text for the settings.
     *
     * @param width The width of the text container.
     * @return The formatted HTML description text for LLM-based test generation.
     */
    private fun getSettingsDescriptionText(width: Int): String {
        return "<html><body style='width: ${(0.65 * width).toInt()} px;'><font face=Monochrome>" +
                "<strong>Set up the notifications</strong><br><br>" +
                "You can change your reminder times to whenever you prefer.</font></body></html>"
    }
}