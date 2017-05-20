package se.graphics.proj;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DirectionalProjectionMap extends ProjectionMap {

    private final Map<Vector3, Boolean> cells;
    private final float ratioValidCells;
    
    public DirectionalProjectionMap(Map<Vector3, Boolean> cells, float ratioValidCells) {
        this.cells = new HashMap<>(Collections.unmodifiableMap(cells));
        this.ratioValidCells = ratioValidCells;
    }
    
    public boolean cellValid(Vector3 positionOnLamp) {
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
