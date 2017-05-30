package geometry;

import math.Intersection;
import math.Vector3;
import se.graphics.proj.Ray;

public abstract class Shape {

    /**
     * @return true if the shape is an instance of Triangle
     */
    public boolean isTriangle() {
        return false;
    }

    /**
     * @return true if the shape is an instance of Sphere
     */
    public boolean isSphere() {
        return false;
    }

    /**
     * @param ray
     *            - the ray intersecting with the shape
     * @return an Intersection instance
     */
    public abstract Intersection intersection(Ray ray);

    /**
     * @param position
     *            - a position in the shape
     * @return the normal at this position
     */
    public abstract Vector3 normalAt(Vector3 position);

    /**
     * @return the center of this shape
     */
    public abstract Vector3 getCenter();

    /**
     * @return a random point in the shape
     */
    public abstract Vector3 randomPoint();

    /**
     * Transforms the object into a instance of the Triangle class
     * 
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
     * 
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
