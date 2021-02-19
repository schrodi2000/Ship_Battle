package com.schrodi;

import processing.core.PApplet;

public class Regie extends PApplet {
    public enum GameState {
        schiffeSetzen,
        spielenGegenSpieler,
        spielenGegenOnline,
        spielenGegenKi,
        kiGegenKi,
        warten,
        spielEnde
    }

    public GameState spielStatus;
    String[] verlauf = new String[10];
    Player s, g;
    // Layout größen Design
    float screenEdgeSize;
    float tileSize;
    final float tileVerkleinerungFaktor;
    float infobereich;
    float infobereichAbstandTiles;
    float spielfeldGegner;
    int mapSize = 7;


    public Regie(Player s, Player g) {
        this.s = s;
        this.g = g;
        width = 1920;
        height = 1080;
        spielStatus = GameState.schiffeSetzen;
        rand = width * 0.1f;
        tileSize = (width - 2 * rand - 4 * infobereichAbstandTiles) / (mapSize*2+3);
        tileVerkleinerungFaktor = 0.9f;
        infobereich = rand + tileSize * mapSize;
        infobereichAbstandTiles = (tileSize * 0.9f - tileSize)*-1;
        spielfeldGegner = infobereich + 3 * tileSize + 4 * infobereichAbstandTiles;

        String[] processingArgs = {"Schiffe-versenken"};
        PApplet.runSketch(processingArgs, this);
    }

    public void settings() {
        size(width, height);
    }

    public void setup() {
        surface.setResizable(true);
        noStroke();
    }

    public void draw() {
        updateLayout();
        background(100, 100, 100);
        drawWindow(s, g);
    } // das ist die eigentliche Regie Funktion

    void updateLayout() {
        screenEdgeSize = width * 0.1f;
        tileSize = (width - 2 * screenEdgeSize - 4 * infobereichAbstandTiles) / (mapSize * 2 + 3);
        infobereich = screenEdgeSize + tileSize * mapSize;
        infobereichAbstandTiles = (tileSize * 0.9f - tileSize) * -1;
        spielfeldGegner = infobereich + 3 * tileSize + 4 * infobereichAbstandTiles;
    }

    void drawWindow(Player spieler, Player gegner) {
        switch (spielStatus) {
            case schiffeSetzen -> {
                drawMap(spieler, false);
                drawMap(gegner, true);

                drawSchiffeSetzen();
            }
        }
    }

    void drawMap(Player player, Boolean isEnemy) {
        float mapPosX;
        if (isEnemy) {
            mapPosX = spielfeldGegner;
        } else {
            mapPosX = screenEdgeSize;
        }
        for (int iy = 0; iy < mapSize; iy++) {
            for (int jx = 0; jx < mapSize; jx++) {
                switch (player.getMapKarteTeil(iy, jx)) {
                    case water -> fill(0, 0, 255);
                    case miss -> fill(122, 122, 255);
                    case deadShip -> fill(255, 0, 0);
                    case aliveShip -> fill(0, 255, 0);
                }
                rect(mapPosX + jx * tileSize, screenEdgeSize + iy * tileSize, tileSize * tileVerkleinerungFaktor, tileSize * tileVerkleinerungFaktor);
            }
        }
    }

    void drawShipSetting() {
        float tilesPosY = screenEdgeSize;
        float tilesPosX = infobereich + infobereichAbstandTiles;
        fill(0, 100, 0);
        for (int i = 3; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                rect(tilesPosX, tilesPosY + j * tileSize, tileSize * tileVerkleinerungFaktor, tileSize * tileVerkleinerungFaktor);
            }
            tilesPosX = tilesPosX + tileSize + infobereichAbstandTiles;
        }
    }

    }

    void addInfo() {

    }
}