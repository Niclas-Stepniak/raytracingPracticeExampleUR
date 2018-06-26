package de.ur.iw.seeRaytracer;

import static java.lang.Math.sqrt;
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

    public Vector3D getMax() {
        return max;
    }

    public Vector3D getMin() {
        return min;
    }

    //Method to get the sidelength of the boundingbox
    public double[] getDimensionsLengths(){
        double[] sides = new double[3];

        Vector3D underMax = new Vector3D(max.getX(),min.getY(),max.getZ());
        Vector3D behindUnderMax = new Vector3D(max.getX(),min.getY(),min.getZ());

        Vector3D xDimension = behindUnderMax.subtract(min);
        Vector3D yDimension = max.subtract(underMax);
        Vector3D zDimension = underMax.subtract(behindUnderMax);

        sides[0]= getLength(xDimension);
        sides[1]= getLength(yDimension);
        sides[2]= getLength(zDimension);

        return sides;
    }

    //method to get the length of the vector
    private double getLength(Vector3D vector){
        double a1 = vector.getX();
        double a2 = vector.getY();
        double a3 = vector.getZ();
        double length = sqrt(a1*a1 + a2*a2 + a3*a3);

        return length;
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

    public AxisAlignedBoundingBox addCameraToBoundingBox(Camera camera){
       Vector3D cameraEye = camera.getEye();
        var minX = this.min.getX();
        var minY = this.min.getY();
        var minZ = this.min.getZ();
        var maxX = this.max.getX();
        var maxY = this.max.getY();
        var maxZ = this.max.getZ();
        minX = Math.min(minX, cameraEye.getX());
        maxX = Math.max(maxX, cameraEye.getX());
        minY = Math.min(minY, cameraEye.getY());
        maxY = Math.max(maxY, cameraEye.getY());
        minZ = Math.min(minZ, cameraEye.getZ());
        maxZ = Math.max(maxZ, cameraEye.getZ());
        System.out.println(cameraEye);
        var min = new Vector3D(minX, minY, minZ);
        var max = new Vector3D(maxX, maxY, maxZ);
        return new AxisAlignedBoundingBox(min, max);
    }
}