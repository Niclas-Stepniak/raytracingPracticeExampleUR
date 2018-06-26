package de.ur.iw.seeRaytracer;

import com.google.common.base.Preconditions;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Camera {
    private final Vector3D eye;
    private final Vector3D normalizedForward;
    private final Vector3D orthoNormalUp;
    private final double horizontalFOVRadians;
    private final double verticalFOVRadians;

    private Camera(Vector3D eye, Vector3D normalizedForward, Vector3D orthoNormalUp, double horizontalFOVRadians, double verticalFOVRadians) {
        this.eye = eye;
        this.normalizedForward = normalizedForward;
        this.orthoNormalUp = orthoNormalUp;
        this.horizontalFOVRadians = horizontalFOVRadians;
        this.verticalFOVRadians = verticalFOVRadians;
    }

    public static Camera buildFromEyeForwardUp(Vector3D eye, Vector3D forward, Vector3D up, int horizontalFOVDegrees, int aspectRatioWidth, int aspectRatioHeight) {
        var normalizedForward = forward.normalize();
        var right = forward.crossProduct(up); // right-hand-side coordinate system
        var orthoNormalUp = right.crossProduct(forward).normalize();

        // convert horizontal FOV to vertical FOV:
        // https://en.wikipedia.org/wiki/Field_of_view_in_video_games
        var aspectRatio = (double) aspectRatioWidth / (double) aspectRatioHeight;
        var horizontalFOVRadians = horizontalFOVDegrees * Math.PI / 180.0;
        var verticalFOVRadians = 2 * Math.atan(Math.tan(horizontalFOVRadians / 2) / aspectRatio);
        return new Camera(eye, normalizedForward, orthoNormalUp, horizontalFOVRadians, verticalFOVRadians);
    }

    public Vector3D getEye() {
        return eye;
    }

    public Vector3D getNormalizedForward() {
        return normalizedForward;
    }

    public Vector3D getOrthoNormalUp() {
        return orthoNormalUp;
    }

    public double getAspectRatio() {
        // computing aspect ratio from FOVs:
        // https://en.wikipedia.org/wiki/Field_of_view_in_video_games
        return Math.tan(horizontalFOVRadians / 2) / Math.tan(verticalFOVRadians / 2);
    }

    public Iterator<CameraRay> createRayIteratorForImage(int imageWidth, int imageHeight) {
        return new CameraRayIterator(imageWidth, imageHeight);
    }

    private class CameraRayIterator implements Iterator<CameraRay> {
        private final int horizontalResolution;
        private final int verticalResolution;
        private final Vector3D pointInFrontOfEye;
        private final Vector3D maxHorizontalShift;
        private final Vector3D maxVerticalShift;
        private int horizontalPixelIndex = 0;
        private int verticalPixelIndex = 0;

        public CameraRayIterator(int horizontalResolution, int verticalResolution) {
            Preconditions.checkArgument(horizontalResolution > 0);
            Preconditions.checkArgument(verticalResolution > 0);
            this.pointInFrontOfEye = eye.add(normalizedForward);
            this.horizontalResolution = horizontalResolution;
            this.verticalResolution = verticalResolution;
            var orthoNormalRight = normalizedForward.crossProduct(orthoNormalUp);
            this.maxHorizontalShift = orthoNormalRight.scalarMultiply(Math.tan(horizontalFOVRadians / 2));
            this.maxVerticalShift = orthoNormalUp.scalarMultiply(-Math.tan(verticalFOVRadians / 2));
        }

        @Override
        public boolean hasNext() {
            return verticalPixelIndex < verticalResolution;
        }

        @Override
        public CameraRay next() {
            if (hasNext()) {
                double fractionX = ((horizontalPixelIndex + 0.5) / horizontalResolution);
                double fractionY = ((verticalPixelIndex + 0.5) / verticalResolution);
                var target = pointInFrontOfEye.add(fractionX * 2 - 1, maxHorizontalShift).add(fractionY * 2 - 1, maxVerticalShift);
                var nextRay = new CameraRay(eye, target.subtract(eye).normalize(), horizontalPixelIndex, verticalPixelIndex);

                horizontalPixelIndex++;
                if (horizontalPixelIndex == horizontalResolution) {
                    horizontalPixelIndex = 0;
                    verticalPixelIndex++;
                }

                return nextRay;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
