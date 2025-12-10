package de.htwberlin.dbtech.aufgaben.ue03.dao.daoInterface;
import de.htwberlin.dbtech.aufgaben.ue03.object.MautAbschnitt;

public interface MautAbschnittDao {
    /**
     * Findet einen MautAbschnitt basierend auf der Abschnitts-ID.
     *
     * @param abschnittsId Die ID des MautAbschnitts.
     * @return Der MautAbschnitt mit der angegebenen ID.
     */
    MautAbschnitt findById(int abschnittsId);
}