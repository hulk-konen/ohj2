package fxgui;

import fi.jyu.mit.fxgui.Dialogs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import printti.Printti;
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
    private DatePicker datePickerAdd;

    @FXML
    private TextArea newTodo;

    @FXML
    private CheckBox isDone;

    private Printti printti;

    public void initialize() {
        submitNewButton.setOnAction(event -> handleSubmitNew());
    }

    @FXML
    private void handleDateSelection() {
        Dialogs.showMessageDialog("Päivä on jo olemassa");
    }

    @FXML
    private void handleBackGUI(ActionEvent event) throws IOException {
        Stage stage = (Stage) goListButton.getScene().getWindow();
        stage.close();
    }

    private void handleSubmitNew() {
        Dialogs.showMessageDialog("WIP");
    }

    public void setPrintti(Printti printti) {
        this.printti = printti;
    }
}