package geometry;

import math.Intersection;
import math.Vector3;
import se.graphics.proj.Ray;

public final class Rectangle extends Shape {
    
    private final Vector3 p1;
    private final Vector3 p2;
    private final Vector3 p3;

    private final Vector3 normal;
    
    public Rectangle(Vector3 point1, Vector3 point2, Vector3 point3) {
        this.p1 = point1;
        this.p2 = point2;
        this.p3 = point3;
        

        Vector3 e1 = p2.minus(p1).normalise();
        Vector3 e2 = p3.minus(p1).normalise();
        this.normal = e1.cross(e2).normalise();
    }
    
    public Rectangle(Triangle t) {
        this(t.v1(), t.v2(), t.v3());
    }

    @Override
    public Intersection intersection(Ray ray) {
        Vector3 start = ray.position();
        Vector3 direction = ray.direction();

        Intersection ret = Intersection.invalidIntersection();

        Vector3 n = normal;
        float a = (p1.minus(start)).dot(n);
        float b = direction.dot(n);

        if (b != 0) {
            float d = a / b;
            Vector3 position = start.plus(direction.times(d));
            
            Vector3 p4 = p1.plus(p2.minus(p1)).plus(p3.minus(p1));
//            System.out.println(p4);
            float e = p2.minus(p1).cross(position.minus(p1)).dot(n);
            float f = p4.minus(p2).cross(position.minus(p2)).dot(n);
            float g = p3.minus(p4).cross(position.minus(p4)).dot(n);
            float h = p1.minus(p3).cross(position.minus(p3)).dot(n);

            if (d > 0.00001 && e >= 0 && f >= 0 && g >= 0 && h >= 0) {
                ret = new Intersection(true, position, d);
            }
        }

        return ret;
    }

    @Override
    public Vector3 normalAt(Vector3 position) {
        return normal;
    }

    @Override
    public Vector3 getCenter() {
        Vector3 axis1 = p2.minus(p1);
        Vector3 axis2 = p3.minus(p1);
        return p1.plus(axis1.times(0.5f).plus(axis2.times(0.5f)));
    }

    @Override
    public Vector3 randomPoint() {
        Vector3 axis1 = p2.minus(p1);
        Vector3 axis2 = p3.minus(p1);
        
        float f1 = (float) Math.random();
        float f2 = (float) Math.random();
        return p1.plus(axis1.times(f1)).plus(axis2.times(f2));
    }

}
