package se.graphics.proj;

public class Ray {
    
    private final Vector3 position;
    private final Vector3 direction;
    
    public Ray(Vector3 position, Vector3 direction) {
        this.position = position;
        this.direction = direction.normalise();
    }
    
    /**
     * For lazy people
     */
    public static Ray ray(Vector3 position, Vector3 direction) {
        return new Ray(position, direction.normalise());
    }
    
    public Vector3 position(){
        return position;
    }
    
    public Vector3 direction(){
        return direction;
    }
    
    public static Ray generateRandomRay(Vector3 position, Vector3 normal){
        float thetaN = (float) Math.atan2(normal.y(), normal.x());
        float phiN = (float) Math.acos(normal.z());

//        float theta = thetaN + (float) (Math.random() * Math.PI);
        float theta = thetaN + (float) (((Math.random() * 2) - 1) * Math.PI / 2);
        float phi = phiN + (float) (((Math.random() * 2) - 1) * Math.PI / 2);
        
        Vector3 newDirection = new Vector3(
                                            (float) (Math.sin(phi) * Math.cos(theta)),
                                            (float) (Math.sin(phi) * Math.sin(theta)),
                                            (float) (Math.cos(phi))
                                            ).normalise();
        
        return new Ray(position, newDirection);
    }
    
    public static Ray specularBounce(Ray ray, Vector3 normal, Vector3 collisionPoint) {
        Vector3 newDir = ray.direction().minus(normal.times(2f).times(normal.dot(ray.direction())));
        return new Ray(collisionPoint, newDir);
    }
}
