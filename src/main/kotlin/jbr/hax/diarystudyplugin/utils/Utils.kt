package jbr.hax.diarystudyplugin.utils

import com.intellij.ide.util.PropertiesComponent
import java.util.*

const val BASE_SURVEY_URL = "https://ubc.ca1.qualtrics.com/jfe/form/SV_cSXEE46kgQuaUzc"
const val DAY_END_SURVEY_URL = "https://ubc.ca1.qualtrics.com/jfe/form/SV_0HY2OaTvb3IPKQu"
const val PLUGIN_NAME = "Diary Study"

// Function to retrieve or create a unique identifier for the user to track survey responses consistently
fun getOrCreateUserId(): String {
    // Use PropertiesComponent to store persistent properties across sessions
    val properties = PropertiesComponent.getInstance()
    var userId = properties.getValue("uniqueUserId")
    if (userId == null) {
        userId = UUID.randomUUID().toString()
        properties.setValue("uniqueUserId", userId)
    }
    return userId
}

fun strTime2Int(time: String): Int {
    val parts = time.split(":")
    return parts[0].toInt() * 60 + parts[1].toInt()
}
