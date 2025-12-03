package de.htwberlin.dbtech.aufgaben.ue03.dao;

import de.htwberlin.dbtech.aufgaben.ue03.object.BuchungData;
import de.htwberlin.dbtech.aufgaben.ue03.object.MautKategorieInfo;
import de.htwberlin.dbtech.aufgaben.ue03.object.FahrzeugData;

public interface MautDao {
    FahrzeugData findFahrzeugData(String kennzeichen);

    BuchungData findBuchungData(String kennzeichen, int abschnittId);

    float getAbschnittLaenge(int abschnittId);

    MautKategorieInfo findMautKategorie(int ssklId, int achsZahl);

    // fzgId ist jetzt long
    void saveMauterhebung(int abschnittId, long fzgId, int katId, float kosten);

    // buchungId ist jetzt long
    void updateBuchungStatus(long buchungId, int neuerStatusId);
}