package de.htwberlin.dbtech.aufgaben.ue03.mauterhebung;

import de.htwberlin.dbtech.aufgaben.ue03.dao.daoImp.*;
import de.htwberlin.dbtech.aufgaben.ue03.dao.daoInterface.*;
import de.htwberlin.dbtech.aufgaben.ue03.object.*;
import de.htwberlin.dbtech.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Date;

public class MautServiceImpl implements IMautService {

	private static final Logger L = LoggerFactory.getLogger(MautServiceImpl.class);

	//für bessere Lesbarkeit
	private static final int STATUS_ID_OFFEN = 1;
	private static final int STATUS_ID_GESCHLOSSEN = 3;
	private static final int SCHWELLE_OPEN_END_ACHSEN = 4;
	private static final float UMRECHNUNG_M_IN_KM = 1000.0f;
	private static final float UMRECHNUNG_CENT_IN_EURO = 100.0f;

	//DAOs
	private FahrzeugDao fahrzeugDao;
	private FahrzeugGeratDao fahrzeugGeratDao;
	private MautAbschnittDao abschnittDao;
	private MautKategorieDao kategorieDao;
	private MauterhebungDao erhebungDao;
	private BuchungDao buchungDao;

	@Override
	public void setConnection(Connection connection) {
		this.fahrzeugDao = new FahrzeugDaoImpl(connection);
		this.fahrzeugGeratDao = new FahrzeugGeratDaoImpl(connection);
		this.abschnittDao = new MautAbschnittDaoImpl(connection);
		this.kategorieDao = new MautKategorieDaoImpl(connection);
		this.erhebungDao = new MauterhebungDaoImpl(connection);
		this.buchungDao = new BuchungDaoImpl(connection);
	}

	@Override
	public void berechneMaut(int mautAbschnittId, int achsZahl, String kennzeichen) {
		Fahrzeug fahrzeug = fahrzeugDao.findByKennzeichen(kennzeichen);

		if (fahrzeug != null) {
			FahrzeugGerat gerat = fahrzeugGeratDao.findByFzId(fahrzeug.getFzId());
			if (gerat != null) {
				verarbeiteAutomatisch(fahrzeug, gerat, mautAbschnittId, achsZahl);
				return;
			}
		}
		verarbeiteManuell(kennzeichen, mautAbschnittId, achsZahl);
	}





	//Hilfsmethoden
	/**
	 * Verarbeitet die Mauterhebung im automatischen Verfahren (mit Gerät).
	 * @param fzg Das Fahrzeugobjekt
	 * @param gerat Das Fahrzeuggerätobjekt
	 * @param abschnittId Die ID des Mautabschnitts
	 * @param achsZahl Die gemessene Achszahl
	 */
	private void verarbeiteAutomatisch(Fahrzeug fzg, FahrzeugGerat gerat, int abschnittId, int achsZahl) {
		if (!isAchszahlValid(fzg.getAchsen(), achsZahl)) {
			logAndThrowInvalidData("Gemessene Achsen (" + achsZahl + ") stimmen nicht mit registrierten Achsen (" + fzg.getAchsen() + ") überein.");
		}

		MautAbschnitt abschnitt = abschnittDao.findById(abschnittId);
		MautKategorie kat = kategorieDao.findBestMatch(fzg.getSsklId(), achsZahl);

		float kosten = berechneKosten(abschnitt.getLaenge(), kat.getMautsatzJeKm());

		Mauterhebung erhebung = new Mauterhebung();
		erhebung.setAbschnittsId(abschnittId);
		erhebung.setFzgId(gerat.getFzgId());
		erhebung.setKategorieId(kat.getKategorieId());
		erhebung.setKosten(kosten);
		erhebung.setBefahrungsdatum(new Date(System.currentTimeMillis()));

		erhebungDao.create(erhebung);
		L.info("Maut automatisch erhoben. Kosten: {} Euro", kosten);
	}

	/**
	 * Verarbeitet die Mauterhebung im manuellen Verfahren (mit Buchung).
	 * @param kennzeichen Das Kennzeichen des Fahrzeugs
	 * @param abschnittId Die IDdes Mautabschnitts
	 * @param achsZahl Diegemessene Achszahl
	 */
	private void verarbeiteManuell(String kennzeichen, int abschnittId, int achsZahl) {
		Buchung buchung = buchungDao.findByKennzeichenAndAbschnitt(kennzeichen, abschnittId);

		if (buchung == null) {
			String msg = "Fahrzeug nicht registriert und keine Buchung gefunden: " + kennzeichen;
			L.error(msg);
			throw new UnkownVehicleException(msg);
		}

		MautKategorie gebuchteKat = kategorieDao.findById(buchung.getKategorieId());

		if (!isAchszahlValid(gebuchteKat.getAchszahl(), achsZahl)) {
			logAndThrowInvalidData("Falsche Achszahl gebucht.");
		}

		if (buchung.getBId() != STATUS_ID_OFFEN) {
			L.error("Buchung nicht mehr offen. Status ID: {}", buchung.getBId());
			throw new AlreadyCruisedException("Buchung ist nicht mehr offen.");
		}

		buchungDao.updateStatus(buchung.getBuchungId(), STATUS_ID_GESCHLOSSEN);
		L.info("Manuelle Buchung validiert und geschlossen.");
	}

	/**
	 * Berechnet die Mautkosten in Euro.
	 * Formel: (Meter / 1000) * (Cent / 100)
	 * @param laengeMeter Die Laenge des Mautabschnitts in Metern
	 * @param satzCentProKm Der Mautsatz in Centpro Kilometer
	 * @return Die berechneten Kostenin Euro
	 */
	private float berechneKosten(float laengeMeter, float satzCentProKm) {
		return (laengeMeter / UMRECHNUNG_M_IN_KM) * (satzCentProKm / UMRECHNUNG_CENT_IN_EURO);
	}

	/**
	 * Validiert die Achszahl. Erlaubt Abweichung nach oben, wenn DB-Wert >= 4 (Open End).
	 * @param dbAchsen Die in der DB hinterlegte Achszahl
	 * @param gemessen Die gemessene Achszahl
	 * @return true, wenn die Achszahlen übereinstimmen oder DB-Wert >= 4 und gemessen > DB-Wert; sonst false
	 */
	private boolean isAchszahlValid(int dbAchsen, int gemessen) {
		return dbAchsen == gemessen || (dbAchsen >= SCHWELLE_OPEN_END_ACHSEN && gemessen > dbAchsen);
	}

	/**
	 * Hilfsmethode zum Loggen und Werfen der Exception (vermeidet Code-Duplizierung).
	 * @param msg Die Fehlermeldung
	 */
	private void logAndThrowInvalidData(String msg) {
		L.error(msg);
		throw new InvalidVehicleDataException(msg);
	}
}