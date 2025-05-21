package printti;

/**
 * Printin todot, osaa mm. lisätä uuden todon
 *
 * @author tohulkko
 * @version 1.0, 11.04.2025
 */

import java.io.*;
import java.util.*;


public class Todos implements Iterable<Todo>{
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "";

    /** Taulukko todoista */
    private final List<Todo> alkiot = new ArrayList<Todo>();

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
        muutettu = true;
    }

    /**
     * Korvaa todon tietorakenteessa.  Ottaa todon omistukseensa.
     * Etsitään samalla id:lla oleva todo.  Jos ei löydy,
     * niin lisätään uutena todona.
     * @param todo lisättävän todon viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     */
    public void korvaaTaiLisaa(Todo todo) throws SailoException {
        int id = todo.getId();
        for (int i = 0; i < getLkm(); i++) {
            if (alkiot.get(i).getId() == id) {
                alkiot.set(i, todo);
                muutettu = true;
                return;
            }
        }
        lisaa(todo);
    }

    /**
     * Lukee harrastukset tiedostosta.
     * @param tied tiedoston nimen alkuosa
     * @throws SailoException jos lukeminen epäonnistuu
     *
     * @example
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Todo todo = new Todo();
                todo.parse(rivi); // voisi olla virhekäsittely
                lisaa(todo);
            }
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }


    /*
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }

    /**
     * Tallentaa todot tiedostoon.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); //  if ... System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Todo todo : this) {
                fo.println(todo.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }


    /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param tied tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tied) {
        tiedostonPerusNimi = tied;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
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
     * task.getDate() === ids[n];
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
    public List<Todo> annaTodot(int date) {
        List<Todo> loydetyt = new ArrayList<Todo>();

        for (Todo task : alkiot) {
            if (task.getDate() == date) {
                loydetyt.add(task);
            }
        }
        return loydetyt;
    }

    /**
     * Poistaa  todon
     * @param todo poistettava todo
     * @return true jos poisto onnistui, false jos todoa ei löytynyt
     */
    public boolean poista(Todo todo) {
        for (int i = 0; i < alkiot.size(); i++) {
            if (alkiot.get(i).getId() == todo.getId()) {
                alkiot.remove(i);
                muutettu = true;
                return true;
            }
        }
        return false;
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

        List<Todo> harrastukset2 = harrasteet.annaTodot(2);

        for (Todo har : harrastukset2) {
            System.out.print(har.getDate() + " ");
            har.tulosta(System.out);
        }
    }
}


