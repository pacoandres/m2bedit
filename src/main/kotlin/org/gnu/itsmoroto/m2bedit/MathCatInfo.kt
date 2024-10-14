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

class MathCatInfo (owner: Window? = null): Stage() {
    val mContainer = BorderPane ()
    val mMainPanel = SplitPane ()
    val mLeftPanel = SplitPane ()
    val mRightPanel = SplitPane ()
    val mButtonPanel = HBox ()
    //Top
    val abstractlabel = Text ()

    //Left
    val languageslabel = Text ()
    val languagesList = ListView<String> ()

    //Right
    val codingslabel = Text ()
    val codingsList = ListView<String> ()

    //Buttons
    val buttonok = Button ()
    /*enum class TypeformNames (val louisname: String){
        Bold ("bold"),
        Italic ("italic"),
        Underline ("underline")

    }*/
    private fun  createSpacer(): Node {
        val spacer = Region();
        // Make it always grow or shrink according to the available space
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }


    init {
        mMainPanel.orientation = Orientation.HORIZONTAL
        mLeftPanel.orientation = Orientation.VERTICAL
        mLeftPanel.padding = Insets (10.0)
        mRightPanel.orientation = Orientation.VERTICAL
        mRightPanel.padding = Insets (10.0)

        fillList ()

        //Top
        abstractlabel.text = M2BApp.mbrstrings.getString("mcexplain")
        //Left panel
        languageslabel.text = M2BApp.mbrstrings.getString("availablelangs")
        languageslabel.style= "-fx-font-weight: bold";

        //Right panel
        codingslabel.text = M2BApp.mbrstrings.getString("codingslabel")
        codingslabel.style= "-fx-font-weight: bold";

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

        mLeftPanel.items.addAll(languageslabel, languagesList)

        mRightPanel.items.addAll(codingslabel, codingsList)
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
        languagesList.selectionModel.selectionMode = SelectionMode.SINGLE
        codingsList.selectionModel.selectionMode = SelectionMode.SINGLE

        for (l in MCConvert.mlanguages){
            languagesList.items.add(l)
        }

        for (c in MCConvert.mcodes){
            codingsList.items.add(c)
        }
    }



}