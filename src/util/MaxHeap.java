package util;

import java.util.ArrayList;
import java.util.List;

import math.Vector3;
import se.graphics.proj.Photon;


public class MaxHeap {

    private Photon[] heapPhotons;
    private Float[] heapDistances;
    private int insertIn;
    private int maxCapacity;
    
    public MaxHeap(int maxCapacity) {
        heapPhotons = new Photon[maxCapacity + 1];
        heapDistances = new Float[maxCapacity + 1];
        insertIn = 1; 
        this.maxCapacity = maxCapacity;
    }
    
    //returns 1 for left child, 2 for second child
    private int greaterChild(int i) {
        
        boolean hasFirstChild = (2*i <= maxCapacity && insertIn > 2*i);
        boolean hasSecondChild = (2*i+1 <= maxCapacity && insertIn > 2*i+1); 
        
        if(hasFirstChild && hasSecondChild) {
            if(heapDistances[2*i] > heapDistances[2*i+1]){
                return 1;
            } else {
                return 2;
            }
        } else if(hasFirstChild) {
            return 1;
        } else if(hasSecondChild) {
            return 2;
        } else {
            return -1;
        }
    }
    
    private void percolateDown(int k){         
        
        int pos = k;
        boolean hasFirstChild = (2*pos <= maxCapacity && insertIn > 2*pos);
        boolean hasSecondChild = (2*pos+1 <= maxCapacity && insertIn > 2*pos+1); 
        boolean firstChildGreater = hasFirstChild && heapDistances[2*pos] > heapDistances[pos];
        boolean secondChildGreater = hasSecondChild && heapDistances[2*pos+1] > heapDistances[pos];
        
        while ((hasFirstChild && firstChildGreater) || (hasSecondChild && secondChildGreater)){
            float distTemp = heapDistances[pos];
            Photon photonTemp = heapPhotons[pos];
            
            if(hasFirstChild && hasSecondChild) {
                int greaterChild = greaterChild(pos);
                if(greaterChild == 1) {
                    heapDistances[pos] = heapDistances[2*pos];
                    heapPhotons[pos] = heapPhotons[2*pos];
                    heapDistances[2*pos] = distTemp;
                    heapPhotons[2*pos] = photonTemp;
                    pos = 2*pos;
                    hasFirstChild = (2*pos <= maxCapacity && insertIn > 2*pos);
                    hasSecondChild = (2*pos+1 <= maxCapacity && insertIn > 2*pos+1); 
                    firstChildGreater = hasFirstChild && heapDistances[2*pos] > heapDistances[pos];
                    secondChildGreater = hasSecondChild && heapDistances[2*pos+1] > heapDistances[pos];

                } else {
                    heapDistances[pos] = heapDistances[2*pos+1];
                    heapPhotons[pos] = heapPhotons[2*pos+1];
                    heapDistances[2*pos+1] = distTemp;
                    heapPhotons[2*pos+1] = photonTemp;
                    pos = 2*pos+1;
                    hasFirstChild = (2*pos <= maxCapacity && insertIn > 2*pos);
                    hasSecondChild = (2*pos+1 <= maxCapacity && insertIn > 2*pos+1); 
                    firstChildGreater = hasFirstChild && heapDistances[2*pos] > heapDistances[pos];
                    secondChildGreater = hasSecondChild && heapDistances[2*pos+1] > heapDistances[pos];
                }
            } else if(hasFirstChild) {
                heapDistances[pos] = heapDistances[2*pos];
                heapPhotons[pos] = heapPhotons[2*pos];
                heapDistances[2*pos] = distTemp;
                heapPhotons[2*pos] = photonTemp;
                pos = 2*pos;
                hasFirstChild = (2*pos <= maxCapacity && insertIn > 2*pos);
                hasSecondChild = (2*pos+1 <= maxCapacity && insertIn > 2*pos+1); 
                firstChildGreater = hasFirstChild && heapDistances[2*pos] > heapDistances[pos];
                secondChildGreater = hasSecondChild && heapDistances[2*pos+1] > heapDistances[pos];
            } else {
                heapDistances[pos] = heapDistances[2*pos+1];
                heapPhotons[pos] = heapPhotons[2*pos+1];
                heapDistances[2*pos+1] = distTemp;
                heapPhotons[2*pos+1] = photonTemp;
                pos = 2*pos+1;
                hasFirstChild = (2*pos <= maxCapacity && insertIn > 2*pos);
                hasSecondChild = (2*pos+1 <= maxCapacity && insertIn > 2*pos+1); 
                firstChildGreater = hasFirstChild && heapDistances[2*pos] > heapDistances[pos];
                secondChildGreater = hasSecondChild && heapDistances[2*pos+1] > heapDistances[pos];
            }           
        }
    }
    
   
    public void insert(Photon p, float distance) {
       
        if(insertIn == 1) {
            heapDistances[1] = distance;
            heapPhotons[1] = p;
            insertIn++;
        } else {
            if(distance < heapDistances[1] && isFull()) {
                heapDistances[1] = distance;
                heapPhotons[1] = p;
                percolateDown(1);
            }else if(!isFull()) {
                heapDistances[insertIn] = distance;
                heapPhotons[insertIn] = p;
                for(int i = (insertIn)/2; i >=1; i/=2) {
                    percolateDown(i);
                }
                insertIn++;
            }
        }        
    }
    
    private float parent(int i)     {
        return heapDistances[(i - 1) / 2]; 
    }
    
    private float leftChild(int i)  {
        return heapDistances[2 * i];
    }
    
    private float rightChild(int i) {
        return heapDistances[2 * i + 1]; 
    }
    
    public Pair<Photon, Float> root(){
        return new Pair<>(heapPhotons[1], heapDistances[1]);
    }
    
    public int inserted() {
        return insertIn - 1;
    }
    
    public boolean isFull() {
        return inserted() == maxCapacity;
    }
    
    public List<Pair<Float,Photon>> asList() {
        List<Pair<Float,Photon>> result = new ArrayList<>();
        for(int i = 1; i < insertIn; ++i) {
            result.add(new Pair<Float, Photon>(heapDistances[i], heapPhotons[i]));
        }
        return result;
    }
    
    @Override public String toString() {
        return printTree(1);
    }
    
    private String printTree(int i) {
        boolean hasFirstChild = (2*i <= maxCapacity && insertIn > 2*i);
        boolean hasSecondChild = (2*i+1 <= maxCapacity && insertIn > 2*i+1);         
        String node;
        if(hasFirstChild && hasSecondChild) {
            node = (heapDistances[i] + " --> ((" + printTree(i*2) +") || (" + printTree(2*i + 1)+"))");
        } else if(hasFirstChild) {
            node = (heapDistances[i] + " --> ((" + printTree(i*2) +") || ())");
        } else if(hasSecondChild) {
            node = (heapDistances[i] + " --> ((),(" + printTree(i*2+1) +"))");
        } else {
            node = heapDistances[i].toString();
        }        
        return node;
    }
    
    
}
