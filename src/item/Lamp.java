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
    

    /**
     * Returns global and caustic photon maps (in order)
     * 
     * @param nbCausticPhotons
     * @param nbGlobalPhotons
     * @param causticProj
     * @param globalProj
     * @param box
     * @return
     */
    public Pair<List<Photon>, List<Photon>> computePhotonMaps(int nbCausticPhotons, int nbGlobalPhotons, ProjectionMap causticProj, ProjectionMap globalProj, List<Item> box) {
        List<Photon> globalMap = new ArrayList<>();
        List<Photon> causticMap = new ArrayList<>();

        List<Ray> primaryCausticRays = this.emitRays(nbCausticPhotons, causticProj);
        Vector3 primaryCausticEnergy = lightColor.times((this.power() / (float) nbCausticPhotons) * causticProj.ratioValidCells());

        for (int i = 0; i < primaryCausticRays.size(); ++i) {
            handleCausticBounces(causticMap, true, true, primaryCausticRays.get(i), box, primaryCausticEnergy);
        }

        List<Ray> primaryGlobalRays = this.emitRays(nbCausticPhotons, globalProj);
        Vector3 primaryGlobalEnergy = lightColor.times((this.power() / (float) nbCausticPhotons) * globalProj.ratioValidCells());

        for (int i = 0; i < primaryGlobalRays.size(); ++i) {
            handleGlobalBounces(globalMap, primaryCausticRays.get(i), box, primaryGlobalEnergy, 6);
        }

        return new Pair<>(globalMap, causticMap);
    }

    public Pair<List<Photon>, List<Photon>> computePhotonMaps(int nbGlobalPhotons, int nbCausticPhotons, List<Item> box) {
        
        List<Photon> globalMap = new ArrayList<>();
        List<Photon> causticMap = new ArrayList<>();

        List<Ray> primaryCausticRays = this.emitRays(nbCausticPhotons);
        Vector3 primaryCausticEnergy = lightColor.times((this.power() / (float) nbCausticPhotons));

        for (int i = 0; i < primaryCausticRays.size(); ++i) {
            handleCausticBounces(causticMap, true, true, primaryCausticRays.get(i), box, primaryCausticEnergy);
        }

        List<Ray> primaryGlobalRays = this.emitRays(nbGlobalPhotons);
        Vector3 primaryGlobalEnergy = lightColor.times((this.power() / (float) nbGlobalPhotons));

        for (int i = 0; i < primaryGlobalRays.size(); ++i) {
            handleGlobalBounces(globalMap, primaryGlobalRays.get(i), box, primaryGlobalEnergy, 1);
        }

        return new Pair<>(globalMap, causticMap);
        
    }
    
  public Pair<List<Photon>, List<Photon>> computePhotonMaps(int nbGlobalPhotons, int nbCausticPhotons,ProjectionMap caustic,  List<Item> box) {
        
        List<Photon> globalMap = new ArrayList<>();
        List<Photon> causticMap = new ArrayList<>();

        List<Ray> primaryCausticRays = this.emitRays(nbCausticPhotons, caustic);
        Vector3 primaryCausticEnergy = lightColor.times((power() / (float) nbCausticPhotons));

        for (int i = 0; i < primaryCausticRays.size(); ++i) {
            handleCausticBounces(causticMap, true, true, primaryCausticRays.get(i), box, primaryCausticEnergy);
        }

        List<Ray> primaryGlobalRays = this.emitRays(nbGlobalPhotons);
        Vector3 primaryGlobalEnergy = lightColor.times((power() / (float) nbGlobalPhotons));

        for (int i = 0; i < primaryGlobalRays.size(); ++i) {
            handleGlobalBounces(globalMap, primaryGlobalRays.get(i), box, primaryGlobalEnergy, 6);
        }

        return new Pair<>(globalMap, causticMap);
        
    }
    
    private void handleGlobalBounces(List<Photon> global, Ray currentRay, List<Item> box, Vector3 energy, int nbRebonds) {
        
        if(nbRebonds == 0) {
            return;
        }
        
        Pair<Intersection, Item> closest = Main.getClosestIntersection(currentRay, box);

        if(!closest.getLeft().valid()) {
            return;
        }
        
        Material currentMaterial = closest.getRight().material();
        Vector3 pos = closest.getLeft().position();
        Vector3 normal = closest.getRight().shape().normalAt(pos);
        normal = ((normal.dot(currentRay.direction())) > 0 ? (normal.times(-1)) : normal);
        float specularCoef = currentMaterial.specularCoef();
        float absorptionCoef = currentMaterial.absorptionCoef();
        Photon toStore = new Photon(pos, energy.entrywiseDot(currentMaterial.reflectance()), currentRay.theta(), currentRay.phi());
        Ray newRay;

        float fate = (float) Math.random();
        float absorptionProb = absorptionCoef;
        float specularProb = absorptionProb + (specularCoef * (1 - absorptionCoef));
        float diffuseProb = 1 - (absorptionProb + specularProb);
        if (fate < absorptionProb) {
            // absorbed photon
            global.add(toStore);
            return;
            
        } else if (fate < (absorptionProb + specularProb)) {
            // specular bounce
            newRay = Ray.specularBounce(currentRay, normal, pos);
            global.add(toStore);

            handleGlobalBounces(global, newRay, box, energy.times(1 / specularProb).entrywiseDot(currentMaterial.reflectance()), nbRebonds - 1);

        } else {
            // diffuse bounce
            newRay = Ray.generateRandomRay(pos, normal);
            global.add(toStore);
            handleGlobalBounces(global, newRay, box, energy.times(1 / diffuseProb).entrywiseDot(currentMaterial.reflectance()), nbRebonds - 1);
           
            return;
        }
    }

    private void handleCausticBounces(List<Photon> caustic, boolean storeCaustic, boolean firstBounce, Ray currentRay, List<Item> box, Vector3 energy) {
        
        Pair<Intersection, Item> closest = Main.getClosestIntersection(currentRay, box);

        if(!closest.getLeft().valid()) {
            return;
        }
        
        Material currentMaterial = closest.getRight().material();
        Vector3 pos = closest.getLeft().position();
        float specularCoef = currentMaterial.specularCoef();

        float absorptionCoef = currentMaterial.absorptionCoef();
        Photon toStore = new Photon(pos, energy, currentRay.theta(), currentRay.phi());
       
        Ray newRay;
        float fate = (float) Math.random();
        float absorptionProb = absorptionCoef;
        float specularProb = absorptionCoef + (specularCoef * (1 - absorptionCoef));

        if (fate < absorptionProb) {
            // absorbed photon
            if (!(specularCoef == 1) && storeCaustic && !firstBounce) {
                caustic.add(toStore);
            }
            return;
        } else if (fate < (absorptionProb + specularProb)) {
            // specular bounce
            Vector3 normal = closest.getRight().shape().normalAt(pos);
            normal = ((normal.dot(currentRay.direction())) > 0 ? (normal.times(-1)) : normal);
            newRay = Ray.specularBounce(currentRay, normal , pos);
            handleCausticBounces(caustic, true, false, newRay, box, energy.times(1 / specularProb).entrywiseDot(currentMaterial.reflectance()));

        } else {
            if (!(specularCoef == 1) && storeCaustic && !firstBounce) {
                caustic.add(toStore);
            }
            return;
        }

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
