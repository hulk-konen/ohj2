package printti;

import java.util.ArrayList;
import java.util.Collection;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Printin päiväykset, osaa mm. lisätä uuden päivän
 *
 * @author tohulkko
 * @version 1.0, 11.04.2025
 */
public class Dates implements Iterable<Pvm> {
    private static final int MAX_JASENIA   = 10;
    private int              lkm           = 0;
    private String           tiedostonNimi = "";
    private Pvm             alkiot[]      = new Pvm[MAX_JASENIA];

    private boolean muutettu = true;
    private String kokoNimi = "";
    private String tiedostonPerusNimi = "dates";



    /**
     * Oletusmuodostaja
     */
    public Dates() {
        // Attribuuttien oma alustus riittää
    }


    /**
     * Lisää uuden päivän tietorakenteeseen.  Ottaa päivän omistukseensa.
     * @param pvm uuden päivän viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Dates dates = new Dates();
     * Pvm pvm1 = new Pvm(), pvm2 = new Pvm();
     * dates.getLkm() === 0;
     * dates.lisaa(pvm1); dates.getLkm() === 1;
     * dates.lisaa(pvm2); dates.getLkm() === 2;
     * dates.lisaa(pvm1); dates.getLkm() === 3;
     * dates.anna(0) === pvm1;
     * dates.anna(1) === pvm2;
     * dates.anna(2) === pvm1;
     * dates.anna(1) == pvm1 === false;
     * dates.anna(1) == pvm2 === true;
     * dates.anna(3) === pvm1; #THROWS IndexOutOfBoundsException
     * dates.lisaa(pvm1); dates.getLkm() === 4;
     * dates.lisaa(pvm1); dates.getLkm() === 5;
     * dates.lisaa(pvm1); #THROWS SailoException
     * </pre>
     */
    public void lisaa(Pvm pvm) throws SailoException {
        if (lkm >= alkiot.length) throw new SailoException("Liikaa alkioita");
        alkiot[lkm] = pvm;
        lkm++;
        muutettu = true;
    }


    /**
     * Palauttaa viitteen i:teen jäseneen.
     * @param i monennenko jäsenen viite halutaan
     * @return viite jäseneen, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public Pvm anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }

    /**
     * Tallentaa harrastukset tiedostoon.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); //  if ... System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Pvm pvm : this) {
                fo.println(pvm.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }



    /**
     * Lukee päivät tiedostosta.  Kesken.
     * @throws SailoException jos lukeminen epäonnistuu
     */
    /*
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }

    /**
     * Lukee harrastukset tiedostosta.
     * @param tied tiedoston nimen alkuosa
     * @throws SailoException jos lukeminen epäonnistuu
     *
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.File;
     *  Harrastukset harrasteet = new Harrastukset();
     *  Harrastus pitsi21 = new Harrastus(); pitsi21.vastaaPitsinNyplays(2);
     *  Harrastus pitsi11 = new Harrastus(); pitsi11.vastaaPitsinNyplays(1);
     *  Harrastus pitsi22 = new Harrastus(); pitsi22.vastaaPitsinNyplays(2);
     *  Harrastus pitsi12 = new Harrastus(); pitsi12.vastaaPitsinNyplays(1);
     *  Harrastus pitsi23 = new Harrastus(); pitsi23.vastaaPitsinNyplays(2);
     *  String tiedNimi = "testikelmit";
     *  File ftied = new File(tiedNimi+".dat");
     *  ftied.delete();
     *  harrasteet.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  harrasteet.lisaa(pitsi21);
     *  harrasteet.lisaa(pitsi11);
     *  harrasteet.lisaa(pitsi22);
     *  harrasteet.lisaa(pitsi12);
     *  harrasteet.lisaa(pitsi23);
     *  harrasteet.tallenna();
     *  harrasteet = new Harrastukset();
     *  harrasteet.lueTiedostosta(tiedNimi);
     *  Iterator<Harrastus> i = harrasteet.iterator();
     *  i.next().toString() === pitsi21.toString();
     *  i.next().toString() === pitsi11.toString();
     *  i.next().toString() === pitsi22.toString();
     *  i.next().toString() === pitsi12.toString();
     *  i.next().toString() === pitsi23.toString();
     *  i.hasNext() === false;
     *  harrasteet.lisaa(pitsi23);
     *  harrasteet.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Pvm pvm = new Pvm();
                pvm.parse(rivi); // voisi olla virhekäsittely
                lisaa(pvm);
            }
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
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
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }



    /**
     * Tallentaa päivät tiedostoon.  Kesken.
     * @throws SailoException jos talletus epäonnistuu
     */
//    public void talleta() throws SailoException {
//        throw new SailoException("Ei osata vielä tallettaa tiedostoa " + tiedostonNimi);
//    }


    /**
     * Palauttaa printin päivien lukumäärän
     * @return päivien lukumäärä
     */
    public int getLkm() {
        return lkm;
    }

    /**
     * Palauttaa "taulukossa" hakuehtoon vastaavien jäsenten viitteet
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi
     * @return tietorakenteen löytyneistä jäsenistä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     *   Jasenet jasenet = new Jasenet();
     *   Jasen jasen1 = new Jasen(); jasen1.parse("1|Ankka Aku|030201-115H|Paratiisitie 13|");
     *   Jasen jasen2 = new Jasen(); jasen2.parse("2|Ankka Tupu||030552-123B|");
     *   Jasen jasen3 = new Jasen(); jasen3.parse("3|Susi Sepe|121237-121V||131313|Perämetsä");
     *   Jasen jasen4 = new Jasen(); jasen4.parse("4|Ankka Iines|030245-115V|Ankkakuja 9");
     *   Jasen jasen5 = new Jasen(); jasen5.parse("5|Ankka Roope|091007-408U|Ankkakuja 12");
     *   jasenet.lisaa(jasen1); jasenet.lisaa(jasen2); jasenet.lisaa(jasen3); jasenet.lisaa(jasen4); jasenet.lisaa(jasen5);
     *   // TODO: toistaiseksi palauttaa kaikki jäsenet
     * </pre>
     */
    @SuppressWarnings("unused")
    public Collection<Pvm> etsi(String hakuehto, int k) {
        Collection<Pvm> loytyneet = new ArrayList<Pvm>();
        for (Pvm pvm : this) {
            loytyneet.add(pvm);
        }
        return loytyneet;
    }

    /**
     * Luokka jäsenten iteroimiseksi.
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #PACKAGEIMPORT
     * #import java.util.*;
     *
     * Jasenet jasenet = new Jasenet();
     * Jasen aku1 = new Jasen(), aku2 = new Jasen();
     * aku1.rekisteroi(); aku2.rekisteroi();
     *
     * jasenet.lisaa(aku1);
     * jasenet.lisaa(aku2);
     * jasenet.lisaa(aku1);
     *
     * StringBuffer ids = new StringBuffer(30);
     * for (Jasen jasen:jasenet)   // Kokeillaan for-silmukan toimintaa
     *   ids.append(" "+jasen.getTunnusNro());
     *
     * String tulos = " " + aku1.getTunnusNro() + " " + aku2.getTunnusNro() + " " + aku1.getTunnusNro();
     *
     * ids.toString() === tulos;
     *
     * ids = new StringBuffer(30);
     * for (Iterator<Jasen>  i=jasenet.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
     *   Jasen jasen = i.next();
     *   ids.append(" "+jasen.getTunnusNro());
     * }
     *
     * ids.toString() === tulos;
     *
     * Iterator<Jasen>  i=jasenet.iterator();
     * i.next() == aku1  === true;
     * i.next() == aku2  === true;
     * i.next() == aku1  === true;
     *
     * i.next();  #THROWS NoSuchElementException
     *
     * </pre>
     */
    public class JasenetIterator implements Iterator<Pvm> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa jäsentä
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä jäseniä
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava jäsen
         * @return seuraava jäsen
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Pvm next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }


    /**
     * Palautetaan iteraattori jäsenistään.
     * @return jäsen iteraattori
     */
    @Override
    public Iterator<Pvm> iterator() {
        return new JasenetIterator();
    }




    /**
     * Testiohjelma päiville
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Dates paivat = new Dates();

        Pvm pv1 = new Pvm(), pv2 = new Pvm();
        pv1.luo();
        pv1.vastaaPvm();
        pv2.luo();
        pv2.vastaaPvm();

        try {
            paivat.lisaa(pv1);
            paivat.lisaa(pv2);

            System.out.println("============= Päivät testi =================");

            for (int i = 0; i < paivat.getLkm(); i++) {
                Pvm date = paivat.anna(i);
                System.out.println("Jäsen nro: " + i);
                date.tulosta(System.out);
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }

}

