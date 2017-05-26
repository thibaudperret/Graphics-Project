package util;

import math.Vector3;
import se.graphics.proj.Photon;
import util.Pair;


public class MaxHeap {

    private Photon[] heapPhotons;
    private Float[] heapDistances;
    int lastIndex;
    int maxCapacity;
    Vector3 distancePoint;
    
    public MaxHeap(int maxCapacity, Vector3 distancePoint) {
        heapPhotons = new Photon[maxCapacity];
        heapDistances = new Float[maxCapacity];
        lastIndex = 1;
        this.distancePoint = distancePoint; 
        this.maxCapacity = maxCapacity;
    }
    
    public void percoateUp(){
        int pos = lastIndex;
        while(pos > 1 && heapDistances[pos/2] > heapDistances[pos]){
            float distTemp = heapDistances[pos];
            Photon photonTemp = heapPhotons[pos];
            heapDistances[pos] = heapDistances[pos/2];
            heapPhotons[pos] = heapPhotons[pos/2];
            heapDistances[pos/2] = distTemp;
            heapPhotons[pos/2] = photonTemp;
            pos = pos/2;
        }
    }
    
    public void insert(Photon p) {
        float dist = p.position().minus(distancePoint);
    }
    
}
