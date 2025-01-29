package fxgui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author tommi
 * @version 1/28/25
 */
public class guiMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("ht/fxgui/guiGUIView.fxml"));
            final Pane root = ldr.load();
            //final guiGUIController guiCtrl = (guiGUIController)ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("gui");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args Ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}