package fxgui;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import printti.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * printin päänökymän kontrolleri. varmaan tulee sisältmään muutakin
 *
 * @author tommi
 * @version vaihe 5.1
 */
public class guiGUIController implements Initializable {

    private Printti printti;
    private Pvm pvmKohdalla;
    private TextArea areaPvm = new TextArea();
    @FXML
    private TextArea textAreaPrintti;

    private String printinnimi = "aamu";

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }

    @FXML
    private Button todoGUIButton;

    @FXML
    private ListChooser<Pvm> chooserDates;


    @FXML
    private void handleAddNewTodo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addGUIView.fxml"));
            Parent root = loader.load();

            addGUIController controller = loader.getController();
            controller.setPrintti(printti);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(todoGUIButton.getScene().getWindow());
            modalStage.setScene(new Scene(root));
            modalStage.setTitle("Add New Todo");

            modalStage.showAndWait();

            haePvm();
        } catch (IOException e) {
            Dialogs.showMessageDialog("Ei voitu avata: " + e.getMessage());
        }
    }

    /**
     * hoitelee uuden Pvm luomisen
     */
    @FXML
    private void handleAddNewPvm() {
        String pvmStr = PvmDialogController.kysyPvm(null, LocalDate.now().toString());

        if (pvmStr == null) return;

        if (!isValidDateFormat(pvmStr)) {
            Dialogs.showMessageDialog("Use yyyy-mm-dd format, thank you!");
            return;
        }

        try {
            boolean isNewDate = !pvmExists(pvmStr);
            Pvm pvm = findOrCreatePvm(pvmStr);
            haePvm();
            showPrinttiForPvm(pvm.getPvm());

            if (isNewDate) {
                Dialogs.showMessageDialog("New date created: " + pvmStr);
            } else {
                Dialogs.showMessageDialog("Date already exists: " + pvmStr);
            }
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Error with date: " + e.getMessage());
        }
    }

    /**
     * tarkastaa onko pvm olemassa
     * @param pvmStr pvm string
     * @return true jos on olemassa
     */
    private boolean pvmExists(String pvmStr) {
        for (int i = 0; i < printti.getDates(); i++) {
            Pvm pvm = printti.annaPvm(i);
            if (pvm.getPvm().equals(pvmStr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * avustaa uuden Pvm tekemisessä. palauttaa tai luo uuden
     * @param pvmStr The date string
     * @return The Pvm object for the date
     * @throws SailoException if there's an error creating the date
     */
    private Pvm findOrCreatePvm(String pvmStr) throws SailoException {
        for (int i = 0; i < printti.getDates(); i++) {
            Pvm pvm = printti.annaPvm(i);
            if (pvm.getPvm().equals(pvmStr)) {
                return pvm;
            }
        }

        Pvm pvm = new Pvm();
        pvm.luo();
        pvm.setPvm(pvmStr);

        printti.lisaa(pvm);
        return pvm;
    }

    /**
     * tarkistaa yyyy-mm-dd formaatin
     * @param pvmStr validoitava
     * @return totta jos totta
     */
    private boolean isValidDateFormat(String pvmStr) {
        try {
            LocalDate.parse(pvmStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

//===========================================================================================
// Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia

    /**
     * Tekee tarvittavat alustukset
     */
    protected void alusta() {
        areaPvm.setFont(new Font("Courier New", 12));

        chooserDates.clear();
        chooserDates.addSelectionListener(e -> naytaPvm());

        textAreaPrintti.setFont(new Font("Courier New", 12));
        textAreaPrintti.setWrapText(true);
    }

    /**
     * Näyttää listasta valitun päivän tiedot
     */
    protected void naytaPvm() {
        pvmKohdalla = chooserDates.getSelectedObject();

        if (pvmKohdalla == null) return;

        showPrinttiForPvm(pvmKohdalla.getPvm());
    }

    /**
     * Näyttää annetun päivämäärän tiedot
     * @param pvm näytettävä päivämäärä
     */
    protected void naytaPvm(Pvm pvm) {
        if (pvm == null) return;
        showPrinttiForPvm(pvm.getPvm());
    }

    /**
     * Hakee päivämäärät listaan
     */
    protected void haePvm() {
        chooserDates.clear();

        for (int i = 0; i < printti.getDates(); i++) {
            Pvm pvm = printti.annaPvm(i);
            chooserDates.add(pvm.getPvm(), pvm);
        }
    }

    /**
     * @param printti Printti jota käytetään tässä käyttöliittymässä
     */
    public void setPrintti(Printti printti) {
        this.printti = printti;
        haePvm();
    }

    public boolean voikoSulkea() {
        tallenna();
        return true;
    }

    /**
     * Tietojen tallennus
     */
    private void tallenna() {
        try {
            printti.talleta();
            Dialogs.showMessageDialog("Saved!");
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Save problem: " + e.getMessage());
        }
    }

    /**
     * Kysytään tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
     */
    public boolean avaa() {
        String uusinimi = PrintinNimiController.kysyNimi(null, printinnimi);
        if (uusinimi == null) return false;
        lueTiedosto(uusinimi);
        return true;
    }

    /**
     * Alustaa kerhon lukemalla sen valitun nimisestä tiedostosta
     * @param nimi tiedosto josta kerhon tiedot luetaan
     */
    protected void lueTiedosto(String nimi) {
        printinnimi = nimi;
        setTitle("Printti - " + printinnimi);

        try {
            printti.lueTiedostosta(nimi);
            haePvm();
            Dialogs.showMessageDialog("Data loaded!");
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Loading error: " + e.getMessage());
        }
    }

    private void setTitle(String title) {
        Stage stage = (Stage) textAreaPrintti.getScene().getWindow();
        if (stage != null) stage.setTitle(title);
    }

    /**
     * Näyttää päivän printin
     * @param date valittu päivä "YYY-MM-DD"
     */
    private void showPrinttiForPvm(String date) {
        textAreaPrintti.clear();
        try {
            for (int i = 0; i < printti.getDates(); i++) {
                Pvm pvm = printti.annaPvm(i);
                if (pvm.getPvm().equals(date)) {
                    List<Todo> todos = printti.annaTodot(pvm);
                    StringBuilder printContent = new StringBuilder("Daily print " + date + "\nTodos:\n");
                    for (Todo todo : todos) {
                        printContent.append(todo.toString()).append("\n");
                    }
                    textAreaPrintti.setText(printContent.toString());
                    return;
                }
            }
            textAreaPrintti.setText("No todos found for " + date);
        } catch (Exception e) {
            textAreaPrintti.setText("Error retrieving data: " + e.getMessage());
        }
    }
}