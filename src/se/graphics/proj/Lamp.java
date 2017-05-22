package se.graphics.proj;

import java.util.ArrayList;
import java.util.List;

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

    public DiffuseLamp asDiffuse() {
        if(isDiffuse()) {
            return (DiffuseLamp)this;
        } else {
            throw new IllegalStateException("Lamp cannot be interpreted as diffuse");
        }
    }
    
    public DirectionalLamp asDirectional() {
        if(isDirectional()) {
            return (DirectionalLamp)this;
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
        return asLamp().color().times(asLamp().power());

    }
    
    //return global and caustic photon maps (in order)
    public Pair<List<Photon>, List<Photon>> computePhotonMaps(int nbCausticPhotons, int nbGlobalPhotons, ProjectionMap map, List<Item> box) {
        
        List<Photon> globalMap = new ArrayList<>();
        List<Photon> causticMap = new ArrayList<>();

        List<Ray> primaryCausticRays = this.emitRays(nbCausticPhotons, map);
        Vector3 primaryCausticEnergy = lightColor.times((this.power()/(float) nbCausticPhotons) * map.ratioValidCells());
    
        for(int i = 0; i < primaryCausticRays.size(); ++i) {
            handleCausticBounces(causticMap, true, true, primaryCausticRays.get(i), box, primaryCausticEnergy);
        }
        
        List<Ray> primaryGlobalRays = this.emitRays(nbCausticPhotons, map);
        Vector3 primaryGlobalEnergy = lightColor.times((this.power()/(float) nbCausticPhotons) * map.ratioValidCells());

        
        for(int i = 0; i < primaryGlobalRays.size(); ++i) {
            handleGlobalBounces(globalMap, primaryCausticRays.get(i), box, primaryGlobalEnergy);
        }
        
        return new Pair<>(globalMap, causticMap);
        
    }   
    
    private void handleGlobalBounces(List<Photon> global, Ray currentRay, List<Item> box, Vector3 energy) {
        Pair<Intersection, Item> closest = Main.getClosestIntersection(currentRay, box);
        
        Material currentMaterial =  closest.getRight().material();
        Vector3 pos = closest.getLeft().position();
        float specularCoef =  currentMaterial.specularCoef();
        
        
            float absorptionCoef = currentMaterial.absorptionCoef();
            Pair<Float, Float> angles = Ray.cartesianToSphericalDir(currentRay.direction());
            
            Photon toStore = new Photon(pos, energy, angles.getLeft(), angles.getRight(), (short)0 );
            if(!(specularCoef == 1)) {
                global.add(toStore);

            }
            Ray newRay;
            
            
            float fate = (float) Math.random();
            if(fate < absorptionCoef) {
                //absorbed photon
                return;
            }  else if( fate < (absorptionCoef + (specularCoef*(1-absorptionCoef)))) {
                // specular bounce
                newRay = Ray.specularBounce(currentRay, closest.getRight().shape().normalAt(pos), pos);
                handleGlobalBounces(global, newRay, box, energy);

            } else {
                //diffuse bounce
                newRay = Ray.generateRandomRay(pos, closest.getRight().shape().normalAt(pos));  
                handleGlobalBounces(global, newRay, box, energy);
                return;
            }
    }
    
    private void handleCausticBounces(List<Photon> caustic, boolean storeCaustic, 
                                boolean firstBounce, Ray currentRay, List<Item> box,Vector3 energy) {
        
        Pair<Intersection, Item> closest = Main.getClosestIntersection(currentRay, box);
        
        Material currentMaterial =  closest.getRight().material();
        Vector3 pos = closest.getLeft().position();
        float specularCoef =  currentMaterial.specularCoef();
        
        
            float absorptionCoef = currentMaterial.absorptionCoef();
            Pair<Float, Float> angles = Ray.cartesianToSphericalDir(currentRay.direction());
            
            Photon toStore = new Photon(pos, energy, angles.getLeft(), angles.getRight(), (short)0 );
            if(! (specularCoef == 1) && storeCaustic && !firstBounce) {               
                caustic.add(toStore);                
            }            
            Ray newRay;            
            float fate = (float) Math.random();
            if(fate < absorptionCoef) {
                //absorbed photon
                return;
            }  else if( fate < (absorptionCoef + (specularCoef*(1-absorptionCoef)))) {
                // specular bounce
                newRay = Ray.specularBounce(currentRay, closest.getRight().shape().normalAt(pos), pos);
                handleCausticBounces(caustic, true, false, newRay, box, energy);

            } else {
               return;
            }
            
            
        
    }
    public List<Photon> computePhotonMaps(int nbPhotons, List<Item> box){return null;};    
    public abstract List<Ray> emitRays(int nbRays, ProjectionMap ProjectionMap);
    public abstract boolean isDiffuse();
    public abstract boolean isDirectional();

}
