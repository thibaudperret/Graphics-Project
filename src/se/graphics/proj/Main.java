package se.graphics.proj;

import processing.core.PApplet;

/**
 * Main class for the project
 */
public class Main extends PApplet {
    
    /**
     * The size of the window, not to be confused with the resolution
     */
    private final static int size = 1600;
    /**
     * The resolution of the windo, not to be confused with the size
     */
    private final static int resolution = 400;
    
    /**
     * The static camera
     */
    private final static Vector3 camera = new Vector3(0f, 0f, -3.1f);
    
    public static void main(String[] args) {
        PApplet.main("se.graphics.proj.Main");
    }
    
    public void settings() {
        size(size, size);
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
    
    /**
     * Draws a pixel at (i, j) in the window of resolution by resolution
     * @param i     - the x coordinate
     * @param j     - the y coordinate
     * @param color - the color of the pixel
     */
    public void drawPixel(int i, int j, Vector3 color) {
        int ratio = size / resolution;
        
        fill(255 * color.x(), 255 * color.y(), 255 * color.z());
        rect(i * ratio, j * ratio, ratio, ratio);
    }
    
    public void mousePressed() {
        String name = System.currentTimeMillis() + ".png";
        System.out.println("saving image as " + name);
        saveFrame(name);
    }
}
