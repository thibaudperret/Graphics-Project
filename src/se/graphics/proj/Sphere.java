package se.graphics.proj;

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
    
    public Intersection intersection(Ray ray) {
        Vector3 start = ray.position();
        Vector3 direction = ray.direction();       
        
        Intersection ret = Intersection.invalidIntersection();
        
        Vector3 v = start.minus(center);
        
        float b = 2f * (direction.dot(v));
        float c = v.dot(v) - radius * radius;
        
        float delta = b * b - 4 * c;
        
        if (delta >= 0) {
            float d = (float) (- (b + Math.sqrt(delta)) / 2f);
//            if (d < 0) {
//                d = (float) (- (b - Math.sqrt(delta)) / 2f);
//            }
            
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

    @Override
    public boolean isSphere() {
        return true;
    }

}
