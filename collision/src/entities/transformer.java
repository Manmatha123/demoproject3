package entities;

import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;

public class transformer {
    public ControlModel x_axis ;
    public ControlModel y_axis ;
    public ControlModel z_axis ;
    public  ArrayList<ControlModel> models = new ArrayList<>();

    public transformer(ControlModel x_axis, ControlModel y_axis, ControlModel z_axis){
        this.x_axis = x_axis ;
        models.add(this.x_axis);
        this.y_axis = y_axis ;
        models.add(this.y_axis);
        this.z_axis = z_axis ;
        models.add(this.z_axis);
    }

    public ArrayList<ControlModel> getmodels(){
        return  models ;
    }
//    public List<ControlModel> getmodels() {
//        return Arrays.asList(x_axis, y_axis, z_axis);
//    }

    public ControlModel getX_axis(){
        return x_axis;
    }
    public ControlModel getY_axis(){
        return y_axis;
    }
    public ControlModel getZ_axis(){
        return z_axis;
    }

    public void setposition(Vector3f position){
        x_axis.setPosition(position);
        y_axis.setPosition(position);
        z_axis.setPosition(position);
    }

//    public static void setposition(float position){
//        x_axis.setPosition(position);
//    }

    public void increasePosition(Vector3f delta) {
        x_axis.increasePosition(delta.x, delta.y, delta.z);
        y_axis.increasePosition(delta.x, delta.y, delta.z);
        z_axis.increasePosition(delta.x, delta.y, delta.z);
    }



    public void setRotX(float rotation){
        x_axis.setRotX(rotation);
    }
    public void setRotY(float rotation){
        y_axis.setRotY(rotation);
    }
    public void setRotZ(float rotation){
        z_axis.setRotZ(rotation);
    }

    public float getRotX(){
        return x_axis.getRotX();
    }
    public float getRotY(){
        return y_axis.getRotY();
    }
    public float getRotZ(){
        return z_axis.getRotZ();
    }

    public void setScaleX(float rotation){
        x_axis.setScaleX(rotation);
    }
    public void setScaleY(float rotation){
        y_axis.setRotY(rotation);
    }
    public void setScaleZ(float rotation){
        z_axis.setRotZ(rotation);
    }

    public Vector3f getScale() {
        return x_axis.getScale();
    }

    public Vector3f getPosition(){
        return x_axis.getPosition();
    }
}
