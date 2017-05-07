package se.graphics.proj;

import java.util.Arrays;
import java.util.List;

public final class Triangle extends GeometricalObject {
    
    private final Vector3 v1;
    private final Vector3 v2;
    private final Vector3 v3;
    
    public Triangle(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 color) {
        super(color);
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }
    
    public Triangle(float v1x, float v1y, float v1z, float v2x, float v2y, float v2z, float v3x, float v3y, float v3z, float c1, float c2, float c3) {
        super(new Vector3(c1, c2, c3));
        this.v1 = new Vector3(v1x, v1y, v1z);
        this.v2 = new Vector3(v2x, v2y, v2z);
        this.v3 = new Vector3(v2x, v3y, v3z);
    }
    
    public Triangle(float v1x, float v1y, float v1z, float v2x, float v2y, float v2z, float v3x, float v3y, float v3z, Vector3 color) {
        super(color);
        this.v1 = new Vector3(v1x, v1y, v1z);
        this.v2 = new Vector3(v2x, v2y, v2z);
        this.v3 = new Vector3(v3x, v3y, v3z);
    }
    
    public static Triangle triangle(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 color) {
        return new Triangle(v1, v2, v3, color);
    }
    
    public Vector3 v1() {
        return v1;
    }
    
    public Vector3 v2() {
        return v2;
    }
    
    public Vector3 v3() {
        return v3;
    }
    
    public List<Vector3> vertices() {
        return Arrays.asList(v1, v2, v3);
    }
    
    public Vector3 normal() {
        Vector3 e1 = v2.minus(v1);
        Vector3 e2 = v3.minus(v1);
        return e1.cross(e2).normalise();
    }

    @Override
    public boolean isTriangle() {
        return true;
    }

    @Override
    public boolean isSphere() {
        return false;
    }

}
