package se.graphics.proj;


import processing.core.PApplet;

public class Main extends PApplet {
    
    private final static int SIZE = 1600;
    private final static int RESOLUTION = 400;
    
    private Vector3 C = new Vector3(0f, 0f, -3.1f);
    
    public static void main(String[] args) {
        PApplet.main("se.graphics.proj.Main");
    }
    
    public void settings() {
        size(SIZE, SIZE);
    }
    
    public void setup() {
        background(0);
        noStroke();
    }
    
    public void draw() {
        long t = System.currentTimeMillis();
        background(0);
        
        long dt = t - System.currentTimeMillis();
        System.out.println("time " + dt + " ms");
    }
    
    public void mousePressed() {
        String name = System.currentTimeMillis() + ".png";
        System.out.println("saving image as " + name);
        saveFrame(name);
    }
    
}
