package se.graphics.proj;

public class Light extends Item {
    
    private final float power;
    private final Vector3 color;

    public Light(Shape shape, float power, Vector3 color) {
        super(shape);
        this.power = power;
        this.color = color;
    }

    public float power() {
        return power;
    }
    
    public Vector3 color() {
        return color;
    }
    
    @Override
    public boolean isLight() {
        return true;
    }
    
}
