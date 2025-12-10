package de.htwberlin.dbtech.aufgaben.ue03.dao.daoImp;

import de.htwberlin.dbtech.aufgaben.ue03.dao.daoInterface.FahrzeugDao;
import de.htwberlin.dbtech.aufgaben.ue03.object.Fahrzeug;
import de.htwberlin.dbtech.aufgaben.ue03.object.FahrzeugGerat;
import de.htwberlin.dbtech.exceptions.DataException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FahrzeugDaoImpl implements FahrzeugDao {
    private final Connection con;
    public FahrzeugDaoImpl(Connection con) { this.con = con; }

    @Override
    public Fahrzeug findByKennzeichen(String kz) {
        try (PreparedStatement ps = con.prepareStatement("SELECT FZ_ID, SSKL_ID, ACHSEN FROM FAHRZEUG WHERE KENNZEICHEN = ?")) {
            ps.setString(1, kz);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Fahrzeug f = new Fahrzeug();
                    f.setFzId(rs.getLong("FZ_ID"));
                    f.setSsklId(DaoHelper.parseIntSafe(rs, "SSKL_ID"));
                    f.setAchsen(DaoHelper.parseIntSafe(rs, "ACHSEN"));
                    f.setKennzeichen(kz);
                    return f;
                }
            }
        } catch (SQLException e) { throw new DataException(e); }
        return null;
    }

    @Override
    public FahrzeugGerat findGeratByFzId(long fzId) {
        try (PreparedStatement ps = con.prepareStatement("SELECT FZG_ID FROM FAHRZEUGGERAT WHERE FZ_ID = ?")) {
            ps.setLong(1, fzId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FahrzeugGerat fg = new FahrzeugGerat();
                    fg.setFzId(fzId);
                    fg.setFzgId(rs.getLong("FZG_ID"));
                    return fg;
                }
            }
        } catch (SQLException e) { throw new DataException(e); }
        return null;
    }
}