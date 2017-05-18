package se.graphics.proj;

import java.util.List;

import processing.core.PApplet;

/**
 * Main class for the project
 */
public class Main extends PApplet {
    
    /**
     * The size of the window, not to be confused with the resolution
     */
    private final static int size = 600;
    
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
    private final static int n = 100;
    
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
                Vector3 color = radiance(r);
                drawPixel(x, y, color);
            }
        }
        
        float dt = (System.currentTimeMillis() - t) / 1000f;
        System.out.println("time " + dt + " s");
    }
    
    private static Vector3 tracePath(Ray ray, int numberSteps) {
//        if (numberSteps == 0) {
//            return Color.BLACK;
//        }
        
        Intersection intersection = Intersection.invalidIntersection();
        Item closest = null;
        
        for (Item item : Loader.cornellBox()) {
            Intersection current = item.intersection(ray);
            if (current.valid() && current.distance() < intersection.distance()) {
                intersection = current;
                closest = item;
            }
        }
        
        if (intersection.valid()) {
            float r = (float) Math.random(); // LOTO TICKET
            
            if (closest.isLamp()) {
                Lamp light = closest.asLamp();
                return light.color().times(1f);
            } else { 
                if (r < 0.5 || numberSteps == 0) { // LAST REBOUND OR LOTO WINNER
                    return closest.asPhysicalObject().material().reflectance().times(1f);
                } else {
                    Vector3 normal = closest.shape().normalAt(intersection.position());
                    
                    if (normal.dot(ray.direction()) > 0) {
                        normal = normal.times(-1f);
                    }
                    
                    Ray rebound = Ray.generateRandomRay(intersection.position(), normal);
                    Material material = closest.asPhysicalObject().material();
                    
                    float f = 2 * (1 - material.absorptionCoef()) * material.diffuseCoef() * normal.dot(rebound.direction());
                    return tracePath(rebound, numberSteps - 1).times(1f);
                }
            }
        } else {
            return Color.BLACK;
        }
    }

    private static Vector3 tracePathAugmented (Ray ray,int numberSteps) {        
        if (numberSteps == 0) {
            return Color.BLACK;
        }
        
        Intersection intersection = Intersection.invalidIntersection();
        Item closest = null;
        
        for (Item item : Loader.cornellBox()) {
            Intersection current = item.intersection(ray);
            if (current.valid() && current.distance() < intersection.distance() && !item.isLight()) {
                intersection = current;
                closest = item;
            }
        }
        
        if (intersection.valid()) {
            float r = (float) Math.random();
            Vector3 shapeNormal = closest.shape().normalAt(intersection.position());
            Vector3 position = intersection.position();
            Vector3 directLight = DirectLight(position, shapeNormal);
            
            if (closest.isLamp()) {
                Lamp lamp = closest.asLamp();
                float pEmit = 0.9f;
                if(r < 0.9) {
                    return lamp.color().times(lamp.power() * (1/pEmit));
                } else {
                    Vector3 normal = closest.shape().normalAt(intersection.position());
                    Ray rebound = Ray.generateRandomRay(intersection.position(), normal);
                    Material material = closest.asLamp().material();
                    
                    float f = (1/(1-pEmit)) * (1 - material.absorptionCoef()) * material.diffuseCoef() * normal.dot(rebound.direction());
                    return tracePathAugmented(rebound, numberSteps - 1)/*.times(f)*/;
                }
            } else  {
                if (r < 0.5) {
                    return closest.asPhysicalObject().material().reflectance().entrywiseDot(directLight);
                } else {
                    Vector3 normal = closest.shape().normalAt(intersection.position());
                    Ray rebound = Ray.generateRandomRay(intersection.position(), normal);
                    Material material = closest.asPhysicalObject().material();
                    
                    float f = 2 * (1 - material.absorptionCoef()) * material.diffuseCoef() * normal.dot(rebound.direction());
                    return tracePathAugmented(rebound, numberSteps - 1)/*.times(f)*/;
                }
            }
        } else {
            return Color.BLACK;
        }
    }
    
    private static Vector3 radiance(Ray ray) {
        
        Vector3 accuColor = Vector3.zeros();
        Vector3 mask = Vector3.ones();
        Ray r = ray;
        
        Intersection primaryIntersection = Intersection.invalidIntersection();
        Item primaryClosest = null;
        
        for (Item item : Loader.cornellBox()) {
            Intersection cur = item.intersection(r);
            if (cur.valid() && cur.distance() < primaryIntersection.distance()) {
                primaryIntersection = cur;
                primaryClosest = item;
            }
        }
        
        if(!primaryIntersection.valid()) {  
            return Color.BLUE;  
        } 
        
        
        for(int j = 0; j < n; ++j) {
            r = Ray.generateRandomRay(primaryIntersection.position(), primaryClosest.shape().normalAt(primaryIntersection.position()));            
            Vector3 color = Vector3.zeros();
            for(int i = 0; i < numberRebounds; ++i) {
                
                
                Intersection intersection = Intersection.invalidIntersection();
                Item closest = null;
                
                for (Item item : Loader.cornellBox()) {
                    Intersection current = item.intersection(r);
                    if (current.valid() && current.distance() < intersection.distance()) {
                        intersection = current;
                        closest = item;
                    }
                }
                
                if(!intersection.valid()) {                
                    color = Vector3.zeros();                
                } else {
                    Vector3 normal = closest.shape().normalAt(intersection.position());
                    normal = normal.dot(ray.direction()) > 0 ? normal.times(-1f) : normal;
                    if(closest.isPhysical()) {
                        color = mask.entrywiseDot(closest.asPhysicalObject().material().reflectance());
                        
                    } else if (closest.isLamp()) {
                        color = mask.entrywiseDot(closest.asLamp().material().reflectance().times(closest.asLamp().power()));
                    } else {
                        color = mask.entrywiseDot(closest.asLight().color().times(closest.asLight().power()));
                    }
                    
                    r = Ray.generateRandomRay(intersection.position(), normal);
                    mask.entrywiseDot(color);
                    mask.times(2 * r.direction().dot(normal));                
                }
            }
            accuColor = accuColor.plus(color);
        }
        
        return accuColor.times(1/(float)(n));        
    }
    
    /**
     * Draws a pixel at (i, j) in the window of resolution by resolution
     * @param i     - the x coordinate
     * @param j     - the y coordinate
     * @param color - the color of the pixel
     */
    public void drawPixel(int i, int j, Vector3 color) {
        int ratio = size / resolution;
        
        if (color.x() > 1 || color.y() > 1 || color.z() > 1) {
            System.out.println("YOYOYO");
        }
        
        fill(255 * color.x(), 255 * color.y(), 255 * color.z());
        rect(i * ratio, j * ratio, ratio, ratio);
    }
    
    public void mousePressed() {
        String name = System.currentTimeMillis() + ".png";
        System.out.println("saving image as " + name);
        saveFrame(name);
    }
    
    private static Vector3 DirectLight(Vector3 position, Vector3 normal) {
        Vector3 totalIllumination = Vector3.zeros();
        Vector3 illumination;
        List<Item> lightSources = Loader.lightSources();
        for (int i = 0; i < lightSources.size(); ++i) {

            Item currentSource = lightSources.get(i);
            Shape currentShape = currentSource.shape();
            Vector3 directionToLight = position.minus(currentShape.getCenter());
            float r = directionToLight.size();
            directionToLight = directionToLight.normalise();

            if (currentSource.isLamp()) {
                Lamp currentLamp = currentSource.asLamp();
                illumination = currentLamp.color().times(currentLamp.power());
                illumination = illumination.times((float) (Math.max(directionToLight.dot(normal), (float) 0) / (4 * Math.PI * r * r)));
            } else {
                Light currentLight = currentSource.asLight();
                illumination = currentLight.color().times(currentLight.power());
                illumination = illumination.times((float) (Math.max(directionToLight.dot(normal), (float) 0) / (4 * Math.PI * r * r)));
            }
            totalIllumination = totalIllumination.plus(illumination);
        }

        return totalIllumination;
    }
}

