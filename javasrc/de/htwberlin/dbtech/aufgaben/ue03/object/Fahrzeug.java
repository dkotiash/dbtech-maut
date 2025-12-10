package de.htwberlin.dbtech.aufgaben.ue03.object;

import java.sql.Date;

public class Fahrzeug {

    private long fzId;
    private int ssklId;
    private int nutzerId;
    private String kennzeichen;
    private String fin;
    private int achsen;
    private float gewicht;
    private Date anmeldedatum;
    private Date abmeldedatum;
    private String zulassungsland;

    // --- Getter & Setter ---

    public long getFzId() {
        return fzId;
    }

    public void setFzId(long fzId) {
        this.fzId = fzId;
    }

    public int getSsklId() {
        return ssklId;
    }

    public void setSsklId(int ssklId) {
        this.ssklId = ssklId;
    }

    public int getNutzerId() {
        return nutzerId;
    }

    public void setNutzerId(int nutzerId) {
        this.nutzerId = nutzerId;
    }

    public String getKennzeichen() {
        return kennzeichen;
    }

    public void setKennzeichen(String kennzeichen) {
        this.kennzeichen = kennzeichen;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public int getAchsen() {
        return achsen;
    }

    public void setAchsen(int achsen) {
        this.achsen = achsen;
    }

    public float getGewicht() {
        return gewicht;
    }

    public void setGewicht(float gewicht) {
        this.gewicht = gewicht;
    }

    public Date getAnmeldedatum() {
        return anmeldedatum;
    }

    public void setAnmeldedatum(Date anmeldedatum) {
        this.anmeldedatum = anmeldedatum;
    }

    public Date getAbmeldedatum() {
        return abmeldedatum;
    }

    public void setAbmeldedatum(Date abmeldedatum) {
        this.abmeldedatum = abmeldedatum;
    }

    public String getZulassungsland() {
        return zulassungsland;
    }

    public void setZulassungsland(String zulassungsland) {
        this.zulassungsland = zulassungsland;
    }
}