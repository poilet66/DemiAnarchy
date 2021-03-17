package me.poilet66.demianarchy;

import me.poilet66.demianarchy.Objects.Royale;

public class RoyaleManager {

    private final DemiAnarchy main;

    private Royale currentRoyale;
    public boolean isRoyale;

    public RoyaleManager(DemiAnarchy main) {
        this.main = main;
        this.isRoyale = false;
    }

    public void setRoyale(Royale royale) {
        this.currentRoyale = royale;
        isRoyale = true;
    }

    public Royale getRoyale() {
        return currentRoyale;
    }

    public void resetRoyale() {
        this.currentRoyale = null;
        isRoyale = false;
    }
}
