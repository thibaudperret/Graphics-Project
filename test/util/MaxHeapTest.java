package util;

import static org.junit.Assert.fail;
import math.Vector3;

import org.junit.Test;

import se.graphics.proj.Photon;

public class MaxHeapTest {

    @Test
    public void firstInsertWorks() {
        Photon p1 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f, (short)0);
//        Photon p2 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f, (short)0);
//        Photon p3 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f, (short)0);
//        Photon p4 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f, (short)0);
//        Photon p5 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f, (short)0);
//        Photon p6 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f, (short)0);
        MaxHeap heap = new MaxHeap(7);
        heap.insert(p1, 0.3f);
        assert(heap.inserted() == 1);
        assert(heap.asList().get(0).equals(p1));
    }
    
    @Test
    public void severalInsertWork() {
        Photon p1 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f, (short)0);
        Photon p2 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f, (short)0);
        Photon p3 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f, (short)0);
        Photon p4 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f, (short)0);
        Photon p5 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f, (short)0);
        Photon p6 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f, (short)0);
        MaxHeap heap = new MaxHeap(7);
        heap.insert(p1, 1f);
        heap.insert(p2, 2f);
        heap.insert(p5, 5f);
        heap.insert(p6, 6f);
        heap.insert(p3, 3f);
        heap.insert(p4, 4f);

        System.out.println(heap);
        assert(heap.inserted() == 6);
        

    }
    
}
