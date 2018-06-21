package de.ur.iw.seeRaytracer;

import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import java.util.ArrayList;

public class Grid implements Iterable<Vector3D>{
  private final Collection<Cube> grid = new ArrayList<>();

  public Grid(double size, AxisAlignedBoundingBox boundingbox) {

    Vector3D boxMin = boundingbox.getMin();
    Vector3D boxMax = boundingbox.getMax();

    Vector3D curCubeMin;
    Vector3D curCubeMax;

    Vector3D getMaxOfCurCube = new Vector3D(size, size, size);
    Vector3D getNextMinInXdir = new Vector3D(size, 0, 0);
    Vector3D getNextMinInYdir = new Vector3D(0, size, 0);
    Vector3D getNextMinInZdir = new Vector3D(0, 0, size);

    double xLength = boundingbox.getDimensionsLengths()[0];
    double yLength = boundingbox.getDimensionsLengths()[1];
    double zLength = boundingbox.getDimensionsLengths()[2];

    Cube currentCube = null;

    if (currentCube == null) {
      curCubeMin = boxMin;
      curCubeMax = curCubeMin.add(getMaxOfCurCube);
      currentCube = new Cube(curCubeMin, curCubeMax);
      grid.add(currentCube);
    }

  }
  
  @Override
  public Iterator<Vector3D> iterator() {
    return null;
  }
}