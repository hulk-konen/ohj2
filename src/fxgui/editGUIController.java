package fxgui;

import fi.jyu.mit.fxgui.Dialogs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import printti.Printti;
import printti.SailoException;
import printti.Todo;

public class editGUIController {

    @FXML
    private TextArea editTodo;

    @FXML
    private CheckBox isDone;

    @FXML
    private Button submitEditButton;

    @FXML
    private Button cancelEditButton;


    private Todo editingTodo;
    private Printti printti;

    /**
     * käynnistetään controller, napit toimintaan
     */
    public void initialize() {
        submitEditButton.setOnAction(event -> handleSubmitEdit());
        cancelEditButton.setOnAction(event -> handleCancelEdit());

    }

    /**
     * Asetetaan printti
     * @param printti nykyinen printti.
     */
    public void setPrintti(Printti printti) {
        this.printti = printti;
    }

    /**
     * hakee todon tiedot
     * @param todo editoitava todo.
     */
    public void setTodoForEditing(Todo todo) {
        this.editingTodo = todo;
        if (todo != null) {
            editTodo.setText(todo.getTask());
            isDone.setSelected(todo.getStatus() == 1);
        }
    }

    /**
     * lähettää editsit eteenpäin
     */
    private void handleSubmitEdit() {
        if (editingTodo == null || printti == null) {
            Dialogs.showMessageDialog("Error: No Todo or Printti available.");
            return;
        }

        editingTodo.setTask(editTodo.getText());
        editingTodo.setStatus(isDone.isSelected() ? 1 : 0);

        try {
            printti.korvaaTaiLisaa(editingTodo);
            Dialogs.showMessageDialog("Todo updated successfully!");
            closeWindow();
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Error saving Todo: " + e.getMessage());
        }
    }

    private void handleCancelEdit() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) submitEditButton.getScene().getWindow();
        stage.close();
    }
}