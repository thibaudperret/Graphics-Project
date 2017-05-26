package item;

import java.util.ArrayList;
import java.util.List;

import geometry.Shape;
import material.Material;
import math.Vector3;
import projection.ProjectionMap;
import se.graphics.proj.Ray;

public class DiffuseLamp extends Lamp {
    
    public DiffuseLamp(Shape shape, Material material, float lightPower, Vector3 lightColor){
        super(shape, material, lightPower, lightColor);
    }
    
    @Override
    public List<Ray> emitRays(int nbRays, ProjectionMap map) {
        //TODO
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