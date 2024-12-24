package jbr.hax.diarystudyplugin.notificationCenter

import com.intellij.ide.util.PropertiesComponent

object NotificationSettings {
    var firstSurveyTime: String = "11:00"
    var secondSurveyTime: String = "13:00"
    var thirdSurveyTime: String = "15:00"
    var endOfDayTime: String = "17:00"

    init {
        val properties = PropertiesComponent.getInstance()
        firstSurveyTime = extractSettings(properties, "firstSurveyTime", firstSurveyTime)
        secondSurveyTime = extractSettings(properties, "secondSurveyTime", secondSurveyTime)
        thirdSurveyTime = extractSettings(properties, "thirdSurveyTime", thirdSurveyTime)
        endOfDayTime = extractSettings(properties, "endOfDayTime", endOfDayTime)
    }

    private fun extractSettings(properties: PropertiesComponent, name: String, default: String): String {
        val setting = properties.getValue(name)
        if (setting == null) {
            properties.setValue(name, default)
            return default
        }
        return setting
    }

    fun printSettings(): String {
        return "First Survey: $firstSurveyTime\n" +
                "Second Survey: $secondSurveyTime\n" +
                "Third Survey: $thirdSurveyTime\n" +
                "End of the Day Survey: $endOfDayTime"
    }
}