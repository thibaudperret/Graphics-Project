package tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import math.Vector3;
import se.graphics.proj.Photon;

public class TreeTest {

    public void test() {
        /*Vector3 pos1 = new Vector3(2, -2, 0);
        Vector3 pos2 = new Vector3(13, 4, 0);
        Vector3 pos3 = new Vector3(9, 4, 0);
        Vector3 pos4 = new Vector3(4, -1, 0);
        Vector3 pos5 = new Vector3(5, 2, 0);
        Vector3 pos6 = new Vector3(6, 0, 0);
        Vector3 pos7 = new Vector3(8, -2, 0);
        Vector3 pos8 = new Vector3(10, 0, 0);
        Vector3 pos9 = new Vector3(9, 2, 0);
        Vector3 pos10 = new Vector3(4, 1, 0);

        Photon p1 = new Photon(pos1, null, 0, 0);
        Photon p2 = new Photon(pos2, null, 0, 0);
        Photon p3 = new Photon(pos3, null, 0, 0);
        Photon p4 = new Photon(pos4, null, 0, 0);
        Photon p5 = new Photon(pos5, null, 0, 0);
        Photon p6 = new Photon(pos6, null, 0, 0);
        Photon p7 = new Photon(pos7, null, 0, 0);
        Photon p8 = new Photon(pos8, null, 0, 0);
        Photon p9 = new Photon(pos9, null, 0, 0);
        Photon p10 = new Photon(pos10, null, 0, 0);
        
        List<Photon> photons = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);*/
        List<Photon> photons = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            Vector3 pos = new Vector3((float) Math.random() * 100, (float) Math.random() * 100, (float) Math.random() * 100);
            photons.add(new Photon(pos, pos, 0f, 0f));
        }
        Tree t = Tree.balance(photons);

        System.out.println(t);
    }
    
    @Test
    public void nearestPhotonsTest() {
        Vector3 pos1 = new Vector3(2, -2, 0);
        Vector3 pos2 = new Vector3(13, 4, 0);
        Vector3 pos3 = new Vector3(9, 4, 0);
        Vector3 pos4 = new Vector3(4, -1, 0);
        Vector3 pos5 = new Vector3(5, 2, 0);
        Vector3 pos6 = new Vector3(6, 0, 0);
        Vector3 pos7 = new Vector3(8, -2, 0);
        Vector3 pos8 = new Vector3(10, 0, 0);
        Vector3 pos9 = new Vector3(9, 2, 0);
        Vector3 pos10 = new Vector3(4, 1, 0);

        Photon p1 = new Photon(pos1, null, 0, 0);
        Photon p2 = new Photon(pos2, null, 0, 0);
        Photon p3 = new Photon(pos3, null, 0, 0);
        Photon p4 = new Photon(pos4, null, 0, 0);
        Photon p5 = new Photon(pos5, null, 0, 0);
        Photon p6 = new Photon(pos6, null, 0, 0);
        Photon p7 = new Photon(pos7, null, 0, 0);
        Photon p8 = new Photon(pos8, null, 0, 0);
        Photon p9 = new Photon(pos9, null, 0, 0);
        Photon p10 = new Photon(pos10, null, 0, 0);
        
        List<Photon> photons = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10);
        Tree t = Tree.balance(photons);
        System.out.println(t);
        t.nearestPhotons(4, new Vector3(5, 1, 0), 1.2f).asList().forEach(p -> System.out.println(p.getLeft().position()));
    }

}
