package entities;
import Collision.AABB;
import Collision.Face;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
    public TextureModel model;
    public Vector3f position;
    public float rotX, rotY, rotZ;
    public Vector3f scale;
    private int textureIndex = 0;
    public int objectType = 0 ;
    public Vector3f offset  ;
    public Vector3f angleOffSet;

    private AABB aabb;
    private Vector3f size;

    public void setModel(TextureModel model) {
        this.model = model;
    }

    public void setAabb(AABB aabb) {
        this.aabb = aabb;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Vector3f getSize() {
        return size;
    }

    public void setSize(Vector3f size) {
        this.size = size;
    }

    public Entity(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = new Vector3f(scale);
        this.model.getRawModel().boundingbox = new AABB(this.model.getRawModel(), position, scale,true);

        Vector3f min=model.getRawModel().getMin();
        Vector3f max=model.getRawModel().getMax();

        Vector3f objSize=new Vector3f(
                (max.x-min.x),
                (max.y-min.y),
                (max.z-min.z)
        );

        objSize.x*=0.006f;
        objSize.z*=0.006f;

        float minY=min.y * scale.y;

        Vector3f centerPos=new Vector3f(position.x,
                position.y + minY + objSize.y/2f,
                position.z);
        this.size=objSize;
        this.aabb=new AABB(centerPos, objSize);
        for(Face newf : this.model.getRawModel().f){
            newf.boundingBox = new AABB(newf,position,scale);
        }

        this.model.getRawModel().rearranging_faces(this.model.getRawModel().f);
        System.out.println("trying to add into total data  from entity file");
//        this.model.getRawModel().rearranging_faces();
        System.out.println("----------------------resorted faces list ----------------------------------");
    }


    public AABB getWorldAABB() {
        AABB local = model.getRawModel().boundingbox;

        Vector3f min = new Vector3f(
                local.min.x * scale.x + position.x,
                local.min.y * scale.y + position.y,
                local.min.z * scale.z + position.z
        );

        Vector3f max = new Vector3f(
                local.max.x * scale.x + position.x,
                local.max.y * scale.y + position.y,
                local.max.z * scale.z + position.z
        );

        return new AABB(min, max);
    }



    public Entity(TextureModel model, Vector3f position, Vector3f offset, float rotX, float rotY, float rotZ, Vector3f scale) {
        this.model = model;
        this.position = position;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = new Vector3f(scale);
        this.objectType = 1 ;
        this.offset = offset ;
        System.out.println("trying to add into total data  from entity file");
        this.model.getRawModel().rearranging_faces();

        for(Face newf : this.model.getRawModel().f){
            newf.boundingBox = new AABB(newf,position,scale);
        }

        Vector3f min=model.getRawModel().getMin();
        Vector3f max=model.getRawModel().getMax();

        Vector3f objSize=new Vector3f(
                (max.x-min.x)*scale.x,
                (max.y-min.y)*scale.y,
                (max.z-min.z)*scale.z
        );

        objSize.x*=0.07f;
        objSize.z*=0.07f;

        float minY=min.y * scale.y;

        Vector3f centerPos=new Vector3f(position.x,
                position.y + minY + objSize.y/2f,
                position.z);
        this.aabb=new AABB(centerPos, objSize);
        this.size=objSize;

        this.model.getRawModel().rearranging_faces(this.model.getRawModel().f);
        this.model.getRawModel().boundingbox = new AABB(this.model.getRawModel(), position, scale,true);
    }

//    public Entity(TextureModel model, Vector3f position, Vector3f offset,Vector3f angleOffSet , float rotX, float rotY, float rotZ, Vector3f scale) {
//        this.model = model;
//        this.position = position;
//        this.rotX = rotX;
//        this.rotY = rotY;
//        this.rotZ = rotZ;
//        this.scale = new Vector3f(scale);
//        this.objectType = 2 ;
//        this.offset = offset ;
//        this.angleOffSet = angleOffSet ;
//        System.out.println("trying to add into total data  from entity file");
//
//        this.model.getRawModel().rearranging_faces();
//        System.out.println("----------------------resorted faces list ----------------------------------");
////        this.model.getRawModel().showFaces();
//        this.model.getRawModel().boundingbox = new AABB(this.model.getRawModel(), position, scale,true);
//    }

    public float getTextureXoffSet(){
        int column = textureIndex % model.getTexture().getNumberofrows();
        return (float) column / (float) model.getTexture().getNumberofrows();
    }

    public float getTextureYoffSet(){
        int row = textureIndex / model.getTexture().getNumberofrows();
        return (float) row / (float) model.getTexture().getNumberofrows();
    }

    public void increasePosition(float dx, float dy, float dz){
        this.position.x+=dx;
        this.position.y+=dy;
        this.position.z+=dz;
        this.aabb.update(position, this.size);
    }


    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
    }

    public TextureModel getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public Vector3f getScale() {
        return scale;
    }

    public float getScaleX() {
        return scale.x;
    }

    public float getScaleY() {
        return scale.y;
    }

    public float getScaleZ() {
        return scale.z;
    }

    public void setScaleX(float x) {
        this.scale.x = x;
    }

    public void setScaleY(float y) {
        this.scale.y = y;
    }

    public void setScaleZ(float z) {
        this.scale.z = z;
    }

    public  void setOffset(Vector3f offset){
        this.offset = new Vector3f(offset);
    }


    public void changeOffSet(double x, double y, double z){
        offset.x = (float) (offset.x + x);
        offset.y = (float) (offset.y + y);
        offset.z = (float) (offset.z + z);
    }

    public AABB getAabb(){
        return this.aabb;
    }


}
