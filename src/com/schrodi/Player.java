package com.schrodi;

import processing.core.PVector;

import java.util.Random;
import java.util.ArrayList;

public class Player {
    public enum Zustand {
        aliveShip,
        deadShip,
        water,
        miss
    }

    public final ArrayList<Schiff> schiffe;
    private final int mapSize;
    private final Zustand[][] mapKarte;
    private boolean shipVisible;

    public Zustand[][] getOfficialMapKarte() {
        Zustand[][] karte = new Zustand[mapSize][mapSize];
        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                if (mapKarte[x][y] == Zustand.aliveShip) {
                    karte[x][y] = Zustand.water;
                } else {
                    karte[x][y] = mapKarte[x][y];
                }
            }
        }
        return karte;
    }

    public Player(int mapSize, boolean shipsVisible) {
        this.shipVisible = shipsVisible;
        schiffe = new ArrayList<>();
        this.mapSize = mapSize;
        mapKarte = new Zustand[mapSize][mapSize];
        for (int ix = 0; ix < mapSize; ix++) {
            for (int jy = 0; jy < mapSize; jy++) {
                mapKarte[ix][jy] = Zustand.water;
            }
        }
    }

    public void setShipVisible(boolean shipsVisible) {
        this.shipVisible = shipsVisible;
    }

    public boolean isShipVisible() {
        return shipVisible;
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
            return true;
        } else {
            return false;
        }
    }

    boolean addAbleSchiff(Schiff schiff) {
        try {
            for (int i = 0; i < schiff.getLänge(); i++) {
                if (schiff.getRichtung() == Schiff.Richtung.horizontal) {//Horizontale Schiffe
                    if (mapKarte[schiff.getX() + i][schiff.getY()] == Zustand.aliveShip) {// schiff darf nicht auf anderes schiff gesetzt werden
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

    public boolean noSchiffAlive() {
        for (Schiff schiff : schiffe) {
            for (int i = 0; i < schiff.getLänge(); i++) {
                if (schiff.getTeilZustand(i) == Zustand.aliveShip) {
                    return false;
                }
            }
        }
        return true;
    }

    public PVector ki(Zustand[][] map, boolean hitLast) {
        PVector shootAt = new PVector(-1, -1);
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map.length; x++) {
                if (map[x][y] == Zustand.deadShip) {
                    Random t = new Random();
                    try {
                        switch (t.nextInt(3)) {
                            case 0:
                                if (map[x][y - 1] == Zustand.water) {
                                    shootAt = new PVector(x, y - 1);
                                }
                                break;
                            case 1:
                                if (map[x - 1][y] == Zustand.water) {
                                    shootAt = new PVector(x - 1, y);
                                }
                                break;
                            case 2:
                                if (map[x][y + 1] == Zustand.water) {
                                    shootAt = new PVector(x, y + 1);
                                }
                                break;
                            case 3:
                                if (map[x + 1][y] == Zustand.water) {
                                    shootAt = new PVector(x + 1, y);
                                }
                                break;
                        }
                    }catch (Exception e){}
                }
            }
        }
        if (shootAt.x == -1) {
            shootAt = new PVector((int) (Math.random() * mapSize), (int) (Math.random() * mapSize));
        }
        return shootAt;
    }

    public void removeSchiff(Schiff schiff) {
        boolean destroyed = true;
        for (int i = 0; i < schiff.getLänge(); i++) {
            if (schiff.getTeilZustand(i) == Zustand.aliveShip) {
                destroyed = false;
            }
        }
        if (destroyed) {
            schiffe.remove(schiff);
        }
    }

    public boolean shootAt(int x, int y) {
        for (Schiff schiff : schiffe) {
            if (schiff.getRichtung() == Schiff.Richtung.horizontal) {
                for (int teilNr = 0; teilNr < schiff.getLänge(); teilNr++) {
                    if (schiff.getX() + teilNr == x && schiff.getY() == y && schiff.getTeilZustand(teilNr) == Zustand.aliveShip) {
                        mapKarte[x][y] = Zustand.deadShip;
                        schiff.setTeilZustand(teilNr, Zustand.deadShip);
                        removeSchiff(schiff);
                        return true;
                    }
                }
            } else {
                for (int teilNr = 0; teilNr < schiff.getLänge(); teilNr++) {
                    if (schiff.getX() == x && schiff.getY() + teilNr == y && schiff.getTeilZustand(teilNr) == Zustand.aliveShip) {
                        mapKarte[x][y] = Zustand.deadShip;
                        schiff.setTeilZustand(teilNr, Zustand.deadShip);
                        removeSchiff(schiff);
                        return true;
                    }
                }
            }
        }
        if (mapKarte[x][y] == Zustand.water) {
            mapKarte[x][y] = Zustand.miss;
            return true;
        }
        return false;
    }

    public void randomAddShips(int dreier, int zweier, int einer) {
        Random random = new Random();
        int x;
        int y;
        int i = 0;
        do {
            x = random.nextInt(mapSize);
            y = random.nextInt(mapSize);
            Schiff.Richtung richtung = (random.nextInt(2) == 1) ? Schiff.Richtung.horizontal : Schiff.Richtung.vertikal;
            if (dreier > 0) {
                if(addSchiff(x, y, 3, richtung)) {
                    dreier--;
                }
            }
            x = random.nextInt(mapSize);
            y = random.nextInt(mapSize);
            if (zweier > 0) {
                if(addSchiff(x, y, 2, richtung)) {
                    zweier--;
                }
            }
            x = random.nextInt(mapSize);
            y = random.nextInt(mapSize);
            if (einer > 0) {
                if(addSchiff(x, y, 1, richtung)) {
                    einer--;
                }
            }
        } while (dreier > 0 || zweier > 0 || einer > 0);
    }

    public Zustand getMapKarteTeil(int x, int y) {
        if (!shipVisible && mapKarte[x][y] == Zustand.aliveShip) {
            return Zustand.water;
        }
        return mapKarte[x][y];
    }
}

