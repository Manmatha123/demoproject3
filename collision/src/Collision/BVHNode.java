package Collision;

import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class BVHNode {
    public AABB aabb;
    public BVHNode left;
    public BVHNode right;
    public List<Face> faces;
    public  String filename ;

    public BVHNode(List<Face> faces, String filename) {
        this.faces = faces;
        this.aabb = calculateAABB(faces);
        this.filename = filename;

        if (faces.size() > 1) {
//            System.out.println("total faces  = "+faces.size());
            splitNode();
        }
    }

    private AABB calculateAABB(List<Face> faces) {
        Vector3f min = new Vector3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        Vector3f max = new Vector3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

        for (Face face : faces) {
            updateBounds(min, max, face.vertices.get(0));
            updateBounds(min, max, face.vertices.get(1));
            updateBounds(min, max, face.vertices.get(2));
        }

        return new AABB(min, max);
    }

    private void updateBounds(Vector3f min, Vector3f max, Vector3f point) {
        min.x = Math.min(min.x, point.x);
        min.y = Math.min(min.y, point.y);
        min.z = Math.min(min.z, point.z);
        max.x = Math.max(max.x, point.x);
        max.y = Math.max(max.y, point.y);
        max.z = Math.max(max.z, point.z);
    }

    private void splitNode() {

//        System.out.println("Splitting the node");

        Vector3f size = new Vector3f();
        Vector3f.sub(aabb.max, aabb.min, size);

        int axis = 0;
        if (size.y > size.x) axis = 1;
        if (size.z > size.y && size.z > size.x) axis = 2;

        int finalAxis = axis;
        faces.sort((f1, f2) -> {
            float center1 = getCenter(f1, finalAxis);
            float center2 = getCenter(f2, finalAxis);
            return Float.compare(center1, center2);
        });

        int mid = faces.size() / 2;
        List<Face> leftFaces = new ArrayList<>(faces.subList(0, mid));
        List<Face> rightFaces = new ArrayList<>(faces.subList(mid, faces.size()));

        this.faces = null;

//        System.out.println("Making the left node");

        left = new BVHNode(leftFaces, this.filename);

//        System.out.println("Making the Rigth node");

        right = new BVHNode(rightFaces, this.filename);
    }

    private float getCenter(Face face, int axis) {
        float center = 0;
        if (axis == 0) {
            center = (face.vertices.get(0).x + face.vertices.get(1).x + face.vertices.get(2).x) / 3.0f;
        } else if (axis == 1) {
            center = (face.vertices.get(0).y + face.vertices.get(1).y + face.vertices.get(2).y) / 3.0f;
        } else if (axis == 2) {
            center = (face.vertices.get(0).z + face.vertices.get(1).z + face.vertices.get(2).z) / 3.0f;
        }
        return center;
    }
}
