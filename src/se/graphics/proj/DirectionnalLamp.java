package se.graphics.proj;

public class DirectionnalLamp extends Lamp{

    private final Vector3 lightDirection;
    
    public DirectionnalLamp(Shape shape, Material material, float lightPower, Vector3 lightColor, Vector3 lightDirection) {
        super(shape, material, lightPower, lightColor);
        this.lightDirection = lightDirection;        
    }
    
    public Vector3 lightDirection() {
        return lightDirection;
    }
    
    public DirectionnalLamp setDirection(Vector3 lightDirection) {
        return new DirectionnalLamp(shape(), material(), power(), color(), lightDirection);
    }
    
    
    @Override
    public boolean isDiffuse() {
        return false;
    }
    
    @Override
    public boolean isDirectionnal() {
        return true;
    }

    
}
