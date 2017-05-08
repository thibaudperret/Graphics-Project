package se.graphics.proj;

public class Material {

    private Vector3 reflectance;
    private Medium medium;
    private float diffuseCoef;
    private float specularCoef;
    private float absorptionCoef;
    
    
        
    public Material(Vector3 reflectance, Medium medium, float diffuseCoef, float specularCoef, float absorptionCoef) {
        
        if(       (diffuseCoef + specularCoef != 1.) 
                || diffuseCoef    > 1. 
                || diffuseCoef    < 0.
                || specularCoef   > 1.
                || specularCoef   < 0.
                || absorptionCoef < 0.
                || absorptionCoef > 1.  )           
        {
            throw new IllegalArgumentException("invalid coefficients");
        }
        
        if(reflectance == null || medium == null) {
            throw new IllegalArgumentException();
        }
        
        this.reflectance = reflectance;
        this.medium = medium;
        this.diffuseCoef = diffuseCoef;
        this.specularCoef = specularCoef;
        this.absorptionCoef = absorptionCoef;        
    }
    
    public static Material idealDiffuse(Vector3 reflectance) {
        return new Material(reflectance, null, 1f, 0f, 0.2f);
    }
    
    public static Material idealSpecular(Vector3 reflectance) {
        return new Material(reflectance, null, 0f, 1f, 0.2f);
    }
    
    public static Material tradeOff(Vector3 reflectance) {
        return new Material(reflectance, null, 0.5f, 0.5f, 0.2f);
    }
    
    public Vector3 reflectance() {
        return reflectance;
    }
    
    public Medium medium() {
        return medium;
    }
    
    public float diffuseCoef() {
        return diffuseCoef;
    }
    
    public float specularCoef() {
        return specularCoef;
    }
    
    public float absorptionCoef(){
        return absorptionCoef;
    }
    
    public Material setReflectance(Vector3 reflectance){
        return new Material(reflectance, medium, diffuseCoef, specularCoef, absorptionCoef);
    }
    
    public Material setMedium(Medium medium) {
        return new Material(reflectance, medium, diffuseCoef, specularCoef, absorptionCoef);
    }
    
    public Material setDiffuseCoef(float diffuseCoef) {
        return new Material(reflectance, medium, diffuseCoef, specularCoef, absorptionCoef);

    }
    
    public Material setSpecularCoef(float specularCoef) {
        return new Material(reflectance, medium, diffuseCoef, specularCoef, absorptionCoef);

    }
    
    public Material setAbsorptionCoef(float absorptionCoef) {
        return new Material(reflectance, medium, diffuseCoef, specularCoef, absorptionCoef);

    }
    
}
