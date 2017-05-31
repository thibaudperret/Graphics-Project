package item;

import java.util.ArrayList;
import java.util.List;

import geometry.Shape;
import material.Material;
import math.Vector3;
import projection.ProjectionMap;
import se.graphics.proj.Ray;
import util.Pair;

public class DiffuseLamp extends Lamp {
    
    public DiffuseLamp(Shape shape, Material material, float lightPower, Vector3 lightColor){
        super(shape, material, lightPower, lightColor);
    }
    
    @Override
    public List<Ray> emitRays(int nbRays, ProjectionMap map) {
        Vector3 pos;
        List<Ray> emitted = new ArrayList<Ray>();
        Ray randomRay;
        Pair<Float, Float> thetaPhi;
        for(int i = 0; i < nbRays; ++i){
            pos = shape().randomPoint();
            do{
                randomRay = Ray.generateRandomRay(pos, shape().normalAt(pos));
                thetaPhi = Ray.cartesianToSphericalDir(randomRay.direction());
            } while(!map.asDiffuseMap().cellValid(pos, thetaPhi.getLeft(), thetaPhi.getRight()));
            
            emitted.add(randomRay);
        }
        return emitted;
    }
    
    public List<Ray> emitRays(int nbRays) {
        Vector3 pos;
        List<Ray> emitted = new ArrayList<Ray>();
        for(int i = 0; i < nbRays; ++i){
            pos = shape().randomPoint();
            emitted.add(Ray.generateRandomRay(pos, shape().normalAt(pos)));
        }
        return emitted;
    }
    

    @Override
    public boolean isDiffuse() {
        return true;
    }
    
    @Override
    public boolean isDirectional() {
        return false;
    }
    
    

    
}
