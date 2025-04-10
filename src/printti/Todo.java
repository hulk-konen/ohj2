package printti;
import java.io.*;
import java.util.Random;

/**
 * Harrastus joka osaa mm. itse huolehtia tunnus_nro:staan.
 *
 * @author Vesa Lappalainen
 * @version 1.0, 22.02.2003
 */
public class Todo {
    private int id;
    private int date;
    private String task;
    private int status;
    private static int seuraavaNro = 1;


    /**
     * Alustetaan harrastus.  Toistaiseksi ei tarvitse tehdä mitään
     */
    public Todo() {
        // Vielä ei tarvita mitään
    }


    /**
     * Alustetaan tietyn jäsenen harrastus.
     * @param jasenNro jäsenen viitenumero
     */
    public Todo(int date) {
        this.date = date;
    }


    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot Harrastukselle.
     * Aloitusvuosi arvotaan, jotta kahdella harrastuksella ei olisi
     * samoja tietoja.
     * @param nro viite henkilöön, jonka harrastuksesta on kyse
     */
    public void vastaaPitsinNyplays(int nro) {
        date = nro;
        task = "Pitsin nypläys";
        Random random = new Random();
        status = random.nextInt(2);
    }


    /**
     * Tulostetaan harrastuksen tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(id + " " + date + " " + task + " " + status);
    }


    /**
     * Tulostetaan henkilön tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Antaa harrastukselle seuraavan rekisterinumeron.
     * @return harrastuksen uusi tunnus_nro
     * @example
     * <pre name="test">
     *   Todo pitsi1 = new Todo();
     *   pitsi1.getId() === 0;
     *   pitsi1.rekisteroi();
     *   Todo pitsi2 = new Todo();
     *   pitsi2.rekisteroi();
     *   int n1 = pitsi1.getId();
     *   int n2 = pitsi2.getId();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        id = seuraavaNro;
        seuraavaNro++;
        return id;
    }


    /**
     * Palautetaan harrastuksen oma id
     * @return harrastuksen id
     */
    public int getId() {
        return id;
    }


    /**
     * Palautetaan mille jäsenelle harrastus kuuluu
     * @return jäsenen id
     */
    public int getDate() {
        return date;
    }


    /**
     * Testiohjelma Harrastukselle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Todo har = new Todo();
        har.rekisteroi();
        har.vastaaPitsinNyplays(2);
        har.tulosta(System.out);
    }

}
