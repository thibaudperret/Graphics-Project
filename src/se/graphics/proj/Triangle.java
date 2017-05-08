package se.graphics.proj;

import java.util.Arrays;
import java.util.List;

import se.graphics.proj.Intersection;
import se.graphics.proj.Vector3;

public final class Triangle extends GeometricalObject {
    
    private final Vector3 v1;
    private final Vector3 v2;
    private final Vector3 v3;
    
    public Triangle(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 color, boolean isLight) {
        super(color, isLight);
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }
    
    public Triangle(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 color) {
        super(color, false);
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }
    
    public Triangle(float v1x, float v1y, float v1z, float v2x, float v2y, float v2z, float v3x, float v3y, float v3z, float c1, float c2, float c3, boolean isLight) {
        super(new Vector3(c1, c2, c3), isLight);
        this.v1 = new Vector3(v1x, v1y, v1z);
        this.v2 = new Vector3(v2x, v2y, v2z);
        this.v3 = new Vector3(v2x, v3y, v3z);
    }
    
    public Triangle(float v1x, float v1y, float v1z, float v2x, float v2y, float v2z, float v3x, float v3y, float v3z, float c1, float c2, float c3) {
        this(v1x, v1y, v1z, v2x, v2y, v2z, v3x, v3y, v3z, c1, c2, c3, false);
    }
    
    public Triangle(float v1x, float v1y, float v1z, float v2x, float v2y, float v2z, float v3x, float v3y, float v3z, Vector3 color, boolean isLight) {
        super(color, isLight);
        this.v1 = new Vector3(v1x, v1y, v1z);
        this.v2 = new Vector3(v2x, v2y, v2z);
        this.v3 = new Vector3(v3x, v3y, v3z);
    }
    
    public Triangle(float v1x, float v1y, float v1z, float v2x, float v2y, float v2z, float v3x, float v3y, float v3z, Vector3 color) {
        this(v1x, v1y, v1z, v2x, v2y, v2z, v3x, v3y, v3z, color, false);
    }
    
    /**
     * For lazy people
     */
    public static Triangle triangle(Vector3 v1, Vector3 v2, Vector3 v3, Vector3 color) {
        return new Triangle(v1, v2, v3, color);
    }
    
    public Intersection intersection(Vector3 start, Vector3 direction) {
        Intersection ret = Intersection.invalidIntersection();
        
        Vector3 n = normal();
        float a = (v1.minus(start)).dot(n);
        float b = direction.dot(n);
        
        if (b != 0) {
            float d = a / b;
            Vector3 position = start.plus(direction.times(d));
            float e = v2.minus(v1).cross(position.minus(v1)).dot(n);
            float f = v3.minus(v2).cross(position.minus(v2)).dot(n);
            float g = v1.minus(v3).cross(position.minus(v3)).dot(n);
            
            if (d > 0.00001 && e >= 0 && f >= 0 && g >= 0) {
                ret = new Intersection(true, position, d);
            }
        }
        
        return ret;
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
