package com.schrodi;

public class Schiff {
    public enum Richtung {
        horizontal,
        vertikal
    }

    private final int laenge;
    private final Player.Zustand[] teile;
    private final int x, y;
    private final Richtung richtung;


    public Schiff(int laenge, int x, int y, Richtung richtung) {// Konstruktor
        teile = new Player.Zustand[laenge];
        this.laenge = laenge;
        this.x = x;
        this.y = y;
        this.richtung = richtung;
        for (int i = 0; i < laenge; i++) {
            teile[i] = Player.Zustand.aliveShip;
        }
    }

    public Player.Zustand getTeilZustand(int teilNr) { //gibt den zustand eines Schiffteils zurück
        return teile[teilNr];
    }

    public void setTeilZustand(int teilNr, Player.Zustand zustand) { //setzt den Zustand eines Schiffteils
        teile[teilNr] = zustand;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Richtung getRichtung() {
        return richtung;
    }

    public int getLaenge() {
        return laenge;
    }
}