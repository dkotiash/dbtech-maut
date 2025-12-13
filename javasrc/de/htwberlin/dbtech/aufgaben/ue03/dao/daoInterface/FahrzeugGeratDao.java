package de.htwberlin.dbtech.aufgaben.ue03.dao.daoInterface;

import de.htwberlin.dbtech.aufgaben.ue03.object.FahrzeugGerat;

public interface FahrzeugGeratDao {
    /**
     * Findet ein Fahrzeuggerät anhand der Fahrzeug-ID.
     * @param fzId Die Fahrzeug-ID.
     * @return Das Fahrzeuggerät mit der angegebenen Fahrzeug-ID.
     */
    FahrzeugGerat findByFzId(long fzId);
}