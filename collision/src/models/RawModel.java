package models;

import Collision.AABB;
import Collision.BVHNode;
import Collision.Face;
import entities.Triangle;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RawModel {

    private int VaoID;
    private int VboID;
    public AABB boundingbox;
   private Vector3f min;
   private Vector3f max;

   private List<Triangle> triangles;


    public AABB getBoundingbox() {
        return boundingbox;
    }

    public void setBoundingbox(AABB boundingbox) {
        this.boundingbox = boundingbox;
    }

    public List<Face> f = new ArrayList<>();
    private int vertexCount;

    public List<Vector3f> vertex;

    public String filename ;
    public BVHNode node ;


    private FloatBuffer matrix44Buffer;




    public RawModel(int VaoID, int vertexCount) {
        this.VaoID = VaoID;
        this.vertexCount = vertexCount;
    }

    public RawModel(int VaoID, int vertexCount, List<Vector3f> vertex) {
        this(VaoID, vertexCount);
        this.vertex = vertex;
    }

    public RawModel(int VaoID, int vertexCount, List<Vector3f> vertex, List<Face> face) {
        this(VaoID, vertexCount, vertex);
        this.f = face;
    }

    public Vector3f getMax() {
        return max;
    }

    public void setMax(Vector3f max) {
        this.max = max;
    }

    public Vector3f getMin() {
        return min;
    }

    public void setMin(Vector3f min) {
        this.min = min;
    }

    public RawModel(int VaoID, int vertexCount, List<Vector3f> vertex, List<Face> face, String filename, Vector3f min, Vector3f max,List<Triangle> triangles) {
        this(VaoID, vertexCount, vertex);
        this.f.clear();
        this.f.addAll(face);
        this.filename = filename;
        Thread cthread = new Thread( () ->{
            this.node = new BVHNode(face, filename);
        });
        this.min=min;
        this.max=max;
        this.triangles=triangles;
    }



    public void rearranging_faces(List<Face> faces) {
        // Validate faces before sorting
        for (Face face : faces) {
            if (face == null || face.boundingBox == null || Float.isNaN(face.boundingBox.min.x)) {
                throw new IllegalArgumentException("Invalid Face object: " + face);
            }
        }
        Collections.sort(faces, new Comparator<Face>() {
            @Override
            public int compare(Face f1, Face f2) {
                return Float.compare(f1.boundingBox.min.x, f2.boundingBox.min.x);
            }
        });

        this.f = new ArrayList<>(faces);
    }
    public void rearranging_faces() {
        // Validate faces before sorting
        for (Face face : this.f) {
            if (face == null || face.boundingBox == null || Float.isNaN(face.boundingBox.min.x)) {
                throw new IllegalArgumentException("Invalid Face object: " + face);
            }
        }
        Collections.sort(this.f, new Comparator<Face>() {
            @Override
            public int compare(Face f1, Face f2) {
                return Float.compare(f1.boundingBox.min.x, f2.boundingBox.min.x);
            }
        });

//        this.f = new ArrayList<>(this.f);
    }

    public void showfaces() {
        for (Face cface : f) {
            cface.boundingBox.show();
        }
    }

    public int getVaoID() {
        return VaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }



}
