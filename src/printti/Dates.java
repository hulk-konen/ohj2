package printti;

/**
 * Printin päiväykset, osaa mm. lisätä uuden päivän
 *
 * @author tohulkko
 * @version 1.0, 11.04.2025
 */
public class Dates {
    private static final int MAX_JASENIA   = 5;
    private int              lkm           = 0;
    private String           tiedostonNimi = "";
    private Pvm             alkiot[]      = new Pvm[MAX_JASENIA];


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
     * Lukee päivät tiedostosta.  Kesken.
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedostonNimi = hakemisto + "/nimet.dat";
        throw new SailoException("Ei osata vielä lukea tiedostoa " + tiedostonNimi);
    }


    /**
     * Tallentaa päivät tiedostoon.  Kesken.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        throw new SailoException("Ei osata vielä tallettaa tiedostoa " + tiedostonNimi);
    }


    /**
     * Palauttaa printin päivien lukumäärän
     * @return päivien lukumäärä
     */
    public int getLkm() {
        return lkm;
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

