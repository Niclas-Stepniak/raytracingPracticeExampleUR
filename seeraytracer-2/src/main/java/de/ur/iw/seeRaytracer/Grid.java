package de.ur.iw.seeRaytracer;

import java.util.Iterator;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import java.util.ArrayList;

public class Grid implements Iterable<Vector3D>{
  private final ArrayList<Cube> grid = new ArrayList<>();

  public Grid(double sideLength, AxisAlignedBoundingBox boundingbox) {

    Vector3D boxMin = boundingbox.getMin();

    Vector3D curCubeMin;
    Vector3D curCubeMax;

    double xLength = boundingbox.getDimensionsLengths()[0];
    double yLength = boundingbox.getDimensionsLengths()[1];
    double zLength = boundingbox.getDimensionsLengths()[2];

    xLength /=sideLength;
    yLength /=sideLength;
    zLength /=sideLength;

    for(int x= 0;x < xLength;x++){
      for(int y = 0;y < yLength;y++){
        for (int z = 0; z < zLength;z++){
          curCubeMin = new Vector3D((boxMin.getX()+(x*sideLength)),(boxMin.getY()+(y*sideLength)),
              (boxMin.getZ()+(z*sideLength)));
          curCubeMax = new Vector3D((curCubeMin.getX()+sideLength),(curCubeMin.getY()+sideLength),
              (curCubeMin.getZ()+sideLength));
          grid.add(new Cube(curCubeMin,curCubeMax));
        }
      }
    }
  }

  public ArrayList<Cube> getGrid() {
    return grid;
  }

  @Override
  public Iterator<Vector3D> iterator() {
    return null;
  }
}