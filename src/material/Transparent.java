package material;

import math.Vector3;

public class Transparent extends Material{
    
    private Medium medium;
    
    public Transparent(float diffuseCoef, float specularCoef, float absorptionCoef, Medium medium) {
        super(medium.absorbedColor(), diffuseCoef, specularCoef, absorptionCoef);
        this.medium = medium;
    }
    
    public Medium medium() {
        return medium;
    }
    
    public Transparent setMedium(Medium newMedium) {
        return new Transparent(diffuseCoef(), specularCoef(), absorptionCoef(), newMedium);
    }
    
    public static Transparent idealSpecular(){
        return new Transparent(0f, 1f, 0.01f, Medium.WATER);
    }
    
    public static Transparent mainlySpecular() {
        return new Transparent(0.2f, 0.8f, 0.01f, Medium.WATER);
    }
    
    @Override
    public Transparent setReflectance(Vector3 reflectance) {
        return new Transparent(diffuseCoef(), specularCoef(), absorptionCoef(), medium());
    }
    
    @Override
    public  Transparent setAbsorptionCoef(float absorptionCoef) {
        return new Transparent(diffuseCoef(), specularCoef(), absorptionCoef, medium);
    }
    
    @Override
    public Transparent setSpecularCoef(float specularCoef) {
        return new Transparent(diffuseCoef(), specularCoef, absorptionCoef(), medium);
    }
    
    @Override
    public Transparent setDiffuseCoef(float diffuseCoef) {
        return new Transparent(diffuseCoef, specularCoef(), absorptionCoef(), medium);
    }
    
    @Override
    public boolean isOpaque() {
        return false;
    }
    
    @Override
    public boolean isTransparent() {
        return true;
    }

 
}
