package org.gnu.itsmoroto.m2bedit

import javafx.application.Application
import javafx.application.HostServices
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import java.util.Locale
import java.util.ResourceBundle

class M2BApp : Application() {
    companion object {
        val mbrstrings = ResourceBundle.getBundle("Strings")
        val mappname = mbrstrings.getString("appname")
        val mversion = mbrstrings.getString("version")
        val mlocale = Locale.getDefault()
        var mnodescount = 0
        var mexportednodes = 0
        var mcurrfilename = mbrstrings.getString("newfile")
        private lateinit var mstage: Stage
        fun setTitle (title: String) {
            mstage.title = mappname + " " +
                    mversion + ": " +title

        }
        val mmlConverter: MMLConvert = MMLConvert ()
        val mcConverter: MCConvert = MCConvert ("./MCRules")

        val lConverter: LConvert = LConvert ()
        var mcurrdir: String = ""
        val mIcon: Image = Image (M2BApp.javaClass.getResourceAsStream("m2bicon.png"))
        lateinit var mHostServices: HostServices
    }
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(M2BApp::class.java.getResource("main-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 600.0, 400.0)
        val pane: BorderPane = scene.root as BorderPane
        mHostServices = hostServices
        stage.icons.add(mIcon)
        mstage = stage
        pane.prefHeightProperty().bind(scene.heightProperty())
        pane.prefWidthProperty().bind(scene.widthProperty())
        setTitle(mcurrfilename)
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    //println (File (".").absolutePath)
    Application.launch(M2BApp::class.java)
}