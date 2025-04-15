package printti;

/**
 * yksittäinen päiväys
 *
 * @author tohulkko
 * @version 1.0, 11.04.2025
 */
import java.io.*;
import java.util.Random;
import java.time.LocalDate;


public class Pvm {
    private int        id;
    private String     pvm = "";
    private static int seuraavaNro    = 1;
    private int        tunnusNro;
    private static final Random random = new Random();


    public String getPvm() {
        return pvm;
    }


    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot päivälle.
     * @param apupvm pvm joka annetaan päivälle
     */
    public void vastaaPvm(String apupvm) {
        pvm = apupvm;
    }

    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot päivälle.
     * Pvm arvotaan, jotta kahdella päivällä ei olisi
     * samoja tietoja.
     */
    public void vastaaPvm() {
        int year = 2000 + random.nextInt(26);
        int month = 1 + random.nextInt(12);

        LocalDate date = LocalDate.of(year, month, 1);
        int maxDays = date.lengthOfMonth();

        int day = 1 + random.nextInt(maxDays);

        String apupvm = String.format("%02d-%02d-%04d", day, month, year);
        vastaaPvm(apupvm);
    }


    /**
     * Tulostetaan pvm tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", id, 3) + "  "
                + pvm);
    }


    /**
     * Tulostetaan pvm tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Antaa päiville seuraavan rekisterinumeron.
     * @return päivän uusi id
     * @example
     * <pre name="test">
     *   Pvm pv1 = new Pvm();
     *   pv1.getId() === 0;
     *   pv1.luo();
     *   Pvm pv2 = new Pvm();
     *   pv2.luo();
     *   int n1 = pv1.getId();
     *   int n2 = pv2.getId();
     *   n1 === n2-1;
     * </pre>
     */
    public int luo() {
        id = seuraavaNro;
        seuraavaNro++;
        return id;
    }

    /**
     * Palauttaa pvm:n id:n.
     * @return pvm id
     */
    public int getId() {
        return id;
    }

    public static void main(String args[]) {
        Pvm pv1 = new Pvm(), pv2 = new Pvm();
        pv1.luo();
        pv2.luo();
        pv1.tulosta(System.out);
        pv1.vastaaPvm();
        pv1.tulosta(System.out);

        pv2.tulosta(System.out);

        pv2.vastaaPvm();
        pv2.tulosta(System.out);

        pv2.vastaaPvm();
        pv2.tulosta(System.out);
    }
}
