package se.graphics.proj;

public abstract class Item {

    private final Shape shape;
    
    public Item(Shape shape) {
        this.shape = shape;
    }
    
    abstract public boolean isPhysical();
    abstract public boolean isLight();
    abstract public boolean isLamp();
    
    public Shape shape() {
        return shape;
    }
    
    public Intersection intersection(Ray ray) {
        return shape.intersection(ray);
    }
    
    public Light asLight() {
        if (isLight()) {
            return (Light) this;
        } else {
            throw new IllegalStateException("cannot interpret item as light");
        }
    }
    
    public Lamp asLamp() {
        if(isLamp()) {
            return (Lamp)this;
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
