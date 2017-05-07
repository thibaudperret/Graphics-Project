package se.graphics.proj;

import se.graphics.proj.Sphere;
import se.graphics.proj.Triangle;
import se.graphics.proj.Vector3;

public abstract class GeometricalObject {
    
    private final Vector3 color;

    public abstract boolean isTriangle();
    public abstract boolean isSphere();
    
    public GeometricalObject(Vector3 color) {
        this.color = color;
    }
    
    public Vector3 color() {
        return color;
    }
    
    public Triangle asTriangle() {
        if (isTriangle()) {
            return (Triangle) this;
        } else {
            throw new IllegalStateException("cannot interpret sphere as triangle");
        }
    }
    
    public Sphere asSphere() {
        if (isSphere()) {
            return (Sphere) this;
        } else {
            throw new IllegalStateException("cannot interpret triangle as sphere");
        }
    }
    
}
