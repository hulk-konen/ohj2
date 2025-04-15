package fxgui;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.PrintStream;
import printti.Printti;
import printti.Pvm;
import printti.Todo;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author tommi
 * @version tyo3
 */
public class guiGUIController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }


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


//===========================================================================================
// Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia

    private Printti printti;
    private Pvm pvmKohdalla;
    private TextArea areaPvm = new TextArea();
   // @FXML private ListChooser<Pvm> chooserDatet;
   // @FXML private ScrollPane panelPvm;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextArea textAreaPrintti;

    private String printinnimi = "aamukahvi";
    @FXML private TextField hakuehto;



    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
     * yksi iso tekstikenttä, johon voidaan tulostaa jäsenten tiedot.
     * Alustetaan myös jäsenlistan kuuntelija
     */
    protected void alusta() {
     //   panelPvm.setContent(areaPvm);
        areaPvm.setFont(new Font("Courier New", 12));
      //  panelPvm.setFitToHeight(true);

  //      chooserDatet.clear();
  //      chooserDatet.addSelectionListener(e -> naytaPvm());
// uusia
        textAreaPrintti.setFont(new Font("Courier New", 12));
        textAreaPrintti.setWrapText(true);
    }


    /**
     * Näyttää listasta valitun jäsenen tiedot, tilapäisesti yhteen isoon edit-kenttään
     */
//    protected void naytaPvm() {
//        pvmKohdalla = chooserDatet.getSelectedObject();
//
//        if (pvmKohdalla == null) return;
//
//        areaPvm.setText("");
//        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(areaPvm)) {
//            pvmKohdalla.tulosta(os);
//        }
//    }




    /**
     * @param  Kerho jota käytetään tässä käyttöliittymässä
     */
    public void setPrintti(Printti printti) {
        this.printti = printti;
    }

    public boolean voikoSulkea() {
        tallenna();
        return true;
    }

    /**
     * Tietojen tallennus
     */
    private void tallenna() {
        Dialogs.showMessageDialog("Tallennetetaan! Mutta ei toimi vielä");
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
        setTitle("Kerho - " + printinnimi);
        String virhe = "Ei osata lukea vielä";  // TODO: tähän oikea tiedoston lukeminen
        // if (virhe != null)
        Dialogs.showMessageDialog(virhe);
    }

    private void setTitle(String title) {
        ModalController.getStage(hakuehto).setTitle(title);
    }


    /**
     * hoitaa päivän vaihdon.
     */
    @FXML
    private void handleDateChange() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate != null) {
            String formattedDate = selectedDate.toString(); // Convert LocalDate to String
            showPrinttiForDate(formattedDate);
        }
    }

    /**
     * Näyttää päivän printin
     * @param date valittu päivä "2025-04-15"
     */
    private void showPrinttiForDate(String date) {
        textAreaPrintti.clear();
        try {
            for (int i = 0; i < printti.getDates(); i++) {
                Pvm pvm = printti.annaDate(i);
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