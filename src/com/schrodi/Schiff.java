package com.schrodi;

public class Schiff {
    public enum Richtung {
        horizontal,
        vertikal
    }

    private int länge;
    private Map.Zustand[] teile;
    private int x, y;
    private Richtung richtung;


    public Schiff(int länge, int x, int y, Richtung richtung) {
        teile = new Map.Zustand[länge];
        this.länge = länge;
        this.x = x;
        this.y = y;
        this.richtung = richtung;
        for (int i = 0; i < länge; i++) {
            teile[i] = Map.Zustand.aliveShip;
        }
    }

    public Map.Zustand getTeilZustand(int teilNr) {
        Map.Zustand teilZustand = teile[teilNr];
        return teilZustand;
    }

    public void setTeilZustand(int teilNr, Map.Zustand zustand) {
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

    public int getLänge() {
        return länge;
    }

    public void kill(int teilNr) {
        teile[teilNr] = Map.Zustand.deadShip;
    }


}