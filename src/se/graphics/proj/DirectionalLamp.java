package se.graphics.proj;

import java.util.List;

public class DirectionalLamp extends Lamp{

    private final Vector3 lightDirection;
    
    public DirectionalLamp(Shape shape, Material material, float lightPower, Vector3 lightColor, Vector3 lightDirection) {
        super(shape, material, lightPower, lightColor);
        this.lightDirection = lightDirection;        
    }
    
    public Vector3 lightDirection() {
        return lightDirection;
    }
    
    public DirectionalLamp setDirection(Vector3 lightDirection) {
        return new DirectionalLamp(shape(), material(), power(), color(), lightDirection);
    }
    

    public List<Photon> emitPhotons(int nbPhotons, ProjectionMap map, List<Item> box) {
        //TODO
        return null;
    }
    
    
    @Override
    public boolean isDiffuse() {
        return false;
    }
    
    @Override
    public boolean isDirectional() {
        return true;
    }

    
}
