package se.graphics.proj;

import static math.Vector3.ones;
import static math.Vector3.vec3;

import geometry.Rectangle;
import geometry.Sphere;
import geometry.Triangle;
import item.DiffuseLamp;
import item.Item;
import item.Lamp;
import item.PhysicalObject;
import java.util.ArrayList;
import java.util.List;
import material.Opaque;
import math.Vector3;
import util.Color;

public final class Loader {

    private static List<Item> items = loadItems();
    private static List<Lamp> lightSources = loadLightSources();
    
    private Loader() {
        loadItems();
    }
    
    private static List<Item> loadItems() {
        List<Item> items = new ArrayList<>();
        // ------------------- BOX -------------------
        Vector3 A = new Vector3(-1f, 1f, -1f);
        Vector3 B = new Vector3(1f, 1f, -1f);
        Vector3 C = new Vector3(-1f, 1f, 1f);
        Vector3 D = new Vector3(1f, 1f, 1f);

        Vector3 E = new Vector3(-1f, -1f, -1f);
        Vector3 F = new Vector3(1f, -1f, -1f);
        Vector3 G = new Vector3(-1f, -1f, 1f);
        
        // Floor
        items.add(PhysicalObject.physicalRectangle(new Rectangle(A, B, C), Opaque.idealDiffuse(Color.WHITE)));
        
        // Left wall
        items.add(PhysicalObject.physicalRectangle(new Rectangle(A, E, C), Opaque.idealDiffuse(Color.LIGHT_BLUE)));

        // Right wall
        items.add(PhysicalObject.physicalRectangle(new Rectangle(B, D, F), Opaque.idealDiffuse(Color.RED)));

        // Ceiling
        items.add(PhysicalObject.physicalRectangle(new Rectangle(E, F, G), Opaque.idealDiffuse(Color.WHITE)));
        
        // Back wall
        items.add(PhysicalObject.physicalRectangle(new Rectangle(C, D, G), Opaque.idealDiffuse(Color.WHITE)));
        
        E = new Vector3(-0.5f, -0.9999f, -0.5f);
        F = new Vector3(0.5f, -0.9999f, -0.5f);
        G = new Vector3(-0.5f, -0.9999f, 0.5f);
        
        // Lamp
        Item lamp1 = new DiffuseLamp(new Rectangle(E, F, G),Opaque.idealDiffuse(Color.WHITE), 10f, Color.WHITE);
        items.add(lamp1);

//        // ------------------- BL1 -------------------
//        A = vec3(290, 0, 114).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//        B = vec3(130, 0, 65).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//        C = vec3(240, 0, 272).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//        D = vec3(82, 0, 225).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//
//        E = vec3(290, 165, 114).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//        F = vec3(130, 165, 65).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//        G = vec3(240, 165, 272).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//        H = vec3(82, 165, 225).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//        
//        // Front
//        items.add(PhysicalObject.physicalTriangle(new Triangle(E, B, A), OpaqueObject.idealDiffuse(Color.WHITE)));
//        items.add(PhysicalObject.physicalTriangle(new Triangle(E, F, B), OpaqueObject.idealDiffuse(Color.WHITE)));
//
//        // Front
//        items.add(PhysicalObject.physicalTriangle(new Triangle(F, D, B), OpaqueObject.idealDiffuse(Color.WHITE)));
//        items.add(PhysicalObject.physicalTriangle(new Triangle(F, H, D), OpaqueObject.idealDiffuse(Color.WHITE)));
//
//        // Back
//        items.add(PhysicalObject.physicalTriangle(new Triangle(H, C, D), OpaqueObject.idealDiffuse(Color.WHITE)));
//        items.add(PhysicalObject.physicalTriangle(new Triangle(H, G, C), OpaqueObject.idealDiffuse(Color.WHITE)));
//
//        // Left
//        items.add(PhysicalObject.physicalTriangle(new Triangle(G, E, C), OpaqueObject.idealDiffuse(Color.WHITE)));
//        items.add(PhysicalObject.physicalTriangle(new Triangle(E, A, C), OpaqueObject.idealDiffuse(Color.WHITE)));
//
//        // Top
//        items.add(PhysicalObject.physicalTriangle(new Triangle(G, F, E), OpaqueObject.idealDiffuse(Color.WHITE)));
//        items.add(PhysicalObject.physicalTriangle(new Triangle(G, H, F), OpaqueObject.idealDiffuse(Color.WHITE)));

//        // ------------------- BL2 -------------------
//        A = vec3(423, 0, 247).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//        B = vec3(265, 0, 296).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//        C = vec3(472, 0, 406).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//        D = vec3(314, 0, 456).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//
//        E = vec3(423, 330, 247).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//        F = vec3(265, 330, 296).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//        G = vec3(472, 330, 406).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//        H = vec3(314, 330, 456).times(2f / 555).minus(ones()).entrywiseDot(new Vector3(-1f, -1f, 1f));
//
//        // Front
//        items.add(PhysicalObject.physicalTriangle(new Triangle(E, B, A), OpaqueObject.idealDiffuse(Color.WHITE)));
//        items.add(PhysicalObject.physicalTriangle(new Triangle(E, F, B), OpaqueObject.idealDiffuse(Color.WHITE)));
//
//        // Front
//        items.add(PhysicalObject.physicalTriangle(new Triangle(F, D, B), OpaqueObject.idealDiffuse(Color.WHITE)));
//        items.add(PhysicalObject.physicalTriangle(new Triangle(F, H, D), OpaqueObject.idealDiffuse(Color.WHITE)));
//
//        // Back
//        items.add(PhysicalObject.physicalTriangle(new Triangle(H, C, D), OpaqueObject.idealDiffuse(Color.WHITE)));
//        items.add(PhysicalObject.physicalTriangle(new Triangle(H, G, C), OpaqueObject.idealDiffuse(Color.WHITE)));
//
//        // Left
//        items.add(PhysicalObject.physicalTriangle(new Triangle(G, E, C), OpaqueObject.idealDiffuse(Color.WHITE)));
//        items.add(PhysicalObject.physicalTriangle(new Triangle(E, A, C), OpaqueObject.idealDiffuse(Color.WHITE)));
//
//        // Top
//        items.add(PhysicalObject.physicalTriangle(new Triangle(G, F, E), OpaqueObject.idealDiffuse(Color.WHITE)));
//        items.add(PhysicalObject.physicalTriangle(new Triangle(G, H, F), OpaqueObject.idealDiffuse(Color.WHITE)));

        items.add(PhysicalObject.physicalSphere(new Sphere(new Vector3(0.2f, 0.7f, -0.4f), 0.3f), Opaque.idealSpecular(Color.WHITE)));
        items.add(PhysicalObject.physicalSphere(new Sphere(new Vector3(-0.5f, -0.1f, 0.4f), 0.4f), Opaque.idealDiffuse(Color.LIGHT_PURPLE)));
        
        return items;
    }
    
    private static List<Lamp> loadLightSources() {
        List<Lamp> lightSources = new ArrayList<>();
        
        // ------------------- LIGHTS -------------------
        Vector3 E = new Vector3(-0.5f, -0.9999f, -0.5f);
        Vector3 F = new Vector3(0.5f, -0.9999f, -0.5f);
        Vector3 G = new Vector3(-0.5f, -0.9999f, 0.5f);
        
        Lamp lamp1 = new DiffuseLamp(new Rectangle(E, F, G),Opaque.idealDiffuse(Color.WHITE), 14f, Color.WHITE);
        lightSources.add(lamp1);
        
        return lightSources;
    }

    /**
     * Lazy evaluation of the Cornell Box
     * @return a list of items representing the Cornell Box
     */
    public static List<Item> cornellBox() {
        return items;
    }
    
    public static List<Lamp> lightSources() {
        return lightSources;
    }
}
