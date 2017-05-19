package se.graphics.proj;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

/**
 * Main class for the project
 */
public class Main extends PApplet {
    
    /**
     * The size of the window, not to be confused with the resolution
     */
    private final static int size = 800;
    
    /**
     * The resolution of the windo, not to be confused with the size
     */
    private final static int resolution = 400;
    
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
    private final static int numberRays = 200;
    
    /**
     * The max number of rebounds of a ray
     */
    private final static int numberRebounds = 2;
    
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
        
        //Better to compute the cornellBox once and then pass it as an argument right?
        final List<Item> box = Loader.cornellBox();

/*=========================== PARALLEL VERSION ===========================*/
 /* In this parallel version, we create a List containing all the pixels (stored as pairs, sorry tits..)
  * Then we create a parallel stream so that we can apply par. methods such as forEach where we do the same job as 
  * in the non-parallel version.
  * SPEEDUP : between 2 and 2.5.
  * PROBLEM : we get aliasing from nowhere.. 
  * TODO : fix it.
 */
        
        List<Pair<Integer, Integer>> parList = new ArrayList<Pair<Integer, Integer>>();
        
        for (int x = 0; x < resolution; ++x) {
            for (int y = 0; y < resolution; ++y) {
                Pair<Integer, Integer> p = new Pair<Integer, Integer>(x, y);
                parList.add(p);
            }
        }      
        
        parList.parallelStream().forEach(i->{
            Ray r = new Ray(camera, new Vector3(i.getLeft() - resolution / 2, i.getRight() - resolution / 2, f));
            Vector3 color = radiance2(r, box);
            drawPixel(i.getLeft(), i.getRight(), color);
        });
        
        
/*=========================== VERSION 1.0 ===========================*/
//        for (int x = 0; x < resolution; ++x) {
//            for (int y = 0; y < resolution; ++y) {
//                Ray r = new Ray(camera, new Vector3(x - resolution / 2, y - resolution / 2, f));
//                Vector3 color = radiance2(r, box);
//                drawPixel(x, y, color);
//            }
//        }
        
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
            Vector3 directLight = directLight(position, shapeNormal);
            
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
        
        Vector3 accuColor = Color.BLACK;
        Vector3 totalColor = Color.BLACK;
        Vector3 mask = Color.WHITE;
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
        
       
        for(int j = 0; j < numberRays ; ++j) {
            
            accuColor = Color.BLACK;
            mask = Color.WHITE;
            
            accuColor = accuColor.plus(primaryClosest.emittedLight());
            Vector3 primaryNormal = primaryClosest.shape().normalAt(primaryIntersection.position());
            primaryNormal = ((primaryNormal.dot(ray.direction())) > 0 ? (primaryNormal.times(-1)) : primaryNormal);
            mask = mask.entrywiseDot(primaryClosest.color());
            mask = mask.times(2 * r.direction().dot(primaryNormal));            
            r = Ray.generateRandomRay(primaryIntersection.position(), primaryNormal);
            Vector3 color = Color.BLACK;
            
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
                    color = Color.BLACK;                
                } else {
                    
                    Vector3 normal = closest.shape().normalAt(intersection.position());
                    normal = ((normal.dot(r.direction())) > 0 ? (normal.times(-1)) : normal);
                    color = closest.color();                    
                    r = Ray.generateRandomRay(intersection.position(), normal);
                    mask = mask.entrywiseDot(color);
                    mask = mask.times(2 * r.direction().dot(normal));    
                    accuColor = accuColor.plus(mask.entrywiseDot(closest.emittedLight()));

                }
            }             
            //totalColor = totalColor.plus(accuColor.times(1f / numberRebounds));
            totalColor = (totalColor.size() < accuColor.times(1f/numberRebounds).size()) ? accuColor.times(1f/ numberRebounds) : totalColor;
        }
        
        //return totalColor.times(1f/n);        
        return totalColor;
    }
    
    private static Vector3 radiance2(Ray originalRay, final List<Item> box) {
        Vector3 totalColor = Color.BLACK;
        
        for (int i = 0; i < numberRays; ++i) {
            Ray ray = originalRay;
            Vector3 accuColor = Color.BLACK;
            Vector3 mask = Color.WHITE;
            
            for (int b = 0; b < numberRebounds; ++b) {               
                Intersection intersection = Intersection.invalidIntersection();
                Item closest = null;
                
                for (Item item : box) {
                    Intersection cur = item.intersection(ray);
                    if (cur.valid() && cur.distance() < intersection.distance()) {
                        intersection = cur;
                        closest = item;
                    }
                }
                
                if (!intersection.valid()) {  
                    // accuColor = accuColor.plus(Color.BLACK);
                    b = numberRebounds;
                } else {
                    if (closest.isLamp()) {
                        accuColor = accuColor.plus(mask.entrywiseDot(closest.emittedLight()));
                    } else {
                        accuColor = accuColor.plus(mask.entrywiseDot(directLight(intersection.position(), closest.shape().normalAt(intersection.position()))));
                    }

                    Vector3 normal = closest.shape().normalAt(intersection.position());
                    normal = ((normal.dot(ray.direction())) > 0 ? (normal.times(-1)) : normal);

                    ray = Ray.generateRandomRay(intersection.position(), normal);

                    mask = mask.entrywiseDot(closest.color()).times(ray.direction().dot(normal) * 2);
                }
            }
            
            totalColor = totalColor.plus(accuColor);
        }
        
        return totalColor.times(1f / numberRays);
    }
    
    
    private static Vector3 radiance3(Ray originalRay) {
        Vector3 totalColor = Color.BLACK;
        
        for (int i = 0; i < numberRays; ++i) {
            Ray ray = originalRay;
            Vector3 accuColor = Color.BLACK;
            Vector3 mask = Color.WHITE;
            
            for (int b = 0; b < numberRebounds; ++b) {               
                Intersection intersection = Intersection.invalidIntersection();
                Item closest = null;
                
                for (Item item : Loader.cornellBox()) {
                    Intersection cur = item.intersection(ray);
                    if (cur.valid() && cur.distance() < intersection.distance()) {
                        intersection = cur;
                        closest = item;
                    }
                }
                
                if (!intersection.valid()) {  
                    // accuColor = accuColor.plus(Color.BLACK);
                    b = numberRebounds;
                } else {
                    
                    float absorbCoef = 0;
                    float specularCoef = 0;
                    float diffuseCoef = 0;
                    
                    Vector3 normal = closest.shape().normalAt(intersection.position());
                    normal = ((normal.dot(ray.direction())) > 0 ? (normal.times(-1)) : normal);
                    
                    if (closest.isLamp()) {
                       
                        absorbCoef = 0;
                        specularCoef = closest.asLamp().material().specularCoef();
                        diffuseCoef = 1 - specularCoef;
                        
                    } else if(closest.asPhysicalObject().material().isOpaque()){
                       
                       absorbCoef = closest.asPhysicalObject().material().absorptionCoef();
                       specularCoef = closest.asPhysicalObject().material().specularCoef();
                       diffuseCoef = 1 - specularCoef;
                       

                    }
                    
                    float randomAbs = (float)Math.random();
                    float randomSpec = (float)Math.random();
                    float bias = 0f;
                    
                    if((!closest.isPhysical() || closest.asPhysicalObject().material().isOpaque())) {
                        
                        //Opaque object or lamp, there is no light transmission possible
                        
                        if (randomAbs < absorbCoef) { 
                            b = numberRebounds;
                        } else {                            
                            if(randomSpec < specularCoef) {
                                bias = (1f / ((1 - absorbCoef) * specularCoef));
                                ray = Ray.SpecularBounce(ray, normal, intersection.position());
                                mask = mask.entrywiseDot(closest.color()).times(ray.direction().dot(normal) * 2 * bias);
                            } else {                                
                                bias = (1f / ((1 - absorbCoef) * diffuseCoef));
                                ray = Ray.generateRandomRay(intersection.position(), normal);
                                mask = mask.entrywiseDot(closest.color()).times(ray.direction().dot(normal) * 2 * bias);
                            }
                        } 
                        
                        if(closest.isLamp()) {
                            accuColor = accuColor.plus(mask.entrywiseDot(closest.emittedLight().times(bias)));
                        } else {
                            accuColor = accuColor.plus(mask.entrywiseDot(directLight(intersection.position(), closest.shape().normalAt(intersection.position())).times(bias)));
                        }
                        
                    } else {
                        
                        //Transparent object, we need to take the transmission into account
                        float randomTrans = (float)Math.random();
                        float transmissionCoef = Medium.computeTransmissionCoef(ray.direction(), normal);
                        
                        if(randomAbs < absorbCoef) {
                            b = numberRebounds;
                        } else {
                            if(randomTrans < transmissionCoef) {
                                bias = 1f/((1 - absorbCoef) * transmissionCoef); 
                                //TODO change ray using n1.sin(i1) = n2.sin(i2) 
                            } else {
                                if(randomSpec < specularCoef) {
                                    bias = 1f/((1 - absorbCoef) * (1-transmissionCoef) * specularCoef);
                                    ray = Ray.SpecularBounce(ray, normal, intersection.position());
                                    mask = mask.entrywiseDot(closest.color()).times(ray.direction().dot(normal) * 2 * bias);
                                } else {
                                    bias = 1f/((1 - absorbCoef) * (1-transmissionCoef) * diffuseCoef);
                                    ray = Ray.generateRandomRay(intersection.position(), normal);
                                    mask = mask.entrywiseDot(closest.color()).times(ray.direction().dot(normal) * 2 * bias);
                                }                                
                            }
                            accuColor = accuColor.plus(mask.entrywiseDot(directLight(intersection.position(), closest.shape().normalAt(intersection.position())).times(bias)));
                        }                    
                    }
                }
            }
            
            totalColor = totalColor.plus(accuColor);
        }
        
        return totalColor.times(1f / numberRays);
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
    
    private static Vector3 directLight(Vector3 position, Vector3 normal) {
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

