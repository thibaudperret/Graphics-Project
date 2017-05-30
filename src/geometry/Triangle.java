package geometry;

import java.util.Arrays;
import java.util.List;

import math.Intersection;
import math.Vector3;
import se.graphics.proj.Ray;

public final class Triangle extends Shape {

    private final Vector3 v1;
    private final Vector3 v2;
    private final Vector3 v3;

    private final Vector3 normal;

    public Triangle(Vector3 v1, Vector3 v2, Vector3 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;

        Vector3 e1 = v2.minus(v1).normalise();
        Vector3 e2 = v3.minus(v1).normalise();
        this.normal = e1.cross(e2).normalise();
    }

    public Triangle(float v1x, float v1y, float v1z, float v2x, float v2y, float v2z, float v3x, float v3y, float v3z) {
        this(new Vector3(v1x, v1y, v1z), new Vector3(v2x, v2y, v2z), new Vector3(v2x, v3y, v3z));
    }

    /**
     * For lazy people
     */
    public static Triangle triangle(Vector3 v1, Vector3 v2, Vector3 v3) {
        return new Triangle(v1, v2, v3);
    }
    
    /*
     * (non-Javadoc)
     * @see geometry.Shape#intersection(se.graphics.proj.Ray)
     * @see https://en.wikipedia.org/wiki/Line%E2%80%93plane_intersection
     */
    public Intersection intersection(Ray ray) {
        Vector3 start = ray.position();
        Vector3 direction = ray.direction();

        Intersection ret = Intersection.invalidIntersection();

        Vector3 n = normal;
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

    public Vector3 normalAt(Vector3 position) {
        return normal;
    }

    @Override
    public boolean isTriangle() {
        return true;
    }

    public Vector3 getCenter() {
        Vector3 axis1 = v2.minus(v1);
        Vector3 axis2 = v3.minus(v1);
        return v1.plus(axis1.times(0.5f).plus(axis2.times(0.5f)));
    }

    public Vector3 randomPoint() {
        float indexV2 = (float) Math.random();
        float indexV3;
        do {
            indexV3 = (float) Math.random();
        } while (indexV3 + indexV2 > 1);
        return v1.plus(v2.minus(v1).times(indexV2)).plus(v3.minus(v1).times(indexV3));
    }

}
