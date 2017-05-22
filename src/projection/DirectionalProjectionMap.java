package projection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import math.Vector3;
import util.Pair;

public class DirectionalProjectionMap extends ProjectionMap {

    private Map<Vector3, Boolean> cells;
    private float translationStep = 0.01f;
    
    public DirectionalProjectionMap(Map<Vector3, Boolean> cells, float ratioValidCells, float translationStep) {
        super(ratioValidCells);
        this.cells = new HashMap<>(Collections.unmodifiableMap(cells));
        this.translationStep = translationStep;
    }
    
    public DirectionalProjectionMap(Map<Vector3, Boolean> cells, float ratioValidCells) {
        super(ratioValidCells);
        this.cells = new HashMap<>(Collections.unmodifiableMap(cells));
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
    
    @Override
    public boolean isDiffuse() {
        return false;
    }
    
    @Override
    public boolean isDirectional() {
        return true;
    }
    
    @Override
    public boolean isPoint() {
        return false;
    }
    
    public boolean surroundingsContainValidCell(Vector3 pos, Vector3 axis1, Vector3 axis2) {
        boolean cell1 = cellValid(pos.plus(axis1.times(translationStep)).plus(axis2).times(translationStep));
        boolean cell2 = cellValid(pos.plus(axis1.times(translationStep)).minus(axis2).times(translationStep));
        boolean cell3 = cellValid(pos.minus(axis1.times(translationStep)).plus(axis2).times(translationStep));
        boolean cell4 = cellValid(pos.minus(axis1.times(translationStep)).minus(axis2).times(translationStep));
        return cell1 || cell2 || cell3 || cell4;

    }
    
    public final static class Builder {
        
        private Map<Vector3, Boolean> cells;
        private int validCells = 0;
        
        public void setCell(Vector3 positionOnLamp, boolean value) {
            cells.put(positionOnLamp, value);
            validCells += (value == true) ? 1 : 0;
        }
        
        public boolean getCell(Vector3 pos) {
            return cells.get(pos);
        }
        
        public DirectionalProjectionMap build() {
            return new DirectionalProjectionMap(cells, validCells/(float)cells.size());
        }
        
    }
    
}
