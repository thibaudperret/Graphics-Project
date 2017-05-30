package tree;

import java.util.List;
import java.util.stream.Collectors;
import math.Vector3;
import se.graphics.proj.Photon;
import util.MaxHeap;
import util.Pair;

/**
 * The abstract class representing a Tree.
 *
 */
public abstract class Tree {
    
    public abstract boolean isNode();
    public abstract boolean isNil();
    
    public Node asNode() {
        if(this.isNode()) {
            return (Node) this;
        }
        else{
            throw new IllegalStateException("cannot interpret nil as node");
        }
    }
    
    /**
     * This function returns a balanced Tree given a list of Photon. It functions recursively, we first try to find 2 
     * groups of photons that are relatively closed to each others and then balance both groups separately.
     * 
     * @param list
     * The list of Photon.
     * @return
     * The balanced Tree.
     */
    public static Tree balance(List<Photon> list) {        
        if(list.isEmpty()) {
            return new Nil();
        }
        
        if(list.size() == 1) {
            return new Node(list.get(0));
        }
        
        if (list.size() == 2) {
            return new Node(list.get(0), new Node(list.get(1)));
        }
        
        float dist = 0;
        Photon max1 = null;
        Photon max2 = null;
        //Here we try to find the 2 most distant photons.
        for(Photon p1: list) {
            for(Photon p2: list) {
                float d = p1.distance(p2);
                if(d > dist) {
                    dist = d;
                    max1 = p1;
                    max2 = p2;
                }
            }
        }
        
        //Then we calculate the vector between the 2 most distant point.
        Vector3 dim = max1.position().minus(max2.position());
        Vector3 dimhat = dim.normalise();
        Vector3 m2 = max2.position();
        
        /* Here we take advantage of lambda function since we are using Java 8. We map each photons into its projection 
         * on the vector dim and we sort them regarding the distance to the point m2.
         */
        List<Photon> sorted = list.stream().map(p -> {
            Vector3 d = p.position().minus(m2);
            Vector3 projected = dimhat.times(d.dot(dimhat));
            return new Pair<>(projected.size(), p);
        }).sorted((p1, p2) -> Float.compare(p1.getLeft(), p2.getLeft())).map(p -> p.getRight()).collect(Collectors.toList());
        
        /* Finally we separate the list in 2 groups and balanced each groups separately. We keep the element in the
         * middle as our top element for the Node.
         */
        int size = sorted.size();
        List<Photon> left = sorted.subList(0, size/2);
        List<Photon> right = sorted.subList(size/2+1, size);
        Photon median = sorted.get(size/2);
        return new Node(median, balance(left), balance(right), dim.normalise());
    }
    
    /**
     * A function to find the n (nbPhotons) closest photons to a point (position).
     * 
     * @param nbPhotons
     * The number of photons to find.
     * @param position
     * The position from where we should find the closest photons.
     * @param maxDistance
     * The maximum distance from which we can take photons.
     * @return
     * A heap with the n closest photons.
     */
    public MaxHeap nearestPhotons(int nbPhotons, Vector3 position, float maxDistance) {
        MaxHeap maxHeap = new MaxHeap(nbPhotons);
        nearestPhotons(maxHeap, position, maxDistance, nbPhotons);
        return maxHeap;
    }
    
    /**
     * A function to find the n (nbPhotons) closest photons to a point (position).
     * 
     * @param maxHeap
     * The heap that we should fill.
     * @param nbPhotons
     * The number of photons to find.
     * @param position
     * The position from where we should find the closest photons.
     * @param maxDistance
     * The maximum distance from which we can take photons.
     * @return
     * A heap with the n closest photons.
     */
    public void nearestPhotons(MaxHeap maxHeap, Vector3 position, float maxDistance, int nbPhotons) {
        if (this.isNil()) {
            //If the current elemet is nil, we do not need to do modifications.
            return;
        }

        Node currentNode = this.asNode();
        Vector3 median = currentNode.photon().position();
        Vector3 normal = currentNode.normal();        
        Vector3 p = position.minus(median);
        
        if (!currentNode.isLeaf()) {
            float delta = p.dot(normal);
            //If we are on the left and the right node is Nil
            if (delta < 0 || currentNode.right().isNil()) {
                currentNode.left().nearestPhotons(maxHeap, position, maxDistance, nbPhotons);
                //Check also if we need to check in the right Tree.
                if (delta * delta < maxDistance * maxDistance && !currentNode.right().isNil()) {
                    currentNode.right().nearestPhotons(maxHeap, position, maxDistance, nbPhotons);
                }
            } else {
                //Otherwise check the photon in the right Tree.
                currentNode.right().nearestPhotons(maxHeap, position, maxDistance, nbPhotons);
                //And verify it we need to do so in the left Tree.
                if (delta * delta < maxDistance * maxDistance) {
                    currentNode.left().nearestPhotons(maxHeap, position, maxDistance, nbPhotons);
                }
            }
        }
        
        //Finally insert the current element if its distance is not too far.
        float delta = p.size();
        if (delta * delta < maxDistance * maxDistance) {
            maxHeap.insert(currentNode.photon(), delta);
        }
    } 
}
