package item;

import geometry.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.HTMLReader.SpecialAction;

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
    
    public List<Photon> emittedPhotons(int nbPhotons, List<Item> box, int nbRebonds) {
        List<Ray> rays = emitRays(nbPhotons);
        System.out.println("Ray size = " + rays.size());
        List<Photon> photons = new ArrayList<>();
        int rebond;
        
        
        for (Ray ray : rays) {
            
            boolean bounce = true;
            rebond = nbRebonds;
            Ray currentRay = ray;
            Vector3 photonPower = lightColor.times(lightPower).times(1/(float)nbPhotons);
            
            
            Pair<Intersection, Item> pair ;
            Intersection intersection;
            Item item;            
            Material material;
            float absorbProb;
            float specularProb;
            float diffuseProb;
            float russian;
            Photon photon;
            Vector3 normal;
            
            do {
                
                pair = Main.getClosestIntersection(currentRay, box);
                intersection = pair.getLeft();
                
                if (intersection.valid()) {
                    
                    item = pair.getRight();            
                    material = item.material();
                    
                    absorbProb = material.absorptionCoef();
                    specularProb = material.specularCoef() * (1 - absorbProb);
                    diffuseProb = 1 - (absorbProb + specularProb);
                    
                    
                    russian = (float)Math.random();
                    photonPower = ((photonPower.entrywiseDot(item.material().reflectance())));
                    photon = new Photon(intersection.position(), photonPower, ray.theta(), ray.phi());
                    normal = item.shape().normalAt(intersection.position());
                    normal = ((normal.dot(currentRay.direction())) > 0 ? (normal.times(-1)) : normal);
                    rebond -= 1;
                    
                    
                    if(material.specularCoef() != 1f) {
                        photons.add(photon);
                    }
                    
                    if (russian < absorbProb) {
                        bounce = false;
                    } else if(russian < absorbProb + specularProb) {
                        currentRay = Ray.specularBounce(currentRay, normal, intersection.position());
                        bounce = true;
                    } else {
                        currentRay = Ray.generateRandomRay(intersection.position(), normal);
                        bounce = true;
                    }
                    
                    
                } else {
                    bounce = false;
                }
                
            }while(bounce && rebond > 0);          
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
