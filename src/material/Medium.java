package material;

import math.Vector3;
import util.Color;

public class Medium {

        private float refractiveIndex;
        private Vector3 absorbedColor;
        public static Medium AIR = new Medium(1.000293f, new Vector3(0, 0, 0));
        public static Medium WATER = new Medium(1.3325f, new Vector3(0, 0, 0));
        public static Medium GLASS = new Medium(1.5168f, Color.WHITE);
        public static Medium VACUUM = new Medium(1f, new Vector3(0f, 0f, 0f));

        
        public Medium(float refractiveIndex, Vector3 absorbedColor) {
            this.refractiveIndex = refractiveIndex;
            this.absorbedColor = absorbedColor;
        }
        
        public float refractiveIndex() {
            return refractiveIndex;
        }
        
        public Vector3 absorbedColor(){
            return absorbedColor;
        }
        
        public Medium setRefractiveIndex(float refractiveIndex) {
            return new Medium(refractiveIndex, absorbedColor);
        }
        
        public Medium setAbsorbedColor(Vector3 absorbedColor) {
            return new Medium(refractiveIndex, absorbedColor);
        }
        
        public static float computeTransmissionCoef(Vector3 incident, Vector3 normal) {
//            return incident.normalise().dot(normal.times(-1f));
            return 0.6f;
        }
    
}
