package se.graphics.proj;

import java.util.ArrayList;
import java.util.List;

public class DiffuseLamp extends Lamp {
    
    public DiffuseLamp(Shape shape, Material material, float lightPower, Vector3 lightColor){
        super(shape, material, lightPower, lightColor);
    }
    

    public List<Photon> emitPrimaryPhotons(int nbPhotons, ProjectionMap map, List<Item> box) {
      //TODO
        
        List<Photon> results = new ArrayList<>();
        Pair<Float, Float> angles;
        
        for(int i = 0; i < nbPhotons; ++i) {
            Vector3 pos = shape().randomPoint();
            do{
                Pair<Float, Float> angles = Ray.generateSphericalRandomRay(pos, shape().normalAt(pos));
            } while(!map.validCell(pos, angles));
            Intersection intersection = Main.getClosestIntersection(ray, box).getLeft();
           //Photon photon = new Photon(intersection.position(), )
            
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
