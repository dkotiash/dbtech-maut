package de.htwberlin.dbtech.aufgaben.ue03.dao.daoImp;

import de.htwberlin.dbtech.aufgaben.ue03.dao.daoInterface.MauterhebungDao;
import de.htwberlin.dbtech.aufgaben.ue03.object.Mauterhebung;
import de.htwberlin.dbtech.exceptions.DataException;

import java.sql.*;

public class MauterhebungDaoImpl implements MauterhebungDao {

    private final Connection con;

    public MauterhebungDaoImpl(Connection con) {
        this.con = con;
    }

    @Override
    public void create(Mauterhebung m) {
        try {
            long nextId = 1;
            try (Statement s = con.createStatement();
                 ResultSet rs = s.executeQuery("SELECT MAX(MAUT_ID) FROM MAUTERHEBUNG")) {
                if (rs.next()) {
                    nextId = rs.getLong(1) + 1;
                }
            }

            m.setMautId(nextId);

            String sql = "INSERT INTO MAUTERHEBUNG (MAUT_ID, ABSCHNITTS_ID, FZG_ID, KATEGORIE_ID, BEFAHRUNGSDATUM, KOSTEN) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setLong(1, m.getMautId());
                ps.setInt(2, m.getAbschnittsId());
                ps.setLong(3, m.getFzgId());
                ps.setInt(4, m.getKategorieId());
                Date datum = (m.getBefahrungsdatum() != null) ? m.getBefahrungsdatum() : new Date(System.currentTimeMillis());
                ps.setDate(5, datum);
                ps.setFloat(6, m.getKosten());

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataException(e);
        }
    }
}