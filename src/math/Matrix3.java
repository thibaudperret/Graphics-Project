package math;

import static java.lang.Math.*;

/**
 * 
 * c1x c2x c3x
 * c1y c2y c3y
 * c1z c2z c3z
 *
 */
public final class Matrix3 {

    private final Vector3 c1;
    private final Vector3 c2;
    private final Vector3 c3;
    
    public Matrix3(Vector3 c1, Vector3 c2, Vector3 c3) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
    }
    
    public Matrix3(float c1x, float c2x, float c3x, float c1y, float c2y, float c3y, float c1z, float c2z, float c3z) {
        this.c1 = new Vector3(c1x, c2x, c3x);
        this.c2 = new Vector3(c1y, c2y, c3y);
        this.c3 = new Vector3(c1z, c2z, c3z);
    }
    
    /**
     * For lazy people
     */
    public static Matrix3 mat3(Vector3 c1, Vector3 c2, Vector3 c3) {
        return new Matrix3(c1, c2, c3);
    }
    
    /**
     * For lazy people
     */
    public static Matrix3 mat3(float c1x, float c2x, float c3x, float c1y, float c2y, float c3y, float c1z, float c2z, float c3z) {
        return new Matrix3(c1x, c1y, c1z, c2x, c2y, c2z, c3x, c3y, c3z);
    }
    
    public static Matrix3 identityMatrix() {
        return new Matrix3(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
    }
    
    public Vector3 c1() {
        return c1;
    }
    
    public Vector3 c2() {
        return c2;
    }
    
    public Vector3 c3() {
        return c3;
    }
    
    public Matrix3 times(float s) {
        return new Matrix3(c1.times(s), c2.times(s), c3.times(s));
    }
    
    public Vector3 times(Vector3 that) {
        Matrix3 t = transposed();
        return new Vector3(t.c1.dot(that), t.c2.dot(that), t.c3.dot(that));
    }
    
    public Matrix3 times(Matrix3 that) {
        Matrix3 thisT = this.transposed();
        return new Matrix3(thisT.times(that.c1), thisT.times(that.c2), thisT.times(that.c3)); 
    }
    
    public Matrix3 transposed() {
        return new Matrix3(c1.x(), c1.y(), c1.z(), c2.x(), c2.y(), c2.z(), c3.x(), c3.y(), c3.z());
    }
    
    public Matrix3 inversed() {
        float a = c1.x();
        float b = c2.x();
        float c = c3.x();
        float d = c1.y();
        float e = c2.y();
        float f = c3.y();
        float g = c1.z();
        float h = c2.z();
        float i = c3.z();
        
        float A = (e * i - f * h);
        float B = -(d * i - f * g);
        float C = (d * h - e * g);
        float D = -(b * i - c * h);
        float E = (a * i - c * g);
        float F = -(a * h - b * g);
        float G = (b * f - c * e);
        float H = - (a * f - c * d);
        float I = (a * e - b * d);
        
        float det = a * A + b * B + c * C;
        
        return new Matrix3(A, D, G, B, E, H, C, F, I).times(1 / det);
    }
    
    public static Matrix3 xRotationMatrix(float alpha) {
        return new Matrix3(1f, 0f, 0f, 0f, (float) cos(alpha), (float) sin(alpha), 0f, (float) -sin(alpha), (float) cos(alpha));
    }
    
    public static Matrix3 yRotationMatrix(float alpha) {
        return new Matrix3((float) cos(alpha), 0f, (float) sin(alpha), 0f, 1f, 0f, (float) -sin(alpha), 0f, (float) cos(alpha));
    }
    
    @Override
    public String toString() {
        String s = c1.x() + "\t " + c2.x() + "\t " + c3.x() + "\n" 
                 + c1.y() + "\t " + c2.y() + "\t " + c3.y() + "\n"
                 + c1.z() + "\t " + c2.z() + "\t " + c3.z();
        return s;
    }
    
}
