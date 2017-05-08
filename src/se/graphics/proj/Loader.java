package se.graphics.proj;

import static se.graphics.proj.Vector3.vec3;
import static se.graphics.proj.Vector3.ones;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Loader {

    private static List<Item> items = new ArrayList<>();
    
    private Loader() {
        
    }

    /**
     * Lazy evaluation of the Cornell Box
     * @return a list of items representing the Cornell Box
     */
    public static List<Item> cornellBox() {
        if (items.isEmpty()) {
            final float L = 555; // Length of Cornell Box side

            // ------------------- BOX -------------------
            Vector3 A = vec3(L, 0, 0).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            Vector3 B = vec3(0, 0, 0).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            Vector3 C = vec3(L, 0, L).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            Vector3 D = vec3(0, 0, L).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));

            Vector3 E = vec3(L, L, 0).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            Vector3 F = vec3(0, L, 0).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            Vector3 G = vec3(L, L, L).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            Vector3 H = vec3(0, L, L).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            
            // Floor
            items.add(PhysicalObject.physicalTriangle(new Triangle(C, B, A), Material.tradeOff(Color.GREEN)));
            items.add(PhysicalObject.physicalTriangle(new Triangle(C, D, B), Material.tradeOff(Color.GREEN)));
            // Left wall
            items.add(PhysicalObject.physicalTriangle(new Triangle(A, E, C), Material.tradeOff(Color.WHITE)));
            items.add(PhysicalObject.physicalTriangle(new Triangle(C, E, G), Material.tradeOff(Color.WHITE)));

            // Right wall
            items.add(PhysicalObject.physicalTriangle(new Triangle(F, B, D), Material.tradeOff(Color.WHITE)));
            items.add(PhysicalObject.physicalTriangle(new Triangle(H, F, D), Material.tradeOff(Color.WHITE)));

            // Ceiling
            items.add(new Light(new Triangle(E, F, G), 14f, Color.WHITE));
            items.add(new Light(new Triangle(F, H, G), 14f, Color.WHITE));

            // Back wall
            items.add(PhysicalObject.physicalTriangle(new Triangle(G, D, C), Material.tradeOff(Color.ORANGE)));
            items.add(PhysicalObject.physicalTriangle(new Triangle(G, H, D), Material.tradeOff(Color.ORANGE)));

            // ------------------- BL1 -------------------
            A = vec3(290, 0, 114).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            B = vec3(130, 0, 65).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            C = vec3(240, 0, 272).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            D = vec3(82, 0, 225).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));

            E = vec3(290, 165, 114).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            F = vec3(130, 165, 65).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            G = vec3(240, 165, 272).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            H = vec3(82, 165, 225).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            
            // Front
            items.add(PhysicalObject.physicalTriangle(new Triangle(E, B, A), Material.tradeOff(Color.RED)));
            items.add(PhysicalObject.physicalTriangle(new Triangle(E, F, B), Material.tradeOff(Color.RED)));

            // Front
            items.add(PhysicalObject.physicalTriangle(new Triangle(F, D, B), Material.tradeOff(Color.RED)));
            items.add(PhysicalObject.physicalTriangle(new Triangle(F, H, D), Material.tradeOff(Color.RED)));

            // Back
            items.add(PhysicalObject.physicalTriangle(new Triangle(H, C, D), Material.tradeOff(Color.RED)));
            items.add(PhysicalObject.physicalTriangle(new Triangle(H, G, C), Material.tradeOff(Color.RED)));

            // Left
            items.add(PhysicalObject.physicalTriangle(new Triangle(G, E, C), Material.tradeOff(Color.RED)));
            items.add(PhysicalObject.physicalTriangle(new Triangle(E, A, C), Material.tradeOff(Color.RED)));

            // Top
            items.add(PhysicalObject.physicalTriangle(new Triangle(G, F, E), Material.tradeOff(Color.RED)));
            items.add(PhysicalObject.physicalTriangle(new Triangle(G, H, F), Material.tradeOff(Color.RED)));

            // ------------------- BL2 -------------------
            A = vec3(423, 0, 247).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            B = vec3(265, 0, 296).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            C = vec3(472, 0, 406).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            D = vec3(314, 0, 456).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));

            E = vec3(423, 330, 247).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            F = vec3(265, 330, 296).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            G = vec3(472, 330, 406).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
            H = vec3(314, 330, 456).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));

            // Front
            items.add(PhysicalObject.physicalTriangle(new Triangle(E, B, A), Material.tradeOff(Color.BLUE)));
            items.add(PhysicalObject.physicalTriangle(new Triangle(E, F, B), Material.tradeOff(Color.BLUE)));

            // Front
            items.add(PhysicalObject.physicalTriangle(new Triangle(F, D, B), Material.tradeOff(Color.BLUE)));
            items.add(PhysicalObject.physicalTriangle(new Triangle(F, H, D), Material.tradeOff(Color.BLUE)));

            // Back
            items.add(PhysicalObject.physicalTriangle(new Triangle(H, C, D), Material.tradeOff(Color.BLUE)));
            items.add(PhysicalObject.physicalTriangle(new Triangle(H, G, C), Material.tradeOff(Color.BLUE)));

            // Left
            items.add(PhysicalObject.physicalTriangle(new Triangle(G, E, C), Material.tradeOff(Color.BLUE)));
            items.add(PhysicalObject.physicalTriangle(new Triangle(E, A, C), Material.tradeOff(Color.BLUE)));

            // Top
            items.add(PhysicalObject.physicalTriangle(new Triangle(G, F, E), Material.tradeOff(Color.BLUE)));
            items.add(PhysicalObject.physicalTriangle(new Triangle(G, H, F), Material.tradeOff(Color.BLUE)));
            
//            items = items.stream().map(t -> {
//                if(t.shape().isTriangle()) {
//                    Vector3 v1 = t.shape().asTriangle().v1();
//                    Vector3 v2 = t.shape().asTriangle().v2();
//                    Vector3 v3 = t.shape().asTriangle().v3();
//                    
//                    v1 = v1.times(2f / 555).minus(ones()).times(-1f);
//                    v2 = v2.times(2f / 555).minus(ones()).times(-1f);
//                    v3 = v3.times(2f / 555).minus(ones()).times(-1f);
//                    
//                    Item r;
//                    
//                    if(t.isPhysical()) {
//                        r = PhysicalObject.physicalTriangle(new Triangle(v1.x(), v1.y(), -v1.z(),
//                                v2.x(), v2.y(), -v2.z(),
//                                v3.x(), v3.y(), -v3.z()), t.asPhysicalObject().material());
//      
//                    } else {
//                        r = new Light(new Triangle(v1.x(), v1.y(), -v1.z(),
//                                v2.x(), v2.y(), -v2.z(),
//                                v3.x(), v3.y(), -v3.z()), t.asLight().power(), t.asLight().color());
//                    }                    
//                    
//                    return r;
//                    
//                } else {
//                    
//                    return t;
//                    
//                }
//                
//                
//                
//            }).collect(Collectors.toList());
             
        }

        return items;
    }

    public static List<Item> testModel() {
        return Arrays.asList(new Light(new Triangle(-0.5f, -0.5f, 1f, 1f, 0f, 1f, 0f, 1f, 1f), 14f, Color.WHITE), new PhysicalObject(new Triangle(-1f, 0f, 2f, -1.5f, -0.33f, 2f, -0.33f, -1f, 2f), Material.tradeOff(Color.BLUE)));
    }
  

}
