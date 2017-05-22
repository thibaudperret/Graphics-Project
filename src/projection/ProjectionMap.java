package projection;

import java.util.List;

import item.DirectionalLamp;
import item.Item;
import item.Lamp;
import math.Vector3;
import projection.DiffuseProjectionMap.Builder;
import se.graphics.proj.Main;
import se.graphics.proj.Ray;
import util.Pair;

public abstract class ProjectionMap {
    
    private float ratioValidCells;
    
    public ProjectionMap(float ratioValidCells) {
        this.ratioValidCells = ratioValidCells;
    }

    public static DiffuseProjectionMap computeDiffuseTriangleMap(Lamp lamp, List<Item> box) {
        
        if(! lamp.shape().isTriangle() || !lamp.isDiffuse()) {
            throw new IllegalStateException("Impossible to create diffuse projection map for non-triangle shape / non-diffuse lamp");
        }
        
        float translationStep = 0.001f;
        
        Vector3 v1 = lamp.shape().asTriangle().v1();
        Vector3 v2 = lamp.shape().asTriangle().v2();
        Vector3 v3 = lamp.shape().asTriangle().v3();
        
        Vector3 translationAxis1 = v2.minus(v1);
        float dist1 = translationAxis1.size();
        
        Vector3 translationAxis2 = v3.minus(v1);
        float dist2 = translationAxis2.size();
        
        float rotationStep = (float)Math.PI / 40f;
        Vector3 orig;
        DiffuseProjectionMap.Builder builder = new DiffuseProjectionMap.Builder();
        
        for ( float stepV2 = 0; stepV2 < dist1; stepV2 += translationStep) {
            for ( float stepV3 = 0; stepV3 < dist2; stepV3 += translationStep) {
                orig = v1.plus(v2.times(stepV2)).plus(v3.times(stepV3));
                for(float theta = (float)-Math.PI/2f; theta < Math.PI/2f; theta += rotationStep) {
                    for(float phi = (float)-Math.PI/2f; phi < Math.PI/2f; phi += rotationStep) {
                       

                        Ray ray = Ray.generateRay(orig, lamp.shape().normalAt(orig), phi, theta);
                        builder.setCell(orig, theta, phi, Main.getClosestIntersection(ray, box).getLeft().valid());
                    }
                }
            }
        }
        
        for ( float stepV2 = 0; stepV2 < dist1; stepV2 += translationStep) {
            for ( float stepV3 = 0; stepV3 < dist2; stepV3 += translationStep) {
                orig = v1.plus(v2.times(stepV2)).plus(v3.times(stepV3));
                for(float theta = (float)-Math.PI/2f; theta < Math.PI/2f; theta += rotationStep) {
                    for(float phi = (float)-Math.PI/2f; phi < Math.PI/2f; phi += rotationStep) {
                       
                    }
                }
            }
        }
        
        return builder.build();        
    }
    
    
    public static DirectionalProjectionMap computeDirectionalTriangleMap(DirectionalLamp lamp, List<Item> box) {
        
        if(! lamp.shape().isTriangle()) {
            throw new IllegalStateException("Impossible to create projection map for non-triangle shape");
        }
        
        
        
        float translationStep = 0.001f;
        
        Vector3 v1 = lamp.shape().asTriangle().v1();
        Vector3 v2 = lamp.shape().asTriangle().v2();
        Vector3 v3 = lamp.shape().asTriangle().v3();
        
        Vector3 translationAxis1 = v2.minus(v1);
        float dist1 = translationAxis1.size();
        
        Vector3 translationAxis2 = v3.minus(v1);
        float dist2 = translationAxis2.size();
        
        Vector3 orig;
        DirectionalProjectionMap.Builder builder = new DirectionalProjectionMap.Builder();
        
        for ( float stepV2 = 0; stepV2 < dist1; stepV2 += translationStep) {
            for ( float stepV3 = 0; stepV3 < dist2; stepV3 += translationStep) {
                orig = v1.plus(v2.times(stepV2)).plus(v3.times(stepV3));
                       

                        Ray ray = new Ray(orig, lamp.asDirectional().lightDirection());
                        builder.setCell(orig, Main.getClosestIntersection(ray, box).getLeft().valid());
                   
            }
        }
        
        DirectionalProjectionMap map = builder.build();
        DirectionalProjectionMap.Builder mapExtended = new DirectionalProjectionMap.Builder(); 
        
        for ( float stepV2 = 0; stepV2 < dist1; stepV2 += translationStep) {
            for ( float stepV3 = 0; stepV3 < dist2; stepV3 += translationStep) {
                orig = v1.plus(v2.times(stepV2)).plus(v3.times(stepV3));
                mapExtended.setCell(orig, map.surroundingsContainValidCell(orig, v2, v3) || map.cellValid(orig));
                
                   
            }
        }
        
        return mapExtended.build(); 
    }
    
    
    public float closerTo(float value, float a, float b) {
        if(Math.abs(value - a) < Math.abs(value - b)) {
            return a;
        } else {
            return b;
        }
    }
    
    public float ratioValidCells() {
        return ratioValidCells;
    }
    
    public abstract boolean isDiffuse();
    public abstract boolean isDirectional();
    public abstract boolean isPoint();
    
    public DirectionalProjectionMap asDirectionalMap() {
        if(isDirectional()) {
            return (DirectionalProjectionMap) this;
        } else {
            throw new IllegalStateException("Impossible to cast a non-directional map into a directional one");
        }
    }
    
    public DiffuseProjectionMap asDiffuseMap() {
        if(isDiffuse()) {
            return (DiffuseProjectionMap) this;
        } else {
            throw new IllegalStateException("Impossible to cast a non-diffuse map into a diffuse one");
        }
    }
    
    public PointProjectionMap asPointMap() {
        if(isDirectional()) {
            return (PointProjectionMap) this;
        } else {
            throw new IllegalStateException("Impossible to cast a non-point map into a point one");
        }
    }
    
    public Pair<Float, Float> boundingValues(float value, float step) {
        float mod = value % step;
        return new Pair<>(value - mod, value + (step - mod));
    }
    

    
}
