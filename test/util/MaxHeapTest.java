package util;

import static org.junit.Assert.*;
import math.Vector3;

import org.junit.Test;

import se.graphics.proj.Photon;

public class MaxHeapTest {

    @Test
    public void firstInsert() {
        Photon p1 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f);
        MaxHeap heap = new MaxHeap(7);
        heap.insert(p1, 0.3f);
        assertTrue(heap.inserted() == 1);
        assertTrue(heap.asList().get(0).getLeft().equals(p1));
    }
    
    @Test
    public void severalInsert() {
        Photon p1 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f);
        Photon p2 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f);
        Photon p3 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f);
        Photon p4 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f);
        Photon p5 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f);
        Photon p6 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f);
        MaxHeap heap = new MaxHeap(7);
        heap.insert(p1, 1f);
        System.out.println(heap + "size = " + heap.inserted());

        heap.insert(p2, 2f);
        System.out.println(heap + "size = " + heap.inserted());

        heap.insert(p3, 3f);
        System.out.println(heap + "size = " + heap.inserted());

        heap.insert(p4, 4f);
        System.out.println(heap + "size = " + heap.inserted());

        heap.insert(p5, 5f);
        System.out.println(heap + "size = " + heap.inserted());

        heap.insert(p6, 6f);
        System.out.println(heap + "size = " + heap.inserted());


        assertTrue(heap.inserted() == 6);        

    }
    
    @Test
    public void insertionOnFullHeap() {
        Photon p1 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f);
        Photon p2 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f);
        Photon p3 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f);
        Photon p4 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f);
        Photon p5 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f);
        Photon p6 = new Photon(new Vector3(1f, 1f, 1f), new Vector3(1f, 1f, 1f), 2f, 5f);
        MaxHeap heap = new MaxHeap(5);
        heap.insert(p1, 1f);
        System.out.println(heap + " size = " + heap.inserted());

        heap.insert(p2, 2f);
        System.out.println(heap + " size = " + heap.inserted());

        heap.insert(p6, 6f);
        System.out.println(heap + "size = " + heap.inserted());

        
        heap.insert(p3, 3f);
        System.out.println(heap + "size = " + heap.inserted());

        heap.insert(p4, 4f);
        System.out.println(heap + "size = " + heap.inserted());

        heap.insert(p5, 5f);
        System.out.println(heap + "size = " + heap.inserted());

        

        assertTrue(heap.inserted() == 5);        
    }
    
}
