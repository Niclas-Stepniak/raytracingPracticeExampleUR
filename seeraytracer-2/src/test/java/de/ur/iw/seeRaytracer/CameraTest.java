package de.ur.iw.seeRaytracer;

import com.google.common.collect.Lists;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CameraTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void buildFromEyeForwardUp() {
        var camera = Camera.buildFromEyeForwardUp(
                new Vector3D(1, 2, 3), new Vector3D(0, 0, 4), new Vector3D(0, 5, 0),
                90,
                1, 1);
        assertEquals(new Vector3D(1, 2, 3), camera.getEye());
        assertEquals(new Vector3D(0, 0, 1), camera.getNormalizedForward());
        assertEquals(new Vector3D(0, 1, 0), camera.getOrthoNormalUp());
    }

    @org.junit.jupiter.api.Test
    void generatePixelRays() {
        var camera = Camera.buildFromEyeForwardUp(
                new Vector3D(1, 2, 3), new Vector3D(0, 0, 4), new Vector3D(0, -5, 0),
                90,
                1, 1);

        var pixelRays = Lists.newArrayList(camera.createRayIteratorForImage(2, 2));
        assertEquals(4, pixelRays.size());
        assertTrue(pixelRays.contains(new CameraRay(camera.getEye(), new Vector3D(-0.5, -0.5, 1).normalize(), 0, 0)));
        assertTrue(pixelRays.contains(new CameraRay(camera.getEye(), new Vector3D(+0.5, -0.5, 1).normalize(), 1, 0)));
        assertTrue(pixelRays.contains(new CameraRay(camera.getEye(), new Vector3D(-0.5, +0.5, 1).normalize(), 0, 1)));
        assertTrue(pixelRays.contains(new CameraRay(camera.getEye(), new Vector3D(+0.5, +0.5, 1).normalize(), 1, 1)));
    }
}