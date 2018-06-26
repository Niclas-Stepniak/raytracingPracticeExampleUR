package de.ur.iw.seeRaytracer;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


class CubeTest {


    @Test
    void pointInCube() {
        Cube cube = new Cube(new Vector3D(0,0,0), new Vector3D(1, 1,1 ));
        Vector3D pointInCube = new Vector3D(0.5,0.5,0.5);
        Vector3D pointOutsideCube = new Vector3D(6,3,6);

        assertTrue(cube.pointInCube(pointInCube));
        assertFalse(cube.pointInCube(pointOutsideCube));
    }

    @Test
    void intersectsWithCube() {
        Cube cube = new Cube(new Vector3D(0,0,0), new Vector3D(1, 1,1 ));
        Vector3D[] vertices1 = { new Vector3D(3.0, 4.0, 0.5), new Vector3D(0.0, 0.0, 0.0),
            new Vector3D(2.0, 2.4, 5.6)};

        Vector3D[] vertices2 = { new Vector3D(3.0, 4.0, 8.0), new Vector3D(10, 5, 3),
            new Vector3D(2.0, 2.4, 5.6)};

        Vector3D[] vertices3 = { new Vector3D(-0.5, 0, 1.5), new Vector3D(1.5, 1.5, 0.5),
            new Vector3D(1.5, 0, 1)};

        Triangle t1 = new Triangle(vertices1);
        Triangle t2 = new Triangle(vertices2);
        Triangle t3 = new Triangle(vertices3);

        assertTrue(cube.intersectsWithCube(t1));
        assertFalse(cube.intersectsWithCube(t2));
        assertTrue(cube.intersectsWithCube(t3));
    }

    @Test
    void getNeighbourCubes() {
        Cube start = new Cube(new Vector3D(0,0,0), new Vector3D(1,1,1));
        ArrayList<Cube> neighbourCubes = new ArrayList<>();
        neighbourCubes.add(new Cube(new Vector3D(-1, 0,0), new Vector3D(0, 1 ,1)));
        neighbourCubes.add(new Cube(new Vector3D(1, 0,0), new Vector3D(2, 1 ,1)));
        neighbourCubes.add(new Cube(new Vector3D(0, -1,0), new Vector3D(1, 0 ,1)));
        neighbourCubes.add(new Cube(new Vector3D(0, 1,0), new Vector3D(1, 2 ,1)));
        neighbourCubes.add(new Cube(new Vector3D(0, 0,-1), new Vector3D(1, 1 ,0)));
        neighbourCubes.add(new Cube(new Vector3D(0, 0,1), new Vector3D(1, 1 ,2)));
        assertEquals(neighbourCubes, start.getNeighbourCubes());

    }
}