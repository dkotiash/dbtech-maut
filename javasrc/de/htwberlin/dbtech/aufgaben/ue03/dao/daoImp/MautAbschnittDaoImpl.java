package de.htwberlin.dbtech.aufgaben.ue03.dao.daoImp;

import de.htwberlin.dbtech.aufgaben.ue03.dao.daoInterface.MautAbschnittDao;
import de.htwberlin.dbtech.aufgaben.ue03.object.MautAbschnitt;
import de.htwberlin.dbtech.exceptions.DataException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MautAbschnittDaoImpl implements MautAbschnittDao {
    private final Connection con;
    public MautAbschnittDaoImpl(Connection con) { this.con = con; }

    @Override
    public MautAbschnitt findById(int abschnittsId) {
        try (PreparedStatement ps = con.prepareStatement("SELECT LAENGE FROM MAUTABSCHNITT WHERE ABSCHNITTS_ID = ?")) {
            ps.setInt(1, abschnittsId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MautAbschnitt ma = new MautAbschnitt();
                    ma.setAbschnittsId(abschnittsId);
                    ma.setLaenge(rs.getFloat("LAENGE"));
                    return ma;
                }
            }
        } catch (SQLException e) { throw new DataException(e); }
        throw new DataException("Abschnitt nicht gefunden: " + abschnittsId);
    }
}