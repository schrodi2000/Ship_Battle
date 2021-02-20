package com.schrodi;

import jdk.jshell.spi.ExecutionControl;

import java.util.Random;
import java.util.ArrayList;

public class Player {
    public enum Zustand {
        aliveShip,
        deadShip,
        water,
        miss
    }

    private final ArrayList<Schiff> schiffe;
    private final int mapSize;
    private final Zustand[][] mapKarte;
    private final boolean shipVisible;


    public Player(int mapSize, boolean shipVisible) {
        this.shipVisible = shipVisible;
        schiffe = new ArrayList<>();
        this.mapSize = mapSize;
        mapKarte = new Zustand[mapSize][mapSize];
        for (int ix = 0; ix < mapSize; ix++) {
            for (int jy = 0; jy < mapSize; jy++) {
                mapKarte[ix][jy] = Zustand.water;
            }
        }
    }

    public boolean addSchiff(int x, int y, int length, Schiff.Richtung richtung) {
        Schiff schiff = new Schiff(length, x, y, richtung);// Erstellt ein Schiff mit den eigenschaften
        if (addAbleSchiff(schiff)) {
            if (schiff.getRichtung() == Schiff.Richtung.horizontal) {
                for (int i = 0; i < schiff.getLänge(); i++) {// Trägt die Teile des Schiffes ein wenn es Horizontal ausgerichtet ist
                    mapKarte[x + i][y] = schiff.getTeilZustand(i);
                }
            } else {
                for (int i = 0; i < schiff.getLänge(); i++) {// Trägt die Teile des Schiffes ein wenn es Vertikal ausgerichtet ist
                    mapKarte[x][y + i] = schiff.getTeilZustand(i);
                }
            }
            schiffe.add(schiff);// fügt das Schiff der Liste hinzu
        } else {
            return false;
        }
        return true;
    }

    boolean addAbleSchiff(Schiff schiff) {
        try {
            for (int i = 0; i < schiff.getLänge(); i++) {
                if (schiff.getRichtung() == Schiff.Richtung.horizontal) {//Horizontale Schiffe
                    if (mapKarte[schiff.getX()][schiff.getY() + i] == Zustand.aliveShip) {// schiff darf nicht auf anderes schiff gesetzt werden
                        return false;
                    }
                    if (schiff.getX() > 0) {
                        if (mapKarte[schiff.getX() - 1][schiff.getY()] == Zustand.aliveShip) {// links vom schiff darf kein anderes Schiff stehen
                            return false;
                        }
                    }
                    if (schiff.getX() + schiff.getLänge() < mapSize) {
                        if (mapKarte[schiff.getX() + schiff.getLänge()][schiff.getY()] == Zustand.aliveShip) {// rechts vom schiff darf kein anderes Schiff stehen
                            return false;
                        }
                    }
                    if (schiff.getY() > 0) {
                        if (mapKarte[schiff.getX() + i][schiff.getY() - 1] == Zustand.aliveShip) {// über dem schiff darf kein anderes Schiff stehen
                            return false;
                        }
                    }
                    if (schiff.getY() + 1 < mapSize) {
                        if (mapKarte[schiff.getX() + i][schiff.getY() + 1] == Zustand.aliveShip) {// unter dem schiff darf kein anderes Schiff stehen
                            return false;
                        }
                    }
                } else {// Vertikale Schiffe
                    if (mapKarte[schiff.getX()][schiff.getY() + i] == Zustand.aliveShip) {// schiff darf nicht auf anderes schiff gesetzt werden
                        return false;
                    }
                    if (schiff.getX() > 0) {
                        if (mapKarte[schiff.getX() - 1][schiff.getY() + i] == Zustand.aliveShip) {// links vom schiff darf kein anderes Schiff stehen
                            return false;
                        }
                    }
                    if (schiff.getX() + 1 < mapSize) {
                        if (mapKarte[schiff.getX() + 1][schiff.getY() + i] == Zustand.aliveShip) {// rechts vom schiff darf kein anderes Schiff stehen
                            return false;
                        }
                    }
                    if (schiff.getY() > 0) {
                        if (mapKarte[schiff.getX()][schiff.getY() - 1] == Zustand.aliveShip) {// über dem schiff darf kein anderes Schiff stehen
                            return false;
                        }
                    }
                    if (schiff.getY() + schiff.getLänge() < mapSize) {
                        if (mapKarte[schiff.getX()][schiff.getY() + schiff.getLänge()] == Zustand.aliveShip) {// unter dem schiff darf kein anderes Schiff stehen
                            return false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean AnySchiffAlive() {
        for (Schiff schiff : schiffe) {
            for (int i = 0; i < schiff.getLänge(); i++) {
                if (schiff.getTeilZustand(i) == Zustand.aliveShip) {
                    return true;
                }
            }
        }
        return false;
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

    public boolean randomAddShips(int dreier, int zweier, int einer) {
        Random random = new Random();
        int versuche = 0;
        int i = 0;
        do {
            i++;
            int x = random.nextInt(mapSize);
            int y = random.nextInt(mapSize);
            Schiff.Richtung richtung = (random.nextInt(2) == 1) ? Schiff.Richtung.horizontal : Schiff.Richtung.vertikal;
            if (addSchiff(x, y, 3, richtung) && dreier > 0) {
                dreier--;
            } else if (addSchiff(x, y, 2, richtung) && zweier > 0) {
                zweier--;
            } else if (addSchiff(x, y, 1, richtung) && einer > 0) {
                einer--;
            }
            versuche++;
        } while ((dreier > 0 || zweier > 0 || einer > 0) && versuche < 1000);
        return dreier == 0 && zweier == 0 && einer == 0;
    }

    public Zustand getMapKarteTeil(int x, int y) {
        if (!shipVisible && mapKarte[x][y] == Zustand.aliveShip) {
            return Zustand.water;
        }
        return mapKarte[x][y];
    }
}

