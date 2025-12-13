package de.htwberlin.dbtech.aufgaben.ue03.object;

import java.sql.Date;

public class Mauterhebung {

    private long mautId;
    private int abschnittsId;
    private long fzgId;
    private int kategorieId;
    private Date befahrungsdatum;
    private float kosten;

    public long getMautId() {
        return mautId;
    }

    public void setMautId(long mautId) {
        this.mautId = mautId;
    }

    public int getAbschnittsId() {
        return abschnittsId;
    }

    public void setAbschnittsId(int abschnittsId) {
        this.abschnittsId = abschnittsId;
    }

    public long getFzgId() {
        return fzgId;
    }

    public void setFzgId(long fzgId) {
        this.fzgId = fzgId;
    }

    public int getKategorieId() {
        return kategorieId;
    }

    public void setKategorieId(int kategorieId) {
        this.kategorieId = kategorieId;
    }

    public Date getBefahrungsdatum() {
        return befahrungsdatum;
    }

    public void setBefahrungsdatum(Date befahrungsdatum) {
        this.befahrungsdatum = befahrungsdatum;
    }

    public float getKosten() {
        return kosten;
    }

    public void setKosten(float kosten) {
        this.kosten = kosten;
    }
}