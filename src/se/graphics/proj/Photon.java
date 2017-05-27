package se.graphics.proj;

import math.Vector3;

public class Photon {

    private final Vector3 position;
    private final Vector3 power;
    private final float phi;
    private final float theta;

    public Photon(Vector3 position, Vector3 power, float theta, float phi) {
        this.position = position;
        this.power = power;
        this.phi = phi;
        this.theta = theta;
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

    public Photon setPosition(Vector3 position) {
        return new Photon(position, power, phi, theta);
    }

    public Photon setPower(Vector3 power) {
        return new Photon(position, power, phi, theta);
    }

    public Photon setPhi(float phi) {
        return new Photon(position, power, phi, theta);
    }

    public Photon setTheta(float theta) {
        return new Photon(position, power, phi, theta);
    }

    public float distance(Photon p) {
        return (float) Math.sqrt(Math.pow(this.position.x() - p.position.x(), 2) + Math.pow(this.position.y() - p.position.y(), 2) + Math.pow(this.position.z() - p.position.z(), 2));
    }

    public boolean equals(Photon other) {
        return position.equals(other.position()) && power.equals(other.power) && phi == other.phi && theta == other.theta;
    }
}
