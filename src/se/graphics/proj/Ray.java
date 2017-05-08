package se.graphics.proj;

public class Ray {
    
    private Vector3 position;
    private Vector3 direction;
    
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
        float phiN = (float) Math.atan2(normal.y(), normal.x());

        float theta = (float) (Math.random() * 2 * Math.PI);
        float phi = phiN + (float) (((Math.random() * 2) - 1) * Math.PI / 2);
        
        Vector3 newDirection = new Vector3(
                                            (float) (Math.sin(phi) * Math.sin(theta)),
                                            (float) (Math.sin(phi) * Math.cos(theta)),
                                            (float) (Math.cos(phi))
                                            ).normalise();
        
        return new Ray(position, newDirection);
    }
}
