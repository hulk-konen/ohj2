package fxgui;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import printti.Todo;

import java.util.List;

/**
 * undone todos lista controlleri
 */
public class undoneTodosController {

    @FXML
    private ListView<String> undoneTodosListView;

    /**
     * Listviewiin undonet
     * @param undoneTodos List of undone Todos.
     */
    public void setUndoneTodos(List<Todo> undoneTodos) {
        for (Todo todo : undoneTodos) {
            undoneTodosListView.getItems().add(todo.getTask());
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) undoneTodosListView.getScene().getWindow();
        stage.close();
    }
}