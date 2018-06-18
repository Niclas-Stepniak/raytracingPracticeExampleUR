package de.ur.iw.seeRaytracer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.awt.*;

/**
 * Holds information on a specific point of a specific surface.
 */
public class SurfaceInformation {
    private final Vector3D position;
    private final Vector3D normal;

    public SurfaceInformation(Vector3D position, Vector3D normal) {
        this.position = position;
        this.normal = normal;
    }

    public Vector3D getPosition() {
        return position;
    }

    public Vector3D getNormal() {
        return normal;
    }

    public Color computeEmittedLightInGivenDirection(Vector3D direction) {
        var cosineOfAngleToNormal = direction.dotProduct(getNormal());
        return new Color((float) cosineOfAngleToNormal, (float) cosineOfAngleToNormal, (float) cosineOfAngleToNormal);
    }
}
