package de.htwberlin.dbtech.aufgaben.ue03.object;

import java.sql.Date;

public class Buchung {

    private long buchungId;
    private int bId;
    private int abschnittsId;
    private int kategorieId;
    private String kennzeichen;
    private Date buchungsdatum;
    private Date befahrungsdatum;
    private float kosten;

    public long getBuchungId() {
        return buchungId;
    }

    public void setBuchungId(long buchungId) {
        this.buchungId = buchungId;
    }

    public int getBId() {
        return bId;
    }

    public void setBId(int bId) {
        this.bId = bId;
    }

    public int getAbschnittsId() {
        return abschnittsId;
    }

    public void setAbschnittsId(int abschnittsId) {
        this.abschnittsId = abschnittsId;
    }

    public int getKategorieId() {
        return kategorieId;
    }

    public void setKategorieId(int kategorieId) {
        this.kategorieId = kategorieId;
    }

    public String getKennzeichen() {
        return kennzeichen;
    }

    public void setKennzeichen(String kennzeichen) {
        this.kennzeichen = kennzeichen;
    }

    public Date getBuchungsdatum() {
        return buchungsdatum;
    }

    public void setBuchungsdatum(Date buchungsdatum) {
        this.buchungsdatum = buchungsdatum;
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