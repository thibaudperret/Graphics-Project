package se.graphics.proj;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DirectionalProjectionMap extends ProjectionMap {

    private Map<Vector3, Boolean> cells;
    private float ratioValidCells;
    private float translationStep = 0.01f;
    
    public DirectionalProjectionMap(Map<Vector3, Boolean> cells, float ratioValidCells, float translationStep) {
        this.cells = new HashMap<>(Collections.unmodifiableMap(cells));
        this.ratioValidCells = ratioValidCells;
        this.translationStep = translationStep;
    }
    
    public DirectionalProjectionMap(Map<Vector3, Boolean> cells, float ratioValidCells) {
        this.cells = new HashMap<>(Collections.unmodifiableMap(cells));
        this.ratioValidCells = ratioValidCells;
    }
    
    public boolean cellValid(Vector3 coordinates) {
        
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
        
        return cells.get(positionOnLamp);
    }
    
    public float ratioValidCells() {
        return ratioValidCells;
    }
    
    public final static class Builder {
        
        private Map<Vector3, Boolean> cells;
        private int validCells = 0;
        
        public void setCell(Vector3 positionOnLamp, boolean value) {
            cells.put(positionOnLamp, value);
            validCells += (value == true) ? 1 : 0;
        }
        
        public DirectionalProjectionMap build() {
            return new DirectionalProjectionMap(cells, validCells/(float)cells.size());
        }
        
    }
    
}
