package com.schrodi;

import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;

public class Regie extends PApplet {
    public enum GameState {
        schiffeSetzen,
        spielerShoots,
        schiffeGegnerSetzen,
        gegnerShoots,
        gewonnen,
        verloren
    }

    public enum GameMode {
        spielenGegenSpieler,
        spielenAmPCGegenSpieler,
        spielenGegenKi,
        kiGegenKi,

    }

    public GameMode gameMode;
    public GameState spielStatus;
    // Layout größen Design
    float screenEdgeSize;
    float tileSize;
    final float tileVerkleinerungFaktor;
    float infobereich;
    float infobereichAbstandTiles;
    float spielfeldGegner;
    int mapSize;
    int dreierSchiffe;
    int zweierSchiffe;
    int einerSchiffe;
    int selectedShipLength;
    boolean mouseLeftClick;
    Schiff.Richtung selectedShipDirection;
    Player spieler;
    Player gegner;
    char pressedKey = 0;
    boolean correctShoot = false;
    Player.Zustand selectedZustand = Player.Zustand.water;


    public Regie() {
        selectedShipLength = 0;
        mapSize = 7;
        spieler = new Player(mapSize, true);
        gegner = new Player(mapSize, false);
        dreierSchiffe = 2;
        zweierSchiffe = 2;
        einerSchiffe = 3;
        width = 1920;
        height = 1080;
        selectedShipDirection = Schiff.Richtung.vertikal;
        spielStatus = GameState.schiffeSetzen;
        screenEdgeSize = width * 0.1f;
        tileSize = (width - 2 * screenEdgeSize - 4 * infobereichAbstandTiles) / (mapSize * 2 + 3);
        tileVerkleinerungFaktor = 1f;
        infobereich = screenEdgeSize + tileSize * mapSize;
        infobereichAbstandTiles = (tileSize * 0.9f - tileSize) * -1;
        spielfeldGegner = infobereich + 3 * tileSize + 4 * infobereichAbstandTiles;
        String[] processingArgs = {"Schiffe-versenken"};
        PApplet.runSketch(processingArgs, this);

        gameMode = GameMode.spielenGegenKi;
    }

    public void settings() {
        size(width, height);
    }

    public void setup() {
        surface.setResizable(true);
        noStroke();
    }

    // das ist die eigentliche Regie Funktion
    public void draw() {
        updateLayout();
        background(100, 100, 100);
        spielAblauf(spieler, gegner);
    }

    void updateLayout() {
        screenEdgeSize = width * 0.1f;
        tileSize = (width - 2 * screenEdgeSize - 4 * infobereichAbstandTiles) / (mapSize * 2 + 3);
        infobereich = screenEdgeSize + tileSize * mapSize;
        infobereichAbstandTiles = (tileSize * 0.9f - tileSize) * -1;
        spielfeldGegner = infobereich + 3 * tileSize + 4 * infobereichAbstandTiles;
    }

    void spielAblauf(Player spieler, Player gegner) {
        switch (spielStatus) {
            case schiffeSetzen: {
                drawMap(spieler, false, true);
                drawShipSelect();
                float textAbstandY = drawText(spielfeldGegner, screenEdgeSize, 2.5f, "Steuerung:");
                textAbstandY = drawText(spielfeldGegner, textAbstandY, 1.5f, "Mit Linksklick kannst du Schiffe auswählen und Setzen.");
                textAbstandY = drawText(spielfeldGegner, textAbstandY, 1.5f, "Mit Rechtsklick kannst du das Schiff drehen.");
                textAbstandY = drawText(spielfeldGegner, textAbstandY, 1.5f, "Setze bitte noch " + dreierSchiffe + ":Dreier, " + zweierSchiffe + ":Zweier und " + einerSchiffe + ":Einer setzen.");
                drawText(spielfeldGegner, textAbstandY, 1.5f, "Wenn du die Schiffe plaziert hast, dann drücke 'K'");
                textAbstandY = drawText(screenEdgeSize, screenEdgeSize + mapSize * tileSize, 1.5f, "Spielmodis:");
                textAbstandY = drawText(screenEdgeSize, textAbstandY, 1.5f, "Spieler gegen KI: 'K'");
                textAbstandY = drawText(screenEdgeSize, textAbstandY, 1.5f, "Spieler gegen anderen Spieler: 'S'");
                drawText(screenEdgeSize, textAbstandY, 1.5f, "Spieler gegen Spieler an eime PC 'P'");
                drawSchiffeSetzen(spieler, false);
                if (einerSchiffe == 0 && zweierSchiffe == 0 && dreierSchiffe == 0 && pressedKey != '0') {
                    switch (pressedKey) {
                        case 'k': {
                            spielStatus = GameState.schiffeGegnerSetzen;
                            gameMode = GameMode.spielenGegenKi;
                            einerSchiffe = -1;
                            zweierSchiffe = -1;
                            dreierSchiffe = -1;
                            System.out.println("K");
                            break;
                        }
                        case 's': {
                            spielStatus = GameState.spielerShoots;
                            gameMode = GameMode.spielenGegenSpieler;
                            einerSchiffe = -1;
                            zweierSchiffe = -1;
                            dreierSchiffe = -1;
                            System.out.println("S");
                            break;
                        }
                        case 'p': {
                            spielStatus = GameState.schiffeGegnerSetzen;
                            gameMode = GameMode.spielenAmPCGegenSpieler;
                            einerSchiffe = -1;
                            zweierSchiffe = -1;
                            dreierSchiffe = -1;
                            System.out.println("P");
                            break;
                        }
                    }
                }
                break;
            }
            case schiffeGegnerSetzen: {
                switch (gameMode) {
                    case spielenAmPCGegenSpieler: {
                        if (einerSchiffe == -1 && zweierSchiffe == -1 && dreierSchiffe == -1) {
                            einerSchiffe = 3;
                            zweierSchiffe = 2;
                            dreierSchiffe = 2;
                        }
                        drawMap(gegner, true, true);
                        drawShipSelect();
                        drawSchiffeSetzen(gegner, true);
                        if (einerSchiffe == 0 && zweierSchiffe == 0 && dreierSchiffe == 0) {
                            if (Math.random() < 0.5) {
                                spielStatus = GameState.spielerShoots;
                            } else {
                                spielStatus = GameState.gegnerShoots;
                            }
                        }
                        break;
                    }
                    case spielenGegenKi: {
                        gegner.randomAddShips(2, 2, 3);
                        if (Math.random() < 0.5) {
                            spielStatus = GameState.spielerShoots;
                        } else {
                            spielStatus = GameState.gegnerShoots;
                        }
                        break;
                    }
                }
                break;
            }
            case spielerShoots: {
                switch (gameMode) {
                    case spielenGegenKi: {
                        drawMap(spieler, false, true);
                        drawMap(gegner, true, false);

                        if (mousePosOnMap(spielfeldGegner, screenEdgeSize).x != -1) {
                            int x = (int) mousePosOnMap(spielfeldGegner, screenEdgeSize).x;
                            int y = (int) mousePosOnMap(spielfeldGegner, screenEdgeSize).y;
                            if (mouseLeftClick) {
                                if (gegner.shootAt(x, y)) {
                                    spielStatus = GameState.gegnerShoots;
                                    if (gegner.noSchiffAlive()) {
                                        spielStatus = GameState.gewonnen;
                                    }
                                }
                            }
                        }
                        break;
                    }
                    case spielenAmPCGegenSpieler: {
                        drawMap(spieler, false, false);
                        drawMap(gegner, true, false);

                        if (mousePosOnMap(spielfeldGegner, screenEdgeSize).x != -1) {
                            int x = (int) mousePosOnMap(spielfeldGegner, screenEdgeSize).x;
                            int y = (int) mousePosOnMap(spielfeldGegner, screenEdgeSize).y;
                            if (mouseLeftClick) {
                                if (gegner.shootAt(x, y)) {
                                    spielStatus = GameState.gegnerShoots;
                                    if (gegner.noSchiffAlive()) {
                                        spielStatus = GameState.gewonnen;
                                    }
                                }
                            }
                        }
                        break;
                    }
                    case spielenGegenSpieler: {
                        drawMap(spieler, false, true);
                        drawMap(gegner, true, true);
                        drawZustandSelect();
                        selectZustand();
                        if (mousePosOnMap(spielfeldGegner, screenEdgeSize).x != -1) {
                            int x = (int) mousePosOnMap(spielfeldGegner, screenEdgeSize).x;
                            int y = (int) mousePosOnMap(spielfeldGegner, screenEdgeSize).y;

                            if (mouseLeftClick) {
                                coloringMap(x, y, gegner, selectedZustand);
                            }
                        }
                        if (mousePosOnMap(screenEdgeSize, screenEdgeSize).x != -1) {
                            int x = (int) mousePosOnMap(screenEdgeSize, screenEdgeSize).x;
                            int y = (int) mousePosOnMap(screenEdgeSize, screenEdgeSize).y;
                            if (mouseLeftClick) {
                                if (spieler.shootAt(x, y)) {
                                    if (spieler.noSchiffAlive()) {
                                        spielStatus = GameState.verloren;
                                    }
                                }
                            }
                        }
                        break;
                    }

                }
                break;
            }
            case gegnerShoots: {
                switch (gameMode) {
                    case spielenGegenKi: {
                        drawMap(spieler, false, true);
                        drawMap(gegner, true, false);
                        if (mouseLeftClick) {
                            break;
                        }
                        PVector shoot = gegner.ki(spieler.getOfficialMapKarte(), correctShoot);
                        int x = (int) shoot.x;
                        int y = (int) shoot.y;
                        correctShoot = spieler.shootAt(x, y);
                        if (correctShoot) {
                            spielStatus = GameState.spielerShoots;
                            if (spieler.noSchiffAlive()) {
                                spielStatus = GameState.verloren;
                            }
                        }
                        break;
                    }
                    case spielenAmPCGegenSpieler: {
                        drawMap(spieler, false, false);
                        drawMap(gegner, true, false);

                        if (mousePosOnMap(spielfeldGegner, screenEdgeSize).x != -1) {
                            int x = (int) mousePosOnMap(spielfeldGegner, screenEdgeSize).x;
                            int y = (int) mousePosOnMap(spielfeldGegner, screenEdgeSize).y;
                            if (mouseLeftClick) {
                                if (gegner.shootAt(x, y)) {
                                    spielStatus = GameState.gegnerShoots;
                                    if (gegner.noSchiffAlive()) {
                                        spielStatus = GameState.gewonnen;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
                break;
            }
            case gewonnen: {
                drawText(screenEdgeSize, screenEdgeSize, 10, "Gewonnen");
                break;
            }
            case verloren: {
                drawText(screenEdgeSize, screenEdgeSize, 10, "Verloren");
                break;
            }
        }
    }

    void coloringMap(int x, int y, Player gegner, Player.Zustand selectedZustand) {
        gegner.mapKarte[x][y] = selectedZustand;
    }

    void drawMap(Player player, Boolean isEnemy, boolean shipsVisible) {
        float mapPosX;
        if (isEnemy) {
            mapPosX = spielfeldGegner;
        } else {
            mapPosX = screenEdgeSize;
        }
        boolean visible = player.isShipVisible();
        player.setShipVisible(shipsVisible);
        String buchstaben = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int iy = 0; iy < mapSize; iy++) {
            for (int jx = 0; jx < mapSize; jx++) {
                switch (player.getMapKarteTeil(jx, iy)) {
                    case water:
                        fill(0, 0, 255);
                        break;
                    case miss:
                        fill(122, 122, 255);
                        break;
                    case deadShip:
                        fill(255, 0, 0);
                        break;
                    case aliveShip:
                        fill(0, 255, 0);
                        break;
                }
                rect(mapPosX + jx * tileSize, screenEdgeSize + iy * tileSize, tileSize * tileVerkleinerungFaktor, tileSize * tileVerkleinerungFaktor);
                drawText(mapPosX + jx * tileSize, screenEdgeSize + iy * tileSize, 1.5f, "" + buchstaben.charAt(iy) + (jx + 1));
            }
        }
        player.setShipVisible(visible);
    }

    void drawShipSelect() {
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

    void drawZustandSelect() {
        float tilesPosY = screenEdgeSize;
        float tilesPosX = infobereich + 2 * infobereichAbstandTiles + tileSize;
        // Water
        fill(0, 0, 255);
        rect(tilesPosX, tilesPosY + 0 * tileSize, tileSize * tileVerkleinerungFaktor, tileSize * tileVerkleinerungFaktor);
        // Miss
        fill(122, 122, 255);
        rect(tilesPosX, tilesPosY + 1 * tileSize, tileSize * tileVerkleinerungFaktor, tileSize * tileVerkleinerungFaktor);
        // AliveShip
        fill(0, 255, 0);
        rect(tilesPosX, tilesPosY + 2 * tileSize, tileSize * tileVerkleinerungFaktor, tileSize * tileVerkleinerungFaktor);
        // DeadShip
        fill(255, 0, 0);
        rect(tilesPosX, tilesPosY + 3 * tileSize, tileSize * tileVerkleinerungFaktor, tileSize * tileVerkleinerungFaktor);
    }

    void drawSchiffeSetzen(Player player, boolean enemy) {
        float mapPosX;
        if (enemy) {
            mapPosX = spielfeldGegner;
        } else {
            mapPosX = screenEdgeSize;
        }
        if (dreierSchiffe > 0 || zweierSchiffe > 0 || einerSchiffe > 0) {
            selectedShipLength = selectShip();
            if (selectedShipLength > -1) {

                drawSelectedShip(selectedShipLength, mapPosX, screenEdgeSize);
                int schiffX = (int) mousePosOnMap(mapPosX, screenEdgeSize).x;
                int schiffY = (int) mousePosOnMap(mapPosX, screenEdgeSize).y;
                if (mouseLeftClick && schiffX > -1 && schiffY > -1) {
                    if (player.addSchiff(schiffX, schiffY, selectedShipLength, selectedShipDirection)) {
                        switch (selectedShipLength) {
                            case 1:
                                einerSchiffe--;
                                break;
                            case 2:
                                zweierSchiffe--;
                                break;
                            case 3:
                                dreierSchiffe--;
                                break;
                        }
                    }
                }
            }
        }
    }

    void selectZustand(){

        // Water
        if (fieldPressed(infobereich + 2 * infobereichAbstandTiles + tileSize, screenEdgeSize + 0 * tileSize, infobereich + 2 * infobereichAbstandTiles + 2 * tileSize, screenEdgeSize + 1 * tileSize)) {
            selectedZustand = Player.Zustand.water;
        }
        // Miss
        else if (fieldPressed(infobereich + 2 * infobereichAbstandTiles + tileSize, screenEdgeSize + 1 * tileSize, infobereich + 2 * infobereichAbstandTiles + 2 * tileSize, screenEdgeSize + 2 * tileSize)) {
            selectedZustand = Player.Zustand.miss;
        }
        // AliveShip
        else if (fieldPressed(infobereich + 2 * infobereichAbstandTiles + tileSize, screenEdgeSize + 2 * tileSize, infobereich + 2 * infobereichAbstandTiles + 2 * tileSize, screenEdgeSize + 3 * tileSize)) {
            selectedZustand = Player.Zustand.aliveShip;
        }
        // DeadShip
        else if (fieldPressed(infobereich + 2 * infobereichAbstandTiles + tileSize, screenEdgeSize + 3 * tileSize, infobereich + 2 * infobereichAbstandTiles + 2 * tileSize, screenEdgeSize + 4 * tileSize)) {
            selectedZustand = Player.Zustand.deadShip;
        }
    }

    int selectShip() {
        //schiff 3
        if (fieldPressed(infobereich + infobereichAbstandTiles, screenEdgeSize, infobereich + infobereichAbstandTiles + tileSize, screenEdgeSize + 3 * tileSize)) {
            return 3;
        }
        //schiff 2
        else if (fieldPressed(infobereich + 2 * infobereichAbstandTiles + tileSize, screenEdgeSize, infobereich + 2 * infobereichAbstandTiles + 2 * tileSize, screenEdgeSize + 2 * tileSize) && zweierSchiffe > 0) {
            return 2;
        }
        //schiff 1
        else if (fieldPressed(infobereich + 3 * infobereichAbstandTiles + tileSize, screenEdgeSize, infobereich + 3 * infobereichAbstandTiles + 3 * tileSize, screenEdgeSize + 1 * tileSize) && einerSchiffe > 0) {
            return 1;
        } else if (einerSchiffe == 0 && selectedShipLength == 1 || zweierSchiffe == 0 && selectedShipLength == 2 || dreierSchiffe == 0 && selectedShipLength == 3) {
            return 0;
        }

        return selectedShipLength;
    }

    // PVector ist eine Koordinate und dann kann ich "mouseOnMap.x" machen.
    PVector mousePosOnMap(float mapPosX, float mapPosY) {
        if (mouseX > mapPosX && mouseX < mapPosX + mapSize * tileSize && mouseY > mapPosY && mouseY < mapPosY + mapSize * tileSize) {
            int tileOnMapX = (int) ((mouseX - mapPosX) / tileSize);
            int tileOnMapY = (int) ((mouseY - mapPosY) / tileSize);
            // ich kriege die Nummer des Teils also das wievielte es ist.
            return new PVector(tileOnMapX, tileOnMapY);
        }
        return new PVector(-1, -1);
    }

    void drawSelectedShip(int selectedShip, float mapPosX, float mapPosY) {
        float mouseOnMapX = mousePosOnMap(mapPosX, mapPosY).x;
        float mouseOnMapY = mousePosOnMap(mapPosX, mapPosY).y;
        for (int i = 0; i < selectedShip; i++) {
            fill(20, 100, 20, 200);
            if (selectedShipDirection == Schiff.Richtung.vertikal) {
                if (mouseOnMapX == -1 && mouseOnMapY == -1) {
                    rect(mouseX - tileSize / 2, mouseY - tileSize / 2 + i * tileSize, tileSize * tileVerkleinerungFaktor, tileSize * tileVerkleinerungFaktor);
                } else {
                    rect(mapPosX + mouseOnMapX * tileSize, mapPosY + mouseOnMapY * tileSize + i * tileSize, tileSize * tileVerkleinerungFaktor, tileSize * tileVerkleinerungFaktor);
                }
            } else {
                if (mouseOnMapX == -1 && mouseOnMapY == -1) {
                    rect(mouseX - tileSize / 2 + i * tileSize, mouseY - tileSize / 2, tileSize * tileVerkleinerungFaktor, tileSize * tileVerkleinerungFaktor);
                } else {
                    rect(mapPosX + mouseOnMapX * tileSize + i * tileSize, mapPosY + mouseOnMapY * tileSize, tileSize * tileVerkleinerungFaktor, tileSize * tileVerkleinerungFaktor);
                }
            }
        }
    }

    boolean fieldPressed(float vonX, float vonY, float bisX, float bisY) {
        return mouseLeftClick && mouseX > vonX && mouseX < bisX && mouseY > vonY && mouseY < bisY;
    }

    float drawText(float x, float y, float size, String text) {
        fill(255);
        textSize(size * screenEdgeSize / 10);
        text(text, x, y + size * screenEdgeSize / 10);
        return y + size * screenEdgeSize / 10;
    }


    // Events. Die laufen automatisch
    public void mousePressed() {
        if (mouseButton == LEFT) {
            mouseLeftClick = true;
        }
        if (mouseButton == RIGHT && selectedShipDirection == Schiff.Richtung.horizontal) {
            selectedShipDirection = Schiff.Richtung.vertikal;

        } else if (mouseButton == RIGHT && selectedShipDirection == Schiff.Richtung.vertikal) {
            selectedShipDirection = Schiff.Richtung.horizontal;
        }
    }

    public void mouseReleased() {
        mouseLeftClick = false;
    }

    @Override
    public void keyPressed() {
        pressedKey = key;
    }

    @Override
    public void keyReleased() {
        pressedKey = 0;
    }
}