package printti;

import java.util.*;

public class Todos {

    private String                      tiedostonNimi = "";

    /** Taulukko harrastuksista */
    private final Collection<Todo> alkiot        = new ArrayList<Todo>();


    /**
     * Harrastusten alustaminen
     */
    public Todos() {
        // toistaiseksi ei tarvitse tehdä mitään
    }


    /**
     * Lisää uuden harrastuksen tietorakenteeseen.  Ottaa harrastuksen omistukseensa.
     * @param task lisättävä harrastus.  Huom tietorakenne muuttuu omistajaksi
     */
    public void lisaa(Todo task) {
        alkiot.add(task);
    }


    /**
     * Lukee jäsenistön tiedostosta.
     * TODO Kesken.
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedostonNimi = hakemisto + ".task";
        throw new SailoException("Ei osata vielä lukea tiedostoa " + tiedostonNimi);
    }


    /**
     * Tallentaa jäsenistön tiedostoon.
     * TODO Kesken.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        throw new SailoException("Ei osata vielä tallettaa tiedostoa " + tiedostonNimi);
    }


    /**
     * Palauttaa kerhon harrastusten lukumäärän
     * @return harrastusten lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }


    /**
     * Iteraattori kaikkien harrastusten läpikäymiseen
     * @return harrastusiteraattori
     *
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     *
     *  Harrastukset harrasteet = new Harrastukset();
     *  Harrastus pitsi21 = new Harrastus(2); harrasteet.lisaa(pitsi21);
     *  Harrastus pitsi11 = new Harrastus(1); harrasteet.lisaa(pitsi11);
     *  Harrastus pitsi22 = new Harrastus(2); harrasteet.lisaa(pitsi22);
     *  Harrastus pitsi12 = new Harrastus(1); harrasteet.lisaa(pitsi12);
     *  Harrastus pitsi23 = new Harrastus(2); harrasteet.lisaa(pitsi23);
     *
     *  Iterator<Harrastus> i2=harrasteet.iterator();
     *  i2.next() === pitsi21;
     *  i2.next() === pitsi11;
     *  i2.next() === pitsi22;
     *  i2.next() === pitsi12;
     *  i2.next() === pitsi23;
     *  i2.next() === pitsi12;  #THROWS NoSuchElementException
     *
     *  int n = 0;
     *  int jnrot[] = {2,1,2,1,2};
     *
     *  for ( Harrastus har:harrasteet ) {
     *    har.getJasenNro() === jnrot[n]; n++;
     *  }
     *
     *  n === 5;
     *
     * </pre>
     */
    //@Override
    public Iterator<Todo> iterator() {
        return alkiot.iterator();
    }


    /**
     * Haetaan kaikki jäsen harrastukset
     * @param tunnusnro jäsenen tunnusnumero jolle harrastuksia haetaan
     * @return tietorakenne jossa viiteet löydetteyihin harrastuksiin
     * @example
     * <pre name="test">
     * #import java.util.*;
     *
     *  Harrastukset harrasteet = new Harrastukset();
     *  Harrastus pitsi21 = new Harrastus(2); harrasteet.lisaa(pitsi21);
     *  Harrastus pitsi11 = new Harrastus(1); harrasteet.lisaa(pitsi11);
     *  Harrastus pitsi22 = new Harrastus(2); harrasteet.lisaa(pitsi22);
     *  Harrastus pitsi12 = new Harrastus(1); harrasteet.lisaa(pitsi12);
     *  Harrastus pitsi23 = new Harrastus(2); harrasteet.lisaa(pitsi23);
     *  Harrastus pitsi51 = new Harrastus(5); harrasteet.lisaa(pitsi51);
     *
     *  List<Harrastus> loytyneet;
     *  loytyneet = harrasteet.annaHarrastukset(3);
     *  loytyneet.size() === 0;
     *  loytyneet = harrasteet.annaHarrastukset(1);
     *  loytyneet.size() === 2;
     *  loytyneet.get(0) == pitsi11 === true;
     *  loytyneet.get(1) == pitsi12 === true;
     *  loytyneet = harrasteet.annaHarrastukset(5);
     *  loytyneet.size() === 1;
     *  loytyneet.get(0) == pitsi51 === true;
     * </pre>
     */
    public List<Todo> annaTodot(int date) {
        List<Todo> loydetyt = new ArrayList<Todo>();
        for (Todo task : alkiot)
            if (task.getDate() == date) loydetyt.add(task);
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

        System.out.println("============= Harrastukset testi =================");

        List<Todo> harrastukset2 = harrasteet.annaTodot(2);

        for (Todo har : harrastukset2) {
            System.out.print(har.getDate() + " ");
            har.tulosta(System.out);
        }

    }

}


