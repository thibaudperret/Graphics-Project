package tree;

import math.Vector3;
import se.graphics.proj.Photon;

/**
 * The class representing a Node.
 * A node has a photon, a Vector3 representing the normal to the plane it comes from, and two children.
 *
 */
public class Node extends Tree {

    private final Photon photon;
    private final Vector3 planeNormal;
    private final Tree left;
    private final Tree right;

    public Node(Photon p) {
        this.photon = p;
        this.left = new Nil();
        this.right = new Nil();
        this.planeNormal = Vector3.zeros();
    }

    public Node(Photon p, Tree l) {
        this.photon = p;
        this.left = l;
        this.right = new Nil();
        this.planeNormal = Vector3.zeros();
    }

    public Node(Photon p, Tree l, Tree r, Vector3 normal) {
        this.photon = p;
        this.left = l;
        this.right = r;
        this.planeNormal = normal;
    }

    public Tree left() {
        return left;
    }

    public Tree right() {
        return right;
    }

    public Photon photon() {
        return photon;
    }

    public Vector3 normal() {
        return planeNormal;
    }
    
    public boolean isLeaf() {
        return left.isNil() && right.isNil();
    }

    @Override
    public boolean isNode() {
        return true;
    }

    @Override
    public boolean isNil() {
        return false;
    }

    @Override
    public String toString() {
        return "(" + photon.position() + ", " + left + ", " + right + ")";
    }
}