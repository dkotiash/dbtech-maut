package de.htwberlin.dbtech.aufgaben.ue03.dao.daoImp;

import de.htwberlin.dbtech.aufgaben.ue03.dao.daoInterface.MautKategorieDao;
import de.htwberlin.dbtech.aufgaben.ue03.object.MautKategorie;
import de.htwberlin.dbtech.exceptions.DataException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MautKategorieDaoImpl implements MautKategorieDao {
    private final Connection con;
    public MautKategorieDaoImpl(Connection con) { this.con = con; }

    @Override
    public MautKategorie findBestMatch(int ssklId, int achsZahl) {
        List<MautKategorie> kats = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement("SELECT KATEGORIE_ID, MAUTSATZ_JE_KM, ACHSZAHL FROM MAUTKATEGORIE WHERE SSKL_ID = ?")) {
            ps.setInt(1, ssklId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MautKategorie mk = new MautKategorie();
                    mk.setKategorieId(rs.getInt("KATEGORIE_ID"));
                    mk.setMautsatzJeKm(rs.getFloat("MAUTSATZ_JE_KM"));
                    mk.setAchszahl(DaoHelper.parseIntSafe(rs, "ACHSZAHL"));
                    kats.add(mk);
                }
            }
        } catch (SQLException e) { throw new DataException(e); }

        MautKategorie best = null;
        for (MautKategorie k : kats) {
            if (k.getAchszahl() <= achsZahl) {
                if (best == null || k.getAchszahl() > best.getAchszahl()) {
                    best = k;
                }
            }
        }
        if (best != null) return best;
        throw new DataException("Keine Kategorie gefunden für SSKL " + ssklId + ", Achsen " + achsZahl);
    }

    @Override
    public MautKategorie findById(int katId) {
        try (PreparedStatement ps = con.prepareStatement("SELECT ACHSZAHL, MAUTSATZ_JE_KM FROM MAUTKATEGORIE WHERE KATEGORIE_ID = ?")) {
            ps.setInt(1, katId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MautKategorie mk = new MautKategorie();
                    mk.setKategorieId(katId);
                    mk.setAchszahl(DaoHelper.parseIntSafe(rs, "ACHSZAHL"));
                    mk.setMautsatzJeKm(rs.getFloat("MAUTSATZ_JE_KM"));
                    return mk;
                }
            }
        } catch (SQLException e) { throw new DataException(e); }
        return null;
    }
}