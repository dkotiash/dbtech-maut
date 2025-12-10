package de.htwberlin.dbtech.aufgaben.ue03.dao.daoInterface;
import de.htwberlin.dbtech.aufgaben.ue03.object.Fahrzeug;
import de.htwberlin.dbtech.aufgaben.ue03.object.FahrzeugGerat;

public interface FahrzeugDao {
    /**
     * Findet ein Fahrzeug basierend auf dem Kennzeichen.
     *
     * @param kz Das Kennzeichen des Fahrzeugs.
     * @return Das Fahrzeug mit dem angegebenen Kennzeichen.
     */
    Fahrzeug findByKennzeichen(String kz);
    /**
     * Findet ein Fahrzeuggerät anhand der Fahrzeug-ID (Fremdschlüssel).
     * Wird benötigt, um zu prüfen, ob ein Auto eine OBU hat.
     */
    FahrzeugGerat findGeratByFzId(long fzId);
}