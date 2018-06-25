package de.ur.iw.seeRaytracer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import static java.lang.Math.max;
import static java.lang.Math.min;

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

    public boolean intersectWithCube(Cube cube){
        double tmin = Double.NEGATIVE_INFINITY, tmax= Double.POSITIVE_INFINITY;

        for (int i = 0; i < 3; ++i) {
            if (this.normalizedDirection.toArray()[i] != 0.0) {
                double t1 = (cube.getMin().toArray()[i] - this.origin.toArray()[i])/this.normalizedDirection.toArray()[i];
                double t2 = (cube.getMax().toArray()[i] - this.origin.toArray()[i])/this.normalizedDirection.toArray()[i];

                tmin = max(tmin, min(t1, t2));
                tmax = min(tmax, max(t1, t2));
            } else if ( this.origin.toArray()[i] <= cube.getMin().toArray()[i] ||  this.origin.toArray()[i] >= cube.getMax().toArray()[i]) {
                return false;
            }
        }

        return tmax > tmin && tmax > 0.0;    }


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
