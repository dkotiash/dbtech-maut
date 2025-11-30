package de.htwberlin.dbtech.aufgaben.ue03.object;

public class BuchungData {
    public long buchungId; // WICHTIG: long statt int
    public int bId; // Status ID
    public int gebuchteAchsen;

    public BuchungData(long buchungId, int bId, int gebuchteAchsen) {
        this.buchungId = buchungId;
        this.bId = bId;
        this.gebuchteAchsen = gebuchteAchsen;
    }
}