package de.htwberlin.dbtech.aufgaben.ue03.object;

import java.sql.Date;

public class FahrzeugGerat {

    private long fzgId;
    private long fzId;
    private String status;
    private String typ;
    private Date einbaudatum;
    private Date ausbaudatum;


    public long getFzgId() {
        return fzgId;
    }

    public void setFzgId(long fzgId) {
        this.fzgId = fzgId;
    }

    public long getFzId() {
        return fzId;
    }

    public void setFzId(long fzId) {
        this.fzId = fzId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public Date getEinbaudatum() {
        return einbaudatum;
    }

    public void setEinbaudatum(Date einbaudatum) {
        this.einbaudatum = einbaudatum;
    }

    public Date getAusbaudatum() {
        return ausbaudatum;
    }

    public void setAusbaudatum(Date ausbaudatum) {
        this.ausbaudatum = ausbaudatum;
    }
}