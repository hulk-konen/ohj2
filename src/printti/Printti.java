package printti;
import java.util.Collection;
import java.util.List;
import java.io.File;

/**
 * printti-luokka, joka huolehtii jäsenistöstä.  Pääosin kaikki metodit
 * ovat vain "välittäjämetodeja" jäsenistöön.
 *
 * @author tohulkko
 * @version 1.0, 24.4.2025
 */
public class Printti {

    private Dates dates = new Dates();
    private Todos todos = new Todos();

    /**
     * Palauttaa printin päivien määrän
     * @return päivien määrä
     */
    public int getDates() {
        return dates.getLkm();
    }

    /**
     * Palauttaa i:n jäsenen
     * @param i monesko jäsen palautetaan
     * @return viite i:teen jäseneen
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public Pvm annaPvm(int i) throws IndexOutOfBoundsException {
        return dates.anna(i);
    }

    /**
     * Haetaan kaikki jäsen harrastukset
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.*;
     *
     *  Printti printti = new Printti();
     *  Pvm pv1 = new Pvm(), pv2 = new Pvm(), pv3 = new Pvm();
     *  pv1.luo();
     *  pv2.luo();
     *  int id1 = pv1.getId();
     *  int id2 = pv2.getId();
     *  Todo pitsi11 = new Todo(id1); printti.lisaa(pitsi11);
     *  Todo pitsi12 = new Todo(id1); printti.lisaa(pitsi12);
     *  Todo pitsi21 = new Todo(id2); printti.lisaa(pitsi21);
     *  Todo pitsi22 = new Todo(id2); printti.lisaa(pitsi22);
     *  Todo pitsi23 = new Todo(id2); printti.lisaa(pitsi23);
     *
     *  List<Todo> loytyneet;
     *  loytyneet = printti.annaTodot(pv3);
     *  loytyneet.size() === 0;
     *  loytyneet = printti.annaTodot(pv1);
     *  loytyneet.size() === 2;
     *  loytyneet.get(0) == pitsi11 === true;
     *  loytyneet.get(1) == pitsi12 === true;
     *  loytyneet = printti.annaTodot(pv2);
     *  loytyneet.size() === 3;
     *  loytyneet.get(0) == pitsi21 === true;
     * </pre>
     */
    public List<Todo> annaTodot(Pvm pvm) {
        return todos.annaTodot(pvm.getId());
    }

    /**
     * Poistaa todon
     * @param todo poistettava todotehtävä
     * @return true jos poisto onnistui, false jos todoa ei löytynyt
     */
    public boolean poistaTodo(Todo todo) {
        return todos.poista(todo);
    }

    /**
     * Lisää Pvm
     * @param pvm lisätttävä pvm
     * <pre name="test">
     * #THROWS SailoException
     * Printti printti = new Printti();
     * Pvm aku1 = new Pvm(), aku2 = new Pvm();
     * printti.getDates() === 0;
     * printti.lisaa(aku1); printti.getDates() === 1;
     * printti.lisaa(aku2); printti.getDates() === 2;
     * printti.lisaa(aku1); printti.getDates() === 3;
     * printti.getDates() === 3;
     * printti.annaPvm(0) === aku1;
     * printti.annaPvm(1) === aku2;
     * printti.annaPvm(2) === aku1;
     * printti.annaPvm(3) === aku1; #THROWS IndexOutOfBoundsException
     * printti.lisaa(aku1); printti.getDates() === 4;
     * printti.lisaa(aku1); printti.getDates() === 5;
     * </pre>
     */
    public void lisaa(Pvm pvm) throws SailoException {
        dates.lisaa(pvm);
    }

    /**
     * Lisää todon
     * @param todo lisättävä
     * @throws SailoException
     */
    public void lisaa(Todo todo) throws SailoException {
        todos.lisaa(todo);
    }

    /**
     * Korvaa jäsenen tietorakenteessa.  Ottaa jäsenen omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva jäsen.  Jos ei löydy,
     * niin lisätään uutena jäsenenä.
     * @param pvm lisätäävän jäsenen viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     */
    public void korvaaTaiLisaa(Pvm pvm) throws SailoException {
        dates.korvaaTaiLisaa(pvm);
    }

    /**
     * Korvaa harrastuksen tietorakenteessa.  Ottaa harrastuksen omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva harrastus.  Jos ei löydy,
     * niin lisätään uutena harrastuksena.
     * @param todo lisärtävän harrastuksen viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     */
    public void korvaaTaiLisaa(Todo todo) throws SailoException {
        todos.korvaaTaiLisaa(todo);
    }

    /**
     * Palauttaa "taulukossa" hakuehtoon vastaavien jäsenten viitteet
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi
     * @return tietorakenteen löytyneistä jäsenistä
     * @throws SailoException Jos jotakin menee väärin
     */
    public Collection<Pvm> etsi(String hakuehto, int k) throws SailoException {
        return dates.etsi(hakuehto, k);
    }

    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        todos.setTiedostonPerusNimi(hakemistonNimi + "todos");
        dates.setTiedostonPerusNimi(hakemistonNimi + "dates");
    }

    /**
     * lukee printin tiedot tiedostosta
     * @throws SailoException jos probleemeja tallteuksen kanssa
     *
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.*;
     * #import java.util.*;
     *
     *  Printti printti = new Printti();
     *
     *  Todos todos = new Todos();
     *  Pvm pvm1 = new Pvm(); pvm1.luo(); pvm1.setPvm("2025-04-23");
     *  Pvm pvm2 = new Pvm(); pvm2.luo(); pvm2.setPvm("2025-04-24");
     *
     * Todo todo = new Todo(pvm1.getId());
     * todo.setTask("Tiskit");
     * todo.setStatus(0);
     * todos.lisaa(todo);
     * Todo todo2 = new Todo(pvm1.getId());
     * todo2.setTask("Pyykit");
     * todo2.setStatus(1);
     * todos.lisaa(todo2);
     * Todo todo3 = new Todo(pvm2.getId());
     * todo3.setTask("Astiat");
     * todo3.setStatus(1);
     * todos.lisaa(todo3);
     *
     *  String directory = "testprintti";
     *  File dir = new File(directory);
     *  File datesFile = new File(directory + "/dates.dat");
     *  File todosFile = new File(directory + "/todos.dat");
     *  dir.mkdir();
     *  datesFile.delete();
     *  todosFile.delete();
     *
     *  printti.lueTiedostosta(directory); #THROWS SailoException
     *  printti.lisaa(pvm1);
     *  printti.lisaa(pvm2);
     *  printti.lisaa(todo);
     *  printti.lisaa(todo2);
     *  printti.lisaa(todo3);
     *  printti.talleta();
     *
     *  printti = new Printti(); // tekee uuden printin
     *  printti.lueTiedostosta(directory); // lukee vanhat
     *
     *  // tarkistaa päivät
     *  List<Pvm> dates = new ArrayList<>();
     *  for (int i = 0; i < printti.getDates(); i++) {
     *      dates.add(printti.annaPvm(i));
     *  }
     *  dates.size() === 2;
     *  dates.get(0).getPvm() === "2025-04-23";
     *  dates.get(1).getPvm() === "2025-04-24";
     *
     *  // tarkistaa todot ekalle päivälle
     *  List<Todo> todosForPvm1 = printti.annaTodot(pvm1);
     *  todosForPvm1.size() === 2;
     *  todosForPvm1.get(0).getTask() === "Tiskit";
     *  todosForPvm1.get(0).getStatus() === 0;
     *  todosForPvm1.get(1).getTask() === "Pyykit";
     *  todosForPvm1.get(1).getStatus() === 1;
     *
     *  // tokalle päivälle
     *  List<Todo> todosForPvm2 = printti.annaTodot(pvm2);
     *  todosForPvm2.size() === 1;
     *  todosForPvm2.get(0).getTask() === "Astiat";
     *  todosForPvm2.get(0).getStatus() === 1;
     *
     *
     *  datesFile.delete() === true;
     *  todosFile.delete() === true;
     *  File datesBackupFile = new File(directory + "/dates.bak");
     *  File todosBackupFile = new File(directory + "/todos.bak");
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        dates = new Dates(); // jos luetaan olemassa olevaan niin helpoin tyhjentää näin
        todos = new Todos();

        setTiedosto(nimi);
        dates.lueTiedostosta();
        todos.lueTiedostosta();
    }


    /**
     * Tallenttaa printin tiedot tiedostoon.
     * Vaikka pvm tallettamien epäonistuisi, niin yritetään silti tallettaa
     * todoita ennen poikkeuksen heittämistä.
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void talleta() throws SailoException {
        String virhe = "";
        try {
            dates.talleta();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }

        try {
            todos.talleta();
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }

    /**
     * Testiohjelma printistä
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Printti printti = new Printti();

        try {
            printti.lueTiedostosta("aamu");

            Pvm pvm1 = new Pvm(), pvm2 = new Pvm();
            pvm1.luo();
            pvm1.vastaaPvm();
            pvm2.luo();
            pvm2.vastaaPvm();

            printti.lisaa(pvm1);
            printti.lisaa(pvm2);
            int id1 = pvm1.getId();
            int id2 = pvm2.getId();
            Todo pitsi11 = new Todo(id1); pitsi11.vastaaPitsinNyplays(id1); printti.lisaa(pitsi11);
            Todo pitsi12 = new Todo(id1); pitsi12.vastaaPitsinNyplays(id1); printti.lisaa(pitsi12);
            Todo pitsi21 = new Todo(id2); pitsi21.vastaaPitsinNyplays(id2); printti.lisaa(pitsi21);
            Todo pitsi22 = new Todo(id2); pitsi22.vastaaPitsinNyplays(id2); printti.lisaa(pitsi22);

            System.out.println("============= Printin testi =================");

            Collection<Pvm> dates = printti.etsi("", -1);
            int i = 0;
            for (Pvm pvm: dates) {

                System.out.println("Pvm paikassa: " + i);
                pvm.tulosta(System.out);
                List<Todo> loytyneet = printti.annaTodot(pvm);
                for (Todo todo : loytyneet)
                    todo.tulosta(System.out);
                i++;
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
