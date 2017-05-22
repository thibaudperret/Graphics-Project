package se.graphics.proj;

import java.util.List;

public abstract class Lamp extends Item {

    private final Material material;
    private final float lightPower;
    private final Vector3 lightColor;

    public Lamp(Shape shape, Material material, float lightPower, Vector3 lightColor) {
        super(shape);
        this.material = material;
        this.lightPower = lightPower;
        this.lightColor = lightColor;
    }
    

    public Material material() {
        return material;
    }

    public float power() {
        return lightPower;
    }

    public DiffuseLamp asDiffuse() {
        if(isDiffuse()) {
            return (DiffuseLamp)this;
        } else {
            throw new IllegalStateException("Lamp cannot be interpreted as diffuse");
        }
    }
    
    public DirectionalLamp asDirectional() {
        if(isDirectional()) {
            return (DirectionalLamp)this;
        } else {
            throw new IllegalStateException("Lamp cannot be interpreted as directional");
        }
    }
    
    @Override
    public Vector3 color() {
        return lightColor;
    }

    @Override
    public boolean isPhysical() {
        return false;
    }

    @Override
    public boolean isLamp() {
        return true;
    }
    
    @Override 
    public Vector3 emittedLight() {
        return asLamp().color().times(asLamp().power());

    }
    
    public abstract List<Photon> emitPhotons(int nbPhotons, ProjectionMap map, List<Item> box);
    
    public abstract List<Photon> emitPhotons(int nbPhotons, List<Item> box);
    
    public abstract boolean isDiffuse();
    public abstract boolean isDirectional();

}
