package se.graphics.proj;

public final class Sphere extends Shape {
    
    private final Vector3 c;
    private final float r;
    
    public Sphere(Vector3 c, float r) {
        this.c = c;
        this.r = r;
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
        
        Vector3 v = start.minus(c);
        
        float b = 2f * (direction.dot(v));
        float c = v.dot(v) - r * r;
        
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
        return c;
    }
    
    public float r() {
        return r;
    }

    @Override
    public boolean isSphere() {
        return true;
    }

}
