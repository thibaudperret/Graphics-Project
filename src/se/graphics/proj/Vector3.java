package se.graphics.proj;

public final class Vector3 {

    private final float x;
    private final float y;
    private final float z;
    
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public static Vector3 vec3(float x, float y, float z) {
        return new Vector3(x, y, z);
    }
    
    public static Vector3 zeros() {
        return new Vector3(0, 0, 0);
    }
    
    public static Vector3 ones() {
        return new Vector3(1f, 1f, 1f);
    }
    
    public float x() {
        return x;
    }
    
    public float y() {
        return y;
    }
    
    public float z() {
        return z;
    }
    
    public float size() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }
    
    public Vector3 normalise() {
        float sz = size();
        return new Vector3(x / sz, y / sz, z / sz);
    }
    
    public Vector3 times(float s) {
        return new Vector3(s * x, s * y, s * z);
    }
    
    public Vector3 plus(Vector3 that) {
        return new Vector3(this.x + that.x, this.y + that.y, this.z + that.z);
    }
    
    public Vector3 minus(Vector3 that) {
        return new Vector3(this.x - that.x, this.y - that.y, this.z - that.z);
    }
    
    public float dot(Vector3 that) {
        return this.x * that.x + this.y * that.y + this.z * that.z;
    }
    
    public Vector3 cross(Vector3 that) {
        return new Vector3(this.y * that.z - this.z * that.y, this.z * that.x - this.x * that.z, this.x * that.y - this.y * that.x);
    }
    
    public Vector3 entrywiseDot(Vector3 that) {
        return new Vector3(this.x * that.x, this.y * that.y, this.z * that.z);
    }
    
    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")T";
    }
    
}
