
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import jbr.hax.diarystudyplugin.notificationCenter.*
import jbr.hax.diarystudyplugin.utils.getOrCreateUserId

class PluginInitializer : ProjectActivity {
    override suspend fun execute(project: Project) {
        // Initialize and log the unique user ID on first project open
        val userId = getOrCreateUserId()
        println("Anonymous User ID initialized: $userId")

        val baseNotifier = BaseSurveyNotifier()
        baseNotifier.schedule(project)

        val dayEndNotifier = DayEndSurveyNotifier()
        dayEndNotifier.schedule(project)
    }
}