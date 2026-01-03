package entities;

import models.RawModel;
import models.TextureModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import textures.ModelTexture;

public class ControlModel extends Entity{

    public boolean selected = false ;

    public ControlModel(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale){
        super(model,position, rotX,rotY,rotZ,new Vector3f(scale.getX(),scale.getY(),scale.getZ()));
    }
    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotation(float rotX, float rotY, float rotZ) {
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
    }

    public void setScale(Vector3f scale) {
        this.scale = new Vector3f(scale);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public Vector3f getScale() {
        return scale;
    }

    public RawModel getRawModel() {
        return model.getRawModel();
    }

    public ModelTexture getTexture() {
        return model.getTexture();
    }

//    public String getName() {
//        return name;
//    }

    public TextureModel getTextureModel() {
        return model;
    }



    public void move(){

        if(selected){
            if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
                Vector3f currentPosition = getPosition();
                setPosition(new Vector3f(currentPosition.x+0.5f, currentPosition.y, currentPosition.z));
            }else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                Vector3f currentPosition = getPosition();
                setPosition(new Vector3f(currentPosition.x-0.5f, currentPosition.y, currentPosition.z));
            }else if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
                Vector3f currentPosition = getPosition();
                setPosition(new Vector3f(currentPosition.x, currentPosition.y, currentPosition.z-0.5f));
            }else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                Vector3f currentPosition = getPosition();
                setPosition(new Vector3f(currentPosition.x, currentPosition.y, currentPosition.z+0.5f));
            }else if(Keyboard.isKeyDown(Keyboard.KEY_O)){
                Vector3f currentPosition = getPosition();
                setPosition(new Vector3f(currentPosition.x, currentPosition.y+0.5f, currentPosition.z));
            }else if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
                Vector3f currentPosition = getPosition();
                setPosition(new Vector3f(currentPosition.x, currentPosition.y-0.5f, currentPosition.z));
            }
        }
    }
}
