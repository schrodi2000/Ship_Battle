package com.schrodi;

import java.util.ArrayList;

public class Map {
    public enum Zustand {
        aliveShip,
        deadShip,
        water,
        miss
    }

    ArrayList<Schiff> schiffe;
    int mapSize;
    Zustand[][] map;

    public Map(int mapSize) {
        this.mapSize = mapSize;
        map = new Zustand[mapSize][mapSize];
        for (int i = 0; i < mapSize - 1; i++) {
            for (int j = 0; j < mapSize - 1; j++) {
                map[i][j] = Zustand.water;
            }
        }
    }

    public void addSchiff(int x, int y, int length, Schiff.Richtung richtung) {
        Schiff schiff = new Schiff(length, x, y, richtung);// Erstellt ein Schiff mit den eigenschaften
        schiffe.add(schiff);// fügt das Schiff der Liste hinzu
        if (schiff.getRichtung() == Schiff.Richtung.horizontal) {
            for (int i = 0; i < schiff.getLänge() - 1; i++) {// Trägt die Teile des Schiffes ein wenn es Horizontal ausgerichtet ist
                map[x + i][y] = schiff.getTeilZustand(i);
            }
        } else {
            for (int i = 0; i < schiff.getLänge() - 1; i++) {// Trägt die Teile des Schiffes ein wenn es Vertikal ausgerichtet ist
                map[x][y + 1] = schiff.getTeilZustand(i);
            }
        }
    }

    public int removeSchiff(int x, int y) {
        //TODO Schiff muss an den Koordinaten gefunden und entfernt werden aus der schiff liste.
        return (-1);
    }

    public boolean shootAt(int x, int y) {
        for (int i = 0; i < schiffe.size() - 1; i++) {
            Schiff schiff = schiffe.get(i);
            if (schiff.getRichtung() == Schiff.Richtung.horizontal) {
                for (int j = 0; j < schiff.getLänge(); j++) {
                    if (schiff.getX() + j == x && schiff.getY() == y && schiff.getTeilZustand(0) == Zustand.aliveShip) {
                        //TODO Schiff muss als kaputt gespeichert werden.
                        map[x][y] = Zustand.deadShip;
                        return true;
                    }
                }
            } else {
                for (int j = 0; j < schiff.getLänge(); j++) {
                    if (schiff.getX() == x && schiff.getY() + j == y && schiff.getTeilZustand(0) == Zustand.aliveShip) {
                        map[x][y] = Zustand.deadShip;
                        return true;
                    }
                }
            }
        }
        map[x][y] = Zustand.miss;
        return false;
    }

    public Zustand[][] getMap() {
        return map;
    }

    private Zustand getField(int x, int y) {
        for (Schiff schiff : schiffe) {
            for (int i = 0; i < schiff.getLänge() - 1; i++) {
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

