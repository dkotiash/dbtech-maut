package de.htwberlin.dbtech.aufgaben.ue03.dao.daoInterface;

import de.htwberlin.dbtech.aufgaben.ue03.object.FahrzeugGerat;

public interface FahrzeugGeratDao {
    /**
     * Findet ein Fahrzeuggerät anhand der Fahrzeug-ID (Fremdschlüssel).
     * Wird benötigt, um zu prüfen, ob ein Auto eine OBU hat.
     */
    FahrzeugGerat findByFzId(long fzId);
}