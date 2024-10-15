package org.gnu.itsmoroto.m2bedit

import javafx.scene.input.MouseEvent
import javafx.event.EventHandler
import javafx.geometry.Orientation
import javafx.scene.control.ComboBox
import javafx.scene.layout.GridPane
import javafx.scene.control.SplitPane
import javafx.scene.text.Text
import javafx.stage.Stage
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.stage.FileChooser
import javafx.stage.Modality
import javafx.stage.Window
import org.gnu.itsmoroto.m2bedit.MainView.Companion.editText
import java.io.File

class CodeSelector (fmt: TextPart.OutFormat, owner: Window? = null): Stage () {
    val mContainer = BorderPane ()
    val mMainPanel = SplitPane ()
    val mLeftPanel = SplitPane ()
    val mRightPanel = SplitPane ()
    val mButtonPanel = HBox ()

    //Left
    val textlabel = Text ("textlabel")
    val textpanel = GridPane ()
    val combotextlabel = Text ()
    val combotext = ComboBox<String> ()

    //Right
    val mathlabel = Text ("mathlabel")
    val mathpanel = GridPane ()
    val labelcomboml = Text ()
    val labelcombomc = Text ()
    val comboml = ComboBox<String> ()
    val combomc = ComboBox<String> ()

    //Buttons
    val buttonok = Button ()
    val buttoncancel = Button ()
    private fun  createSpacer(): Node {
        val spacer = Region()
        // Make it always grow or shrink according to the available space
        HBox.setHgrow(spacer, Priority.ALWAYS)
        return spacer
    }

    val mfmt = fmt
    init {
        mMainPanel.orientation = Orientation.HORIZONTAL
        mMainPanel
        mLeftPanel.orientation = Orientation.VERTICAL
        mLeftPanel.padding = Insets (10.0)
        mRightPanel.orientation = Orientation.VERTICAL
        mRightPanel.padding = Insets (10.0)

        fillCombos ()

        //Left panel
        combotextlabel.text = M2BApp.mbrstrings.getString("combotextlabel")
        textpanel.add(combotextlabel, 0, 0)
        textpanel.add(combotext, 1, 0)
        textpanel.padding = Insets (10.0)
        textpanel.hgap = 10.0
        textlabel.text = M2BApp.mbrstrings.getString("textcode")
        textlabel.style = "-fx-font-weight: bold"
        mLeftPanel.items.addAll(textlabel, textpanel)
        mLeftPanel.setDividerPositions(0.1)

        //Right panel
        labelcomboml.text = M2BApp.mbrstrings.getString("combotextlabel")
        labelcombomc.text = M2BApp.mbrstrings.getString("combomathlabel")
        mathpanel.add(labelcomboml, 0, 0)
        mathpanel.add(comboml, 1, 0)
        mathpanel.add(labelcombomc, 0, 1)
        mathpanel.add(combomc, 1, 1)
        mathpanel.padding = Insets (10.0)
        mathpanel.vgap = 20.0
        mathpanel.hgap = 10.0
        mathlabel.text = M2BApp.mbrstrings.getString("mathcode")
        mathlabel.style = "-fx-font-weight: bold"
        mRightPanel.items.addAll(mathlabel, mathpanel)
        mLeftPanel.setDividerPositions(0.1)

        buttonok.text = M2BApp.mbrstrings.getString("oklabel")
        buttoncancel.text = M2BApp.mbrstrings.getString("cancellabel")
        val okhandler = EventHandler<MouseEvent> (
            fun (_: MouseEvent){
                exportAs()
            }
        )
        buttonok.onMouseClicked = okhandler

        val cancelhandler = EventHandler<MouseEvent> (
            fun (_: MouseEvent){
                close()
            }
        )
        buttoncancel.onMouseClicked = cancelhandler
        mButtonPanel.children.add(createSpacer())
        mButtonPanel.children.add(buttonok)
        mButtonPanel.children.add(createSpacer())
        mButtonPanel.children.add(buttoncancel)
        mButtonPanel.children.add(createSpacer())

        mMainPanel.items.addAll(mLeftPanel, mRightPanel)
        mMainPanel.setDividerPositions (0.5)

        mContainer.center = mMainPanel
        mContainer.bottom = mButtonPanel


        scene = Scene (mContainer, 600.0, 200.0)
        initModality(Modality.APPLICATION_MODAL)
        title = M2BApp.mbrstrings.getString("braillecode")
        if (owner != null)
            initOwner(owner)
    }
    private fun fillCombos (){
        var index = 0
        val lang = System.getProperty("user.language")
        val reg = System.getProperty("user.country")
        val loc = "$lang-$reg"
        var selindex = -1
        for (i in LConvert.mtables){
            combotext.items.add(index, i.IndexName)
            if (i.Region in setOf(lang, loc))
                selindex = index
            index++
        }
        if (selindex != -1)
            combotext.selectionModel.select (selindex)

        index = 0
        selindex = -1
        for (i in MCConvert.mlanguages){
            comboml.items.add(index, i)
            if (i == lang)
                selindex = index
            index++
        }
        if (selindex != -1)
            comboml.selectionModel.select (selindex)

        index = 0
        for (i in MCConvert.mcodes){
            combomc.items.add(index, i)
            index++
        }
    }

    private fun warn (message: String){
        val errdlg = Alert (Alert.AlertType.ERROR)
        errdlg.headerText = message
        errdlg.initOwner(mContainer.scene.window)
        errdlg.showAndWait()
    }
    private fun exportAs (){
        val ntable = combotext.selectionModel.selectedIndex
        if (ntable == -1){
            warn (M2BApp.mbrstrings.getString("nolouistable"))
            return
        }
        val ltable = LConvert.mtables[ntable].TablePath
        val mclang = comboml.selectionModel.selectedItem
        if (mclang == null){
            warn(M2BApp.mbrstrings.getString("nomclang"))
            return
        }
        val mccode = combomc.selectionModel.selectedItem
        if (mccode == null){
            warn(M2BApp.mbrstrings.getString("nomccode"))
            return
        }
        val fc = FileChooser ()
        var ext = "*." + mfmt.extension
        var success = true
        fc.extensionFilters.addAll(FileChooser.ExtensionFilter (mfmt.extension, ext))
        if (M2BApp.mcurrdir != "")
            fc.initialDirectory = File (M2BApp.mcurrdir)
        val f = fc.showSaveDialog(mContainer.scene.window)
        if (f != null){
            val tmp = fc.selectedExtensionFilter.extensions[0]
            ext = tmp.substring(tmp.lastIndexOf('.') + 1)
            val fout: File
            if (!f.absolutePath.endsWith(".$ext")) {
                val newfile = f.absolutePath + "." + ext

                fout = File(newfile)

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
                fout = f
            val tmpfile = fout.name + ".tmp"
            val ftmp = File.createTempFile(tmpfile, "tmp")
            M2BApp.mcurrdir = fout.absolutePath.substring(0, fout.absolutePath.lastIndexOf(File.separatorChar))
            val writer: M2BFileWriter = M2BFileWriter (ftmp, mfmt)
            M2BApp.mnodescount = 0
            try {
                val text = TextPart(0, TextPart.TextTypes.Plain.id)
                text.findChilds()
                M2BApp.lConverter.setTable(ltable)
                M2BApp.mcConverter.setCodes(mclang, mccode)
                if (mfmt == TextPart.OutFormat.UTF) {
                    text.b2File<String>(writer::writeString, mfmt)
                } else {
                    text.b2File<ByteArray>(writer::writeBytes, mfmt)
                }
            }
            catch (e: LatexError){
                val errdlg = Alert (Alert.AlertType.ERROR)
                editText.selectRange(e.mnode.mstart, e.mnode.mend)
                errdlg.headerText = e.message
                errdlg.initOwner(editText.scene.window)
                errdlg.showAndWait()
                success = false
            }
            catch (e: UnclosedException){
                val errdlg = Alert (Alert.AlertType.ERROR)
                editText.selectRange(e.where, e.where + e.label.start.length)
                errdlg.headerText = e.message
                errdlg.initOwner(editText.scene.window)
                errdlg.showAndWait()
                success = false
            }
            writer.close()
            if (success){
                ftmp.copyTo(fout, true)
            }
            ftmp.delete()
        }
        else
            return
        close()
    }
}