package de.ur.iw.seeRaytracer;

import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
// neue Klasse statt Unterklasse von AABB besser?


public class Cube {
    private Vector3D min;
    private Vector3D max;
    private ArrayList<Vector3D> vertices;

    public Cube(Vector3D min, Vector3D max) {
        this.min = min;
        this.max = max;
        vertices = new ArrayList<>();
        setVertices();
    }

    public Vector3D getMin() {
        return min;
    }

    public Vector3D getMax() {
        return max;
    }

    private void setVertices() {
        /* Ecken, die jeweils eine Würfelfläche bilden
         * v1, v3, v4, v7
         * v1, v3, v5, v6
         * v2, v7, v4, v0,
         * v2, v6, v5, v0
         * v2, v6, v1, v7
         * v5, v3, v4, v1
         * */
        double edgeLength = (min.distance(max)) * Math.sqrt(1 / 3);
        Vector3D v0 = min;
        Vector3D v1 = max;
        Vector3D v2 = new Vector3D(min.getX(), min.getY() + edgeLength, min.getZ());
        Vector3D v3 = new Vector3D(max.getX(), max.getY() - edgeLength, max.getZ());
        Vector3D v4 = new Vector3D(max.getX() - edgeLength, max.getY() - edgeLength, max.getZ());
        Vector3D v5 = new Vector3D(min.getX() + edgeLength, min.getY(), min.getZ());
        Vector3D v6 = new Vector3D(min.getX() + edgeLength, min.getY() + edgeLength, min.getZ());
        Vector3D v7 = new Vector3D(max.getX() - edgeLength, max.getY(), max.getZ());

        vertices.add(v0);
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);
        vertices.add(v4);
        vertices.add(v5);
        vertices.add(v6);
        vertices.add(v7);
    }

    public boolean pointInCube(Vector3D point) {
        return (point.getX() >= min.getX() && point.getX() < max.getX()
                && point.getY() >= min.getY() && point.getY() < max.getY()
                && point.getZ() >= min.getZ() && point.getZ() < max.getZ());
    }

    public boolean intersectsWithCube(Triangle triangle) {
        // test if cube intersects with vertex first, if not, test intersection with edges of triangle
        boolean intersection;
        Vector3D v0 = triangle.getVertex(0);
        Vector3D v1 = triangle.getVertex(1);
        Vector3D v2 = triangle.getVertex(2);

        intersection = pointInCube(v0) || pointInCube(v1) || pointInCube(v2);

/*        if (!intersection) {
            Vector3D e0 = v0.subtract(v1);
            Vector3D e1 = v0.subtract(v2);
            Vector3D e2 = v1.subtract(v2);

            var e0Ray = new Ray(v1, e0.normalize());
            var e1Ray = new Ray(v2, e1.normalize());
            var e2Ray = new Ray(v2, e2.normalize());

            intersection = e0Ray.intersectWithCube(this) != false || e1Ray.intersectWithCube(this) != false
                || e2Ray.intersectWithCube(this) != false;
        }*/
        return intersection;
    }

    public ArrayList<Vector3D> getVertices() {
        return vertices;
    }

    @Override
    public int hashCode() {
        return min.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cube)) return false;
        Cube other = (Cube) obj;
        if (this.getMin().getX() != other.getMin().getX()) return false;
        if (this.getMin().getY() != other.getMin().getY()) return false;
        if (this.getMin().getZ() != other.getMin().getZ()) return false;
        if (this.getMax().getX() != other.getMax().getX()) return false;
        if (this.getMax().getY() != other.getMax().getY()) return false;
        if (this.getMax().getZ() != other.getMax().getZ()) return false;
        return true;
    }

    public ArrayList<Cube> getNeighbourCubes(){
        ArrayList<Cube> neighbourCubes = new ArrayList<>();
        double[] min = this.min.toArray();
        double[] max = this.max.toArray();
        for(int i = 0; i < 3; i++){
            double[] minLeft = new double[3];
            double[] maxLeft = new double[3];
            double[] minRight = new double[3];
            double[] maxRight = new double[3];
            for( int j = 0; j < 3; j++){
                if( i == j){
                    minLeft[j] = min[j] - (max[j] - min[j]);
                    maxLeft[j] = min[j];
                    minRight[j] = max[j];
                    maxRight[j] = max[j] + (max[j] - min[j]);
                }
                else{
                    minLeft[j] = min[j];
                    maxLeft[j] = max[j];
                    minRight[j] = min[j];
                    maxRight[j] = max[j];
                }
            }
            neighbourCubes.add(new Cube(new Vector3D(minLeft[0], minLeft[1], minLeft[2]), new Vector3D(maxLeft[0], maxLeft[1], maxLeft[2])));
            neighbourCubes.add(new Cube(new Vector3D(minRight[0], minRight[1], minRight[2]), new Vector3D(maxRight[0], maxRight[1], maxRight[2])));
        }
        return neighbourCubes;
    }



}
