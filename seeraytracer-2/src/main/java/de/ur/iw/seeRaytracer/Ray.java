package de.ur.iw.seeRaytracer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Ray {
    private final Vector3D origin;
    private final Vector3D normalizedDirection;

    public Ray(Vector3D origin, Vector3D normalizedDirection) {
        this.origin = origin;
        this.normalizedDirection = normalizedDirection;
    }

    public Vector3D getOrigin() {
        return origin;
    }

    public Vector3D getNormalizedDirection() {
        return normalizedDirection;
    }

    public Vector3D computePointFromDistanceToOrigin(double distance) {
        return origin.add(distance, normalizedDirection);
    }

    @Override
    public String toString() {
        return "de.ur.iw.seeRaytracer.Ray{" +
                "origin=" + origin +
                ", normalizedDirection=" + normalizedDirection +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return com.google.common.base.Objects.equal(origin, ray.origin) &&
                com.google.common.base.Objects.equal(normalizedDirection, ray.normalizedDirection);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(origin, normalizedDirection);
    }
}
