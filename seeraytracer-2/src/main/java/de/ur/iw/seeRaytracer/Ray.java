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

    public Vector3D intersectWithCube(Cube cube){
        Vector3D invertedDirection = new Vector3D(1 / this.normalizedDirection.getX(),
                1 / this.normalizedDirection.getY(),
                1/ this.normalizedDirection.getZ());
        double tmin = (cube.getMin().getX() - this.origin.getX()) * invertedDirection.getX();
        double tmax = (cube.getMax().getX() - this.origin.getX()) * invertedDirection.getX();
        Vector3D minPointOfNextBox = new Vector3D(cube.getMax().getX(), cube.getMin().getY(), cube.getMin().getZ());

        if (tmin > tmax) {
            double tmp = tmax;
            tmax = tmin;
            tmin = tmp;
        }

        double tymin = (cube.getMin().getY() - this.origin.getY()) * invertedDirection.getY();
        double tymax = (cube.getMax().getY() - this.origin.getY()) * invertedDirection.getY();

        if (tymin > tymax){
            double tmp = tymax;
            tymax = tymin;
            tymin = tmp;
        }

        if ((tmin > tymax) || (tymin > tmax))
            return null;

        if (tymin > tmin)
            tmin = tymin;

        if (tymax < tmax) {
            tmax = tymax;
            minPointOfNextBox =  new Vector3D(cube.getMin().getX(), cube.getMax().getY(), cube.getMin().getZ());
        }

        double tzmin = (cube.getMin().getZ() - this.origin.getZ()) * invertedDirection.getZ();
        double tzmax = (cube.getMax().getZ() - this.origin.getZ()) * invertedDirection.getZ();
        if (tzmin > tzmax){
            double tmp = tzmax;
            tzmax = tzmin;
            tzmin = tmp;
        }

        if ((tmin > tzmax) || (tzmin > tmax))
            return null;

        if (tzmax < tmax) {
            minPointOfNextBox =  new Vector3D(cube.getMin().getX(), cube.getMin().getY(), cube.getMax().getZ());
        }

        return minPointOfNextBox;
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
