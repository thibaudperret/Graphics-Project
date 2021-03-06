package geometry;

import math.Intersection;
import math.Vector3;
import se.graphics.proj.Ray;

public final class Sphere extends Shape {

    private final Vector3 center;
    private final float radius;

    public Sphere(Vector3 c, float r) {
        this.center = c;
        this.radius = r;
    }

    /**
     * For lazy people
     */
    public static Sphere sphere(Vector3 c, float r) {
        return new Sphere(c, r);
    }

    /*
     * (non-Javadoc)
     * @see geometry.Shape#intersection(se.graphics.proj.Ray)
     * @see https://en.wikipedia.org/wiki/Line%E2%80%93sphere_intersection
     */
    public Intersection intersection(Ray ray) {
        Vector3 start = ray.position();
        Vector3 direction = ray.direction();

        Intersection ret = Intersection.invalidIntersection();

        Vector3 v = start.minus(center);

        float b = 2f * (direction.dot(v));
        float c = v.dot(v) - radius * radius;

        float delta = b * b - 4 * c;

        if (delta >= 0) {
            float d = (float) (-(b + Math.sqrt(delta)) / 2f);

            if (d > 0) {
                Vector3 position = start.plus(direction.times(d));
                ret = new Intersection(true, position, d);
            }
        }

        return ret;
    }

    public Vector3 c() {
        return center;
    }

    public float r() {
        return radius;
    }

    public Vector3 normalAt(Vector3 position) {
        return position.minus(center).normalise();
    }

    public Vector3 getCenter() {
        return center;
    }

    public Vector3 randomPoint() {
        return center.plus(new Vector3((float) (2 * Math.random()) - 1, (float) (2 * Math.random()) - 1, (float) (2 * Math.random()) - 1).normalise().times(radius));
    }

    @Override
    public boolean isSphere() {
        return true;
    }

}
