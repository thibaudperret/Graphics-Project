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
    
    /**
     * @return true if the object is an instance of PhysicalObject
     */
    abstract public boolean isPhysical();
    /**
     * @return true if the object is an instance of Lamp
     */
    abstract public boolean isLamp();
    
    /**
     * @return the emitted light of the object
     */
    abstract public Vector3 emittedLight();
    
    public Shape shape() {
        return shape;
    }
    
    public Material material() {
        return material;
    }
    
    /**
     * Returns the intersection with the shape composing the object
     * @see geometry.Shape#intersection(se.graphics.proj.Ray)
     */
    public Intersection intersection(Ray ray) {
        return shape.intersection(ray);
    }
    
    /**
     * @return the color of the item
     */
    public abstract Vector3 color();
    
    /**
     * Transforms the object into a instance of the Lamp class
     * 
     * @return the Triangle version of the object if it is indeed a Lamp
     */
    public Lamp asLamp() {
        if(isLamp()) {
            return (Lamp) this;
        } else {
            throw new IllegalStateException("cannot interpret item as lamp");
        }
    }
    
    /**
     * Transforms the object into a instance of the PhysicalObject class
     * 
     * @return the Triangle version of the object if it is indeed a PhysicalObject
     */
    public PhysicalObject asPhysicalObject() {
        if (isPhysical()) {
            return (PhysicalObject) this;
        } else {
            throw new IllegalStateException("cannot interpret item as physical object");
        }
    }
    
}
