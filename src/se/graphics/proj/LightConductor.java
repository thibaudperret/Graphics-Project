package se.graphics.proj;

public class LightConductor extends Material{
    
    private Medium medium;
    
  
    
    public LightConductor(float diffuseCoef, float specularCoef, float absorptionCoef, Medium medium) {
        super(medium.absorbedColor(), diffuseCoef, specularCoef, absorptionCoef);
        this.medium = medium;
    }
    
    public Medium medium() {
        return medium;
    }
    
    public LightConductor setMedium(Medium newMedium) {
        return new LightConductor(diffuseCoef(), specularCoef(), absorptionCoef(), newMedium);
    }
    
    public static LightConductor idealSpecular(){
        return new LightConductor(0f, 1f, 0.01f, Medium.WATER);
    }
    
    public static LightConductor mainlySpecular() {
        return new LightConductor(0.2f, 0.8f, 0.01f, Medium.WATER);
    }
    
    @Override
    public LightConductor setReflectance(Vector3 reflectance) {
        return new LightConductor(diffuseCoef(), specularCoef(), absorptionCoef(), medium());
    }
    
    @Override
    public  LightConductor setAbsorptionCoef(float absorptionCoef) {
        return new LightConductor(diffuseCoef(), specularCoef(), absorptionCoef, medium);

    }
    
    @Override
    public LightConductor setSpecularCoef(float specularCoef) {
        return new LightConductor(diffuseCoef(), specularCoef, absorptionCoef(), medium);

    }
    
    @Override
    public LightConductor setDiffuseCoef(float diffuseCoef) {
        return new LightConductor(diffuseCoef, specularCoef(), absorptionCoef(), medium);

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
