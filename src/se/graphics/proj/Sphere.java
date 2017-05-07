package se.graphics.proj;

import se.graphics.proj.GeometricalObject;
import se.graphics.proj.Sphere;
import se.graphics.proj.Vector3;

public final class Sphere extends GeometricalObject {
    
    private final Vector3 c;
    private final float r;
    
    public Sphere(Vector3 c, float r, Vector3 color, boolean isLight) {
        super(color, isLight);
        this.c = c;
        this.r = r;
    }
    
    public Sphere(Vector3 c, float r, Vector3 color) {
        this(c, r, color, false);
    }
    
    /**
     * For lazy people
     */
    public static Sphere sphere(Vector3 c, float r, Vector3 color) {
        return new Sphere(c, r, color);
    }

    /**
     * For lazy people
     */
    public static Sphere sphere(Vector3 c, float r, Vector3 color, boolean isLight) {
        return new Sphere(c, r, color, isLight);
    }
    
    public Vector3 c() {
        return c;
    }
    
    public float r() {
        return r;
    }

    @Override
    public boolean isTriangle() {
        return false;
    }

    @Override
    public boolean isSphere() {
        return true;
    }

}
