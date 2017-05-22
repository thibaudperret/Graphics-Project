package se.graphics.proj;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class PointProjectionMap extends ProjectionMap{

    private final Map<Pair<Float, Float>, Boolean> cells;
    
    public PointProjectionMap(Map<Pair<Float, Float>, Boolean> cells, float ratioValidCells) {
        super(ratioValidCells);
        this.cells = new HashMap<>(Collections.unmodifiableMap(cells));
    }
    
    public boolean cellValid(Float theta, float phi) {
        return cells.get(new Pair<Float, Float>(theta, phi));
    }
    
    
    @Override
    public boolean isDiffuse() {
        return false;
    }
    
    @Override
    public boolean isDirectional() {
        return false;
    }
    
    @Override
    public boolean isPoint() {
        return true;
    }
    
    public final static class Builder {
        
        private Map<Pair<Float, Float>, Boolean> cells;
        private int validCells = 0;
        
        public void setCell(float theta, float phi, boolean value) {
            cells.put(new Pair<Float, Float>(theta, phi), value);
            validCells += (value == true) ? 1 : 0;
        }
        
        public boolean getCell(float theta, float phi) {
            return cells.get(new Pair<>(theta, phi));
        }
        
        public PointProjectionMap build() {
            return new PointProjectionMap(cells, validCells/(float)cells.size());
        }
        
    }
    
}
