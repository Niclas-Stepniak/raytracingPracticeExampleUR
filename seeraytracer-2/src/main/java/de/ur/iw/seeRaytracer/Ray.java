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
        double tmin = (cube.getMin().getX() - this.getOrigin().getX()) / this.getNormalizedDirection().getX();
        double tmax = (cube.getMax().getX() - this.getOrigin().getX()) / this.getNormalizedDirection().getX();
        Vector3D minPointOfNextBox = new Vector3D(cube.getMax().getX(), cube.getMin().getY(), cube.getMin().getZ());


        if (tmin > tmax) {
            double tmp = tmax;
            tmax = tmin;
            tmin = tmp;
        }

        double tymin = (cube.getMin().getY() - this.getOrigin().getY()) / this.getNormalizedDirection().getY();
        double tymax = (cube.getMax().getY() - this.getOrigin().getY()) / this.getNormalizedDirection().getY();

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


        double tzmin = (cube.getMin().getZ() - this.getOrigin().getZ()) / this.getNormalizedDirection().getZ();
        double tzmax = (cube.getMax().getZ() - this.getOrigin().getZ()) / this.getNormalizedDirection().getZ();
        if (tzmin > tzmax){
            double tmp = tzmax;
            tzmax = tzmin;
            tzmin = tmp;
        }

        if ((tmin > tzmax) || (tzmin > tmax))
            return null;

        if (tzmin > tmin)
            tmin = tzmin;

        if (tzmax < tmax) {
            tmax = tzmax;
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
