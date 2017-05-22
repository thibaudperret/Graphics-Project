package se.graphics.proj;

public abstract class Shape {
            
    public boolean isTriangle() {
        return false;
    }
    
    public boolean isSphere() {
        return false;
    }
    
    public abstract Intersection intersection(Ray ray);
    public abstract Vector3 normalAt(Vector3 position);
    public abstract Vector3 getCenter();
    public abstract Vector3 randomPoint();

    
    /**
     * Transforms the object into a instance of the Triangle class
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
