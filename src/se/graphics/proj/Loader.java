package se.graphics.proj;

import static se.graphics.proj.Triangle.triangle;
import static se.graphics.proj.Sphere.sphere;
import static se.graphics.proj.Vector3.vec3;
import static se.graphics.proj.Vector3.ones;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Loader {

    private static List<Triangle> triangles = new ArrayList<>();
    private static List<Sphere> spheres = new ArrayList<>();
    

    private static final float a = 0.15f;
    private static final float b = 0.75f;

    private static Vector3 red = vec3(b, a, a);
    private static Vector3 yellow = vec3(b, b, a);
    private static Vector3 green = vec3(a, b, a);
    private static Vector3 cyan = vec3(a, b, b);
    private static Vector3 blue = vec3(a, a, b);
    private static Vector3 purple = vec3(b, a, b);
    private static Vector3 white = vec3(b, b, b);

    private Loader() {

    }

    /**
     * Lazy evaluation of the Cornell Box
     */
    public static List<Triangle> cornellBox() {
        if (triangles.isEmpty()) {
            final float L = 555; // Length of Cornell Box side            

            // ------------------- BOX -------------------
            Vector3 A = vec3(L, 0, 0);
            Vector3 B = vec3(0, 0, 0);
            Vector3 C = vec3(L, 0, L);
            Vector3 D = vec3(0, 0, L);

            Vector3 E = vec3(L, L, 0);
            Vector3 F = vec3(0, L, 0);
            Vector3 G = vec3(L, L, L);
            Vector3 H = vec3(0, L, L);
            
            // Floor
            triangles.add(triangle(C, B, A, green));
            triangles.add(triangle(C, D, B, green));

            // Left wall
            triangles.add(triangle(A, E, C, purple));
            triangles.add(triangle(C, E, G, purple));

            // Right wall
            triangles.add(triangle(F, B, D, yellow));
            triangles.add(triangle(H, F, D, yellow));

            // Ceiling
            triangles.add(triangle(E, F, G, cyan));
            triangles.add(triangle(F, H, G, cyan));

            // Back wall
            triangles.add(triangle(G, D, C, white));
            triangles.add(triangle(G, H, D, white));

            // ------------------- BL1 -------------------
            A = vec3(290, 0, 114);
            B = vec3(130, 0, 65);
            C = vec3(240, 0, 272);
            D = vec3(82, 0, 225);

            E = vec3(290, 165, 114);
            F = vec3(130, 165, 65);
            G = vec3(240, 165, 272);
            H = vec3(82, 165, 225);
            
            // Front
            triangles.add(triangle(E, B, A, red));
            triangles.add(triangle(E, F, B, red));

            // Front
            triangles.add(triangle(F, D, B, red));
            triangles.add(triangle(F, H, D, red));

            // Back
            triangles.add(triangle(H, C, D, red));
            triangles.add(triangle(H, G, C, red));

            // Left
            triangles.add(triangle(G, E, C, red));
            triangles.add(triangle(E, A, C, red));

            // Top
            triangles.add(triangle(G, F, E, red));
            triangles.add(triangle(G, H, F, red));

            // ------------------- BL2 -------------------
            A = vec3(423, 0, 247);
            B = vec3(265, 0, 296);
            C = vec3(472, 0, 406);
            D = vec3(314, 0, 456);

            E = vec3(423, 330, 247);
            F = vec3(265, 330, 296);
            G = vec3(472, 330, 406);
            H = vec3(314, 330, 456);

            // Front
            triangles.add(triangle(E, B, A, blue));
            triangles.add(triangle(E, F, B, blue));

            // Front
            triangles.add(triangle(F, D, B, blue));
            triangles.add(triangle(F, H, D, blue));

            // Back
            triangles.add(triangle(H, C, D, blue));
            triangles.add(triangle(H, G, C, blue));

            // Left
            triangles.add(triangle(G, E, C, blue));
            triangles.add(triangle(E, A, C, blue));

            // Top
            triangles.add(triangle(G, F, E, blue));
            triangles.add(triangle(G, H, F, blue));
            
            triangles = triangles.stream().map(t -> {
                Vector3 v1 = t.v1();
                Vector3 v2 = t.v2();
                Vector3 v3 = t.v3();

                v1 = v1.times(2f / 555).minus(ones()).times(-1f);
                v2 = v2.times(2f / 555).minus(ones()).times(-1f);
                v3 = v3.times(2f / 555).minus(ones()).times(-1f);
                
                Triangle r = new Triangle(v1.x(), v1.y(), -v1.z(),
                                          v2.x(), v2.y(), -v2.z(),
                                          v3.x(), v3.y(), -v3.z(),
                                          t.color());
                          
                return r;
            }).collect(Collectors.toList());
        }

        return triangles;
    }
    
    public static List<Sphere> thibaudBox() {
        if (spheres.isEmpty()) {            
            spheres.add(sphere(vec3(0f, 0f, 0f), 0.05f, white));
            spheres.add(sphere(vec3(0f, 0f, 0.8f), 0.4f, blue));
        }
        
        return spheres;
    }

}
