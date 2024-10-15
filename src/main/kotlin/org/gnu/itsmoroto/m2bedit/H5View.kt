package org.gnu.itsmoroto.m2bedit

import javafx.beans.value.ChangeListener
import javafx.concurrent.Worker.State

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.web.WebErrorEvent
import javafx.stage.Stage
import javafx.scene.web.WebView
import javafx.stage.Modality
import netscape.javascript.JSObject


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
        //mEngine.isJavaScriptEnabled = false //Commented as I can't find a way for back without javascript
        mEngine.onError = this
        scene = Scene (mPanel, 600.0, 400.0)
        initModality(Modality.APPLICATION_MODAL)
    }

    fun setContent (content: String){
        val html = preface + content + postface
        title = M2BApp.mappname + " " + M2BApp.mbrstrings.getString("preview")
        mEngine.loadContent(html)
    }

    fun setURL (uri: String, mainView: MainView): Boolean{
        title = M2BApp.mappname + " " + "Manual"

        val url = javaClass.getResource(uri)
        if (url == null) {
            //throw Exception ("Can't find manual files")
            return false
        }

        mEngine.loadWorker.stateProperty().addListener(ChangeListener<State>{
            ob, old, new ->
            if (new == State.SUCCEEDED){
                val w = mEngine.executeScript("window") as JSObject
                w.setMember("eopener", Eopener ())
                w.setMember("m2bedit", mainView)
            }
        }) //eopener.open opens url in external browser.
        mEngine.load(url.toExternalForm())
        return true
    }

    override fun handle(error: WebErrorEvent?) {
        if (error == null)
            return
        throw Exception (error.toString())
    }

    fun showHelp (){
        initModality(Modality.NONE)
        show()
    }

}