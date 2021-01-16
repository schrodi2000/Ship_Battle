package com.schrodi;
import processing.core.PApplet;
public class Window extends PApplet{
    int x,y;
    public Window(int x,int y){
        this.x = x;
        this.y = y;
        String[] processingArgs = {"title"};
        PApplet.runSketch(processingArgs, this);
    }
    @Override
    public void settings() {
        size(x,y);
    }
    @Override
    public void draw() {
        background(140);
    }
    @Override
    public void mousePressed() {

    }
}
