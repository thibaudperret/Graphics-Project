package se.graphics.proj;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
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
    private final static int size = 1600;

    /**
     * The resolution of the window, not to be confused with the size
     */
    private final static int resolution = 200;

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
    private final static int numberRays = 1000;

    /**
     * The max number of rebounds of a ray
     */
    private final static int numberRebounds = 4;

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
        pathTracingParallel();
    }

    
    /*
     * PROBLEM : we get aliasing. 
     * TODO : fix it. 
     */
    public void pathTracingParallel() {
        long t = System.currentTimeMillis();
        background(0);
        final List<Item> box = Loader.cornellBox();

        List<Pair<Integer, Integer>> parList = new ArrayList<Pair<Integer, Integer>>();

        for (int x = 0; x < resolution; ++x) {
            for (int y = 0; y < resolution; ++y) {
                Pair<Integer, Integer> p = new Pair<Integer, Integer>(x, y);
                parList.add(p);
            }
        }

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
                            // accuColor = accuColor.plus(mask.entrywiseDot(directLight(intersection.position(), normal, box).times(bias)));
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

        fill(255 * color.x(), 255 * color.y(), 255 * color.z());
        rect(i * ratio, j * ratio, ratio, ratio);
    }

    public void mousePressed() {
        String name = System.currentTimeMillis() + "-res" + resolution + "-ray" + numberRays + "-reb" + numberRebounds + ".png";
        System.out.println("saving image as " + name);
        saveFrame(name);
        
        try{
            AudioClip clip = Applet.newAudioClip(new URL("file:///C:/User/Thibaud/Documents/GitHub/Graphics-Project/tada.wav"));
            clip.play();
            Thread.sleep(2000);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    private static Pair<Intersection, Item> getClosestIntersection(Ray ray, List<Item> box) {
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
            Vector3 r = lamp.shape().getCenter().minus(position);
            Vector3 rhat = r.normalise();

            float rsz = r.size();
            float f = (float) (1 / (4 * Math.PI * rsz * rsz));

            Pair<Intersection, Item> pair = getClosestIntersection(new Ray(position, r), box);
            // Intersection blocking = Intersection.invalidIntersection();
            Intersection blockingIntersection = pair.getLeft();
            Item blocking = pair.getRight();

            Vector3 light;

            if (!blockingIntersection.valid() || blockingIntersection.distance() > rsz || blocking.isLamp()) {
                light = lamp.color().times(Math.max(rhat.dot(normal), 0f)).times(f).times(lamp.power());
            } else {
                light = Color.BLACK;
            }

            totalLight = totalLight.plus(light);
        }

        return totalLight;
    }
}
