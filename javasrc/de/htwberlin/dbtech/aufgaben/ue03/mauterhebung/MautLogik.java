package de.htwberlin.dbtech.aufgaben.ue03.mauterhebung;

import de.htwberlin.dbtech.aufgaben.ue03.dao.MautDao;
import de.htwberlin.dbtech.aufgaben.ue03.object.BuchungData;
import de.htwberlin.dbtech.aufgaben.ue03.object.FahrzeugData;
import de.htwberlin.dbtech.aufgaben.ue03.object.MautKategorieInfo;
import de.htwberlin.dbtech.exceptions.*;

public class MautLogik {

    private final MautDao mautDao;

    // Wir übergeben das DAO im Konstruktor, damit der Prozessor arbeiten kann
    public MautLogik(MautDao mautDao) {
        this.mautDao = mautDao;
    }

    public void verarbeiteAutomatischesVerfahren(FahrzeugData fzg, int abschnittId, int achsZahl) {
        if (!isAchszahlValid(fzg.achsen, achsZahl)) {
            throw new InvalidVehicleDataException("Gemessene Achsen (" + achsZahl +
                    ") stimmen nicht mit registrierten Achsen (" + fzg.achsen + ") überein.");
        }

        float laengeMeter = mautDao.getAbschnittLaenge(abschnittId);
        MautKategorieInfo katInfo = mautDao.findMautKategorie(fzg.ssklId, achsZahl);

        // Berechnung: (Meter / 1000) * (Cent / 100) = Euro
        float kosten = (laengeMeter / 1000.0f) * (katInfo.mautsatz / 100.0f);

        mautDao.saveMauterhebung(abschnittId, fzg.fzgId, katInfo.kategorieId, kosten);
    }

    public void verarbeiteManuellesVerfahren(String kennzeichen, int abschnittId, int achsZahl) {
        BuchungData buchung = mautDao.findBuchungData(kennzeichen, abschnittId);

        if (buchung == null) {
            throw new UnkownVehicleException("Fahrzeug nicht registriert und keine Buchung gefunden: " + kennzeichen);
        }

        if (!isAchszahlValid(buchung.gebuchteAchsen, achsZahl)) {
            throw new InvalidVehicleDataException("Falsche Achszahl gebucht.");
        }

        if (buchung.bId != 1) { // 1 = offen
            throw new AlreadyCruisedException("Buchung ist nicht mehr offen.");
        }

        mautDao.updateBuchungStatus(buchung.buchungId, 3); // 3 = abgeschlossen
    }

    private boolean isAchszahlValid(int dbAchsen, int gemesseneAchsen) {
        if (dbAchsen == gemesseneAchsen) return true;
        // Erlaube höhere Achszahlen bei "Open End" Kategorien (>= 4)
        return dbAchsen >= 4 && gemesseneAchsen > dbAchsen;
    }
}