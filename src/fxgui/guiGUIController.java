package fxgui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

/**
 * @author tommi
 * @version tyo3
 */
public class guiGUIController {
    @FXML
    private Button listGUIButton;

    @FXML
    private void openListGUI(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("listGUIView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) listGUIButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}