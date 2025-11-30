package de.htwberlin.dbtech.aufgaben.ue03.mauterhebung;

import de.htwberlin.dbtech.aufgaben.ue03.dao.*;
import de.htwberlin.dbtech.aufgaben.ue03.object.BuchungData;
import de.htwberlin.dbtech.aufgaben.ue03.object.MautKategorieInfo;
import de.htwberlin.dbtech.aufgaben.ue03.object.FahrzeugData;
import de.htwberlin.dbtech.exceptions.*;
import java.sql.Connection;

public class MautServiceImpl implements IMautService {

	private MautDao mautDao;

	@Override
	public void setConnection(Connection connection) {
		this.mautDao = new MautDaoImpl(connection);
	}

	@Override
	public void berechneMaut(int mautAbschnittId, int achsZahl, String kennzeichen) {

		FahrzeugData obu = mautDao.findObuData(kennzeichen);

		if (obu != null) {
			// === AUTOMATISCHES VERFAHREN (OBU) ===

			// Validierung: Gemessene Achsen müssen <= registrierte Achsen sein (oder Kategorie-Logik)
			// Fix aus Test 4: Erlaube höhere Achszahlen, wenn DB-Wert >= 4 (Open End Kategorie)
			if (!isAchszahlValid(obu.achsen, achsZahl)) {
				throw new InvalidVehicleDataException("Gemessene Achsen (" + achsZahl +
						") stimmen nicht mit registrierten Achsen (" + obu.achsen + ") überein.");
			}

			// 1. Länge in Metern holen
			float laengeMeter = mautDao.getAbschnittLaenge(mautAbschnittId);

			// 2. Mautsatz in Cent/km holen
			MautKategorieInfo katInfo = mautDao.findMautKategorie(obu.ssklId, achsZahl);

			// 3. Kosten berechnen (Meter -> km, Cent -> Euro)
			// Faktor 100.000 Korrektur für Testfall 6
			float kosten = (laengeMeter / 1000.0f) * (katInfo.mautsatz / 100.0f);

			mautDao.saveMauterhebung(mautAbschnittId, obu.fzgId, katInfo.kategorieId, kosten);

		} else {
			// === MANUELLES VERFAHREN (BUCHUNG) ===

			BuchungData buchung = mautDao.findBuchungData(kennzeichen, mautAbschnittId);

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
	}

	/**
	 * Prüft, ob die gemessene Achszahl zur gespeicherten passt.
	 * Berücksichtigt Kategorien wie ">= 4".
	 */
	private boolean isAchszahlValid(int dbAchsen, int gemesseneAchsen) {
		if (dbAchsen == gemesseneAchsen) return true;
		// Wenn in DB 4 steht, ist oft ">= 4" gemeint. Dann ist 5, 10 etc. erlaubt.
		if (dbAchsen >= 4 && gemesseneAchsen > dbAchsen) return true;
		return false;
	}
}