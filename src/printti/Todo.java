package printti;
import fi.jyu.mit.ohj2.Mjonot;

import printti.Tietue;

import java.io.*;
import java.util.Random;

/**
 * Todo joka osaa mm. itse huolehtia id:staan.
 *
 * @author tohulkko
 * @version 1.0, 11.04.2025
 */

public class Todo implements Cloneable, Tietue {

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
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setId(int nr) {
        id = nr;
        if ( id >= seuraavaNro ) seuraavaNro = id + 1;
    }

    public void setDate(int date) {
        this.date = date;
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
     * Antaa todolle seuraavan id:n.
     * @return todon uusi id
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
     * Palautetaan mille datelle todo kuuluu
     * @return päivän id
     */
    public int getDate() {
        return date;
    }


    @Override
    public int getKenttia() {
        return 0;
    }

    @Override
    public int ekaKentta() {
        return 0;
    }

    @Override
    public String getKysymys(int k) {
        return "";
    }

    @Override
    public String anna(int k) {
        return "";
    }

    @Override
    public String aseta(int k, String s) {
        return "";
    }

    @Override
    public Todo clone() throws CloneNotSupportedException {
        return (Todo) super.clone();
    }

    /**
     * Palauttaa todon tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return todo tolppaeroteltuna merkkijonona
     * @example
     * <pre name="test">
     *   Todo todo = new Todo();
     *   todo.parse("   2   |  2  |   Kalastus  | 0  ");
     *   todo.toString()    === "2|2|Kalastus|0";
     * </pre>
     */
    @Override
    public String toString() {
        return "" + getId() + "|" + date + "|" + task + "|" + status;
    }


    /**
     * Selvitää todon tiedot | erotellusta merkkijonosta.
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta todon tiedot otetaan
     * @example
     * <pre name="test">
     *   Pvm pvm1 = new Pvm();
     *   pvm1.parse("   2   | 2025-04-23");
     *   pvm1.getId() === 2;
     *   pvm1.toString() === "2|2025-04-23";
     *
     *   Pvm pvm2 = new Pvm();
     *   pvm2.luo();
     *   int n = pvm2.getId();
     *   pvm2.parse("" + (n + 20) + "|2025-05-01");
     *   pvm2.luo();
     *   pvm2.getId() === n + 20 + 1;
     *   pvm2.toString() === "" + (n + 20 + 1) + "|2025-05-01";
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setId(Mjonot.erota(sb, '|', getId()));
        date = Mjonot.erota(sb, '|', date);
        task = Mjonot.erota(sb, '|', task);
        status = Mjonot.erota(sb, '|', status);
    }


    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.toString().equals(obj.toString());
    }


    @Override
    public int hashCode() {
        return id;
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
