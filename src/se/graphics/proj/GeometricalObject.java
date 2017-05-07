package se.graphics.proj;

import se.graphics.proj.Sphere;
import se.graphics.proj.Triangle;
import se.graphics.proj.Vector3;

public abstract class GeometricalObject {
    
    private final Vector3 color;
    private final boolean isLight;

    public abstract boolean isTriangle();
    public abstract boolean isSphere();
    
    public GeometricalObject(Vector3 color, boolean isLight) {
        this.color = color;
        this.isLight = isLight;
    }
    
    public Vector3 color() {
        return color;
    }
    
    public boolean isLight() {
        return isLight;
    }
    
    /**
     * Transforms the object into a instance of the Triangle class
     * @return the Triangle version of the object if it is indeed a Triangle
     */
    public Triangle asTriangle() {
        if (isTriangle()) {
            return (Triangle) this;
        } else {
            throw new IllegalStateException("cannot interpret sphere as triangle");
        }
    }
    
    /**
     * Transforms the object into a instance of the Sphere class
     * @return the Sphere version of the object if it is indeed a Sphere
     */
    public Sphere asSphere() {
        if (isSphere()) {
            return (Sphere) this;
        } else {
            throw new IllegalStateException("cannot interpret triangle as sphere");
        }
    }
    
}
