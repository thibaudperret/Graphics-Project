package se.graphics.proj;

public class Node extends Tree {
    
    private final Photon photon;
    private final Tree left;
    private final Tree right;
    
    public Node(Photon p){
        this.photon = p;
        this.left = new Nil();
        this.right = new Nil();
    }
    
    public Node(Photon p, Node l){
        this.photon = p;
        this.left = l;
        this.right = new Nil();
    }
    
    public Node(Photon p, Tree l, Tree r){
        this.photon = p;
        this.left = l;
        this.right = r;
    }
    
    public Tree left(){
        return left;
    }
    
    public Tree right(){
        return right;
    }
    
    public Photon photon(){
        return photon;
    }
    
    @Override
    public boolean isNode() {
        return true;
    }

    @Override
    public boolean isNil() {
        return false;
    }
}