package fxgui;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import printti.Printti;
import printti.Pvm;
import printti.SailoException;
import printti.Todo;


import java.io.IOException;

/**
 * Uusien todoitten lisäys
 * Updated to support modal behavior.
 * @author tohulkko
 * @version tyo5
 */
public class addGUIController {
    @FXML
    private Button goListButton;

    @FXML
    private Button submitNewButton;

    @FXML
    private TextArea newTodo;

    @FXML
    private CheckBox isDone;

    private int checkStatus = 0;

    @FXML
    private ListChooser<Pvm> chooserDatesTodo;

    private Printti printti;

    public void initialize() {
        chooserDatesTodo.clear();
        if (printti != null) {
            haePvm();
        }
        submitNewButton.setOnAction(event -> handleSubmitNew());
    }

    protected void haePvm() {
        chooserDatesTodo.clear();
        for (int i = 0; i < printti.getDates(); i++) {
            Pvm pvm = printti.annaPvm(i);
            chooserDatesTodo.add(pvm.getPvm(), pvm);
        }
    }

    @FXML
    private void handleDateSelection() {
        Pvm selectedPvm = chooserDatesTodo.getSelectedObject();
    }

    @FXML
    private void handleBackGUI(ActionEvent event) throws IOException {
        Stage stage = (Stage) goListButton.getScene().getWindow();
        stage.close();
    }

    private void handleSubmitNew() {
        if (isDone.isSelected()) {
            checkStatus = 1;
        } else if (!isDone.isSelected()) {
            checkStatus = 0;
        }

        Pvm selectedPvm = chooserDatesTodo.getSelectedObject();
        if (selectedPvm == null) {
            Dialogs.showMessageDialog("Please select a date.");
            return;
        }
        Todo uusi = new Todo(selectedPvm.getId());
        uusi.rekisteroi();
        uusi.setTask(newTodo.getText());
        uusi.setStatus(checkStatus);

        try {
            printti.lisaa(uusi);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Error adding new Todo: " + e.getMessage());
        }
        Dialogs.showMessageDialog("New Todo added!");
    }

    public void setPrintti(Printti printti) {
        this.printti = printti;
    }
}