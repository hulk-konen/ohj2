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
     *
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
     * dates.lisaa(pvm1);
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
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }

    /**
     * Lukee päivämäärät tiedostosta.
     * @param tied tiedoston nimen alkuosa
     * @throws SailoException jos lukeminen epäonnistuu
     *
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.File;
     * Dates dates = new Dates();
     * Pvm pvm1 = new Pvm(); pvm1.luo();
     * pvm1.setPvm("2025-04-23");
     * Pvm pvm2 = new Pvm(); pvm2.luo();
     * pvm2.setPvm("2025-04-24");
     * Pvm pvm3 = new Pvm(); pvm3.luo();
     * pvm3.setPvm("2025-04-25");
     * String tiedNimi = "testdates";
     * File ftied = new File(tiedNimi + ".dat");
     * ftied.delete();
     *
     * try {
     *     dates.lueTiedostosta(tiedNimi);
     *     fail("Expected SailoException was not thrown");
     * } catch (SailoException e) {
     * }
     *
     * try {
     *     dates.lisaa(pvm1);
     * } catch (SailoException e) {
     *     fail("SailoException: " + e.getMessage());
     * }
     *
     * try {
     *     dates.lisaa(pvm2);
     * } catch (SailoException e) {
     *     fail("SailoException: " + e.getMessage());
     * }
     *
     * try {
     *     dates.lisaa(pvm3);
     * } catch (SailoException e) {
     *     fail("SailoException: " + e.getMessage());
     * }
     *
     * try {
     *     dates.talleta();
     * } catch (SailoException e) {
     *     fail("SailoException save: " + e.getMessage());
     * }
     *
     * dates = new Dates();
     * try {
     *     dates.lueTiedostosta(tiedNimi);
     * } catch (SailoException e) {
     *     fail("SailoException read: " + e.getMessage());
     * }
     *
     * Iterator<Pvm> i = dates.iterator();
     * assertEquals(pvm1.toString(), i.next().toString());
     * assertEquals(pvm2.toString(), i.next().toString());
     * assertEquals(pvm3.toString(), i.next().toString());
     * assertFalse(i.hasNext());
     *
     * try {
     *     dates.lisaa(pvm3);
     * } catch (SailoException e) {
     *     fail("Unexpected SailoException: " + e.getMessage());
     * }
     *
     * try {
     *     dates.talleta();
     * } catch (SailoException e) {
     *     fail("Unexpected SailoException save: " + e.getMessage());
     * }
     *
     * assertTrue(ftied.delete());
     * File fbak = new File(tiedNimi + ".bak");
     * assertTrue(fbak.delete());
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
     * Asettaa tiedoston perusnimen ilman tarkenninta
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
     * Palauttaa printin päivien lukumäärän
     * @return päivien lukumäärä
     */
    public int getLkm() {
        return lkm;
    }

    /**
     * Palauttaa "taulukossa" hakuehtoon vastaavien päivämäärien viitteet.
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi
     * @return tietorakenteen löytyneistä päivämääristä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Dates dates = new Dates();
     * Pvm pvm1 = new Pvm();
     * pvm1.parse("1|2025-04-23");
     *
     * Pvm pvm2 = new Pvm();
     * pvm2.parse("2|2025-04-24");
     *
     * Pvm pvm3 = new Pvm();
     * pvm3.parse("3|2025-04-25");
     *
     * Pvm pvm4 = new Pvm();
     * pvm4.parse("4|2025-04-26");
     *
     * try {
     *     dates.lisaa(pvm1);
     * } catch (SailoException e) {
     *     fail("SailoException adding pvm1: " + e.getMessage());
     * }
     *
     * try {
     *     dates.lisaa(pvm2);
     * } catch (SailoException e) {
     *     fail("SailoException adding pvm2: " + e.getMessage());
     * }
     *
     * try {
     *     dates.lisaa(pvm3);
     * } catch (SailoException e) {
     *     fail("SailoException adding pvm3: " + e.getMessage());
     * }
     *
     * try {
     *     dates.lisaa(pvm4);
     * } catch (SailoException e) {
     *     fail("SailoException adding pvm4: " + e.getMessage());
     * }
     *
     * Collection<Pvm> loytyneet = dates.etsi("", -1);
     * loytyneet.size() === 4;
     * Iterator<Pvm> i = loytyneet.iterator();
     * i.next().toString() === pvm1.toString();
     * i.next().toString() === pvm2.toString();
     * i.next().toString() === pvm3.toString();
     * i.next().toString() === pvm4.toString();
     * i.hasNext() === false;
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
     * Testataan Dates-luokan iterointi.
     * @example
     * <pre name="test">
     * #THROWS NoSuchElementException
     * #PACKAGEIMPORT
     * #import java.util.*;
     *
     * Dates dates = new Dates();
     * Pvm pvm1 = new Pvm();
     * pvm1.parse("1|2025-04-23");
     * Pvm pvm2 = new Pvm();
     * pvm2.parse("2|2025-04-24");
     * Pvm pvm3 = new Pvm();
     * pvm3.parse("3|2025-04-25");
     *
     * try {
     *     dates.lisaa(pvm1);
     * } catch (SailoException e) {
     *     fail("Unexpected SailoException during adding pvm1: " + e.getMessage());
     * }
     *
     * try {
     *     dates.lisaa(pvm2);
     * } catch (SailoException e) {
     *     fail("Unexpected SailoException during adding pvm2: " + e.getMessage());
     * }
     *
     * try {
     *     dates.lisaa(pvm3);
     * } catch (SailoException e) {
     *     fail("Unexpected SailoException during adding pvm3: " + e.getMessage());
     * }
     *
     * // Testataan for-silmukalla iterointia
     * StringBuffer ids = new StringBuffer();
     * for (Pvm pvm : dates) {
     *     ids.append(" ").append(pvm.getId());
     * }
     * ids.toString() === " 1 2 3";
     *
     * // Testataan iteraattorilla iterointia
     * ids = new StringBuffer();
     * Iterator<Pvm> i = dates.iterator();
     * while (i.hasNext()) {
     *     Pvm pvm = i.next();
     *     ids.append(" ").append(pvm.getId());
     * }
     * ids.toString() === " 1 2 3";
     *
     * // Testataan yksittäisiä next-kutsuja
     * Iterator<Pvm> iter = dates.iterator();
     * iter.next() == pvm1 === true;
     * iter.next() == pvm2 === true;
     * iter.next() == pvm3 === true;
     *
     * // Testataan, että seuraavan elementin puuttuminen heittää poikkeuksen
     * iter.next(); #THROWS NoSuchElementException
     * </pre>
     */
    public class DatesIterator implements Iterator<Pvm> {
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
        return new DatesIterator();
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

