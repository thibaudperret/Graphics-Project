package item;

import geometry.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import material.Material;
import math.Intersection;
import math.Vector3;
import projection.ProjectionMap;
import se.graphics.proj.Main;
import se.graphics.proj.Photon;
import se.graphics.proj.Ray;
import util.Pair;

public abstract class Lamp extends Item {

    private final float lightPower;
    private final Vector3 lightColor;

    public Lamp(Shape shape, Material material, float lightPower, Vector3 lightColor) {
        super(shape, material);
        this.lightPower = lightPower;
        this.lightColor = lightColor;
    }

    public float power() {
        return lightPower;
    }

    /**
     * Transforms the object into a instance of the DiffuseLamp class
     * 
     * @return the Triangle version of the object if it is indeed a DiffuseLamp
     */
    public DiffuseLamp asDiffuse() {
        if (isDiffuse()) {
            return (DiffuseLamp) this;
        } else {
            throw new IllegalStateException("Lamp cannot be interpreted as diffuse");
        }
    }

    /**
     * Transforms the object into a instance of the DirectionalLamp class
     * 
     * @return the Triangle version of the object if it is indeed a
     *         DirectionalLamp
     */
    public DirectionalLamp asDirectional() {
        if (isDirectional()) {
            return (DirectionalLamp) this;
        } else {
            throw new IllegalStateException("Lamp cannot be interpreted as directional");
        }
    }

    @Override
    public Vector3 color() {
        return lightColor;
    }

    @Override
    public boolean isPhysical() {
        return false;
    }

    @Override
    public boolean isLamp() {
        return true;
    }

    @Override
    public Vector3 emittedLight() {
        return lightColor.times(power());

    }
    
    public List<Photon> emittedPhotons(int nbPhotons, List<Item> box) {
        List<Ray> rays = emitRays(nbPhotons);
        List<Photon> photons = new ArrayList<>();
        
        for (Ray ray : rays) {
            Pair<Intersection, Item> pair = Main.getClosestIntersection(ray, box);
            Intersection intersection = pair.getLeft();
            Item item = pair.getRight();
            
            if (intersection.valid()) {
                Photon photon = new Photon(intersection.position(), lightColor.times(lightPower).entrywiseDot(item.material().reflectance()), ray.theta(), ray.phi());
                photons.add(photon);
            }
        }
        
        return photons;
    }

    /**
     * Creates rays for a particular lamp
     * 
     * @param nbRays
     * @param ProjectionMap
     * @return the list of light rays of the lamp
     */
    public abstract List<Ray> emitRays(int nbRays, ProjectionMap ProjectionMap);
    
    public abstract List<Ray> emitRays(int nbRays);

    /**
     * @return true if the object is an instance of the DiffuseLamp class
     */
    public abstract boolean isDiffuse();

    /**
     * @return true if the object is an instance of the DirectionalLamp class
     */
    public abstract boolean isDirectional();

}
