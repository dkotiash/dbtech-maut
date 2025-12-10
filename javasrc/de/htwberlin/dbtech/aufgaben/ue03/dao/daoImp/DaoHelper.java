package de.htwberlin.dbtech.aufgaben.ue03.dao.daoImp;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoHelper {
    static int parseIntSafe(ResultSet rs, String col) throws SQLException {
        String s = rs.getString(col);
        if (s == null) return 0;
        String c = s.replaceAll("[^0-9]", "");
        return c.isEmpty() ? 0 : Integer.parseInt(c);
    }
}