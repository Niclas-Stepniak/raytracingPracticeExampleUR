package de.ur.iw.seeRaytracer;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Scene {
    private final Collection<Triangle> triangles = new ArrayList<>();
    /*since we already handle the triangles here we should handle the grid here aswell
     * but i put the initializier for the first build up into the Grid class and used it as a
     * gridBuildingHelp in the Main class*/
    private HashMap<Cube,ArrayList<Triangle>> grid = new HashMap<>();

    /**
     * Adds triangles to this scene.
     */
    public void addAll(Collection<Triangle> triangles) {
        this.triangles.addAll(triangles);
    }

    public void setGrid(HashMap<Cube, ArrayList<Triangle>> grid) {
        this.grid = grid;
        System.out.println("Scene Line 24: Grid Length = "+ grid.size());
    }
    /*Goes through all cubes for every cube it goes through all triangles, if a triangle is touching
    * the cube, it will be safed as a part of the list in the Hashmap under the cube, if a cube has
    * no triangle intersection, its gonna be kicked out.*/

    public void safeTrianglesInGrid(){

        for (Cube cube : grid.keySet()){
            boolean intersectedWithTriangle = false;

            for(Triangle triangle : triangles){
                if(cube.intersectsWithCube(triangle) == true){
                    grid.get(cube).add(triangle);
                    intersectedWithTriangle = true;
                }
            }

            if (intersectedWithTriangle == false){
                grid.remove(cube);
            }
        }
    }


    /**
     * Computes the color of the light that is seen when looking at the scene along the given ray.
     */
    public Color computeLightThatFlowsBackAlongRay(Ray ray) {
        // determine which part of the scene gets hit by the given ray, if any
        SurfaceInformation closestSurface = findFirstIntersection(ray);

        if (closestSurface != null) {
            // return surface color at intersection point
            return closestSurface.computeEmittedLightInGivenDirection(ray.getNormalizedDirection().negate());
        } else {
            // nothing hit -> return transparent
            return new Color(0, 0, 0, 0);
        }
    }

    public Collection<Triangle> getTriangles() {
        return triangles;
    }

    /* We get too many cubes with that
    public double getAverageTriangleSize(){
      double sum = 0;
      double count = 0;

      for (var triangle : triangles) {
        sum += triangle.getTriangleSize();
        count++;
      }

       return ((double) sum / (double) count);
    }
*/
    /**
     * Traces the given ray through the scene until it intersects anything.
     *
     * @param ray describes the exact path through the scene that will be searched.
     * @return information on the surface point where the first intersection of the ray with any scene object occurs - or null for no intersection.
     */
    private SurfaceInformation findFirstIntersection(Ray ray) {
        SurfaceInformation closestIntersection = null;
        double distanceToClosestIntersection = Double.POSITIVE_INFINITY;
        for (var triangle : triangles) {
            var intersection = triangle.intersectWith(ray);
            if (intersection != null) {
                double distanceToSurface = intersection.getPosition().distance(ray.getOrigin());
                if (distanceToSurface < distanceToClosestIntersection) {
                    distanceToClosestIntersection = distanceToSurface;
                    closestIntersection = intersection;
                }
            }
        }
        return closestIntersection;
    }

    private Triangle getVisibleTriangle(Cube cube, Ray ray) {
        ArrayList<Triangle> allTriangles = grid.get(cube);
        HashMap<Triangle, Double> distancesList = new HashMap<>();

        for (Triangle triangle : allTriangles) {
            Vector3D cameraPosition = null; // get camera position
            SurfaceInformation triangleIntersection = triangle.intersectWith(ray);

            double distanceToOrigin = cameraPosition.distance(triangleIntersection.getPosition());
            distancesList.put(triangle, distanceToOrigin);
        }
        Map.Entry<Triangle, Double> min = null;
        for (Map.Entry<Triangle, Double> entry : distancesList.entrySet()) {
            if (min ==  null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }

        return min.getKey();
    }
}
