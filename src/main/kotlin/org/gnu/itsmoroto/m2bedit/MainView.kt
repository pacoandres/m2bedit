package org.gnu.itsmoroto.m2bedit

import javafx.fxml.FXML

import javafx.scene.control.TextArea
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.MenuItem
import javafx.scene.layout.BorderPane
import javafx.stage.FileChooser


import javafx.stage.Modality
import javafx.stage.Stage
import java.io.File

class MainView {
    /*@FXML
    private lateinit var editText: TextArea
    @FXML
    private lateinit var scrollArea: ScrollPane*/
    @FXML private lateinit var container: BorderPane
    @FXML private lateinit var filemenu: MenuItem
    @FXML private lateinit var helpmenu: MenuItem
    @FXML private lateinit var aboutitem: MenuItem

    @FXML private lateinit var exportmenu: MenuItem
    @FXML private lateinit var utfitem: MenuItem
    @FXML private lateinit var brfitem: MenuItem
    @FXML private lateinit var banitem: MenuItem
    @FXML private lateinit var viewmenu: MenuItem
    @FXML private lateinit var previewitem: MenuItem
    @FXML private lateinit var saveitem: MenuItem
    @FXML private lateinit var saveasitem: MenuItem
    @FXML private lateinit var newitem: MenuItem
    @FXML private lateinit var openitem: MenuItem
    private var mnewfile: Boolean = true

    companion object {
        val editText: TextArea = TextArea()
    }
    //val editText: TextFlow = TextFlow ()

    @FXML
    fun initialize (){
        /*val panel: BorderPane = scrollArea.parent as BorderPane
        scrollArea.prefWidthProperty().bind(panel.widthProperty())
        scrollArea.prefWidthProperty().bind(panel.heightProperty())
        editText.prefHeightProperty().bind(scrollArea.heightProperty())
        editText.prefWidthProperty().bind(scrollArea.widthProperty())*/
        /*val scene: Scene = container.parent as Scene
        container.prefWidthProperty().bind(scene.widthProperty())
        container.prefHeightProperty().bind(scene.heightProperty())*/
        editText.isWrapText = true
        container.center = editText
        //editText.text = "En un lugar de la mancha\nde cuyo nombre"
        //editText.selectRange(18, 35)
        editText.prefHeightProperty().bind(container.heightProperty())
        editText.prefWidthProperty().bind(container.widthProperty())
        filemenu.text = M2BApp.mbrstrings.getString("file")
        helpmenu.text = M2BApp.mbrstrings.getString("help")
        aboutitem.text = M2BApp.mbrstrings.getString("about")
        exportmenu.text = M2BApp.mbrstrings.getString("export")
        brfitem.text = M2BApp.mbrstrings.getString("brftext")
        banitem.text = M2BApp.mbrstrings.getString("bantext")
        viewmenu.text = M2BApp.mbrstrings.getString("see")
        previewitem.text = M2BApp.mbrstrings.getString("preview")
        saveitem.text = M2BApp.mbrstrings.getString("save")
        saveasitem.text = M2BApp.mbrstrings.getString("saveas")
        newitem.text = M2BApp.mbrstrings.getString("new")
        openitem.text = M2BApp.mbrstrings.getString("open")
    }

    @FXML
    private fun onAbout (){
        /*editText.text = "Esto es una \\textbf{prueba sencilla} para empezar.\n" +
                "Con salto de línea y \$ecuacion\$"
        val test = TextPart (0, TextPart.TextTypes.Plain.id)
        test.findChilds()
        test.traceContent()*/
        val stage = Stage()
        val panel = BorderPane ()
        val scene = Scene(panel, 300.0, 200.0)
        val aboutText= TextArea ()
        panel.center = aboutText
        aboutText.isEditable = false
        //BorderPane.setMargin(aboutText, Insets (20.0,20.0,20.0,20.0))
        panel.style = "-fx-padding: 10;"
        aboutText.text = M2BApp.mbrstrings.getString("abouttext")
        stage.scene = scene
        stage.title = M2BApp.mbrstrings.getString("about")
        stage.icons.add(M2BApp.mIcon)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.show();
    }

    private fun exportAs (fmt: TextPart.OutFormat){
        val cd = CodeSelector (fmt, editText.scene.window)
        cd.showAndWait()
    }
    @FXML private fun onUTF (){
        exportAs (TextPart.OutFormat.UTF)
    }

    @FXML private fun onBRF (){
        exportAs(TextPart.OutFormat.BRF)
    }

    @FXML private fun onBAN (){
        exportAs(TextPart.OutFormat.BAN)
    }

    @FXML private fun onPreview (){
        val preview = H5View ()
        /*val s = "<math display=\"inline\" xmlns=\"http://www.w3.org/1998/Math/MathML\">"+
        "<mrow><mi>x</mi><mo>=</mo><mfrac displaystyle=\"true\"><mrow><mo>−</mo><mi>b</mi>"+
        "<mo>±</mo><msqrt><mrow><msup><mi>b</mi><mn>2</mn></msup><mo>−</mo><mn>4</mn>" +
        "<mi>a</mi><mi>c</mi></mrow></msqrt></mrow><mrow><mn>2</mn><mi>a</mi>" +
                "</mrow></mfrac></mrow></math>"*/
        val text = TextPart(0, TextPart.TextTypes.Plain.id)
        TextPart.htmlstring.clear()
        try {
            text.findChilds()
            text.toHtml()
            preview.setContent(TextPart.htmlstring.toString())
            preview.initOwner(editText.scene.window)
            preview.show()
        }
        catch (e: LatexError){
            val errdlg = Alert (Alert.AlertType.ERROR)
            editText.selectRange(e.mnode.mstart, e.mnode.mend)
            errdlg.headerText = e.message
            errdlg.initOwner(editText.scene.window)
            errdlg.showAndWait()
        }
        catch (e: UnclosedException){
            val errdlg = Alert (Alert.AlertType.ERROR)
            editText.selectRange(e.where, e.where + e.label.start.length)
            errdlg.headerText = e.message
            errdlg.initOwner(editText.scene.window)
            errdlg.showAndWait()
        }
    }
    fun onSave (){
        if (mnewfile)
            onSaveAs()
        else
            FileManager.saveFile(File (M2BApp.mcurrfilename))
    }
    fun onSaveAs (){
        val fc = FileChooser ()
        var ext = "*.m2b"
        fc.extensionFilters.addAll(FileChooser.ExtensionFilter (
            M2BApp.mbrstrings.getString("m2bfile"), ext))
        if (M2BApp.mcurrdir != "")
            fc.initialDirectory = File (M2BApp.mcurrdir)
        val f = fc.showSaveDialog(editText.scene.window)
        if (f != null) {
            val tmp = fc.selectedExtensionFilter.extensions[0]
            ext = tmp.substring(tmp.lastIndexOf('.') + 1)
            val newfile: String
            val fout: File
            if (!f.absolutePath.endsWith("." + ext)) {
                newfile = f.absolutePath + "." + ext
                fout = File (newfile)
                if (fout.exists()) {
                    val owdlg = Alert(Alert.AlertType.CONFIRMATION)
                    owdlg.title = M2BApp.mbrstrings.getString("overwrite") + " " +
                            fout.name
                    owdlg.headerText = owdlg.title
                    owdlg.contentText = M2BApp.mbrstrings.getString("overwritequestion")
                    owdlg.initOwner(editText.scene.window)
                    val res = owdlg.showAndWait().get()
                    if (res != ButtonType.OK)
                        return
                }
            }
            else
                fout =f

            FileManager.saveFile(fout)
            setFileInfo(fout)
        }
    }

    fun onNew () {
        val owdlg = Alert(Alert.AlertType.CONFIRMATION)
        owdlg.title = M2BApp.mbrstrings.getString("newfile")
        owdlg.headerText = owdlg.title
        owdlg.contentText = M2BApp.mbrstrings.getString("newfilequestion")
        owdlg.initOwner(editText.scene.window)
        val res = owdlg.showAndWait().get()
        if (res != ButtonType.OK)
            return
        M2BApp.mcurrfilename = M2BApp.mbrstrings.getString("newfile")
        mnewfile = true
        editText.clear()
    }
    fun onOpen (){
        val fc = FileChooser ()
        var ext = "*.m2b"
        fc.extensionFilters.addAll(FileChooser.ExtensionFilter (
            M2BApp.mbrstrings.getString("m2bfile"), ext))
        if (M2BApp.mcurrdir != "")
            fc.initialDirectory = File (M2BApp.mcurrdir)
        val f = fc.showOpenDialog(editText.scene.window)
        if (f != null){
            if (FileManager.readFile(f))
                setFileInfo(f)
            else {
                val fc = Alert (Alert.AlertType.ERROR)
                fc.headerText =M2BApp.mbrstrings.getString("wrongfileformat")
                fc.initOwner(editText.scene.window)
                fc.showAndWait()
            }
        }
    }
    private fun setFileInfo (f: File){
        M2BApp.mcurrfilename = f.absolutePath
        M2BApp.mcurrdir = f.absolutePath.substring(0, f.absolutePath.lastIndexOf(File.separatorChar))
        M2BApp.setTitle(f.name.substring(0, f.name.lastIndexOf('.')))
        mnewfile = false
    }

    fun onManual (){
        /*For changing java default language
            -Duser.language=.... in the VM options.
         */
        val preview = H5View ()
        if (!preview.setURL("/help/" + M2BApp.mlocale.language + "/index.html"))
            preview.setURL("/help/es/index.html")
        preview.initOwner(editText.scene.window)
        preview.show()
    }
}