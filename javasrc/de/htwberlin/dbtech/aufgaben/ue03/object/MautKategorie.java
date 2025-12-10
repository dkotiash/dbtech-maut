package de.htwberlin.dbtech.aufgaben.ue03.object;

public class MautKategorie {

    private int kategorieId;
    private int ssklId;
    private String katBezeichnung;
    private int achszahl;
    private float mautsatzJeKm;


    public int getKategorieId() {
        return kategorieId;
    }

    public void setKategorieId(int kategorieId) {
        this.kategorieId = kategorieId;
    }

    public int getSsklId() {
        return ssklId;
    }

    public void setSsklId(int ssklId) {
        this.ssklId = ssklId;
    }

    public String getKatBezeichnung() {
        return katBezeichnung;
    }

    public void setKatBezeichnung(String katBezeichnung) {
        this.katBezeichnung = katBezeichnung;
    }

    public int getAchszahl() {
        return achszahl;
    }

    public void setAchszahl(int achszahl) {
        this.achszahl = achszahl;
    }

    public float getMautsatzJeKm() {
        return mautsatzJeKm;
    }

    public void setMautsatzJeKm(float mautsatzJeKm) {
        this.mautsatzJeKm = mautsatzJeKm;
    }
}