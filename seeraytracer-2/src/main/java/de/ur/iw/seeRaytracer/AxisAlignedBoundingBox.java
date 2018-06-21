package de.ur.iw.seeRaytracer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * Represents a cuboid in 3D space whose sides are axis-aligned.
 */
public class AxisAlignedBoundingBox {
    private final Vector3D min;
    private final Vector3D max;

    private AxisAlignedBoundingBox(Vector3D min, Vector3D max) {
        this.min = min;
        this.max = max;
    }

    public static AxisAlignedBoundingBox createFrom(Iterable<Triangle> triangles) {
        var minX = Double.POSITIVE_INFINITY;
        var minY = Double.POSITIVE_INFINITY;
        var minZ = Double.POSITIVE_INFINITY;
        var maxX = Double.NEGATIVE_INFINITY;
        var maxY = Double.NEGATIVE_INFINITY;
        var maxZ = Double.NEGATIVE_INFINITY;

        for (Triangle triangle : triangles) {
            for (Vector3D vertex : triangle) {
                minX = Math.min(minX, vertex.getX());
                maxX = Math.max(maxX, vertex.getX());
                minY = Math.min(minY, vertex.getY());
                maxY = Math.max(maxY, vertex.getY());
                minZ = Math.min(minZ, vertex.getZ());
                maxZ = Math.max(maxZ, vertex.getZ());
            }
        }

        var min = new Vector3D(minX, minY, minZ);
        var max = new Vector3D(maxX, maxY, maxZ);

        return new AxisAlignedBoundingBox(min, max);
    }

    public Vector3D getCenter() {
        return min.add(max).scalarMultiply(0.5);
    }

    public double getMaxDiameter() {
        return min.distance(max);
    }
}
