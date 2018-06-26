package de.ur.iw.seeRaytracer;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Grid implements Iterable<Vector3D>{
  private HashMap<Cube,ArrayList<Triangle>> grid = new HashMap<>();

  //Just because
  private final int sideLengthFactor = 42;

  public Grid(AxisAlignedBoundingBox boundingbox) {

    Vector3D boxMin = boundingbox.getMin();


    Vector3D curCubeMin = null;
    Vector3D curCubeMax = null;

    double xLength = boundingbox.getDimensionsLengths()[0];
    double yLength = boundingbox.getDimensionsLengths()[1];
    double zLength = boundingbox.getDimensionsLengths()[2];

    double sideLength = xLength/sideLengthFactor;
    System.out.println((int)sideLength);

    xLength /= sideLength;
    yLength /= sideLength;
    zLength /= sideLength;

    //making the length increase by one to defintly get all points in

    for(int x= 0;x < xLength+1;x++){
      for(int y = 0;y < yLength+1;y++){
        for (int z = 0; z < zLength+1;z++){
          curCubeMin = new Vector3D((boxMin.getX()+(x*sideLength)),(boxMin.getY()+(y*sideLength)),
              (boxMin.getZ()+(z*sideLength)));
          curCubeMax = new Vector3D((curCubeMin.getX()+sideLength),(curCubeMin.getY()+sideLength),
              (curCubeMin.getZ()+sideLength));
          grid.put(new Cube(curCubeMin,curCubeMax),new ArrayList<Triangle>());
        }
      }
    }

  }

  public HashMap<Cube,ArrayList<Triangle> > getGrid() {
    return grid;
  }

  @Override
  public Iterator<Vector3D> iterator() {
    return null;
  }
}
