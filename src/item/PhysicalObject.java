package item;

import geometry.Shape;
import geometry.Sphere;
import geometry.Triangle;
import material.Material;
import math.Vector3;
import util.Color;

public final class PhysicalObject extends Item {
    

    public PhysicalObject(Shape shape, Material material) {
        super(shape, material);
    }
    
    public static PhysicalObject physicalTriangle(Triangle triangle, Material material) {
        return new PhysicalObject(triangle, material);
    }
    
    public static PhysicalObject physicalSphere(Sphere sphere, Material material) {
        return new PhysicalObject(sphere, material);
    }
    
    
    @Override
    public Vector3 color() {
        return material().reflectance();
    }
    
    @Override
    public boolean isPhysical() {
        return true;
    }
    
    @Override
    public boolean isLamp() {
        return false;
    }
    
    @Override 
    public Vector3 emittedLight() {
        return Color.BLACK;
    }

}
