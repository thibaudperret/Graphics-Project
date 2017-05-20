package se.graphics.proj;

public class DiffuseLamp extends Lamp {
    
    public DiffuseLamp(Shape shape, Material material, float lightPower, Vector3 lightColor){
        super(shape, material, lightPower, lightColor);
    }
    
    
    @Override
    public boolean isDiffuse() {
        return true;
    }
    
    @Override
    public boolean isDirectionnal() {
        return false;
    }

    
}
