package se.graphics.proj;

public final class PhysicalObject extends Item {
    
    private final Material material;

    public PhysicalObject(Shape shape, Material material) {
        super(shape);
        this.material = material;
    }
    
    public static PhysicalObject physicalTriangle(Triangle triangle, Material material) {
        return new PhysicalObject(triangle, material);
    }
    
    public static PhysicalObject physicalSphere(Sphere sphere, Material material) {
        return new PhysicalObject(sphere, material);
    }
    
    public Material material() {
        return material;
    }
    
    @Override
    public Vector3 color() {
        return material.reflectance();
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
