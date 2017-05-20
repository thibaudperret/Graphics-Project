package se.graphics.proj;

import java.util.List;

public class DiffuseLamp extends Lamp {
    
    public DiffuseLamp(Shape shape, Material material, float lightPower, Vector3 lightColor){
        super(shape, material, lightPower, lightColor);
    }
    

    public List<Photon> emitPhotons(int nbPhotons, ProjectionMap map, List<Item> box) {
      //TODO note that it only works for triangles
        
        if(!shape().isTriangle()) {
            throw new IllegalStateException("The shape is not a triangle");
        }
        
        for(int i = 0; i < nbPhotons; ++i) {
            Vector3 pos = shape().asTriangle().randomPoint();
        }
        
        return null;
    }
    
    @Override
    public boolean isDiffuse() {
        return true;
    }
    
    @Override
    public boolean isDirectional() {
        return false;
    }

    
}
