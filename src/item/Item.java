package item;

import geometry.Shape;
import material.Material;
import math.Intersection;
import math.Vector3;
import se.graphics.proj.Ray;

public abstract class Item {

    private final Shape shape;
    private Material material;
    
    public Item(Shape shape, Material material) {
        this.shape = shape;
        this.material = material;
    }
    
    abstract public boolean isPhysical();
    abstract public boolean isLamp();
    
    abstract public Vector3 emittedLight();
    
    public Shape shape() {
        return shape;
    }
    
    public Material material() {
        return material;
    }
    
    public Intersection intersection(Ray ray) {
        return shape.intersection(ray);
    }
    
    public abstract Vector3 color();
    
    public Lamp asLamp() {
        if(isLamp()) {
            return (Lamp) this;
        } else {
            throw new IllegalStateException("cannot interpret item as lamp");
        }
    }
    
    public PhysicalObject asPhysicalObject() {
        if (isPhysical()) {
            return (PhysicalObject) this;
        } else {
            throw new IllegalStateException("cannot interpret item as physical object");
        }
    }
    
}
