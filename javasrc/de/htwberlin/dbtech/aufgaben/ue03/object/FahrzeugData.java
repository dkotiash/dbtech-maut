package de.htwberlin.dbtech.aufgaben.ue03.object;

public class FahrzeugData {
    public long fzgId;
    public int ssklId;
    public int achsen;

    public FahrzeugData(long fzgId, int ssklId, int achsen) {
        this.fzgId = fzgId;
        this.ssklId = ssklId;
        this.achsen = achsen;
    }
}