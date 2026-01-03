package Collision;

import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Face
{
    public List<Vector3f> vertices = new ArrayList<>();
    public AABB boundingBox;

    public  Vector3f position;
    public float scale;

    public int index;


    public void addface(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3)
    {
        this.vertices.add(vertex1);
        this.vertices.add(vertex2);
        this.vertices.add(vertex3);

    }

    public void addface(Vector3f vertex1, Vector3f vertex2, Vector3f vertex3,int ind)
    {
        this.vertices.add(vertex1);
        this.vertices.add(vertex2);
        this.vertices.add(vertex3);
        this.index = ind;

    }

    public void setUpAABB(Vector3f scale)
    {
        this.boundingBox = new AABB(this, scale);
    }

    public void setUpAABB(Vector3f pos, Vector3f scale)
    {
        this.boundingBox = new AABB(this, pos, scale);
    }
}
