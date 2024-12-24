package jbr.hax.diarystudyplugin.notificationCenter

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import jbr.hax.diarystudyplugin.utils.strTime2Int
import java.util.*
import kotlin.concurrent.timer


interface SurveyNotifierInterface {

    fun getReminderTimes(): List<Int>

    fun createDialog(project: Project): NotificationDialog

    fun schedule(project: Project) {
        var calendarInstance = Calendar.getInstance()
        calendarInstance.add(Calendar.MINUTE, -4)
        var previousTime = calendarInstance.get(Calendar.HOUR_OF_DAY) * 60 + calendarInstance.get(Calendar.MINUTE)

        // Checking every 5 minutes if the current time matches any survey time
        timer("SurveyNotificationTimer", true, 0L, 1 * 60 * 1000) {
            calendarInstance = Calendar.getInstance()
            val day = calendarInstance.get(Calendar.DAY_OF_WEEK)
            val now = calendarInstance.get(Calendar.HOUR_OF_DAY) * 60 + calendarInstance.get(Calendar.MINUTE)
            if (day != 1 && day != 7) {
                //println("Checking time: ${calendarInstance.get(Calendar.HOUR_OF_DAY)}:${calendarInstance.get(Calendar.MINUTE)}")
                getReminderTimes().forEach { reminderTime ->
                    if (reminderTime in (previousTime + 1)..now) {
                        val nowHours = calendarInstance.get(Calendar.HOUR_OF_DAY)
                        val nowMinutes = calendarInstance.get(Calendar.MINUTE)
                        println("Current time: $nowHours:$nowMinutes. Time to show the notification.")
                        showNotification(project)
                    }
                }
            }
            previousTime = now
        }
    }

    fun showNotification(project: Project) {
        // Ensure the dialog is created and shown on the Event Dispatch Thread (EDT).
        // All UI updates must be done on the EDT to avoid threading issues.
        ApplicationManager.getApplication().invokeLater {
            val dialog = createDialog(project)
            dialog.show()
        }
    }
}

class BaseSurveyNotifier : SurveyNotifierInterface {

    override fun getReminderTimes(): List<Int> {
        return listOf(
            strTime2Int(NotificationSettings.firstSurveyTime),
            strTime2Int(NotificationSettings.secondSurveyTime),
            strTime2Int(NotificationSettings.thirdSurveyTime)
        )
    }

    override fun createDialog(project: Project) = BaseNotificationDialog(project)
}

class DayEndSurveyNotifier : SurveyNotifierInterface {

    override fun getReminderTimes(): List<Int> {
        return listOf(
            strTime2Int(NotificationSettings.endOfDayTime)
        )
    }

    override fun createDialog(project: Project) = DayEndNotificationDialog(project)
}