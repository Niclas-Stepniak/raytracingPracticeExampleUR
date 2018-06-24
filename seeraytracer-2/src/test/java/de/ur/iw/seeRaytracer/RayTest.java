package de.ur.iw.seeRaytracer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    @Test
    void intersectWithCube() {
        Cube cube = new Cube(new Vector3D(0,0,0), new Vector3D(1, 1,1 ));
        Vector3D nextMinPointX = new Vector3D(1,0,0);
        Vector3D nextMinPointY = new Vector3D(0,1,0);
        Vector3D nextMinPointZ = new Vector3D(0,0,1);
        Ray rayX = new Ray(new Vector3D(0.5, 0.5, 0.5), new Vector3D(1, 0,0 ));
        Ray rayY = new Ray(new Vector3D(0.5, 0.5, 0.5), new Vector3D(0, 1,0 ));
        Ray rayZ = new Ray(new Vector3D(0.5, 0.5, 0.5), new Vector3D(0, 0,1 ));
        
        assertEquals(nextMinPointX, rayX.intersectWithCube(cube));
        assertEquals(nextMinPointY, rayY.intersectWithCube(cube));
        assertEquals(nextMinPointZ, rayZ.intersectWithCube(cube));
    }
}