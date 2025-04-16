package printti;
import java.util.List;

/**
 * printti-luokka, joka huolehtii jäsenistöstä.  Pääosin kaikki metodit
 * ovat vain "välittäjämetodeja" jäsenistöön.
 *
 * @author Vesa Lappalainen
 * @version 1.0, 09.02.2003
 * @version 1.1, 23.02.2003
 * @version 1.2, 07.01.2008 / testit
 * @version 1.3, 03.03.2013 / Harrastukset
 */
public class Printti {

    private final Dates dates = new Dates();
    private final Todos todos = new Todos();

    /**
     * Palautaa printin pvm määrän
     * @return jäsenmäärä
     */
    public int getDates() {
        return dates.getLkm();
    }


    /**
     * Poistaa dates ja todos ne joilla on nro. Kesken.
     * @param nro viitenumero, jonka mukaan poistetaan
     * @return montako datea poistettiin
     */
    public int poista(@SuppressWarnings("unused") int nro) {
        return 0;
    }


    /**
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
     * printti.lisaa(aku1);            #THROWS SailoException
     * </pre>
     */
    public void lisaa(Pvm pvm) throws SailoException {
        dates.lisaa(pvm);
    }

    public void lisaa(Todo todo) throws SailoException {
        todos.lisaa(todo);
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
     *  Pvm aku1 = new Pvm(), aku2 = new Pvm(), aku3 = new Pvm();
     *  int id1 = aku1.getId();
     *  int id2 = aku2.getId();
     *  Todo pitsi11 = new Todo(id1); printti.lisaa(pitsi11);
     *  Todo pitsi12 = new Todo(id1); printti.lisaa(pitsi12);
     *  Todo pitsi21 = new Todo(id2); printti.lisaa(pitsi21);
     *  Todo pitsi22 = new Todo(id2); printti.lisaa(pitsi22);
     *  Todo pitsi23 = new Todo(id2); printti.lisaa(pitsi23);
     *
     *  List<Todo> loytyneet;
     *  loytyneet = printti.annaTodot(aku3);
     *  loytyneet.size() === 0;
     *  loytyneet = printti.annaTodot(aku1);
     *  loytyneet.size() === 2;
     *  loytyneet.get(0) == pitsi11 === true;
     *  loytyneet.get(1) == pitsi12 === true;
     *  loytyneet = printti.annaTodot(aku2);
     *  loytyneet.size() === 3;
     *  loytyneet.get(0) == pitsi21 === true;
     * </pre>
     */
    public List<Todo> annaTodot(Pvm pvm) {
        return todos.annaTodot(pvm.getId());
    }


    /**
     * Lukee kerhon tiedot tiedostosta
     * @param pvm jota käyteään lukemisessa
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String pvm) throws SailoException {
        dates.lueTiedostosta(pvm);
        todos.lueTiedostosta(pvm);
    }


    /**
     * Tallettaa kerhon tiedot tiedostoon
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void talleta() throws SailoException {
        dates.talleta();
        todos.talleta();
    }


    /**
     * Testiohjelma kerhosta
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Printti printti = new Printti();

        try {
            // kerho.lueTiedostosta("kelmit");

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

            System.out.println("============= Kerhon testi =================");

            for (int i = 0; i < printti.getDates(); i++) {
                Pvm pvm = printti.annaPvm(i);
                System.out.println("Pvm paikassa: " + i);
                pvm.tulosta(System.out);
                List<Todo> loytyneet = printti.annaTodot(pvm);
                for (Todo todo : loytyneet)
                    todo.tulosta(System.out);
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
