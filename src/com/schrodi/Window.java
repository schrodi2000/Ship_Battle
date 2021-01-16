package com.schrodi;
import processing.core.PApplet;
public class Window extends PApplet{
    private float sizeX, sizeY,field;
    public Window(int field, String title, int sizeX, int sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.field = field;
        String[] processingArgs = {title};
        PApplet.runSketch(processingArgs, this);
    }
    @Override
    public void settings() {
        size((int)sizeX, (int)sizeY);
    }
    @Override
    public void draw() {
        rectMode(RADIUS);
        for(int i=1; i<=field; i++){
            for(int m=1; m<=field; m++){
                rect(i*(sizeX /field), m*(sizeY /field), sizeX /field, sizeY /field);
            }
        }
    }
    @Override
    public void mousePressed() {

    }
}
