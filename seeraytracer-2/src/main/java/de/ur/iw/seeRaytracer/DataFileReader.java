package de.ur.iw.seeRaytracer;

import com.mokiat.data.front.parser.*;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DataFileReader {
    public static List<Triangle> parseTrianglesFromOBJ(InputStream fileData) throws IOException {
        List<Triangle> triangles = new ArrayList<>();

        final IOBJParser parser = new OBJParser();
        final OBJModel model = parser.parse(fileData);

        for (OBJObject object : model.getObjects()) {
            for (OBJMesh mesh : object.getMeshes()) {
                for (OBJFace face : mesh.getFaces()) {
                    var references = face.getReferences();
                    if (references.size() == 3) {
                        var objVertex0 = model.getVertex(references.get(0));
                        var objVertex1 = model.getVertex(references.get(1));
                        var objVertex2 = model.getVertex(references.get(2));
                        var triangle = new Triangle(
                                new Vector3D(objVertex0.x, objVertex0.y, objVertex0.z),
                                new Vector3D(objVertex1.x, objVertex1.y, objVertex1.z),
                                new Vector3D(objVertex2.x, objVertex2.y, objVertex2.z)
                        );
                        triangles.add(triangle);

                    } else {
                        throw new IllegalArgumentException("Can only read faces with exactly 3 vertices, but encountered " + references.size());
                    }
                }
            }

        }
        return triangles;
    }
}
