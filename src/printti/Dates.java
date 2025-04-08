package printti;

/**
 * Kerhon jäsenistö joka osaa mm. lisätä uuden jäsenen
 *
 * @author Vesa Lappalainen
 * @version 1.0, 22.02.2003
 * @version 1.1, 19.02.2012
 */
public class Dates {
    private static final int MAX_JASENIA   = 5;
    private int              lkm           = 0;
    private String           tiedostonNimi = "";
    private Date            alkiot[]      = new Date[MAX_JASENIA];


    /**
     * Oletusmuodostaja
     */
    public Dates() {
        // Attribuuttien oma alustus riittää
    }


    /**
     * Lisää uuden jäsenen tietorakenteeseen.  Ottaa jäsenen omistukseensa.
     * @param date lisätäävän jäsenen viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Jasenet jasenet = new Jasenet();
     * Jasen aku1 = new Jasen(), aku2 = new Jasen();
     * jasenet.getLkm() === 0;
     * jasenet.lisaa(aku1); jasenet.getLkm() === 1;
     * jasenet.lisaa(aku2); jasenet.getLkm() === 2;
     * jasenet.lisaa(aku1); jasenet.getLkm() === 3;
     * jasenet.anna(0) === aku1;
     * jasenet.anna(1) === aku2;
     * jasenet.anna(2) === aku1;
     * jasenet.anna(1) == aku1 === false;
     * jasenet.anna(1) == aku2 === true;
     * jasenet.anna(3) === aku1; #THROWS IndexOutOfBoundsException
     * jasenet.lisaa(aku1); jasenet.getLkm() === 4;
     * jasenet.lisaa(aku1); jasenet.getLkm() === 5;
     * jasenet.lisaa(aku1);  #THROWS SailoException
     * </pre>
     */
    public void lisaa(Date date) throws SailoException {
        if (lkm >= alkiot.length) throw new SailoException("Liikaa alkioita");
        alkiot[lkm] = date;
        lkm++;
    }


    /**
     * Palauttaa viitteen i:teen jäseneen.
     * @param i monennenko jäsenen viite halutaan
     * @return viite jäseneen, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public Date anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }


    /**
     * Lukee jäsenistön tiedostosta.  Kesken.
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        tiedostonNimi = hakemisto + "/nimet.dat";
        throw new SailoException("Ei osata vielä lukea tiedostoa " + tiedostonNimi);
    }


    /**
     * Tallentaa jäsenistön tiedostoon.  Kesken.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void talleta() throws SailoException {
        throw new SailoException("Ei osata vielä tallettaa tiedostoa " + tiedostonNimi);
    }


    /**
     * Palauttaa kerhon jäsenten lukumäärän
     * @return jäsenten lukumäärä
     */
    public int getLkm() {
        return lkm;
    }


    /**
     * Testiohjelma jäsenistölle
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Dates paivat = new Dates();

        Date pv1 = new Date(), pv2 = new Date();
        pv1.luo();
        pv1.vastaaPvm();
        pv2.luo();
        pv2.vastaaPvm();

        try {
            paivat.lisaa(pv1);
            paivat.lisaa(pv2);

            System.out.println("============= Päivät testi =================");

            for (int i = 0; i < paivat.getLkm(); i++) {
                Date date = paivat.anna(i);
                System.out.println("Jäsen nro: " + i);
                date.tulosta(System.out);
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }

}

