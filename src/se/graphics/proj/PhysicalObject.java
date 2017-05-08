package se.graphics.proj;

public final class PhysicalObject extends Item {
    
    private final Material material;

    public PhysicalObject(Shape shape, Material material) {
        super(shape);
        this.material = material;
    }
    
    public Material material() {
        return material;
    }
    
    @Override
    public boolean isPhysical() {
        return true;
    }

}
