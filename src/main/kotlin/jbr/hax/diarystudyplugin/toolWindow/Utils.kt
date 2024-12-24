package jbr.hax.diarystudyplugin.toolWindow

import java.awt.Desktop
import javax.swing.JTextPane
import javax.swing.event.HyperlinkEvent

fun createDescriptionPane(): JTextPane {
    return JTextPane().apply {
        isEditable = false
        contentType = "text/html"
        addHyperlinkListener { evt ->
            if (HyperlinkEvent.EventType.ACTIVATED == evt.eventType) {
                Desktop.getDesktop().browse(evt.url.toURI())
            }
        }
    }
}