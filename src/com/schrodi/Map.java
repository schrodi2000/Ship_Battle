package com.schrodi;

import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;

public class Map {
    public enum Zustand {
        aliveShip,
        deadShip,
        water,
        miss
    }

    private ArrayList<Schiff> schiffe;
    private int mapSize;
    private Zustand[][] mapKarte;


    public Map(int mapSize) {
        schiffe = new ArrayList<>();
        this.mapSize = mapSize;
        mapKarte = new Zustand[mapSize][mapSize];
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                mapKarte[i][j] = Zustand.water;
            }
        }
    }

    public void addSchiff(int x, int y, int length, Schiff.Richtung richtung) {
        Schiff schiff = new Schiff(length, x, y, richtung);// Erstellt ein Schiff mit den eigenschaften
        schiffe.add(schiff);// fügt das Schiff der Liste hinzu
        if (schiff.getRichtung() == Schiff.Richtung.horizontal) {
            for (int i = 0; i < schiff.getLänge(); i++) {// Trägt die Teile des Schiffes ein wenn es Horizontal ausgerichtet ist
                mapKarte[x + i][y] = schiff.getTeilZustand(i);
            }
        } else {
            for (int i = 0; i < schiff.getLänge(); i++) {// Trägt die Teile des Schiffes ein wenn es Vertikal ausgerichtet ist
                mapKarte[x][y + i] = schiff.getTeilZustand(i);
            }
        }
    }

    public int removeSchiff(int x, int y) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("remove schiff ist noch nicht vorhanden");
    }

    public boolean shootAt(int x, int y) {
        for (Schiff schiff : schiffe) {
            if (schiff.getRichtung() == Schiff.Richtung.horizontal) {
                for (int teilNr = 0; teilNr < schiff.getLänge(); teilNr++) {
                    if (schiff.getX() + teilNr == x && schiff.getY() == y && schiff.getTeilZustand(teilNr) == Zustand.aliveShip) {
                        mapKarte[x][y] = Zustand.deadShip;
                        schiff.setTeilZustand(teilNr, Zustand.deadShip);
                        return true;
                    }
                }
            } else {
                for (int teilNr = 0; teilNr < schiff.getLänge(); teilNr++) {
                    if (schiff.getX() == x && schiff.getY() + teilNr == y && schiff.getTeilZustand(teilNr) == Zustand.aliveShip) {
                        mapKarte[x][y] = Zustand.deadShip;
                        schiff.setTeilZustand(teilNr, Zustand.deadShip);
                        return true;
                    }
                }
            }
        }
        mapKarte[x][y] = Zustand.miss;
        return false;
    }

    public Zustand getMapKarteTeil(int x, int y) {
        return mapKarte[x][y];
    }

    private Zustand getField(int x, int y) {
        for (Schiff schiff : schiffe) {
            for (int i = 0; i < schiff.getLänge(); i++) {
                if (y == schiff.getY() && x == schiff.getX() + i && schiff.getRichtung() == Schiff.Richtung.horizontal) {
                    schiff.getTeilZustand(i);
                } else if (x == schiff.getX() && y == schiff.getY() + i && schiff.getRichtung() == Schiff.Richtung.vertikal) {
                    schiff.getTeilZustand(i);
                }
            }
        }
        return Zustand.water;
    }
}

