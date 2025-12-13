package de.htwberlin.dbtech.aufgaben.ue03.dao.daoImp;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoHelper {
    /**
     * Sicheres Parsen von Integer-Werten aus ResultSets.
     * Entfernt alle nicht-numerischen Zeichen vor dem Parsen.
     * Gibt 0 zurück bei null oder ungültigen Inhalten.
     * @param rs ResultSet
     * @param col Spaltenname
     * @return geparster Integer-Wert, oder 0 bei null oder ungültigen Inhalten
     * @throws SQLException bei SQL-Fehlern
     */
    static int parseIntSafe(ResultSet rs, String col) throws SQLException {
        String s = rs.getString(col);
        if (s == null) return 0;
        String c = s.replaceAll("[^0-9]", "");
        return c.isEmpty() ? 0 : Integer.parseInt(c);
    }
}