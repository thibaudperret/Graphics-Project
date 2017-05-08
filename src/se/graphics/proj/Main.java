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
    private final static int resolution = 100;
    
    /**
     * The focal length of the camera
     */
    private final static float f = resolution;
    
    /**
     * The static camera
     */
    private final static Vector3 camera = new Vector3(0f, 0f, -3.1f);
    
    /**
     * The number of rays
     */
    private final static int n = 10;
    
    /**
     * The max number of rebounds of a ray
     */
    private final static int numberRebounds = 1;
    
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
        
        for (int x = 0; x < resolution; ++x) {
            for (int y = 0; y < resolution; ++y) {
                Ray r = new Ray(camera, new Vector3(x - resolution / 2, y - resolution / 2, f));
                Vector3 color = Vector3.zeros();
                for (int i = 0; i < n; ++i) {
                    color = color.plus(tracePath(r, numberRebounds).times(1f / n));
                }

                drawPixel(x, y, color);
            }
        }
        
        long dt = System.currentTimeMillis() - t;
        System.out.println("time " + dt + " ms");
    }
    
    private static Vector3 tracePath(Ray ray, int numberSteps) {
        if (numberSteps == 0) {
            return Color.BLACK;
        }
        
        Intersection intersection = Intersection.invalidIntersection();
        Item closest = null;
        
        for (Item item : Loader.testModel()) {
            Intersection current = item.intersection(ray);
            if (current.valid() && current.distance() < intersection.distance()) {
                intersection = current;
                closest = item;
            }
        }
        
        if (intersection.valid()) {
            if (closest.isLight()) {
                System.out.println("YO");
                Light light = closest.asLight();
                return light.color().times(2 * light.power());
            } else {
                Vector3 normal = closest.shape().normalAt(intersection.position());
                Ray rebound = Ray.generateRandomRay(intersection.position(), normal);
                Material material = closest.asPhysicalObject().material();
                
                Vector3 rrrr = tracePath(rebound, numberSteps - 1).times(2 * (1 - material.absorptionCoef()) * material.diffuseCoef() * normal.dot(rebound.direction()));
                return rrrr;
            }
        } else {
            System.out.println("BLAKC");
            return Color.BLACK;
        }
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
