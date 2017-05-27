package tree;

import java.util.List;
import java.util.stream.Collectors;

import math.Vector3;
import se.graphics.proj.Photon;
import util.MaxHeap;
import util.Pair;

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
        
        Vector3 dim = max1.position().minus(max2.position());
        Vector3 dimhat = dim.normalise();
        Vector3 m2 = max2.position();
        
        List<Photon> sorted = list.stream().map(p -> {
            Vector3 d = p.position().minus(m2);
            Vector3 projected = dimhat.times(d.dot(dimhat));
            return new Pair<>(projected.size(), p);
        }).sorted((p1, p2) -> Float.compare(p1.getLeft(), p2.getLeft())).map(p -> p.getRight()).collect(Collectors.toList());
        
        int size = sorted.size();
        List<Photon> left = sorted.subList(0, size/2);
        List<Photon> right = sorted.subList(size/2+1, size);
        Photon median = sorted.get(size/2);
        return new Node(median, balance(left), balance(right), dim.normalise());
    }
    
    public MaxHeap nearestPhotons(int nbPhotons, Vector3 position, float maxDistance) {
        MaxHeap maxHeap = new MaxHeap(nbPhotons);
        nearestPhotons(maxHeap, position, maxDistance, nbPhotons);
        return maxHeap;
    }
    
    public void nearestPhotons(MaxHeap maxHeap, Vector3 position, float maxDistance, int nbPhotons) {
        if (this.isNil()) {
            // NO MODIFICATION
            return;
        }

        Node currentNode = this.asNode();
    
        Vector3 median = currentNode.photon().position();
        Vector3 normal = currentNode.normal();
        
        Vector3 p = position.minus(median);
        if (!currentNode.isLeaf()) {
            float delta = p.dot(normal);
            
            if (delta < 0 || currentNode.right().isNil()) {
                currentNode.left().nearestPhotons(maxHeap, position, maxDistance, nbPhotons);
                if (delta * delta < maxDistance * maxDistance && !currentNode.right().isNil()) {
                    currentNode.right().nearestPhotons(maxHeap, position, maxDistance, nbPhotons);
                }
            } else {
                currentNode.right().nearestPhotons(maxHeap, position, maxDistance, nbPhotons);
                if (delta * delta < maxDistance * maxDistance) {
                    currentNode.left().nearestPhotons(maxHeap, position, maxDistance, nbPhotons);
                }
            }
        }
        
        
        float delta = p.size();

        if (delta * delta < maxDistance * maxDistance) {
            maxHeap.insert(currentNode.photon(), delta);
        }
    }

//    public MaxHeap nearestPhotons(int nbPhotons, Vector3 position, float maxDistance, MaxHeap maxHeap) {
//        if (this.isNil()/* || nbPhotons <= 0*/) {
//            return maxHeap;
//        }
//        
//        Node node = this.asNode();
//        Vector3 nodePos = node.photon().position();
//        
//        Vector3 diff = position.minus(nodePos);
//        float delta = diff.dot(node.normal());
//        
//        if (node.left().isNil() && node.right().isNil()) {
//            if(diff.size() < maxDistance) {
//                maxHeap.insert(node.photon(), diff.size());
//            }
//            return maxHeap;
//        }
//        if (!node.left().isNil() && node.right().isNil()) {
//            
//        }
//        
//        float d = 0;
//        
//        if(maxHeap.inserted() > 0){
//            if (delta <= 0) {
//                maxHeap = node.left().nearestPhotons(nbPhotons, position, maxDistance, maxHeap);
//                d = nodePos.minus(maxHeap.root().getLeft().position()).size();
//                if (Math.pow(delta, 2) < Math.pow(d, 2)) {
//                    maxHeap = node.right().nearestPhotons(/* ??? */ nbPhotons - maxHeap.inserted(), position, d, maxHeap);
//                }
//            } else {
//                maxHeap = node.right().nearestPhotons(nbPhotons, position, maxDistance, maxHeap);
//                d = nodePos.minus(maxHeap.root().getLeft().position()).size();
//                if (Math.pow(delta, 2) < Math.pow(d, 2)) {
//                    maxHeap = node.left().nearestPhotons(/* ??? */ nbPhotons - maxHeap.inserted(), position, d, maxHeap);
//                }
//            }
//            
//            float deltaSquare = delta * delta;
//            d = nodePos.minus(maxHeap.root().getLeft().position()).size();
//            
//            if (deltaSquare < d * d && diff.size() < maxDistance) {
//                maxHeap.insert(node.photon(), diff.size());
//            }
//        }
//        return maxHeap;
//    }
    
}
