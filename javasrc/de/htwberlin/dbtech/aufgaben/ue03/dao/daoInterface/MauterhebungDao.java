package de.htwberlin.dbtech.aufgaben.ue03.dao.daoInterface;

import de.htwberlin.dbtech.aufgaben.ue03.object.Mauterhebung;

public interface MauterhebungDao {
    /**
     * Speichert eine neue Mauterhebung in der Datenbank.
     * Ermittelt automatisch die nächste ID.
     */
    void create(Mauterhebung mauterhebung);
}