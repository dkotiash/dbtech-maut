// java
package de.htwberlin.dbtech.aufgaben.ue03.dao;

import de.htwberlin.dbtech.aufgaben.ue03.object.BuchungData;
import de.htwberlin.dbtech.aufgaben.ue03.object.MautKategorieInfo;
import de.htwberlin.dbtech.aufgaben.ue03.object.FahrzeugData;
import de.htwberlin.dbtech.exceptions.DataException;

import java.sql.*;


public class MautDaoImpl implements MautDao {

    private final Connection connection;

    public MautDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public FahrzeugData findObuData(String kennzeichen) {
        // FIX: Wir holen fg.FZG_ID (Geräte-ID), NICHT f.FZ_ID (Fahrzeug-ID).
        // f.FZ_ID ist zu groß für die MAUTERHEBUNG Tabelle.
        String sql = "SELECT fg.FZG_ID, f.SSKL_ID, f.ACHSEN " +
                "FROM FAHRZEUG f " +
                "JOIN FAHRZEUGGERAT fg ON f.FZ_ID = fg.FZ_ID " +
                "WHERE f.KENNZEICHEN = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, kennzeichen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long fzgId = rs.getLong("FZG_ID"); // Jetzt korrekte Spalte
                    int sskl = parseIntSafe(rs, "SSKL_ID");
                    int achsen = parseIntSafe(rs, "ACHSEN");
                    return new FahrzeugData(fzgId, sskl, achsen);
                }
            }
        } catch (SQLException e) { throw new DataException(e); }
        return null;
    }

    @Override
    public BuchungData findBuchungData(String kennzeichen, int abschnittId) {
        String sql = "SELECT b.BUCHUNG_ID, b.B_ID, mk.ACHSZAHL " +
                "FROM BUCHUNG b " +
                "JOIN MAUTKATEGORIE mk ON b.KATEGORIE_ID = mk.KATEGORIE_ID " +
                "WHERE b.KENNZEICHEN = ? AND b.ABSCHNITTS_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, kennzeichen);
            ps.setInt(2, abschnittId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long bId = rs.getLong("BUCHUNG_ID");
                    int statusId = parseIntSafe(rs, "B_ID");
                    int achszahl = parseIntSafe(rs, "ACHSZAHL");
                    return new BuchungData(bId, statusId, achszahl);
                }
            }
        } catch (SQLException e) { throw new DataException(e); }
        return null;
    }

    @Override
    public float getAbschnittLaenge(int abschnittId) {
        String sql = "SELECT LAENGE FROM MAUTABSCHNITT WHERE ABSCHNITTS_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, abschnittId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getFloat("LAENGE");
            }
        } catch (SQLException e) { throw new DataException(e); }
        throw new DataException("Abschnitt nicht gefunden: " + abschnittId);
    }

    @Override
    public MautKategorieInfo findMautKategorie(int ssklId, int achsZahl) {
        // FIX für ORA-01722: Wir laden alle Kategorien der SSKL und suchen den Match in Java.
        // Das SQL "ACHSZAHL <= ?" schlägt fehl, weil in der DB Strings wie ">= 4" stehen.
        String sql = "SELECT KATEGORIE_ID, MAUTSATZ_JE_KM, ACHSZAHL FROM MAUTKATEGORIE WHERE SSKL_ID = ?";

        MautKategorieInfo bestMatch = null;
        int bestMatchAchsen = -1;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, ssklId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int currentId = rs.getInt("KATEGORIE_ID");
                    float currentSatz = rs.getFloat("MAUTSATZ_JE_KM");
                    // parseIntSafe wandelt ">= 4" in 4 um
                    int catAchsen = parseIntSafe(rs, "ACHSZAHL");

                    // Wir suchen die Kategorie mit den höchsten Achsen, die noch <= input ist.
                    // Beispiel Input 5: Passt auf Kat 2 und Kat 4. Wir nehmen 4.
                    if (catAchsen <= achsZahl) {
                        if (catAchsen > bestMatchAchsen) {
                            bestMatchAchsen = catAchsen;
                            bestMatch = new MautKategorieInfo(currentId, currentSatz);
                        }
                    }
                }
            }
        } catch (SQLException e) { throw new DataException(e); }

        if (bestMatch != null) return bestMatch;
        throw new DataException("Keine Mautkategorie gefunden für SSKL " + ssklId + " und Achsen " + achsZahl);
    }

    @Override
    public void saveMauterhebung(int abschnittId, long fzgId, int katId, float kosten) {
        try {
            long nextId = 1;
            try (Statement s = connection.createStatement();
                 ResultSet rs = s.executeQuery("SELECT MAX(MAUT_ID) FROM MAUTERHEBUNG")) {
                if (rs.next()) nextId = rs.getLong(1) + 1;
            }

            String sql = "INSERT INTO MAUTERHEBUNG (MAUT_ID, ABSCHNITTS_ID, FZG_ID, KATEGORIE_ID, BEFAHRUNGSDATUM, KOSTEN) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setLong(1, nextId);
                ps.setInt(2, abschnittId);
                ps.setLong(3, fzgId); // Das muss die OBU-ID sein (fg.FZG_ID), nicht die Auto-ID!
                ps.setInt(4, katId);
                ps.setDate(5, new Date(System.currentTimeMillis()));
                ps.setFloat(6, kosten);
                ps.executeUpdate();
            }
        } catch (SQLException e) { throw new DataException(e); }
    }

    @Override
    public void updateBuchungStatus(long buchungId, int neuerStatusId) {
        String sql = "UPDATE BUCHUNG SET B_ID = ? WHERE BUCHUNG_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, neuerStatusId);
            ps.setLong(2, buchungId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new DataException(e); }
    }

    // Entfernt Sonderzeichen und parst Zahlen robust
    private int parseIntSafe(ResultSet rs, String columnLabel) throws SQLException {
        String val = rs.getString(columnLabel);
        if (val == null) return 0;
        String cleanVal = val.replaceAll("[^0-9]", ""); // Alles außer Ziffern weg
        if (cleanVal.isEmpty()) return 0;
        try {
            return Integer.parseInt(cleanVal);
        } catch (NumberFormatException e) {
            throw new SQLException("Parse Fehler bei Spalte " + columnLabel + ": " + val, e);
        }
    }
}