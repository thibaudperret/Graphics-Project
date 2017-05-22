package se.graphics.proj;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DiffuseProjectionMap extends ProjectionMap{
    
    private Map<Pair<Vector3, Pair<Float, Float>>, Boolean> cells;    
    private float translationStep = (float)Math.PI / 40f;
    private float rotationStep = 0.001f;
    
    public DiffuseProjectionMap(Map<Pair<Vector3, Pair<Float, Float>>, Boolean> cells, float ratioValidCells, float translationStep, float rotationStep) {
        super(ratioValidCells);
        this.translationStep = translationStep;
        this.rotationStep = rotationStep;
        this.cells = new HashMap<>(Collections.unmodifiableMap(cells));
    }
    
    public DiffuseProjectionMap(Map<Pair<Vector3, Pair<Float, Float>>, Boolean> cells, float ratioValidCells) {
        super(ratioValidCells);
        this.cells = new HashMap<>(Collections.unmodifiableMap(cells));
    }
    
    public boolean cellValid(Vector3 coordinates,float theta, float phi) {
        
        float x = coordinates.x();
        float y = coordinates.y();
        float z = coordinates.z();
        
        Pair<Float, Float> boundingValues = boundingValues(x, translationStep);
        float newX = closerTo(x, boundingValues.getLeft(), boundingValues.getRight());
        
        boundingValues = boundingValues(y, translationStep);
        float newY = closerTo(y, boundingValues.getLeft(), boundingValues.getRight());
        
        boundingValues = boundingValues(z, translationStep);
        float newZ = closerTo(z, boundingValues.getLeft(), boundingValues.getRight());
       
        Vector3 positionOnLamp = new Vector3(newX, newY, newZ);
        
        boundingValues = boundingValues(theta, rotationStep);
        float newTheta = closerTo(theta, boundingValues.getLeft(), boundingValues.getRight());
        
        boundingValues = boundingValues(phi, rotationStep);
        float newPhi = closerTo(phi, boundingValues.getLeft(), boundingValues.getRight());
                
        return cells.get(new Pair<>(positionOnLamp, new Pair<>(newTheta, newPhi)));
    }
    
    @Override
    public boolean isDiffuse() {
        return true;
    }
    
    @Override
    public boolean isDirectional() {
        return false;
    }
    
    @Override
    public boolean isPoint() {
        return false;
    }
    
    
    public final static class Builder {
        
        private Map<Pair<Vector3, Pair<Float, Float>>, Boolean> cells;
        private int validCells = 0;
        private float translationStep;
        private float rotationStep;
        
        
        public Builder() {
            cells = new HashMap<Pair<Vector3,Pair<Float,Float>>, Boolean>();
        }
        
        public void setCell(Vector3 positionOnLamp,float theta, float phi, boolean value) {
            cells.put(new Pair<>(positionOnLamp, new Pair<>(theta, phi)), value);
            validCells += (value == true) ? 1 : 0;
        }
        
        public boolean getCell(Vector3 pos, float theta, float phi) {
            return cells.get(new Pair<>(pos, new Pair<>(theta, phi)));
        }
        
        public DiffuseProjectionMap build() {
            return new DiffuseProjectionMap(cells, validCells/(float)cells.size());
        }
        
    }
}
