package fxgui;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;

/**
 * Tulostuksen hoitava luokka
 *
 * @author tohulkko
 * @version 14.5.2025
 */
public class TulostusController implements ModalControllerInterface<String> {
    @FXML TextArea tulostusAlue;

    @FXML private void handleOK() {
        ModalController.closeStage(tulostusAlue);
    }

    @FXML private void handleTulosta() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if ( job != null && job.showPrintDialog(null) ) {
            WebEngine webEngine = new WebEngine();
            webEngine.loadContent("<pre>" + tulostusAlue.getText() + "</pre>");
            webEngine.print(job);
            job.endJob();
        }
    }

    @Override
    public String getResult() {
        return null;
    }

    @Override
    public void setDefault(String oletus) {
        tulostusAlue.setText(oletus);
    }


    @Override
    public void handleShown() {
        //
    }

    public TextArea getTextArea() {
        return tulostusAlue;
    }


    public static TulostusController tulosta(String tulostus) {
        TulostusController tulostusCtrl =
                ModalController.showModeless(TulostusController.class.getResource("TulostusView.fxml"),
                        "Tulostus", tulostus);
        return tulostusCtrl;
    }

}
