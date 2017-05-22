package se.graphics.proj;

import java.util.ArrayList;
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
    
    @Override
    public List<Ray> emitRays(int nbRays, ProjectionMap map) {
        //TODO
        List<Ray> emitted = new ArrayList<Ray>();
        for(int i = 0; i < nbRays; ++i){
            emitted.add(Ray.generateRandomRay(shape().randomPoint(), lightDirection));
        }
        return emitted;
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
