package se.graphics.proj;

public abstract class Item {

    private final Shape shape;
    
    public Item(Shape shape) {
        this.shape = shape;
    }
    
    public boolean isPhysical() {
        return false;
    }
    
    public boolean isLight() {
        return false;
    }
    
    public Intersection intersection(Ray ray) {
        return shape.intersection(ray);
    }
    
}
