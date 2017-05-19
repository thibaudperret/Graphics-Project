package se.graphics.proj;

public abstract class Material {

    private Vector3 reflectance;
    private float diffuseCoef;
    private float specularCoef;
    private float absorptionCoef;
    
    
        
    public Material(Vector3 reflectance, float diffuseCoef, float specularCoef, float absorptionCoef) {
        
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
        
        if(reflectance == null) {
            throw new IllegalArgumentException();
        }
        
        this.reflectance = reflectance;
        this.diffuseCoef = diffuseCoef;
        this.specularCoef = specularCoef;
        this.absorptionCoef = absorptionCoef;        
    }
    

    
    public Vector3 reflectance() {
        return reflectance;
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
    
    public abstract Material setReflectance(Vector3 reflectance);    
    public abstract Material setDiffuseCoef(float diffuseCoef);    
    public abstract Material setSpecularCoef(float specularCoef);    
    public abstract Material setAbsorptionCoef(float absorptionCoef);

    
    public abstract boolean isOpaque();
    public abstract boolean isTransparent();
    
    public OpaqueObject asOpaque() {
        if(isOpaque()) {
            return (OpaqueObject) this;
        } else {
            throw new IllegalStateException("Cannot interpret material as Opaque");
        }
    }
    
    public LightConductor asTransparent() {
        if(isOpaque()) {
            return (LightConductor) this;
        } else {
            throw new IllegalStateException("Cannot interpret material as Transparent");
        }
    }
    
    
}
