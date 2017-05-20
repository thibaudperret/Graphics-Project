package se.graphics.proj;

public class Opaque extends Material{
    
    public Opaque(Vector3 reflectance, float diffuseCoef, float specularCoef, float absorptionCoef) {
        super(reflectance, diffuseCoef, specularCoef, absorptionCoef);
    }
    
    
    public Opaque setReflectance(Vector3 reflectance){
        return new Opaque(reflectance, diffuseCoef(), specularCoef(), absorptionCoef());
    }
    
    public static Opaque idealDiffuse(Vector3 reflectance) {
        return new Opaque(reflectance, 1f, 0f, 0.2f);
    }
    
    public static Opaque idealSpecular(Vector3 reflectance) {
        return new Opaque(reflectance, 0f, 1f, 0.2f);
    }
    
    public static Opaque tradeOff(Vector3 reflectance) {
        return new Opaque(reflectance, 0.2f, 0.8f, 0.2f);
    }


    @Override
    public Material setDiffuseCoef(float diffuseCoef) {
        return new Opaque(reflectance(), diffuseCoef, specularCoef(), absorptionCoef());
    }


    @Override
    public Material setSpecularCoef(float specularCoef) {
        return new Opaque(reflectance(), diffuseCoef(), specularCoef, absorptionCoef());

    }


    @Override
    public Material setAbsorptionCoef(float absorptionCoef) {
        return new Opaque(reflectance(), diffuseCoef(), specularCoef(), absorptionCoef);

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
