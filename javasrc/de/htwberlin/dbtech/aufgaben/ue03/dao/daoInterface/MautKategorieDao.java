package de.htwberlin.dbtech.aufgaben.ue03.dao.daoInterface;
import de.htwberlin.dbtech.aufgaben.ue03.object.MautKategorie;

public interface MautKategorieDao {
    /**
     * Findet die beste passende MautKategorie basierend auf der Schadstoffklasse (ssklId)
     * und der Achszahl (achsZahl) des Fahrzeugs.
     *
     * @param ssklId   Die Schadstoffklasse des Fahrzeugs.
     * @param achsZahl Die Anzahl der Achsen des Fahrzeugs.
     * @return Die beste passende MautKategorie.
     */
    MautKategorie findBestMatch(int ssklId, int achsZahl);

    /**
     * Findet eine MautKategorie basierend auf der Kategorie-ID.
     *
     * @param katId Die ID der MautKategorie.
     * @return Die MautKategorie mit der angegebenen ID.
     */
    MautKategorie findById(int katId);
}