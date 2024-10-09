package org.gnu.itsmoroto.m2bedit

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.web.WebErrorEvent
import javafx.stage.Stage
import javafx.scene.web.WebView
import javafx.stage.Modality
import org.gnu.itsmoroto.m2bedit.M2BApp.Companion.mbrstrings
import java.io.File
import java.net.URI

class H5View (): Stage (), EventHandler<WebErrorEvent>{
    val mPanel = BorderPane ()
    val mWView = WebView ()
    val mEngine = mWView.engine
    val preface = "<html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"\" xml:lang=\"\">" +
            "<head><meta charset=\"utf-8\" /></head><body>"
    val postface = "</body></html>"
    init {

        mPanel.style = "-fx-padding: 10;"
        mPanel.center = mWView
        mEngine.isJavaScriptEnabled = false
        mEngine.onError = this
        scene = Scene (mPanel, 600.0, 400.0)
        initModality(Modality.APPLICATION_MODAL)
    }

    fun setContent (content: String){
        val html = preface + content + postface
        title = M2BApp.mappname + " " + M2BApp.mbrstrings.getString("preview")
        mEngine.loadContent(html)
    }

    fun setURL (uri: String): Boolean{
        title = M2BApp.mappname + " " + "Manual"

        val url = javaClass.getResource(uri)
        if (url == null) {
            //throw Exception ("Can't find manual files")
            return false
        }
        mEngine.load(url.toExternalForm())
        return true
    }

    override fun handle(error: WebErrorEvent?) {
        if (error == null)
            return
        throw Exception (error.toString())
    }

}