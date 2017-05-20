package se.graphics.proj;

import java.util.List;

public abstract class ProjectionMap {

    
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
        
        return builder.build();        
    }
    
    
    public static DirectionalProjectionMap computeDirectionalTriangleMap(Lamp lamp, List<Item> box) {
        
        if(! lamp.shape().isTriangle() || !lamp.isDirectional()) {
            throw new IllegalStateException("Impossible to create projection map for non-triangle shape / non-directional lamp");
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
        
        return builder.build();        
    }
    
}
