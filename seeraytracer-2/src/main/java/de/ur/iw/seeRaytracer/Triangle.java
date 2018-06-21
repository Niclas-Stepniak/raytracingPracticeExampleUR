package de.ur.iw.seeRaytracer;

import static java.lang.Math.sqrt;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.google.common.collect.UnmodifiableIterator;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Triangle implements Iterable<Vector3D> {
    private final Vector3D[] vertices;

    public Triangle(Vector3D... vertices) {
        Preconditions.checkArgument(vertices.length == 3);
        this.vertices = vertices;
    }

    public Vector3D getVertex(int index) {
        return vertices[index];
    }

    public SurfaceInformation intersectWith(Ray ray) {
        // the following code stems from
        // https://www.scratchapixel.com/lessons/3d-basic-rendering/ray-tracing-rendering-a-triangle

        Vector3D v0v1 = vertices[1].subtract(vertices[0]);
        Vector3D v0v2 = vertices[2].subtract(vertices[0]);
        Vector3D pvec = ray.getNormalizedDirection().crossProduct(v0v2);
        double det = v0v1.dotProduct(pvec);

        // ray and triangle are parallel if det is close to 0
        final double EPSILON = 1e-8;
        if (Math.abs(det) < EPSILON) return null;

        double invDet = 1 / det;

        Vector3D tvec = ray.getOrigin().subtract(vertices[0]);
        double u = tvec.dotProduct(pvec) * invDet;
        if (u < 0 || u > 1) return null;

        Vector3D qvec = tvec.crossProduct(v0v1);
        double v = ray.getNormalizedDirection().dotProduct(qvec) * invDet;
        if (v < 0 || u + v > 1) return null;

        double t = v0v2.dotProduct(qvec) * invDet;

        // explicitly compute the actual surface properties at the intersection point
        Vector3D position = ray.computePointFromDistanceToOrigin(t);
        Vector3D normal = v0v1.crossProduct(v0v2).normalize();
        // this code detects an intersection from both sides
        // -> ensure that the surface normal points against the impact direction
        if (normal.dotProduct(ray.getNormalizedDirection()) > 0) {
            normal = normal.negate();
        }
        return new SurfaceInformation(position, normal);
    }

    public double getAverageTriangleSize(){
      /*nach Satz des Heron*/
      Vector3D A = vertices[0];
      Vector3D B = vertices[1];
      Vector3D C = vertices[2];

      Vector3D a = C.subtract(B);
      Vector3D b = A.subtract(C);
      Vector3D c = B.subtract(A);

      double s = (getLength(a)+getLength(b)+getLength(c))/2;

      double F = sqrt(s*(s-getLength(a))*(s-getLength(b))*(s-getLength(c)));
      return F;
    }

    private double getLength(Vector3D vector){
      double a1 = vector.getX();
      double a2 = vector.getY();
      double a3 = vector.getZ();
      double length = sqrt(a1*a1 + a2*a2 + a3*a3);

      return length;
    }

    @Override
    public UnmodifiableIterator<Vector3D> iterator() {
        return Iterators.forArray(vertices);
    }
}
