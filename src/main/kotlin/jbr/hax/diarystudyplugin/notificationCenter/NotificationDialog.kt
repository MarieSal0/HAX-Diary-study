package jbr.hax.diarystudyplugin.notificationCenter

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.dsl.builder.*
import jbr.hax.diarystudyplugin.toolWindow.openSurveyTab
import jbr.hax.diarystudyplugin.utils.*
import java.awt.event.ActionEvent
import javax.swing.Action
import javax.swing.JComponent
import java.util.Timer
import java.util.TimerTask

abstract class NotificationDialog(protected val project: Project): DialogWrapper(project) {

    protected abstract val message: String
    protected abstract val url: String

    protected abstract fun showReminder()

    init {
        title = "Survey Reminder"
    }

    override fun createCenterPanel(): JComponent? {
        return panel {
            row {
                label(message)
            }
        }
    }

    // Define the actions (buttons) that appear in the dialog
    override fun createActions(): Array<Action> {
        return arrayOf(
            // Action to open the survey in an embedded browser
            object : DialogWrapperAction("Open Survey") {
                override fun doAction(e: ActionEvent?) {
                    openSurvey()
                    close(OK_EXIT_CODE)
                }
            },
            // Action to remind the user in 10 minutes
            object : DialogWrapperAction("Remind Later") {
                override fun doAction(e: ActionEvent?) {
                    scheduleReminder()
                    close(CANCEL_EXIT_CODE)
                }
            }
        )
    }

    // Open the survey in an embedded browser tool window
    private fun openSurvey() {
        val toolWindow = ToolWindowManager.getInstance(project).getToolWindow(PLUGIN_NAME)
        if (toolWindow == null) {
            thisLogger().error("No tool window found with ID: $PLUGIN_NAME")
            return
        }
        toolWindow.show(null)
        openSurveyTab(toolWindow, url)
    }

    // Reschedule the notification
    private fun scheduleReminder() {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                // Ensure the notification is shown on the Event Dispatch Thread (EDT)
                ApplicationManager.getApplication().invokeLater {
                    showReminder()
                }
            }
        }, 10 * 60 * 1000)  // Delay set to 10 minutes (10 * 60 * 1000 milliseconds)
    }
}

class BaseNotificationDialog(project: Project): NotificationDialog(project) {
    override val message = "Please take a moment to complete the survey."
    override val url = BASE_SURVEY_URL

    init {
        init()
        super.init()
    }

    override fun showReminder() {
        BaseSurveyNotifier().showNotification(project)
    }
}

class DayEndNotificationDialog(project: Project): NotificationDialog(project) {
    override val message = "Please take a moment to complete the survey."
    override val url = DAY_END_SURVEY_URL

    init {
        init()
        super.init()
    }

    override fun showReminder() {
        DayEndSurveyNotifier().showNotification(project)
    }
}