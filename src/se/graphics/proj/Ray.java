package se.graphics.proj;

import material.Medium;
import math.Vector3;
import util.Pair;

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
         
        return generateRay(position, normal, phi, theta);
    }
    public static Pair<Float, Float> generateSphericalRandomDirection(Vector3 normal) {
        float thetaN = (float) Math.atan2(normal.y(), normal.x());
        float phiN = (float) Math.acos(normal.z());
        return new Pair<>(thetaN + (float) (((Math.random() * 2) - 1) * Math.PI / 2), phiN + (float) (((Math.random() * 2) - 1) * Math.PI / 2));
    }
    
    public static Ray generateRay(Vector3 position, Vector3 normal, float phi, float theta) {
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
    
//    public static Ray refractedRay(Vector3 direction, Vector3 normal, Vector3 collisionPoint, Medium oldMedium, Medium newMedium) {
//        float theta1 = (float) Math.acos(Math.abs(direction.dot(normal)) / (direction.size() * normal.size()));
//        float theta2 = (float) Math.asin(Math.sin(theta1) * oldMedium.refractiveIndex() / newMedium.refractiveIndex());
//        
//        Vector3 xp = direction.minus(normal.times(direction.dot(normal)));
//        float b = - direction.size() * (float) Math.cos(theta1);
//        
//        Vector3 newDirection = xp.times((float) Math.cos(1 - theta2)).plus(normal.times(b * (float) Math.cos(theta2))).plus(direction.minus(xp).minus(normal.times(b)));
//        
//        return new Ray(collisionPoint, newDirection);
//    }
    
    public static Ray refractedRay(Vector3 direction, Vector3 normal, Vector3 collisionPoint, Medium oldMedium, Medium newMedium) {
        direction = direction.normalise(); // ça fait pas de mal
        
        float ratio = oldMedium.refractiveIndex() / newMedium.refractiveIndex();
        Vector3 nCrossDir = normal.cross(direction);
        Vector3 newDir = (normal.cross(nCrossDir.times(-1f))).times(ratio);
        newDir = newDir.minus(normal.times((float) Math.sqrt(1 - ratio * ratio * (nCrossDir.dot(nCrossDir)))));
        return new Ray(collisionPoint, newDir);
    }
    
    public static Pair<Float, Float> cartesianToSphericalDir(Vector3 cartesianDirection) {
        //TODO check if conversion is valid given our conventions
        float x = cartesianDirection.x();
        float y = cartesianDirection.y();
        float z = cartesianDirection.z();
        float size = cartesianDirection.size();
        
        return new Pair<Float, Float>((float)Math.acos(z/size), (float)Math.atan(y/x));
    }
    
    public static Vector3 sphericalToCartesianDir(Pair<Float, Float> angles) {
        //TODO check if conversion is valid given our conventions

        float theta = angles.getLeft();
        float phi = angles.getRight();
        
        float sinTheta = (float)Math.sin(theta);
        
        return new Vector3((float)(sinTheta*Math.cos(phi)),(float)(sinTheta*Math.sin(phi)), (float)Math.cos(theta)).normalise();
        
    }
    

    
   }
