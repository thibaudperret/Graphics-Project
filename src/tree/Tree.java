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

    public Pair<MaxHeap, Float> nearestPhotons(int nbPhotons, Vector3 position, float maxDistance, MaxHeap maxHeap) {
        if (this.isNil() || nbPhotons <= 0) {
            return new Pair<>(maxHeap, maxDistance);
        }
        
        Node node = this.asNode();
        Vector3 nodePos = node.photon().position();
        
        Vector3 diff = position.minus(nodePos);
        float delta = diff.dot(node.normal());
        
        Pair<MaxHeap, Float> pair;
        
        if (delta < 0) {
            pair = node.left().nearestPhotons(nbPhotons, position, maxDistance, maxHeap);
            if (Math.pow(delta, 2) < Math.pow(pair.getRight(), 2)) {
                pair = node.right().nearestPhotons(/* ??? */ nbPhotons - pair.getLeft().inserted(), position, pair.getRight(), pair.getLeft());
            }
        } else {
            pair = node.right().nearestPhotons(nbPhotons, position, maxDistance, maxHeap);
            if (Math.pow(delta, 2) < Math.pow(pair.getRight(), 2)) {
                pair = node.left().nearestPhotons(/* ??? */ nbPhotons - pair.getLeft().inserted(), position, pair.getRight(), pair.getLeft());
            }
        }
        
        float deltaSquare = delta * delta;
        MaxHeap returnHeap = pair.getLeft();
        float d = pair.getRight();
        
        if (deltaSquare < d * d) {
            returnHeap.insert(node.photon());
            d = nodePos.minus(returnHeap.root().getLeft().position());
        }
        
        return new Pair<>(returnHeap, d);
    }
}
