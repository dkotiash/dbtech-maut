package de.htwberlin.dbtech.aufgaben.ue03.dao.daoImp;

import de.htwberlin.dbtech.aufgaben.ue03.dao.daoInterface.BuchungDao;
import de.htwberlin.dbtech.aufgaben.ue03.object.Buchung;
import de.htwberlin.dbtech.exceptions.DataException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuchungDaoImpl implements BuchungDao {
    private final Connection con;
    public BuchungDaoImpl(Connection con) { this.con = con; }

    @Override
    public Buchung findByKennzeichenAndAbschnitt(String kz, int abschnittId) {
        try (PreparedStatement ps = con.prepareStatement("SELECT BUCHUNG_ID, B_ID, KATEGORIE_ID FROM BUCHUNG WHERE KENNZEICHEN = ? AND ABSCHNITTS_ID = ?")) {
            ps.setString(1, kz);
            ps.setInt(2, abschnittId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Buchung b = new Buchung();
                    b.setBuchungId(rs.getLong("BUCHUNG_ID"));
                    b.setBId(DaoHelper.parseIntSafe(rs, "B_ID"));
                    b.setKategorieId(rs.getInt("KATEGORIE_ID"));
                    b.setKennzeichen(kz);
                    return b;
                }
            }
        } catch (SQLException e) { throw new DataException(e); }
        return null;
    }

    @Override
    public void updateStatus(long buchungId, int neuerStatus) {
        try (PreparedStatement ps = con.prepareStatement("UPDATE BUCHUNG SET B_ID = ? WHERE BUCHUNG_ID = ?")) {
            ps.setInt(1, neuerStatus);
            ps.setLong(2, buchungId);
            ps.executeUpdate();
        } catch (SQLException e) { throw new DataException(e); }
    }
}