package de.ur.iw.seeRaytracer;

import java.util.HashMap;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainClass {
  private static Grid gridBuildingHelp;
  private static HashMap<Cube, Triangle> hashGrid;

  public static void main(String[] args) throws IOException {
    hashGrid = new HashMap<>();
    var bunnyOBJ = ClassLoader.getSystemResourceAsStream("bunny.obj");
    var triangles = DataFileReader.parseTrianglesFromOBJ(bunnyOBJ);

    //int imageWidth = 1920;
    int imageWidth = 320;

    Camera camera = createCameraThatLooksAtBunnyTriangles(triangles);

    var scene = new Scene();
    scene.addAll(triangles);

    int imageHeight = (int) (imageWidth / camera.getAspectRatio());
    var image = renderImage(scene, camera, imageWidth, imageHeight);
    ImageIO.write(image, "PNG", new File("bunny.png"));
  }

  /**
   * Creates a camera that keeps a set of triangles in view. The camera's distance to the triangles is relative to
   * the size of their bounding box. The camera's orientation is chosen specifically to look good with the data in the
   * file bunny.obj.
   */
  private static Camera createCameraThatLooksAtBunnyTriangles(List<Triangle> triangles) {
    var boundingBox = AxisAlignedBoundingBox.createFrom(triangles);
    gridBuildingHelp = new Grid(boundingBox);
    var distanceFromCameraToTriangles = 0.8 * boundingBox.getMaxDiameter(); // somewhat arbitrary value
    var lookAt = boundingBox.getCenter();
    var lookDirection = new Vector3D(0, 0, -1); // chosen so that the bunny is viewed from its front side
    var cameraPosition = lookAt.subtract(distanceFromCameraToTriangles, lookDirection);
    var up = new Vector3D(0, 1, 0); // chosen so that the bunny is viewed with its ears upwards

    return Camera.buildFromEyeForwardUp(
        cameraPosition, lookDirection, up,
        70,
        16, 9
    );
  }

  private static void insertIntoHashGrid(Triangle triangle) {
    boolean intersection = false;
    var grid = gridBuildingHelp.getGrid();
    for (Cube cube : grid) {
      intersection = triangle.intersectsWithCube(cube);
      if(intersection) {
        hashGrid.put(cube, triangle);
      }
    }
  }

  private static BufferedImage renderImage(Scene scene, Camera camera, int imageWidth, int imageHeight) {
    var image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
    var cameraRays = camera.createRayIteratorForImage(imageWidth, imageHeight);
    cameraRays.forEachRemaining((cameraRay) -> {
      var pixelColor = scene.computeLightThatFlowsBackAlongRay(cameraRay);
      image.setRGB(cameraRay.getPixelCoordinateX(), cameraRay.getPixelCoordinateY(), pixelColor.getRGB());

      // print progress to console
      if (cameraRay.getPixelCoordinateX() == 0) {
        System.out.println("Tracing row " + (cameraRay.getPixelCoordinateY() + 1) + " of " + imageHeight);
      }
    });
    return image;
  }
}