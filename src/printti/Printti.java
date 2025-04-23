package printti;
import java.util.Collection;
import java.util.List;
import java.io.File;

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

    private Dates dates = new Dates();
    private Todos todos = new Todos();

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
     * Lukee kerhon tiedot tiedostosta
     * @param pvm jota käyteään lukemisessa
     * @throws SailoException jos lukeminen epäonnistuu
     */
//    public void lueTiedostosta(String pvm) throws SailoException {
//        dates.lueTiedostosta(pvm);
//        todos.lueTiedostosta(pvm);
//    }


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
     * Lukee kerhon tiedot tiedostosta
     * @param nimi jota käyteään lukemisessa
     * @throws SailoException jos lukeminen epäonnistuu
     *
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.*;
     * #import java.util.*;
     *
     *  Kerho kerho = new Kerho();
     *
     *  Jasen aku1 = new Jasen(); aku1.vastaaAkuAnkka(); aku1.rekisteroi();
     *  Jasen aku2 = new Jasen(); aku2.vastaaAkuAnkka(); aku2.rekisteroi();
     *  Harrastus pitsi21 = new Harrastus(); pitsi21.vastaaPitsinNyplays(aku2.getTunnusNro());
     *  Harrastus pitsi11 = new Harrastus(); pitsi11.vastaaPitsinNyplays(aku1.getTunnusNro());
     *  Harrastus pitsi22 = new Harrastus(); pitsi22.vastaaPitsinNyplays(aku2.getTunnusNro());
     *  Harrastus pitsi12 = new Harrastus(); pitsi12.vastaaPitsinNyplays(aku1.getTunnusNro());
     *  Harrastus pitsi23 = new Harrastus(); pitsi23.vastaaPitsinNyplays(aku2.getTunnusNro());
     *
     *  String hakemisto = "testikelmit";
     *  File dir = new File(hakemisto);
     *  File ftied  = new File(hakemisto+"/nimet.dat");
     *  File fhtied = new File(hakemisto+"/harrastukset.dat");
     *  dir.mkdir();
     *  ftied.delete();
     *  fhtied.delete();
     *  kerho.lueTiedostosta(hakemisto); #THROWS SailoException
     *  kerho.lisaa(aku1);
     *  kerho.lisaa(aku2);
     *  kerho.lisaa(pitsi21);
     *  kerho.lisaa(pitsi11);
     *  kerho.lisaa(pitsi22);
     *  kerho.lisaa(pitsi12);
     *  kerho.lisaa(pitsi23);
     *  kerho.tallenna();
     *  kerho = new Kerho();
     *  kerho.lueTiedostosta(hakemisto);
     *  Collection<Jasen> kaikki = kerho.etsi("",-1);
     *  Iterator<Jasen> it = kaikki.iterator();
     *  it.next() === aku1;
     *  it.next() === aku2;
     *  it.hasNext() === false;
     *  List<Harrastus> loytyneet = kerho.annaHarrastukset(aku1);
     *  Iterator<Harrastus> ih = loytyneet.iterator();
     *  ih.next() === pitsi11;
     *  ih.next() === pitsi12;
     *  ih.hasNext() === false;
     *  loytyneet = kerho.annaHarrastukset(aku2);
     *  ih = loytyneet.iterator();
     *  ih.next() === pitsi21;
     *  ih.next() === pitsi22;
     *  ih.next() === pitsi23;
     *  ih.hasNext() === false;
     *  kerho.lisaa(aku2);
     *  kerho.lisaa(pitsi23);
     *  kerho.tallenna();
     *  ftied.delete()  === true;
     *  fhtied.delete() === true;
     *  File fbak = new File(hakemisto+"/nimet.bak");
     *  File fhbak = new File(hakemisto+"/harrastukset.bak");
     *  fbak.delete() === true;
     *  fhbak.delete() === true;
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
     * Tallenttaa kerhon tiedot tiedostoon.
     * Vaikka jäsenten tallettamien epäonistuisi, niin yritetään silti tallettaa
     * harrastuksia ennen poikkeuksen heittämistä.
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
     * Tallettaa kerhon tiedot tiedostoon
     * @throws SailoException jos tallettamisessa ongelmia
     */
//    public void talleta() throws SailoException {
//        dates.talleta();
//        todos.talleta();
//    }


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

            Collection<Pvm> dates = printti.etsi("", -1);
            int i = 0;
            for (Pvm pvm: dates) {

//                for (int i = 0; i < printti.getDates(); i++) {
//                Pvm pvm = printti.annaPvm(i);
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
