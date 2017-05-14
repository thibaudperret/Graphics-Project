package se.graphics.proj;

public class OpaqueObject extends Material{
    
    public OpaqueObject(Vector3 reflectance, float diffuseCoef, float specularCoef, float absorptionCoef) {
        super(reflectance, diffuseCoef, specularCoef, absorptionCoef);
    }
    
    
    public OpaqueObject setReflectance(Vector3 reflectance){
        return new OpaqueObject(reflectance, diffuseCoef(), specularCoef(), absorptionCoef());
    }
    
    public static OpaqueObject idealDiffuse(Vector3 reflectance) {
        return new OpaqueObject(reflectance, 1f, 0f, 0.2f);
    }
    
    public static OpaqueObject idealSpecular(Vector3 reflectance) {
        return new OpaqueObject(reflectance, 0f, 1f, 0.2f);
    }
    
    public static OpaqueObject tradeOff(Vector3 reflectance) {
        return new OpaqueObject(reflectance, 0.8f, 0.2f, 0.2f);
    }


    @Override
    public Material setDiffuseCoef(float diffuseCoef) {
        return new OpaqueObject(reflectance(), diffuseCoef, specularCoef(), absorptionCoef());
    }


    @Override
    public Material setSpecularCoef(float specularCoef) {
        return new OpaqueObject(reflectance(), diffuseCoef(), specularCoef, absorptionCoef());

    }


    @Override
    public Material setAbsorptionCoef(float absorptionCoef) {
        return new OpaqueObject(reflectance(), diffuseCoef(), specularCoef(), absorptionCoef);

    }


    @Override
    public boolean isOpaque() {
        return true;
    }


    @Override
    public boolean isTransparent() {
        return false;
    }
    

}
