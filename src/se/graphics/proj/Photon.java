package se.graphics.proj;

public class Photon {
    
    private final Vector3 position;
    private final Vector3 power;
    private final float phi;
    private final float theta;
    private final short flag;
    
    public Photon(Vector3 position, Vector3 power, float phi, float theta, short flag) {
        this.position = position;
        this.power = power;
        this.phi = phi;
        this.theta = theta;
        this.flag = flag;        
    }

    public Vector3 position() {
        return position;
    }
    
    public Vector3 power() {
        return power;
    }
    
    public float phi() {
        return phi;
    }
    
    public float theta() {
        return theta;
    }
    
    public short flag() {
        return flag;
    }
    
    public Photon setPosition(Vector3 position) {
        return new Photon(position, power, phi, theta, flag);
    }
    
    public Photon setPower(Vector3 power) {
        return new Photon(position, power, phi, theta, flag);
    }
    
    public Photon setPhi(float phi) {
        return new Photon(position, power, phi, theta, flag);
    }
    
    public Photon setTheta(float theta) {
        return new Photon(position, power, phi, theta, flag);
    }
    
    public Photon setFlag(short flag) {
        return new Photon(position, power, phi, theta, flag);
    }
    
    public float distance(Photon p) {
        return (float) Math.sqrt( Math.pow(this.position.x() - p.position.x(), 2) + Math.pow(this.position.y() - p.position.y(), 2)
                                    + Math.pow(this.position.z() - p.position.z(), 2));
    }
}
