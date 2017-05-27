package util;

import java.util.ArrayList;
import java.util.List;

import se.graphics.proj.Photon;

public class MaxHeap

{
    private Float[] heapDistances;
    private Photon[] heapPhoton;
    private int size;
    private int maxsize;
 
    private static final int FRONT = 1;
 
    public MaxHeap(int maxsize)
    {
        this.maxsize = maxsize;
        this.size = 0;
        heapDistances = new Float[this.maxsize + 1];
        heapDistances[0] = Float.MAX_VALUE;

        heapPhoton = new Photon[this.maxsize + 1];
        heapPhoton[0] = null;
    }
 
    private int parent(int pos)
    {
        return pos / 2;
    }
 
    private int leftChild(int pos)
    {
        return (2 * pos);
    }
 
    private int rightChild(int pos)
    {
        return (2 * pos) + 1;
    }
 
    private boolean isLeaf(int pos)
    {
        if (pos >=  (size / 2)  &&  pos <= size)
        {
            return true;
        }
        return false;
    }
 
    private void swap(int fpos,int spos)
    {
        float tmpDist;
        Photon tmpPhot;
        tmpDist = heapDistances[fpos];
        tmpPhot = heapPhoton[fpos];
        heapDistances[fpos] = heapDistances[spos];
        heapPhoton[fpos] = heapPhoton[spos];

        heapDistances[spos] = tmpDist;
        heapPhoton[spos] = tmpPhot;
    }
 
    private void maxHeapify(int pos)
    {
        if (!isLeaf(pos))
        { 
            if ( heapDistances[pos] < heapDistances[leftChild(pos)]  || heapDistances[pos] < heapDistances[rightChild(pos)])
            {
                if (heapDistances[leftChild(pos)] > heapDistances[rightChild(pos)])
                {
                    swap(pos, leftChild(pos));
                    maxHeapify(leftChild(pos));
                }else
                {
                    swap(pos, rightChild(pos));
                    maxHeapify(rightChild(pos));
                }
            }
        }
    }
 
    public void insert(Photon elementPhot, float elementDist)
    {
        if(size == maxsize) {
            if(elementDist < heapDistances[FRONT]) {
                heapDistances[FRONT] = elementDist;
                heapPhoton[FRONT] = elementPhot;
                maxHeapify(FRONT);
            }
        } else {
            int current = ++size;

            heapDistances[current] = elementDist;
            heapPhoton[current] = elementPhot;
     
            while(parent(current) >= FRONT && heapDistances[current] > heapDistances[parent(current)])
            {
                swap(current,parent(current));
                current = parent(current);
            }   
        }        
    }
 
   
 
    public void maxHeap()
    {
        for (int pos = (size / 2); pos >= 1; pos--)
        {
            maxHeapify(pos);
        }
    }
    
    public int inserted() {
        return size;
    }
    
    public List<Pair<Photon,Float>> asList() {
        List<Pair<Photon,Float>> result = new ArrayList<>();
        for(int i = FRONT; i <= size; ++i) {
            result.add(new Pair<Photon,Float>(heapPhoton[i], heapDistances[i]));
        }
        return result;
    }
    
    @Override public String toString() {
        return printTree(1);
    }
    
    private String printTree(int i) {
        boolean hasFirstChild = (2*i <= maxsize && size >= 2*i);
        boolean hasSecondChild = (2*i+1 <= maxsize && size >= 2*i+1);         
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