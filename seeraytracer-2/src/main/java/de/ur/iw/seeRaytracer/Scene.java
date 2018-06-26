package de.ur.iw.seeRaytracer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Scene {

    private final Collection<Triangle> triangles = new ArrayList<>();
    /*since we already handle the triangles here we should handle the grid here aswell
     * but i put the initializier for the first build up into the Grid class and used it as a
     * gridBuildingHelp in the Main class*/
    private HashMap<Cube,ArrayList<Triangle>> grid = new HashMap<>();
    private Camera camera;

    /**
     * Adds triangles to this scene.
     */
    public void addAll(Collection<Triangle> triangles) {
        this.triangles.addAll(triangles);
    }

    public void addCamera(Camera camera){
        this.camera = camera;
    }

    public void setGrid(HashMap<Cube, ArrayList<Triangle>> grid) {
        this.grid = grid;
        safeTrianglesInGrid();
    }

    public HashMap<Cube, ArrayList<Triangle>> getGrid(){ return this.grid;}
    /*Goes through all cubes for every cube it goes through all triangles, if a triangle is touching
    * the cube, it will be safed as a part of the list in the Hashmap under the cube, if a cube has
    * no triangle intersection, its gonna be kicked out.*/

    public void safeTrianglesInGrid(){
        ArrayList<Cube> toDelete = new ArrayList<>();
        for (Cube cube : grid.keySet()){
            boolean intersectedWithTriangle = false;

            for(Triangle triangle : triangles){
                if(cube.intersectsWithCube(triangle) == true){
                    grid.get(cube).add(triangle);
                    intersectedWithTriangle = true;
                }
            }
            if ((intersectedWithTriangle == false )&&(cube.pointInCube(camera.getEye())==false)){
                toDelete.add(cube);
            }
        }
        for (Cube cube : toDelete){
            grid.remove(cube);
        }
    }

    public ArrayList<Triangle> getTrianglesOfCubeThatGetHitByTheRayInTheCube(Cube cube,Ray ray){
        ArrayList<Triangle> HitTriangles = new ArrayList<>();
        for (Triangle triangle:grid.get(cube)){
            if(rayHitsATriangleInCube(triangle,cube,ray)==true){
                HitTriangles.add(triangle);
            }
        }
        if(triangles.isEmpty()){
            return null;
        }else{
        return HitTriangles;
        }
    }

    public boolean rayHitsATriangleInCube(Triangle triangle, Cube cube, Ray ray){
      boolean intersection = false;
        SurfaceInformation triangleRayIntersection = triangle.intersectWith(ray);

        if(triangleRayIntersection!=null){
            Vector3D point = triangleRayIntersection.getPosition();
            if (cube.pointInCube(point)==true){
                intersection = true;
            }
        }

      return intersection;
    }

    /**
     * Computes the color of the light that is seen when looking at the scene along the given ray.
     */
    public Color computeLightThatFlowsBackAlongRay(Ray ray, Cube cameraOriginCube) {
        // determine which part of the scene gets hit by the given ray, if any
        SurfaceInformation closestSurface = findFirstIntersection(ray, cameraOriginCube);

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

    /**
     * Traces the given ray through the scene until it intersects anything.
     *
     * @param ray describes the exact path through the scene that will be searched.
     * @return information on the surface point where the first intersection of the ray with any scene object occurs - or null for no intersection.
     */
    private SurfaceInformation findFirstIntersection(Ray ray, Cube cameraOriginCube) {
            SurfaceInformation closestIntersection = null;
            double distanceToClosestIntersection = Double.POSITIVE_INFINITY;
            var currentCube = cameraOriginCube;
            do {
                for (var triangle : this.grid.get(currentCube)) {
                    var intersection = triangle.intersectWith(ray);
                    if (intersection != null) {
                        double distanceToSurface = intersection.getPosition().distance(ray.getOrigin());
                        if (distanceToSurface < distanceToClosestIntersection) {
                            distanceToClosestIntersection = distanceToSurface;
                            closestIntersection = intersection;
                        }
                    }
                }
                if(closestIntersection != null) return closestIntersection;
                else{
                    ArrayList<Cube> neighbours = currentCube.getNeighbourCubes();
                    Cube furtherCube = null;
                    double distanceToFurtherCube = Double.NEGATIVE_INFINITY;
                    for(Cube cube: neighbours){

                        if(ray.intersectWithCube(cube)){
                            double distance = cube.getMin().distance(ray.getOrigin());
                            if ( distance > distanceToFurtherCube){
                                distanceToFurtherCube = distance;
                                furtherCube = cube;
                            }
                        }
                    }
                    if(furtherCube == null || !this.grid.containsKey(furtherCube)) break;
                    currentCube = furtherCube;
                }


            }while(true);
        return null;
    }
}
