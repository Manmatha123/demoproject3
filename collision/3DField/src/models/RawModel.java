package models;

import org.lwjgl.util.vector.Vector3f;

public class RawModel {
    private int vaoID;
    private int vertexCount;

    private Vector3f min;
    private Vector3f max;
    

    public Vector3f getMin() {
        return min;
    }

    public Vector3f getMax() {
        return max;
    }

    public RawModel(int vaoID,int vertexCount, Vector3f min, Vector3f max){
        this.vaoID=vaoID;
        this.vertexCount=vertexCount;
        this.min=min;
        this.max=max;
    }
       public RawModel(int vaoID,int vertexCount){
        this.vaoID=vaoID;
        this.vertexCount=vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}