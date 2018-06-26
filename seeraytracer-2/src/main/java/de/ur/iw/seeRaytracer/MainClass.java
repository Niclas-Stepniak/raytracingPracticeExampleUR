/* Gruppe: Luca Schumann, Stefanie Madl, Till Emme, Niclas Stepniak
 * Aufgabenaufteilung wie zuvor. 
 * Bei Problemen haben alle Teilnehmer sich eingebracht.
 * 
 * 1. Schnitttest Punkt-Würfel, 2. Schnitttest Strahl-Würfel 7.Funktion die die korrekte Intersection abgibt
 * Till Emme
 *
 * 3. Schnitttest Dreieck-Würfel, 4. Welche Zellen werden von einem gegebenen Dreieck berührt?
 * Stefanie Madl
 *
 * 5. Welche Dreiecke werden im Cube getroffen, 6.Funktion zum Aufbau des Gitters und einspeichern der Dreiecke
 * Niclas Stepniak
 *
 * 7. Funktion die die korrekte Intersection abgibt, 8. Funktion zum Sortieren der Zellen entlang des Strahls
 * Luca Schumann 
 *
 * GitHub repository: https://github.com/Miles7up/raytracingPracticeExampleUR/
 * 
 */
package de.ur.iw.seeRaytracer;

import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainClass {
  private static Grid gridBuildingHelp;
  public static void main(String[] args) throws IOException {

    var bunnyOBJ = ClassLoader.getSystemResourceAsStream("bunny.obj");
    var triangles = DataFileReader.parseTrianglesFromOBJ(bunnyOBJ);

    //int imageWidth = 1920;
    int imageWidth = 320;

    Camera camera = createCameraThatLooksAtBunnyTriangles(triangles);

    var scene = new Scene();
    scene.addCamera(camera);
    scene.addAll(triangles);
    scene.setGrid(gridBuildingHelp.getGrid());
    scene.safeTrianglesInGrid();

    int imageHeight = (int) (imageWidth / camera.getAspectRatio());
    var image = renderImage(scene, camera, imageWidth, imageHeight);
    ImageIO.write(image, "PNG", new File("bunny.png"));
  }

  /**
   * Creates a camera that keeps a set of triangles in view. The camera's distance to the triangles
   * is relative to the size of their bounding box. The camera's orientation is chosen specifically
   * to look good with the data in the file bunny.obj.
   */
  private static Camera createCameraThatLooksAtBunnyTriangles(List<Triangle> triangles) {
    var boundingBox = AxisAlignedBoundingBox.createFrom(triangles);
    var distanceFromCameraToTriangles =
        0.8 * boundingBox.getMaxDiameter(); // somewhat arbitrary value
    var lookAt = boundingBox.getCenter();
    var lookDirection = new Vector3D(0, 0,
        -1); // chosen so that the bunny is viewed from its front side
    var cameraPosition = lookAt.subtract(distanceFromCameraToTriangles, lookDirection);
    var up = new Vector3D(0, 1, 0); // chosen so that the bunny is viewed with its ears upwards

    Camera camera = Camera.buildFromEyeForwardUp(
        cameraPosition, lookDirection, up,
        70,
        16, 9
    );
    boundingBox = boundingBox.addCameraToBoundingBox(camera);
    gridBuildingHelp = new Grid(boundingBox);

    return camera;
  }

  private static BufferedImage renderImage(Scene scene, Camera camera, int imageWidth,
      int imageHeight) {
    var image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

    Cube cameraOriginCube = null;
    var eye = camera.getEye();
    for(Cube cube: scene.getGrid().keySet()){
        if(cube.pointInCube(eye)){
            cameraOriginCube = cube;
            break;
        }
    }
      if (cameraOriginCube == null) {
          throw new Error("Fehler beim Ermitteln der Kameraposition im Grid");
      }

      var cameraRays = camera.createRayIteratorForImage(imageWidth, imageHeight);
      while (cameraRays.hasNext()) {
          CameraRay cameraRay = cameraRays.next();
          var pixelColor = scene.computeLightThatFlowsBackAlongRay(cameraRay, cameraOriginCube);
          image.setRGB(cameraRay.getPixelCoordinateX(), cameraRay.getPixelCoordinateY(),
                  pixelColor.getRGB());

          // print progress to console
          if (cameraRay.getPixelCoordinateX() == 0) {
              System.out
                      .println("Tracing row " + (cameraRay.getPixelCoordinateY() + 1) + " of " + imageHeight);
          }
      }
      return image;
  }
}
