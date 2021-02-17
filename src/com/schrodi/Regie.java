package com.schrodi;

import processing.core.PApplet;

public class Regie extends PApplet {
    public enum GameState {
        schiffeSetzen,
        spielenGegenSpieler,
        spielenGegenOnline,
        spielenGegenKi,

        kiGegenKi,
        spielEnde
    }

    GameState spielStatus;
    float seitenVerhaeltniss;
    String[] verlauf = new String[10];
    Player s,g;

    public Regie(Player s, Player g) {
        this.s = s;
        this.g = g;
        seitenVerhaeltniss = 16/9f;
        width = 1920;
        height = (int) (width / seitenVerhaeltniss);
        spielStatus = GameState.schiffeSetzen;
        String[] processingArgs = {"Schiffe-versenken"};
        PApplet.runSketch(processingArgs, this);
    }

    public void settings() {
        size(width, height);
    }

    public void setup(){
        surface.setResizable(true);
    }

    public void draw() {
        seitenVerhaeltniss = (float)width/(float)height; // Seitenverhältnis aktualisieren
        drawWindow(s, g);
    }

    void seitenverhaeltnissBeibehalten(){
        width = width;
        surface.setSize(width, (int) (width * 0.5625));
    }

    void drawWindow(Player spieler, Player gegner) {
        switch (spielStatus) {
            case schiffeSetzen: {
                drawMap(spieler, false);
                drawMap(gegner, true);

                drawSchiffeliste();
            }
        }
    }

    void drawMap(Player player, Boolean isEnemy) {
        int mapPosX;
        float tileHeight = height *seitenVerhaeltniss*(30/700f);
        float tileWidth = width *(30/700f);
        int mapPosY = (int)(width *0.10);
        if (isEnemy) {
            mapPosX = (int)(width *0.60);
        } else {
            mapPosX = (int)(width *0.10);
            mapPosY = mapPosX;
        }
        for(int iy = 0; iy<7; iy++){
            for(int jx = 0; jx<7; jx++){
                switch(player.getMapKarteTeil(iy,jx)){
                    case water -> fill(0,0,255);
                    case miss -> fill(122,122,255);
                    case deadShip -> fill(255,0,0);
                    case aliveShip -> fill(0,255,0);
                }
                rect(mapPosX+jx*tileWidth,mapPosY+iy*tileHeight, (int)tileWidth-10, (int)tileHeight-10);
            }
        }

    }

    void drawSchiffeliste() {

    }

    void addInfo() {

    }
}