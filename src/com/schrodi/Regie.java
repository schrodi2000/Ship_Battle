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
        screenEdgeSize = width * 0.1f;
        tileSize = (width - 2 * screenEdgeSize - 4 * infobereichAbstandTiles) / (mapSize * 2 + 3);
        tileVerkleinerungFaktor = 1f;
        infobereich = screenEdgeSize + tileSize * mapSize;
        infobereichAbstandTiles = (tileSize * 0.9f - tileSize) * -1;
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
                drawShipSetting();
                float y2 = drawText(spielfeldGegner, screenEdgeSize, 2.5f, "Steuerung:");
                y2 = drawText(spielfeldGegner, y2, 1.5f, "Mit Linksklick kannst du Schiffe auswählen und Setzen.");
                drawText(spielfeldGegner, y2, 1.5f, "Mit Rechtsklick kannst du das Schiff drehen.");
            }
            case spielenGegenSpieler -> {
                drawMap(spieler, false);
                drawMap(gegner, true);
            }
            case spielenGegenKi -> {
            }
            case spielenGegenOnline -> {
            }
            case warten -> {
            }
            case spielEnde -> {
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

    boolean schiffeSetzen() {
        int selectedShip = selectShip();
        // TODO schiffeSetzen
        return false;
    }

    int selectShip() {
        //schiff 3
        if (fieldPressed(infobereich + infobereichAbstandTiles, screenEdgeSize, infobereich + infobereichAbstandTiles + tileSize, screenEdgeSize + 3 * tileSize)) {
            return 3;
        }
        //schiff 2
        else if (fieldPressed(infobereich + 2 * infobereichAbstandTiles + tileSize, screenEdgeSize, infobereich + 2 * infobereichAbstandTiles + 2 * tileSize, screenEdgeSize + 2 * tileSize)) {
            return 2;
        }
        //schiff 1
        else if (fieldPressed(infobereich + 3 * infobereichAbstandTiles + tileSize, screenEdgeSize, infobereich + 3 * infobereichAbstandTiles + 3 * tileSize, screenEdgeSize + 1 * tileSize)) {
            return 1;
        }

        return 0;
    }

    int[] mouseOnMap(float mapPosX, float mapPosY) {
        if(mouseX > mapPosX && mouseX < mapPosX + mapSize * tileSize && mouseY > mapPosY && mouseY < mapPosY + mapSize * tileSize) {
            int tileOnMapX = (int)((mouseX - mapPosX) / tileSize);
            int tileOnMapY = (int)((mouseX - mapPosY) / tileSize);
            // ich kriege die Nummer des Teils also das wievielte es ist.
            return new int[]{tileOnMapX, tileOnMapY};
        }
        return new int[]{-1, -1};
    }

    void drawSelectedShip(int selectedShip) {
        for (int i = 0; i < selectedShip; i++) {

        }
    }

    boolean fieldPressed(float vonX, float vonY, float bisX, float bisY) {
        if (mousePressed && mouseX > vonX && mouseX < bisX && mouseY > vonY && mouseY < bisY) {
            return true;
        }
        return false;
    }

    float drawText(float x, float y, float size, String text) {
        fill(255);
        textSize(size * screenEdgeSize / 10);
        text(text, x, y + size * screenEdgeSize / 10);
        return y + size * screenEdgeSize / 10;
    }
}