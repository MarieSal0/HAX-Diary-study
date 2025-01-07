package jbr.hax.diarystudyplugin.toolWindow

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.wm.ToolWindow
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBPanel
import com.intellij.util.ui.FormBuilder
import jbr.hax.diarystudyplugin.notificationCenter.NotificationSettings
import jbr.hax.diarystudyplugin.utils.strTime2Int
import java.awt.*
import javax.swing.*

class SettingsTab(private val toolWindow: ToolWindow) {
    // Tool Window panel
    private var tabPanel: JPanel = JBPanel<JBPanel<*>>(BorderLayout())

    private var reminder1: JTextField = JTextField(NotificationSettings.firstSurveyTime)
    private var reminder2: JTextField = JTextField(NotificationSettings.secondSurveyTime)
    private var reminder3: JTextField = JTextField(NotificationSettings.thirdSurveyTime)
    private var reminderEnd: JTextField = JTextField(NotificationSettings.endOfDayTime)

    init {
        reminder1.columns = 5
        reminder2.columns = 5
        reminder3.columns = 5
        reminderEnd.columns = 5
    }

    fun getContent(): JPanel {
        addSettingsTab()
        return tabPanel
    }


    // Tab close button
    private val closeButton = JButton("x").apply {
        //isBorderPainted = false
        isOpaque = false
        isContentAreaFilled = false
        background = JBColor.PanelBackground.brighter()
        addActionListener {
            // Remove the tab when clicked
            val content = toolWindow.contentManager.getContent(tabPanel)
            if (content != null) {
                toolWindow.contentManager.removeContent(content, true)
            }
        }
    }

    // Settings save button
    private val saveButton = JButton("Save").apply {
        //isBorderPainted = false
        isOpaque = false
        isContentAreaFilled = false
        background = JBColor.PanelBackground.brighter()
        addActionListener {
            val reminders = listOf(reminder1, reminder2, reminder3, reminderEnd).map { Pair(saveReminderSettings(it), it) }
            if (reminders.all { it.first != null }) {
                val timings = reminders.map { Pair(strTime2Int(it.first!!), it.second) }.sortedBy { it.first }
                if (areTimingsValid(timings)) {
                    val properties = PropertiesComponent.getInstance()
                    NotificationSettings.firstSurveyTime = reminders[0].first!!
                    properties.setValue("firstSurveyTime",  NotificationSettings.firstSurveyTime)
                    NotificationSettings.secondSurveyTime = reminders[1].first!!
                    properties.setValue("secondSurveyTime", NotificationSettings.secondSurveyTime)
                    NotificationSettings.thirdSurveyTime = reminders[2].first!!
                    properties.setValue("thirdSurveyTime",  NotificationSettings.thirdSurveyTime)
                    NotificationSettings.endOfDayTime = reminders[3].first!!
                    properties.setValue("endOfDayTime",   NotificationSettings.endOfDayTime)
                    background = JBColor.PanelBackground.brighter()
                } else background = Color.RED
            } else {
                background = Color.RED
            }
        }
    }

    private fun areTimingsValid(timings: List<Pair<Int, JTextField>>): Boolean {
        for (i in 1 until timings.size-1) {
            if (timings[i].first - timings[i - 1].first < 60) {
                timings[i].second.background = Color.RED
                timings[i - 1].second.background = Color.RED
                return false
            }
        }
        // Check if end of workday survey is after the latest during the day survey and at least 30min that
        if (timings[4].first <= timings[3].first && timings[4].first - timings[3].first < 30) {
            timings[4].second.background = Color.RED
            timings[3].second.background = Color.RED
            return false
        }

        return true
    }

    private fun saveReminderSettings(reminder: JTextField): String? {
        val time = reminder.text
        val regex = Regex("^(?:[01]\\d|2[0-3]):[0-5]\\d$")
        if (!regex.matches(time)) {
            reminder.background = Color.RED
            return null
        }
        reminder.background = JBColor.PanelBackground.brighter()
        return time
    }


    private fun reminderRow(labelText: String, reminder: JTextField): JPanel {
        val linePanel = JPanel(FlowLayout(FlowLayout.LEFT))
        linePanel.add(JLabel("\t" + labelText))
        linePanel.add(reminder)
        return linePanel
    }


    private fun addSettingsTab() {
        val buttonPanel = JPanel(BorderLayout())
        buttonPanel.add(closeButton, BorderLayout.EAST)
        buttonPanel.add(saveButton, BorderLayout.WEST)

        val intro1 = JPanel(FlowLayout(FlowLayout.LEFT))
        intro1.add(JLabel("\t" + "Please, use the HH:MM 24h format, e.g. 09:25, 12:40, 18:15."))
        val intro2 = JPanel(FlowLayout(FlowLayout.LEFT))
        intro2.add(JLabel("\t" + "Notifications should be set at least 1h apart."))

        val settingsPanel = FormBuilder.createFormBuilder()
            .setFormLeftIndent(30)
            .addVerticalGap(25)
            .addComponent(intro1, 10)
            .addComponent(intro2, 10)
            .addComponent(reminderRow("\tFirst survey reminder time:\t", reminder1), 10)
            .addComponent(reminderRow("\tSecond survey reminder time:\t", reminder2), 10)
            .addComponent(reminderRow("\tThird survey reminder time:\t", reminder3), 10)
            .addComponent(reminderRow("\tEnd of the day survey reminder time:\t", reminderEnd), 10)
            .addComponentFillVertically(JPanel(), 20)
            .panel

        tabPanel.add(buttonPanel, BorderLayout.NORTH)
        tabPanel.add(settingsPanel, BorderLayout.CENTER)
    }
}