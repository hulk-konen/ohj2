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
 * @version 1/28/25
 */
public class listGUIController {
    @FXML
    private Button backGUIButton;

    @FXML
    private Button newEntryButton;

    @FXML
    private void handleBackGUI(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("guiGUIView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) backGUIButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleNewEntry(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("editGUIView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) newEntryButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleEditEntry(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("editGUIView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Button clickedButton = (Button) event.getSource();
        Stage stage = (Stage) clickedButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}