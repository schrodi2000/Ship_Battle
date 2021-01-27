package com.schrodi;

public class Schiff {
    public enum Richtung {
        horizontal,
        vertikal
    }

    private int länge;
    private Player.Zustand[] teile;
    private int x, y;
    private Richtung richtung;


    public Schiff(int länge, int x, int y, Richtung richtung) {
        teile = new Player.Zustand[länge];
        this.länge = länge;
        this.x = x;
        this.y = y;
        this.richtung = richtung;
        for (int i = 0; i < länge; i++) {
            teile[i] = Player.Zustand.aliveShip;
        }
    }

    public Player.Zustand getTeilZustand(int teilNr) {
        Player.Zustand teilZustand = teile[teilNr];
        return teilZustand;
    }

    public void setTeilZustand(int teilNr, Player.Zustand zustand) {
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
        teile[teilNr] = Player.Zustand.deadShip;
    }


}