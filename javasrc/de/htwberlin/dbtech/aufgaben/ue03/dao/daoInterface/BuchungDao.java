package de.htwberlin.dbtech.aufgaben.ue03.dao.daoInterface;
import de.htwberlin.dbtech.aufgaben.ue03.object.Buchung;

public interface BuchungDao {
    /**
     * Findet eine Buchung basierend auf dem Kennzeichen und dem Abschnitt.
     *
     * @param kz Das Kennzeichen des Fahrzeugs.
     * @param abschnittId Die ID des Mautabschnitts.
     * @return Die Buchung mit dem angegebenen Kennzeichen und Abschnitt.
     */
    Buchung findByKennzeichenAndAbschnitt(String kz, int abschnittId);

    /**
     * Aktualisiert den Status einer Buchung basierend auf der Buchungs-ID.
     *
     * @param buchungId  Die ID der Buchung.
     * @param neuerStatus Der neue Status der Buchung.
     */
    void updateStatus(long buchungId, int neuerStatus);
}