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

    public Map(int mapSize) {
        this.mapSize = mapSize;
    }

    public void addSchiff(int x, int y, int länge, Schiff.Richtung richtung) {
        schiffe.add(new Schiff(länge, x, y, richtung));
    }

    public boolean schießenAuf(int x, int y) {
        for (int i = 0; i < schiffe.size() - 1; i++) {
            Schiff schiff = schiffe.get(i);

            if (schiff.getRichtung() == Schiff.Richtung.horizontal) {
                for (int j = 0; j < schiff.getLänge(); j++) {
                    if (schiff.getX() + j == x && schiff.getY() == y && schiff.getTeilZustand(0) == Zustand.aliveShip) {
                        return true;
                    }
                }
            } else {
                for (int j = 0; j < schiff.getLänge(); j++) {
                    if (schiff.getX() == x && schiff.getY() + j == y && schiff.getTeilZustand(0) == Zustand.aliveShip) {
                        return true;
                    }
                }
            }

        }
        return false;
    }//TODO Wenn auf wasser geschossen wird, soll das gespeichert werden

    public Zustand[][] getMap() {
        Zustand map[][] = new Zustand[mapSize][mapSize];
        for (int iy = 0; iy < mapSize; iy++) {
            for (int jx = 0; jx < mapSize; jx++) {
                map[jx][iy] = getField(jx,iy);
                //TODO Map aufbauen für window
            }
        }
        return map;
    }//TODO Wenn die liste der Wasser treffer gemacht ist, soll wenn wasser an dieser stelle ist, abgefragt werden, ob da ein wassertreffer ist

    private Zustand getField(int x, int y) {
        for (Schiff schiff : schiffe) {
            for (int i = 0; i < schiff.getLänge() - 1; i++) {
                if(y== schiff.getY() && x== schiff.getX()+i && schiff.getRichtung() == Schiff.Richtung.horizontal){
                    schiff.getTeilZustand(i);
                }
                else if(x== schiff.getX() && y== schiff.getY()+i && schiff.getRichtung() == Schiff.Richtung.vertikal){
                    schiff.getTeilZustand(i);
                }
            }
        }
        return Zustand.water;
    }
}

