package com.schrodi;

import java.util.ArrayList;

public class Map {
    public enum Zustand {aliveShip, deadShip, water}

    public Zustand map[][];
    ArrayList<Schiff> schiffe;
    int mapSize;

    public Map (int mapSize){
        this.mapSize = mapSize;
        map = new Zustand[mapSize][mapSize];
    }

    public void addSchiff(int x, int y, int länge, Schiff.Richtung richtung){
        schiffe.add(new Schiff(länge,x,y,richtung));
    }

    public boolean schießenAuf(int x, int y){
        for(int i = 0; i<schiffe.size()-1; i++){
            Schiff schiff = schiffe.get(i);

            if(schiff.getRichtung() == Schiff.Richtung.horizontal) {
                for (int j = 0; j < schiff.getLänge(); j++) {
                    if (schiff.getX() + j == x && schiff.getY() == y && schiff.getTeilZustand(0) == Zustand.aliveShip) {
                        return true;
                    }
                }
            }
            else{
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
        for(int iy = 0; iy<mapSize; iy++){
            for(int jx = 0; jx<mapSize; jx++){
                for(int m = 0; m<schiffe.size();m++){
                    Schiff schiff = schiffe.get(m);
                    if(schiff.getRichtung() == Schiff.Richtung.horizontal && schiff.getX() <= jx && schiff.getX() + schiff.getLänge() >= jx){
                        map[iy][jx]=schiff.getTeilZustand(1);
                    }
                }

                //TODO Map aufbauen für window
            }
        }
        return map;
    }//TODO Wenn die liste der Wasser treffer gemacht ist, soll wenn wasser an dieser stelle ist, abgefragt werden, ob da ein wassertreffer ist

}
