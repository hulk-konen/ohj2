package printti;

/**
 * Printin todot, osaa mm. lisätä uuden todon
 *
 * @author tohulkko
 * @version 1.0, 11.04.2025
 */

import java.util.*;

public class Todos implements Iterable<Todo>{

    private String                      tiedostonNimi = "";

    /** Taulukko todoista */
    private final Collection<Todo> alkiot        = new ArrayList<Todo>();


    /**
     * Todon alustaminen
     */
    public Todos() {
        // toistaiseksi ei tarvitse tehdä mitään
    }


    /**
     * Lisää uuden todon tietorakenteeseen.  Ottaa todon omistukseensa.
     * @param task lisättävä todo.  Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Todo task) {
        alkiot.add(task);
    }


    /**
     * Lukee todot tiedostosta.
     * TODO Kesken.
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedostonNimi = hakemisto + ".task";
        throw new SailoException("Ei osata vielä lukea tiedostoa " + tiedostonNimi);
    }


    /**
     * Tallentaa todot tiedostoon.
     * TODO Kesken.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        throw new SailoException("Ei osata vielä tallettaa tiedostoa " + tiedostonNimi);
    }


    /**
     * Palauttaa printin todoitten lukumäärän
     * @return todoitten lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }


    /**
     * Iteraattori kaikkien todoitten läpikäymiseen
     * @return todo -iteraattori
     *
     * @exampleitera
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     *
     *  Todos todot = new Todos();
     *  Todo todo21 = new Todo(2); todot.lisaa(todo21);
     *  Todo todo11 = new Todo(1); todot.lisaa(todo11);
     *  Todo todo22 = new Todo(2); todot.lisaa(todo22);
     *  Todo todo12 = new Todo(1); todot.lisaa(todo12);
     *  Todo todo23 = new Todo(2); todot.lisaa(todo23);
     *
     *  Iterator<Todo> i2=todot.iterator();
     *  i2.next() === todo21;
     *  i2.next() === todo11;
     *  i2.next() === todo22;
     *  i2.next() === todo12;
     *  i2.next() === todo23;
     *  i2.next() === todo12;  #THROWS NoSuchElementException
     *
     *  int n = 0;
     *  int ids[] = {2,1,2,1,2};
     *
     * for (Todo task:todot) {
     * task.getId() === ids[n];
     * n++;
    }
     * assert n == ids.length : "Käsittelemättmiä todo, tarkasta";
     * n === 5;
     *
     * </pre>
     */
    //@Override
    public Iterator<Todo> iterator() {
        return alkiot.iterator();
    }


    /**
     * Haetaan kaikki päivän todot
     * @param date päivän id jolle todoita haetaan
     * @return tietorakenne jossa viiteet löydetteyihin todoihin
     * @example
     * <pre name="test">
     * #import java.util.*;
     *
     *  Todos todot = new Todos();
     *  Todo pitsi21 = new Todo(2); todot.lisaa(pitsi21);
     *  Todo pitsi11 = new Todo(1); todot.lisaa(pitsi11);
     *  Todo pitsi22 = new Todo(2); todot.lisaa(pitsi22);
     *  Todo pitsi12 = new Todo(1); todot.lisaa(pitsi12);
     *  Todo pitsi23 = new Todo(2); todot.lisaa(pitsi23);
     *  Todo pitsi51 = new Todo(5); todot.lisaa(pitsi51);
     *
     *  List<Todo> loytyneet;
     *  loytyneet = todot.annaTodot(3);
     *  loytyneet.size() === 0;
     *  loytyneet = todot.annaTodot(1);
     *  loytyneet.size() === 2;
     *  loytyneet.get(0) == pitsi11 === true;
     *  loytyneet.get(1) == pitsi12 === true;
     *  loytyneet = todot.annaTodot(5);
     *  loytyneet.size() === 1;
     *  loytyneet.get(0) == pitsi51 === true;
     * </pre>
     */
//    public List<Todo> annaTodot(int date) {
//        List<Todo> loydetyt = new ArrayList<Todo>();
//        for (Todo task : alkiot)
//            if (task.getPvm() == date) loydetyt.add(task);
//        return loydetyt;
//    }

    // Add this to your code temporarily
    public List<Todo> annaTodot(int date) {
        List<Todo> loydetyt = new ArrayList<Todo>();
        System.out.println("Looking for date: " + date);
        System.out.println("Collection size: " + alkiot.size());

        for (Todo task : alkiot) {
            System.out.println("Todo date: " + task.getDate() + ", id: " + task.getId());
            if (task.getDate() == date) {
                System.out.println("Match found!");
                loydetyt.add(task);
            }
        }

        System.out.println("Found " + loydetyt.size() + " matches");
        return loydetyt;
    }


    /**
     * Testiohjelma harrastuksille
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Todos harrasteet = new Todos();
        Todo pitsi1 = new Todo();
        pitsi1.rekisteroi();
        pitsi1.vastaaPitsinNyplays(2);
        Todo pitsi2 = new Todo();
        pitsi2.rekisteroi();
        pitsi2.vastaaPitsinNyplays(1);
        Todo pitsi3 = new Todo();
        pitsi3.rekisteroi();
        pitsi3.vastaaPitsinNyplays(2);
        Todo pitsi4 = new Todo();
        pitsi4.rekisteroi();
        pitsi4.vastaaPitsinNyplays(2);

        harrasteet.lisaa(pitsi1);
        harrasteet.lisaa(pitsi2);
        harrasteet.lisaa(pitsi3);
        harrasteet.lisaa(pitsi2);
        harrasteet.lisaa(pitsi4);

        System.out.println("============= Todot testi =================");

        List<Todo> harrastukset2 = harrasteet.annaTodot(1);

        for (Todo har : harrastukset2) {
            System.out.print(har.getDate() + " ");
            har.tulosta(System.out);
        }

    }

}


