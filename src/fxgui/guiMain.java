package fxgui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import printti.Printti;

/**
 * @author tommi
 * @version tyo7
 */
public class guiMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("guiGUIView.fxml"));
            final Pane root = ldr.load();
            final guiGUIController guiCtrl = (guiGUIController)ldr.getController();

            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("gui.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Daily Print");


            primaryStage.setOnCloseRequest((event) -> {
                if ( !guiCtrl.voikoSulkea() ) event.consume();
            });

            Printti printti = new Printti();
            guiCtrl.setPrintti(printti);


            primaryStage.show();
            if ( !guiCtrl.avaa() ) Platform.exit();
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