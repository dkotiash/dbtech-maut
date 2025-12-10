package de.htwberlin.dbtech.aufgaben.ue03.dao.daoImp;

import de.htwberlin.dbtech.aufgaben.ue03.dao.daoInterface.FahrzeugGeratDao;
import de.htwberlin.dbtech.aufgaben.ue03.object.FahrzeugGerat;
import de.htwberlin.dbtech.exceptions.DataException;
import java.sql.*;

public class FahrzeugGeratDaoImpl implements FahrzeugGeratDao {

    private final Connection con;

    public FahrzeugGeratDaoImpl(Connection con) {
        this.con = con;
    }

    @Override
    public FahrzeugGerat findByFzId(long fzId) {
        String sql = "SELECT FZG_ID, FZ_ID, STATUS, TYP, EINBAUDATUM, AUSBAUDATUM " +
                "FROM FAHRZEUGGERAT WHERE FZ_ID = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, fzId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    FahrzeugGerat fg = new FahrzeugGerat();
                    fg.setFzgId(rs.getLong("FZG_ID"));
                    fg.setFzId(rs.getLong("FZ_ID"));
                    fg.setStatus(rs.getString("STATUS"));
                    fg.setTyp(rs.getString("TYP"));
                    fg.setEinbaudatum(rs.getDate("EINBAUDATUM"));
                    fg.setAusbaudatum(rs.getDate("AUSBAUDATUM"));
                    return fg;
                }
            }
        } catch (SQLException e) {
            throw new DataException(e);
        }
        return null;
    }
}