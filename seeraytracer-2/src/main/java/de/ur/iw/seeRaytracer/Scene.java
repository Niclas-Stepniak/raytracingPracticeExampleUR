package de.ur.iw.seeRaytracer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class Scene {
    private final Collection<Triangle> triangles = new ArrayList<>();

    /**
     * Adds triangles to this scene.
     */
    public void addAll(Collection<Triangle> triangles) {
        this.triangles.addAll(triangles);
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

    public double getAverageTriangleSize(){
      var sum;
      int count;

      for (var triangle : triangles) {
        sum += triangle.getTriangleSize();
        count++;
      }

       return sum / (double)count;
    }

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
}
