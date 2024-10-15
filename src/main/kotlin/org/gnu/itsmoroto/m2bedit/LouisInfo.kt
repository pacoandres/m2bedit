package org.gnu.itsmoroto.m2bedit

import javafx.beans.value.ChangeListener
import javafx.scene.input.MouseEvent
import javafx.event.EventHandler
import javafx.geometry.Orientation
import javafx.scene.control.SplitPane
import javafx.scene.text.Text
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window

class LouisInfo (owner: Window? = null): Stage() {
    val mContainer = BorderPane ()
    val mMainPanel = SplitPane ()
    val mLeftPanel = SplitPane ()
    val mRightPanel = SplitPane ()
    val mButtonPanel = HBox ()
    //Top
    val abstractlabel = Text ()

    //Left
    val tablesList = ListView<String> ()

    //Right
    val desclabel = Text ()
    val description = Text ()
    val typeslabel = Text ()
    val types = Text ()

    //Buttons
    val buttonok = Button ()
    /*enum class TypeformNames (val louisname: String){
        Bold ("bold"),
        Italic ("italic"),
        Underline ("underline")

    }*/
    private fun  createSpacer(): Node {
        val spacer = Region()
        // Make it always grow or shrink according to the available space
        HBox.setHgrow(spacer, Priority.ALWAYS)
        return spacer
    }


    init {
        mMainPanel.orientation = Orientation.HORIZONTAL
        mLeftPanel.orientation = Orientation.VERTICAL
        mLeftPanel.padding = Insets (10.0)
        mRightPanel.orientation = Orientation.VERTICAL
        mRightPanel.padding = Insets (10.0)

        fillList ()

        //Top
        abstractlabel.text = M2BApp.mbrstrings.getString("luoisexplain")
        //Left panel


        //Right panel
        desclabel.text = M2BApp.mbrstrings.getString("description")
        desclabel.style= "-fx-font-weight: bold"

        typeslabel.text = M2BApp.mbrstrings.getString("emphavail")
        typeslabel.style= "-fx-font-weight: bold"

        buttonok.text = M2BApp.mbrstrings.getString("oklabel")

        val okhandler = EventHandler<MouseEvent> (
            fun (_: MouseEvent){
                close ()
            }
        )
        buttonok.onMouseClicked = okhandler


        mButtonPanel.children.add(createSpacer())
        mButtonPanel.children.add(buttonok)
        mButtonPanel.children.add(createSpacer())

        mLeftPanel.items.add(tablesList)

        mRightPanel.items.addAll(desclabel, description, typeslabel, types)
        mMainPanel.items.addAll(mLeftPanel, mRightPanel)
        mMainPanel.setDividerPositions (0.5)

        mContainer.top = abstractlabel
        mContainer.center = mMainPanel
        mContainer.bottom = mButtonPanel


        scene = Scene (mContainer, 950.0, 450.0)
        initModality(Modality.NONE)
        title = M2BApp.mbrstrings.getString("availablelangs")
        if (owner != null)
            initOwner(owner)
    }
    private fun fillList (){
        val lang = System.getProperty("user.language")
        val reg = System.getProperty("user.country")
        val loc = "$lang-$reg"
        var index = -1
        var i = 0
        for (table in LConvert.mtables){
            tablesList.items.add(table.IndexName)
            //If table has no region, region has the locale.
            if (table.Region in setOf(lang, loc))
                index = i
            i++
        }
        tablesList.selectionModel.selectionMode = SelectionMode.SINGLE
        if (index != -1) {
            tablesList.selectionModel.select(index)
            tablesList.scrollTo(index)
            fillInfo(index)
        }

        tablesList.selectionModel.selectedItemProperty().addListener ( ChangeListener<String> (
            fun (_, _, _){
                fillInfo(tablesList.selectionModel.selectedIndex)
            }
            )
        )
    }

    fun fillInfo (index: Int){
        description.text = LConvert.mtables[index].Description
        M2BApp.lConverter.setTable(LConvert.mtables[index].TablePath)
        val typeforms = M2BApp.lConverter.getTypeformNames()
        types.text = ""
        if (typeforms.isEmpty()) {
            types.text = M2BApp.mbrstrings.getString("notypefomrs")
            return
        }
        val typestext = StringBuilder ()
        for (tf in typeforms){
            for (tt in TextPart.TextTypes.entries){
                if (tt.louisname == tf){
                    typestext.append(M2BApp.mbrstrings.getString(tf))
                    typestext.append("\n")
                }
            }
        }
        types.text = typestext.toString()
    }


}