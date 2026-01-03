package entities;

import models.TextureModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

import java.util.ArrayList;
import java.util.List;


public class ControlObject_Test extends Entity{

    public TextureModel textureModel;
    private float currentTurnSpeed = 0;

    public  Vector3f scale;
    private long startTimeMillis;


     static final float Run_speed = 300;
     static final float Turn_speed = 120;

     private float radious=0.0f;

    public float getRadious() {
        return radious;
    }

    public void setRadious(float radious) {
        this.radious = radious;
    }


    private static float GRAVITY=-50;
    private static float APPLY_FORCE=80;
    private static float FORCE_IMPACT=0;
    private static boolean isFlying=false;
    private static Vector3f basePosition;



    public static List<Entity> totalEntities = new ArrayList<>();
    public static List<Entity> subentities = new ArrayList<>();
    public static boolean undo = false ;
    public ControlObject_Test(TextureModel model, Vector3f position, float rotX, float rotY, float rotZ, Vector3f scale){
        super(model,position, rotX,rotY,rotZ,scale);
        this.position = position;
        startTimeMillis = System.currentTimeMillis();
        this.textureModel = model;
        this.scale = new Vector3f(scale);
        basePosition=new Vector3f(position);
    }


    public ControlObject_Test(TextureModel model, Vector3f position, Vector3f offset, float rotX, float rotY, float rotZ, Vector3f scale){

        super(model,position,offset, rotX,rotY,rotZ,scale);
        this.position = position;
//        this.initialPosition = position;

        startTimeMillis = System.currentTimeMillis();
        this.textureModel = model;
        this.scale = new Vector3f(scale);
        this.model = model ;
        basePosition=new Vector3f(position);
        this.model.getRawModel().rearranging_faces();

    }



    public void move(float delta) {
        checkInputs(delta);

        if(isFlying){
            FORCE_IMPACT+=GRAVITY* DisplayManager.getframetimeseconds();
            super.increasePosition(0, FORCE_IMPACT*DisplayManager.getframetimeseconds(),0);

            if(super.getPosition().y < basePosition.y){
            FORCE_IMPACT=0;
            super.getPosition().y=basePosition.y;
            isFlying=false;
            }
        }


//        super.increaseRotation(0, currentTurnSpeed * DisplayManager.getframetimeseconds(), 0);
//
//        float distance =currentSpeed * DisplayManager.getframetimeseconds();
//
//        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
//        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
//        super.increasePosition(dx, 0, dz);
//
//
//
//        float yaw = super.getRotY();
//        float pitch = super.getRotX();
//        float roll = super.getRotZ();
//
//        Matrix3x3 resultantMat1;
//        Matrix3x3 resultantMat2;
//        Matrix3x3 rotAboutZ;
//        Matrix3x3 rotAboutY;
//        Matrix3x3 rotAboutX;
//        rotAboutY = Matrix3x3.rotationMatrixAxisY(Math.toRadians(yaw));
//        rotAboutX = Matrix3x3.rotationMatrixAxisX(Math.toRadians(pitch));
//        rotAboutZ = Matrix3x3.rotationMatrixAxisZ(Math.toRadians(roll));
//        resultantMat1 = Matrix3x3.multiply(rotAboutZ, rotAboutY);
//        resultantMat2 = Matrix3x3.multiply(resultantMat1, rotAboutX);
//
//        Vector3f result = new Vector3f();
//        Matrix3x3.transform(resultantMat2, direction, result);
//
//
//        float d = currentSpeed * DisplayManager.getframetimeseconds();
//        float dir_dx = result.x * d;
//        float dir_dz = result.z * d;
//        float dir_dy = result.y * d;
//
//        // Update the position variable
//        position.x += dir_dx;
//        position.y += dir_dy;
//        position.z += dir_dz;

    }

    private void checkInputs(float delta) {

        float distance = Run_speed * delta;
        float rotation = Turn_speed * delta;


        // Move forward
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            float dx = (float) (distance * Math.sin(Math.toRadians(getRotY())));
            float dz = (float) (distance * Math.cos(Math.toRadians(getRotY())));
            increasePosition(dx, 0,  dz);
        }

        // Move backward
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            float dx = (float) (distance * Math.sin(Math.toRadians(getRotY())));
            float dz = (float) (distance * Math.cos(Math.toRadians(getRotY())));
            increasePosition(-dx, 0, -dz);
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
            float rotationCurr =20 * DisplayManager.getframetimeseconds();
           increaseRotation(0, -rotationCurr,0);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
            float rotationCurr = 20 * DisplayManager.getframetimeseconds();
            increaseRotation(0, rotationCurr,0);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_U)){
            position.setY(position.y+0.5f);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.setY(position.y-0.5f);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
            System.out.println("Space key pressed");
            FORCE_IMPACT=APPLY_FORCE;
            isFlying=true;
        }


        // Rotate left
//        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
//            increaseRotation(0, -rotation, 0);
//        }
//
//        // Rotate right
//        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
//            increaseRotation(0, rotation, 0);
//        }



    }


}
