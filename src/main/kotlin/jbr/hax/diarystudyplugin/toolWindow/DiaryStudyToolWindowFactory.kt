package jbr.hax.diarystudyplugin.toolWindow

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory

/**
 * This class is responsible for creating the tabs and the UI of the DiaryStudy tool window.
 */
class DiaryStudyToolWindowFactory : ToolWindowFactory {

    /**
     * Initialises the UI of the tool window.
     */
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val descriptionTab = DescriptionTab(toolWindow)
        val contentFactory: ContentFactory = ContentFactory.getInstance()
        val content: Content = contentFactory.createContent(descriptionTab.getContent(), "Description", false)
        toolWindow.contentManager.addContent(content)
    }
}