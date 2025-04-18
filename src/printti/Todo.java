package printti;
import java.io.*;
import java.util.Random;

/**
 * Todo joka osaa mm. itse huolehtia id:staan.
 *
 * @author tohulkko
 * @version 1.0, 11.04.2025
 */
public class Todo {
    private int id;
    private int date;
    private String task;
    private int status;
    private static int seuraavaNro = 1;


    /**
     * Alustetaan todo.  Toistaiseksi ei tarvitse tehdä mitään
     */
    public Todo() {
      //  rekisteroi();
        // Vielä ei tarvita mitään
    }


    /**
     * Alustetaan tietyn päivän todo.
     * @param date päivän numero
     */
    public Todo(int date) {
        this.date = date;
    }


    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot Todolle.
     * Status arvotaan, testaamista varten.
     * @param nro viite päivään, jonka todosta on kyse
     */
    public void vastaaPitsinNyplays(int nro) {
        date = nro;
        task = "Pitsin nypläys";
        Random random = new Random();
        status = random.nextInt(2);
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    /**
     * Tulostetaan todon tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(id + " " + date + " " + task + " " + status);
    }


    /**
     * Tulostetaan todon tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    public String getTask() {
        return task;
    }

    public int getStatus() { return status; }


    /**
     * Antaa harrastukselle seuraavan id:n.
     * @return harrastuksen uusi id
     * @example
     * <pre name="test">
     *   Todo todo1 = new Todo();
     *   todo1.getId() === 0;
     *   todo1.rekisteroi();
     *   Todo todo2 = new Todo();
     *   todo2.rekisteroi();
     *   int n1 = todo1.getId();
     *   int n2 = todo2.getId();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        id = seuraavaNro;
        seuraavaNro++;
        return id;
    }


    /**
     * Palautetaan todon oma id
     * @return todon id
     */
    public int getId() {
        return id;
    }


    /**
     * Palautetaan mille datelle harrastus kuuluu
     * @return päivän id
     */
    public int getDate() {
        return date;
    }


    /**
     * Testiohjelma Todolle.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Todo har = new Todo();
        har.rekisteroi();
        har.vastaaPitsinNyplays(2);
        har.tulosta(System.out);
    }

}
