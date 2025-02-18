package fxgui;

import fi.jyu.mit.fxgui.Dialogs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import java.io.IOException;

/**
 * @author tommi
 * @version tyo3
 */
public class editGUIController {
    @FXML
    private Button backGUIButton;

    @FXML
    private DatePicker datePicker;

    @FXML
    private void handleDateSelection() {
        Dialogs.showMessageDialog("Päivä on jo olemassa");
    }

    @FXML
    private void handleBackGUI(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("listGUIView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) backGUIButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}