package se.graphics.proj;

public class Ray {
    
    private Vector3 position;
    private Vector3 direction;
    
    public Ray(Vector3 position, Vector3 direction) {
        this.position = position;
        this.direction = direction;
    }
    
    /**
     * For lazy people
     */
    public static Ray ray(Vector3 position, Vector3 direction) {
        return new Ray(position, direction);
    }
    
    public Vector3 position(){
        return position;
    }
    
    public Vector3 direction(){
        return direction;
    }
    
    public static Ray generateRandomRay(Vector3 position, Vector3 normal){
        Vector3 axis = new Vector3(-normal.y(), normal.x(), 0);
        
        float theta = (float) (Math.random() * 2 * Math.PI);
        float phi = (float) (Math.random() * Math.PI / 2);
        
        Vector3 newDirection = new Vector3(
                                            (float) (axis.x() * Math.sin(theta) * Math.sin(phi)),
                                            (float) (axis.y() * Math.sin(theta) * Math.cos(phi)),
                                            (float) (axis.z() * Math.cos(theta))
                                            ).normalise();
        
        return new Ray(position, newDirection);
    }
}
