package se.graphics.proj;

import se.graphics.proj.Intersection;
import se.graphics.proj.Vector3;

public final class Intersection {
    
    private final boolean valid;
    private final Vector3 position;
    private final float distance;
    
    public Intersection(boolean valid, Vector3 position, float distance) {
        this.valid = valid;
        this.position = position;
        this.distance = distance;
    }
    
    public static Intersection invalidIntersection() {
        return new Intersection(false, null, Float.MAX_VALUE);
    }

    public boolean valid() {
        return valid;
    }

    public Vector3 position() {
        return position;
    }
    
    public float distance() {
        return distance;
    }

}
