package printti;


/**
 * Rajapinta tietueelle johon voidaan taulukon avulla rakentaa
 * "attribuutit".
 * @author tohulkko
 * @version Mar 23, 2025
 * @example
 */
public interface Tietue {

    /**
     * Tehdään identtinen klooni tietueesta
     * @return kloonattu tietue
     * @throws CloneNotSupportedException jos kloonausta ei tueta
     */
    public abstract Tietue clone() throws CloneNotSupportedException;


    /**
     * Palauttaa tietueen tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return tietue tolppaeroteltuna merkkijonona
     */
    @Override
    public abstract String toString();

}