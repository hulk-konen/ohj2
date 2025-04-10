package printti;
import java.util.List;

/**
 * Kerho-luokka, joka huolehtii jäsenistöstä.  Pääosin kaikki metodit
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
     * Palautaa kerhon jäsenmäärän
     * @return jäsenmäärä
     */
    public int getDates() {
        return dates.getLkm();
    }


    /**
     * Poistaa jäsenistöstä ja harrasteista ne joilla on nro. Kesken.
     * @param nro viitenumero, jonka mukaan poistetaan
     * @return montako jäsentä poistettiin
     */
    public int poista(@SuppressWarnings("unused") int nro) {
        return 0;
    }


    /**
     * <pre name="test">
     * #THROWS SailoException
     * Kerho kerho = new Kerho();
     * Jasen aku1 = new Jasen(), aku2 = new Jasen();
     * aku1.rekisteroi(); aku2.rekisteroi();
     * kerho.getJasenia() === 0;
     * kerho.lisaa(aku1); kerho.getJasenia() === 1;
     * kerho.lisaa(aku2); kerho.getJasenia() === 2;
     * kerho.lisaa(aku1); kerho.getJasenia() === 3;
     * kerho.getJasenia() === 3;
     * kerho.annaJasen(0) === aku1;
     * kerho.annaJasen(1) === aku2;
     * kerho.annaJasen(2) === aku1;
     * kerho.annaJasen(3) === aku1; #THROWS IndexOutOfBoundsException
     * kerho.lisaa(aku1); kerho.getJasenia() === 4;
     * kerho.lisaa(aku1); kerho.getJasenia() === 5;
     * kerho.lisaa(aku1);            #THROWS SailoException
     * </pre>
     */
    public void lisaa(Date date) throws SailoException {
        dates.lisaa(date);
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
    public Date annaDate(int i) throws IndexOutOfBoundsException {
        return dates.anna(i);
    }


    /**
     * Haetaan kaikki jäsen harrastukset
     * <pre name="test">
     * #import java.util.*;
     *
     *  Kerho kerho = new Kerho();
     *  Jasen aku1 = new Jasen(), aku2 = new Jasen(), aku3 = new Jasen();
     *  aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi();
     *  int id1 = aku1.getId();
     *  int id2 = aku2.getId();
     *  Harrastus pitsi11 = new Harrastus(id1); kerho.lisaa(pitsi11);
     *  Harrastus pitsi12 = new Harrastus(id1); kerho.lisaa(pitsi12);
     *  Harrastus pitsi21 = new Harrastus(id2); kerho.lisaa(pitsi21);
     *  Harrastus pitsi22 = new Harrastus(id2); kerho.lisaa(pitsi22);
     *  Harrastus pitsi23 = new Harrastus(id2); kerho.lisaa(pitsi23);
     *
     *  List<Harrastus> loytyneet;
     *  loytyneet = kerho.annaHarrastukset(aku3);
     *  loytyneet.size() === 0;
     *  loytyneet = kerho.annaHarrastukset(aku1);
     *  loytyneet.size() === 2;
     *  loytyneet.get(0) == pitsi11 === true;
     *  loytyneet.get(1) == pitsi12 === true;
     *  loytyneet = kerho.annaHarrastukset(aku2);
     *  loytyneet.size() === 3;
     *  loytyneet.get(0) == pitsi21 === true;
     * </pre>
     */
    public List<Todo> annaTodot(Date date) {
        return todos.annaTodot(date.getId());
    }


    /**
     * Lukee kerhon tiedot tiedostosta
     * @param date jota käyteään lukemisessa
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String date) throws SailoException {
        dates.lueTiedostosta(date);
        todos.lueTiedostosta(date);
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

            Date pvm1 = new Date(), pvm2 = new Date();
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
                Date date = printti.annaDate(i);
                System.out.println("Pvm paikassa: " + i);
                date.tulosta(System.out);
                List<Todo> loytyneet = printti.annaTodot(date);
                for (Todo todo : loytyneet)
                    todo.tulosta(System.out);
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
