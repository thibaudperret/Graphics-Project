package se.graphics.proj;

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
    
    public abstract boolean isDiffuse();
    public abstract boolean isDirectionnal();

}
