    package fxgui;

    import fi.jyu.mit.fxgui.Dialogs;
    import fi.jyu.mit.fxgui.ListChooser;
    import fi.jyu.mit.fxgui.StringGrid;
    import javafx.application.Platform;
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
    import java.util.stream.Collectors;

    import static fxgui.TietueDialogController.getFieldId;


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
        private StringGrid stringGridTodos;

        @FXML private void handleTallenna() {
            tallenna();
        }

        @FXML private void handleAvaa() {
            avaa();
        }

        @FXML private void handleLopeta() {
            tallenna();
            Platform.exit();
        }

        @FXML
        private void handleAddNewTodo() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("addGUIView.fxml"));
                Parent root = loader.load();

                addGUIController controller = loader.getController();
                controller.setPrintti(printti);
                controller.haePvm();

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

        @FXML
        private void handleEditTodo() {
            int selectedRow = stringGridTodos.getRowNr();
            if (selectedRow < 0) {
                Dialogs.showMessageDialog("Select a Todo to edit!");
                return;
            }

            List<Todo> todos = printti.annaTodot(pvmKohdalla);
            if (selectedRow >= todos.size()) {
                Dialogs.showMessageDialog("Invalid Todo selection!");
                return;
            }

            Todo selectedTodo = todos.get(selectedRow);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("editGUIView.fxml"));
                Parent root = loader.load();

                editGUIController controller = loader.getController();
                controller.setPrintti(printti);
                controller.setTodoForEditing(selectedTodo);

                Stage modalStage = new Stage();
                modalStage.initModality(Modality.APPLICATION_MODAL);
                modalStage.initOwner(todoGUIButton.getScene().getWindow());
                modalStage.setScene(new Scene(root));
                modalStage.setTitle("Edit Todo");

                modalStage.showAndWait();

                showTodosForPvm(pvmKohdalla);
            } catch (IOException e) {
                Dialogs.showMessageDialog("Could not Todo: " + e.getMessage());
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


        @FXML
        private void handleHaeTekemattomat() {
            if (printti == null) {
                Dialogs.showMessageDialog("No data available!");
                return;
            }

            // Get all undone Todos for the selected date
            List<Todo> undoneTodos = printti.annaTodot(pvmKohdalla).stream()
                    .filter(todo -> todo.getStatus() == 0) // Status 0 means undone
                    .sorted((t1, t2) -> t1.getTask().compareToIgnoreCase(t2.getTask())) // Sort by task name
                    .collect(Collectors.toList());
            if (undoneTodos.isEmpty()) {
                Dialogs.showMessageDialog("Nice, no undone things!");
                return;
            }

                try {

                // Load the dialog
                FXMLLoader loader = new FXMLLoader(getClass().getResource("undoneTodosView.fxml")); // Ensure this path is correct
                Parent root = loader.load();

                // Set the undone Todos in the dialog controller
                undoneTodosController controller = loader.getController();
                controller.setUndoneTodos(undoneTodos);

                // Show the dialog
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.setTitle("Undone Todos");
                dialogStage.setScene(new Scene(root));
                dialogStage.showAndWait();

            } catch (IOException e) {
                Dialogs.showMessageDialog("Error opening dialog: " + e.getMessage());
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
         * @param pvmStr  date string
         * @return Pvm
         * @throws SailoException jos luonnissa ongelmia
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

            stringGridTodos.initTable(new String[] {"Pvm", "Todo", "Done"});
            stringGridTodos.setEditable(false);
        }

        /**
         * Näyttää listasta valitun päivän tiedot
         */
        protected void naytaPvm() {
            pvmKohdalla = chooserDates.getSelectedObject();

            if (pvmKohdalla == null) return;

            showPrinttiForPvm(pvmKohdalla.getPvm());
            showTodosForPvm(pvmKohdalla);
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
         * Hakee päivämäärien tiedot listaan ja asettaa annetun ID:n aktiiviseksi.
         * @param id päivämäärän ID, joka aktivoidaan haun jälkeen
         */
        protected void hae(int id) {
            chooserDates.clear();

            int index = 0;
            boolean found = false;

            try {
                for (int i = 0; i < printti.getDates(); i++) {
                    Pvm pvm = printti.annaPvm(i);

                    chooserDates.add(pvm.getPvm(), pvm);

                    if (pvm.getId() == id) {
                        index = i;
                        found = true;
                    }
                }

                if (!found) {
                    Dialogs.showMessageDialog("Date with ID " + id + " not found!");
                }

                chooserDates.setSelectedIndex(index);
                naytaPvm();

            } catch (Exception ex) {
                Dialogs.showMessageDialog("Error fetching dates! " + ex.getMessage());
            }
        }

        /**
         * @param printti Printti jota käytetään tässä käyttöliittymässä
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
        private String tallenna() {
            try {
                printti.talleta();
                return null;
            } catch (SailoException e) {
                Dialogs.showMessageDialog("Save problem: " + e.getMessage());
                return e.getMessage();
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
            haePvm();
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
                            printContent.append(todo.getTask()).append(" status: ");
                            printContent.append(todo.getStatus()).append("\n");
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

        private void showTodosForPvm(Pvm pvm) {
            stringGridTodos.clear();

            List<Todo> todos = printti.annaTodot(pvm);
            for (Todo todo : todos) {
                String status = todo.getStatus() == 1 ? "Done" : "Not Done";
                stringGridTodos.add(pvm.getPvm(), todo.getTask(), status);
            }
        }

        private void muokkaa(int k) {
            if ( pvmKohdalla == null ) return;
            try {
                Pvm jasen;
                jasen = TietueDialogController.kysyTietue(null, pvmKohdalla.clone(), k);
                if ( jasen == null ) return;
                printti.korvaaTaiLisaa(jasen);
                hae(jasen.getId());
            } catch (CloneNotSupportedException e) {
                //
            } catch (SailoException e) {
                Dialogs.showMessageDialog(e.getMessage());
            }
        }

    }