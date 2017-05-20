package se.graphics.proj;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DiffuseProjectionMap {
    private final Map<Pair<Vector3, Pair<Float, Float>>, Boolean> cells;
    private final float ratioValidCells;
    
    public DiffuseProjectionMap(Map<Pair<Vector3, Pair<Float, Float>>, Boolean> cells, float ratioValidCells) {
        this.cells = new HashMap<>(Collections.unmodifiableMap(cells));
        this.ratioValidCells = ratioValidCells;
    }
    
    public boolean cellValid(Vector3 positionOnLamp,float theta, float phi) {
        return cells.get(new Pair<>(positionOnLamp, new Pair<>(theta, phi)));
    }
    
    public float ratioValidCells() {
        return ratioValidCells;
    }
    
    public final static class Builder {
        
        private Map<Pair<Vector3, Pair<Float, Float>>, Boolean> cells;
        private int validCells = 0;
        
        public Builder() {
            cells = new HashMap<Pair<Vector3,Pair<Float,Float>>, Boolean>();
        }
        
        public void setCell(Vector3 positionOnLamp,float theta, float phi, boolean value) {
            cells.put(new Pair<>(positionOnLamp, new Pair<>(theta, phi)), value);
            validCells += (value == true) ? 1 : 0;
        }
        
        public DiffuseProjectionMap build() {
            return new DiffuseProjectionMap(cells, validCells/(float)cells.size());
        }
        
    }
}
