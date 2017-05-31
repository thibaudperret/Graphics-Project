package se.graphics.proj;

import item.DiffuseLamp;
import item.DirectionalLamp;
import item.Item;
import item.Lamp;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import material.Medium;
import math.Intersection;
import math.Vector3;
import processing.core.PApplet;
import tree.Tree;
import util.Color;
import util.MaxHeap;
import util.Pair;

/**
 * Main class for the project
 */
public class Main extends PApplet {

    /**
     * The size of the window, not to be confused with the resolution
     */
    private final static int size = 800;

    /**
     * The resolution of the window, not to be confused with the size
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
    private final static int numberRays = 10;

    /**
     * The max number of rebounds of a ray
     */
    private final static int numberRebounds = 3;
    
    /**
     * The number of photons generated in the photon map
     */
    private final static int nbPhotons = 5000;
    

    public static void main(String[] args) {
        PApplet.main("se.graphics.proj.Main");
    }

    public void settings() {
        size(size, size);
    }

    public void setup() {
        background(0);
        noStroke();
        Loader.loadItems();

    }

    public void draw() {
        photonMapping();
        
    }

    public void photonMapping(){
        long t = System.currentTimeMillis();
        background(0);

        final List<Item> box = Loader.cornellBox();
        final List<Lamp> lamps = Loader.lightSources();
        List<Photon> photons = new ArrayList<>();
        
        for (Lamp lamp : lamps) {
            photons.addAll(lamp.emittedPhotons(nbPhotons, box, numberRebounds));
        }
        
        Tree tree = Tree.balance(photons);
        
        for (int i = 0; i < resolution; ++i) {
            for (int j = 0; j < resolution; ++j) {
                
                Ray ray = new Ray(camera, new Vector3(i - resolution / 2, j - resolution / 2, f));
                Pair<Intersection, Item> pair = getClosestIntersection(ray, box);
                Intersection intersection = pair.getLeft();
                Item item = pair.getRight();
                
                if (intersection.valid()) {
                    Vector3 normal = item.shape().normalAt(intersection.position());
                    normal = ((normal.dot(ray.direction())) > 0 ? (normal.times(-1)) : normal);
                    //Vector3 color = mdRendering(tree, numberRays, ray, box);
                    Vector3 color = photonMappingRadiance(intersection, item, tree, normal, box);
                    //Vector3 color = directLight(intersection.position(), normal, box);
                    drawPixel(i, j, color);
                }
            }
        }
        System.out.println(photons.size());

        float dt = (System.currentTimeMillis() - t) / 1000f;
        System.out.println("time " + dt + " s");
    }
    
    private Vector3 getRadianceAt(Vector3 position, Tree tree, Vector3 normal) {
       
        MaxHeap maxHeap = tree.nearestPhotons(nbPhotons/10, position, 0.3f);
       
        if (maxHeap.inserted() <= 0) {
            return Color.BLACK;
        }

        List<Pair<Photon, Float>> nearest = maxHeap.asList();
//        List<Photon> nearest = photons.stream().sorted((p1, p2) -> {
//            float dist1 = p1.position().minus(position).size();
//            float dist2 = p2.position().minus(position).size();
//            return Float.compare(dist1, dist2);
//        }).collect(Collectors.toList()).subList(0, 2);
        
        float weight = 0f;
        float maxDistance = maxHeap.rootDistance();
        float brdf = (float)(1/(2*Math.PI));
        //float brdf = 1f;
        Vector3 total = Vector3.zeros();
        
        for (Pair<Photon, Float> pair : nearest) {
            Photon photon = pair.getLeft();
            Vector3 photonDir = Ray.sphericalToCartesian(photon.theta(), photon.phi()).normalise();
            weight = 1 - pair.getRight()/maxDistance;
            if(weight < 0) {
                System.out.println("Beurp");
            }
            normal = photonDir.dot(normal) < 0 ? normal.times(-1) : normal;
            total = toColor(total.plus(photon.power().times( brdf * weight * photonDir.dot(normal))));
            if(total.x() < 0 || total.y() < 0 || total.z() < 0) {
                System.out.println("neg");
            }
            //total = total.plus(toColor(photon.power().times(-brdf * photonDir.dot(normal))));
        }
        
        //total = total.times((float)((1/3f) * Math.PI * maxDistance * maxDistance));
        total = toColor((total).times((float)(1/( 3 * Math.PI * maxDistance * maxDistance))));
        return total;
    }
    
    public static Vector3 toColor(Vector3 color) {
        float a = Float.max(color.x(), Float.max(color.y(), color.z()));
        if(color.x() < 0 || color.y() < 0 || color.z() < 0) {
            System.out.println("neg");
            return Color.BLACK;
        }
        if (a > 1) {
            color = color.times(1 / a);
        }
        return color;
    }
    
    /* PROBLEM : we get aliasing. TODO : fix it. */
    public void pathTracingParallel() {
        long t = System.currentTimeMillis();
        background(0);
        final List<Item> box = Loader.cornellBox();

        List<Pair<Integer, Integer>> parList = new ArrayList<Pair<Integer, Integer>>();

        // Put every pixel in a list
        for (int x = 0; x < resolution; ++x) {
            for (int y = 0; y < resolution; ++y) {
                Pair<Integer, Integer> p = new Pair<Integer, Integer>(x, y);
                parList.add(p);
            }
        }

        // Transform list into strwam to enable Java 8 parallelisation
        parList.parallelStream().forEach(i -> {
            Ray r = new Ray(camera, new Vector3(i.getLeft() - resolution / 2, i.getRight() - resolution / 2, f));
            Vector3 color = radiance(r, box);
            drawPixel(i.getLeft(), i.getRight(), color);
        });

        float dt = (System.currentTimeMillis() - t) / 1000f;
        System.out.println("time " + dt + " s");
    }

    
    
    private static Vector3 radiance(Ray originalRay, List<Item> box) {
        Vector3 totalColor = Color.BLACK;
        Medium currentMedium = Medium.VACUUM;

        for (int i = 0; i < numberRays; ++i) {
            Ray ray = originalRay;
            Vector3 accuColor = Color.BLACK;
            Vector3 mask = Color.WHITE;

            for (int b = 0; b < numberRebounds; ++b) {
                Pair<Intersection, Item> pair = getClosestIntersection(ray, box);
                Intersection intersection = pair.getLeft();
                Item closest = pair.getRight();

                if (!intersection.valid()) {
                    b = numberRebounds;
                } else {

                    float specularCoef = 0;
                    float diffuseCoef = 0;

                    Vector3 normal = closest.shape().normalAt(intersection.position());
                    normal = ((normal.dot(ray.direction())) > 0 ? (normal.times(-1)) : normal);

                    if (closest.isLamp()) {

                        specularCoef = closest.asLamp().material().specularCoef();
                        diffuseCoef = 1 - specularCoef;

                    } else {

                        specularCoef = closest.asPhysicalObject().material().specularCoef();
                        diffuseCoef = 1 - specularCoef;

                    }

                    float randomSpec = (float) Math.random();
                    float bias = 0f;

                    if ((!closest.isPhysical() || closest.asPhysicalObject().material().isOpaque())) {

                        // Opaque object or lamp, there is no light transmission
                        // possible

                        if (randomSpec < specularCoef) {
                            bias = (1f / specularCoef);
                            ray = Ray.specularBounce(ray, normal, intersection.position());
                            mask = mask.entrywiseDot(closest.color()).times(ray.direction().dot(normal) * 2 * bias);
                        } else {
                            bias = (1f / diffuseCoef);
                            ray = Ray.generateRandomRay(intersection.position(), normal);
                            mask = mask.entrywiseDot(closest.color()).times(ray.direction().dot(normal) * 2 * bias);
                        }

                        if (closest.isLamp()) {
                            accuColor = accuColor.plus(mask.entrywiseDot(closest.emittedLight().times(bias)));
                        } else {
                            // accuColor =
                            // accuColor.plus(mask.entrywiseDot(directLight(intersection.position(),
                            // normal, box).times(bias)));
                        }

                    } else {

                        // Transparent object, we need to take the transmission
                        // into account
                        float randomTrans = (float) Math.random();
                        float transmissionCoef = Medium.computeTransmissionCoef(ray.direction(), normal);

                        if (randomTrans < transmissionCoef) {
                            bias = 1f / transmissionCoef;
                            // TODO change ray using n1.sin(i1) = n2.sin(i2)
                            ray = Ray.refractedRay(ray.direction(), normal, intersection.position(), currentMedium, closest.asPhysicalObject().material().asTransparent().medium());
                            mask = mask.entrywiseDot(Vector3.ones().minus(closest.asPhysicalObject().material().asTransparent().medium().absorbedColor())).times(ray.direction().dot(normal) * 2 * bias);
                        } else {
                            if (randomSpec < specularCoef) {
                                bias = 1f / ((1 - transmissionCoef) * specularCoef);
                                ray = Ray.specularBounce(ray, normal, intersection.position());
                                mask = mask.entrywiseDot(closest.color()).times(ray.direction().dot(normal) * 2 * bias);
                            } else {
                                bias = 1f / ((1 - transmissionCoef) * diffuseCoef);
                                ray = Ray.generateRandomRay(intersection.position(), normal);
                                mask = mask.entrywiseDot(closest.color()).times(ray.direction().dot(normal) * 2 * bias);
                            }
                        }
                        accuColor = accuColor.plus(mask.entrywiseDot(directLight(intersection.position(), closest.shape().normalAt(intersection.position()), box).times(bias)));
                    }
                }
            }

            totalColor = totalColor.plus(accuColor);
        }

        return totalColor.times(1f / numberRays);
    }

    /**
     * Draws a pixel at (i, j) in the window of resolution by resolution
     * 
     * @param i
     *            - the x coordinate
     * @param j
     *            - the y coordinate
     * @param color
     *            - the color of the pixel
     */
    public void drawPixel(int i, int j, Vector3 color) {
        int ratio = size / resolution;
        color = toColor(color);
        fill(255 * color.x(), 255 * color.y(), 255 * color.z());
        rect(i * ratio, j * ratio, ratio, ratio);
    }

    public void mousePressed() {
        String name = System.currentTimeMillis() + "-res" + resolution + "-ray" + numberRays + "-reb" + numberRebounds + ".png";
        System.out.println("saving image as " + name);
        saveFrame(name);

        try {
            AudioClip clip = Applet.newAudioClip(new URL("file:///C:/User/Thibaud/Music/tada.wav"));
            clip.play();
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

   

    public Vector3 mdRendering(Tree globalPhotonMap, int nbRays, Ray incidentRay, List<Item> box) {
       
        Vector3 radiance = Vector3.zeros();
        Pair<Intersection, Item> pair = getClosestIntersection(incidentRay, box);
        Intersection intersection = pair.getLeft();
        Item item = pair.getRight();
        Ray newRay;
        
        if(intersection.valid()) {
            for(int i = 0; i < nbRays; ++i) {
                
                float fate = (float)Math.random();
                Vector3 normal = item.shape().normalAt(intersection.position());
                normal = (normal.dot(incidentRay.direction()) > 0) ? normal.times(-1) : normal; 
                if(fate < item.material().diffuseCoef()) {
                     newRay = Ray.generateRandomRay(intersection.position(), normal);
                } else {
                    newRay = Ray.specularBounce(incidentRay, normal, intersection.position());
                }
                
                Pair<Intersection, Item> p2 = getClosestIntersection(newRay, box);
                if(p2.getLeft().valid()) {
                    Vector3 normal2 = p2.getRight().shape().normalAt(p2.getLeft().position());
                    normal2 = (normal2.dot(newRay.direction()) > 0) ? normal2.times(-1) : normal2; 
                    radiance = toColor(radiance.plus(item.color().entrywiseDot(getRadianceAt(p2.getLeft().position(), globalPhotonMap, normal2))));
                
                }
            }
        }
        
        //return radiance.times(1/(float)nbRays);
        return radiance;
    }
    
  
    public Vector3 photonMappingRadiance(Intersection intersection,Item item, Tree tree, Vector3 normal, List<Item> box) {
        Vector3 radiance = toColor(toColor(getRadianceAt(intersection.position(), tree, normal)).plus(toColor(item.color().entrywiseDot(directLight(intersection.position(), normal, box)))));
        return radiance.plus(item.emittedLight());
    }
    
    public static Pair<Intersection, Item> getClosestIntersection(Ray ray, List<Item> box) {
        Intersection intersection = Intersection.invalidIntersection();
        Item closest = null;

        for (Item item : box) {
            Intersection cur = item.intersection(ray);
            if (cur.valid() && cur.distance() < intersection.distance()) {
                intersection = cur;
                closest = item;
            }
        }

        return new Pair<>(intersection, closest);
    }

    private static Vector3 directLight(Vector3 position, Vector3 normal, List<Item> box) {
        Vector3 totalLight = Vector3.zeros();

        for (Item source : Loader.lightSources()) {
            Lamp lamp = source.asLamp();
            Vector3 LightAggregate = Vector3.zeros();
            
            for(int i = 0; i < 50; ++i) {
                Vector3 r = lamp.shape().randomPoint().minus(position);
                Vector3 rhat = r.normalise();

                float rsz = r.size();
                float f = (float) (1 / (4 * Math.PI * rsz * rsz));

                Pair<Intersection, Item> pair = getClosestIntersection(new Ray(position, r), box);
                // Intersection blocking = Intersection.invalidIntersection();
                Intersection blockingIntersection = pair.getLeft();
                Item blocking = pair.getRight();

                Vector3 light;

                normal = (rhat.dot(normal) < 0) ? normal.times(-1) : normal;
                if (!blockingIntersection.valid() || blockingIntersection.distance() > rsz || blocking.isLamp()) {
                    light = lamp.color().times(rhat.dot(normal)).times(f).times(lamp.power() / 100f);
                } else {
                    light = Color.BLACK;
                }

                LightAggregate = LightAggregate.plus(light);
            }
           totalLight = totalLight.plus(LightAggregate.times(1/50f));
        }

        return totalLight;
    }

    
    
}
