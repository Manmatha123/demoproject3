package entities;

import models.TextureModel;
import org.lwjgl.util.vector.Vector3f;


public class wing_tips extends ControlObject_Test {

    private ControlObject_Test aircraft;
    private Vector3f position = new Vector3f(150f,60f,-452.95f);
    private boolean deflectAileron = false;


    public wing_tips(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale, ControlObject_Test aircraft){
        super(model, new Vector3f(aircraft.getPosition().x, aircraft.getPosition().y,aircraft.getPosition().z), rotX, rotY, rotZ, new Vector3f(scale.getX(),scale.getY(),scale.getZ()));
        this.aircraft = aircraft;
        this.position = aircraft.getPosition();
    }

    public void move() {
        this.setRotX(aircraft.getRotX());
        this.setRotY(aircraft.getRotY());
        this.setRotZ(aircraft.getRotZ());
    }

    public Vector3f getPosition() {
        return position;
    }

}
