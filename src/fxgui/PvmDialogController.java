package fxgui;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * PVm lisäys dialogi
 * @author tommi
 * @version tyo3
 */
public class PvmDialogController implements ModalControllerInterface<String> {

    @FXML private TextField dateField;
    private String pvmValue = null;

    @FXML
    private void handleOK() {
        pvmValue = dateField.getText();
        ModalController.closeStage(dateField);
    }

    @FXML
    private void handleCancel() {
        pvmValue = null;
        ModalController.closeStage(dateField);
    }

    @Override
    public String getResult() {
        return pvmValue;
    }

    @Override
    public void handleShown() {
        dateField.requestFocus();
    }

    @Override
    public void setDefault(String oletus) {
        dateField.setText(oletus);
    }

    /**
     * Näyttää pvm kyselyn
     * @param modalityStage modaali joka omistaa tämän kyselyn
     * @param oletus pvm oletus
     * @return pvm syötetty
     */
    public static String kysyPvm(Stage modalityStage, String oletus) {
        return ModalController.showModal(
                PvmDialogController.class.getResource("PvmDialogView.fxml"),
                "Add New Date",
                modalityStage, oletus);
    }
}