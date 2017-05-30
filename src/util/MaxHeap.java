package util;

import java.util.ArrayList;
import java.util.List;
import se.graphics.proj.Photon;

/**
 * A classic max heap class, in this project we use it to find the n closest neighbors of a point.
 * Our MaxHeap contains an array fill with the n (arg size) closest photons and another array fill with
 * the corresponding distance. The first element of each array is empty because it was easier for us to start
 * at index 1 (we can access children of index i at index 2i and 2i+1).
 * 
 */
public class MaxHeap {
    private Float[] heapDistances;
    private Photon[] heapPhoton;
    private int size;
    private int maxsize;
    private static final int FRONT = 1;

    public MaxHeap(int maxsize) {
        this.maxsize = maxsize;
        this.size = 0;
        heapDistances = new Float[this.maxsize + 1];
        heapDistances[0] = Float.MAX_VALUE;

        heapPhoton = new Photon[this.maxsize + 1];
        heapPhoton[0] = null;
    }

    private int parent(int pos) {
        return pos / 2;
    }

    private int leftChild(int pos) {
        return (2 * pos);
    }

    private int rightChild(int pos) {
        return (2 * pos) + 1;
    }
    
    /**
     * This function checks if a node at a given index is a leaf (if this node has at least one child).
     * 
     * @param pos
     * The index of the position.
     * @return
     * True if the node at index pos is a leaf.
     */
    private boolean isLeaf(int pos) {
        if (pos > (size / 2) && pos <= size) {
            return true;
        }
        return false;
    }

    /**
     * This function is use to swap two element in both array.
     * 
     * @param fpos 
     * The first index to swap.
     * @param spos 
     * The second index to swap.
     */
    private void swap(int fpos, int spos) {
        float tmpDist;
        Photon tmpPhot;
        tmpDist = heapDistances[fpos];
        tmpPhot = heapPhoton[fpos];
        heapDistances[fpos] = heapDistances[spos];
        heapPhoton[fpos] = heapPhoton[spos];

        heapDistances[spos] = tmpDist;
        heapPhoton[spos] = tmpPhot;
    }

    /**
     * The function maxHeapify verify that a node is greater than all its children. If it is not the case, the function
     * swap the node with its greater children and call maxHeapify on it.
     * 
     * @param pos 
     * The index we want to verify the maxHeap property.
     */
    private void maxHeapify(int pos) {
        if (!isLeaf(pos)) {
            //Check if the left child or the right child is greater.
            if (heapDistances[pos] < heapDistances[leftChild(pos)] || heapDistances[pos] < heapDistances[rightChild(pos)]) {
                //If so, check which one is the greatest. Then swap with it and call maxHeapify on it.
                if (heapDistances[leftChild(pos)] > heapDistances[rightChild(pos)]) {
                    swap(pos, leftChild(pos));
                    maxHeapify(leftChild(pos));
                } else {
                    swap(pos, rightChild(pos));
                    maxHeapify(rightChild(pos));
                }
            }
        }
    }

    /**
     * This function insert a photon and its distance if the photon is smaller enough to belong to the heap.
     * 
     * @param elementPhot
     * The photon to insert.
     * @param elementDist
     * The distance to insert.
     */
    public void insert(Photon elementPhot, float elementDist) {
        if (size == maxsize) {
            //If the heap is full, check if the element need to be inserted.
            if (elementDist < heapDistances[FRONT]) {
                heapDistances[FRONT] = elementDist;
                heapPhoton[FRONT] = elementPhot;
                maxHeapify(FRONT);
            }
        } else {
            //Otherwise increment the size and insert it at the bottom
            int current = ++size;
            heapDistances[current] = elementDist;
            heapPhoton[current] = elementPhot;
            //Then reorder the heap so that it keeps the maxHeap properties.
            while (parent(current) >= FRONT && heapDistances[current] > heapDistances[parent(current)]) {
                swap(current, parent(current));
                current = parent(current);
            }
        }
    }

    /**
     * The function maxHeap() calls the function maxHeapify() on each parent. After a call to this function, 
     * the heap satisfy all the maxHeap properties.
     */
    public void maxHeap() {
        for (int pos = (size / 2); pos >= 1; pos--) {
            maxHeapify(pos);
        }
    }

    public int inserted() {
        return size;
    }
    
    public Pair<Photon, Float> root() {
        return new Pair<>(heapPhoton[FRONT], heapDistances[FRONT]);
    }
    
    public Photon rootPhoton() {
        return heapPhoton[FRONT];
    }
    
    public float rootDistance() {
        return heapDistances[FRONT];
    }

    public List<Pair<Photon, Float>> asList() {
        List<Pair<Photon, Float>> result = new ArrayList<>();
        for (int i = FRONT; i <= size; ++i) {
            result.add(new Pair<Photon, Float>(heapPhoton[i], heapDistances[i]));
        }
        return result;
    }

    @Override
    public String toString() {
        return printTree(1);
    }

    private String printTree(int i) {
        boolean hasFirstChild = (2 * i <= maxsize && size >= 2 * i);
        boolean hasSecondChild = (2 * i + 1 <= maxsize && size >= 2 * i + 1);
        String node;
        if (hasFirstChild && hasSecondChild) {
            node = (heapDistances[i] + " --> ((" + printTree(i * 2) + ") || (" + printTree(2 * i + 1) + "))");
        } else if (hasFirstChild) {
            node = (heapDistances[i] + " --> ((" + printTree(i * 2) + ") || ())");
        } else if (hasSecondChild) {
            node = (heapDistances[i] + " --> ((),(" + printTree(i * 2 + 1) + "))");
        } else {
            node = heapDistances[i].toString();
        }
        return node;
    }
}