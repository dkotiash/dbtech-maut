package de.htwberlin.dbtech.aufgaben.ue02;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.htwberlin.dbtech.exceptions.DataException;

/**
 * Die Klasse realisiert die Mautverwaltung.
 * 
 * @author Patrick Dohmeier
 */
public class MautVerwaltungImpl implements IMautVerwaltung {

	private static final Logger L = LoggerFactory.getLogger(MautVerwaltungImpl.class);
	private Connection connection;

	@Override
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	private Connection getConnection() {
		if (connection == null) {
			throw new DataException("Connection not set");
		}
		return connection;
	}

	@Override
	public String getStatusForOnBoardUnit(long fzg_id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String query = "SELECT * FROM fahrzeuggerat f WHERE f.FZG_ID = ?";
		String result = "";

		try {
			ps = getConnection().prepareStatement(query);
			ps.setLong(1, fzg_id);
			rs = ps.executeQuery();

			if (rs.next()) {
				result = rs.getString("status");
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (NullPointerException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public int getUsernumber(int maut_id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void registerVehicle(long fz_id, int sskl_id, int nutzer_id, String kennzeichen, String fin, int achsen,
			int gewicht, String zulassungsland) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateStatusForOnBoardUnit(long fzg_id, String status) {
		if (status == null || status.isBlank()) {
			throw new IllegalArgumentException("status darf nicht leer sein");
		}
		String query = "UPDATE U596635.FAHRZEUGGERAT SET STATUS = ? WHERE FZG_ID = ?";;

		try {
			PreparedStatement ps = getConnection().prepareStatement(query);
			// 1. Platzhalter (= STATUS)
			ps.setString(1, status);
			// 2. Platzhalter (= FZG_ID)
			ps.setLong(2, fzg_id);
			int rows = ps.executeUpdate();
			if (rows == 0) {
				throw new IllegalStateException("Kein Fahrzeuggerät mit FZG_ID=" + fzg_id + " gefunden.");
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (NullPointerException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void deleteVehicle(long fz_id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Mautabschnitt> getTrackInformations(String abschnittstyp) {
		// TODO Auto-generated method stub
		return null;
	}

}
