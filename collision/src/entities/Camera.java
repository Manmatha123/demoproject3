package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;


public class Camera {

    private float distanceFromPlayer =100;
    private float angleAroundPlayer = 0;

    private Entity entity;

//    set the position of the camera
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch = 5;
    private float yaw;
    private float roll;
    private ControlObject_Test p1;

    public static int cam_view_count = 0;
    public static int keyE = 0;

    private boolean keyCheck = true;

    private String c;

    private boolean current_drone = false;

    public Camera(ControlObject_Test p1){
        this.p1 = p1;
        current_drone = true;
    }

    public Camera() {

    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void move() {

        calculateZoom();
        calculatePitch();
        calculateAngleAround();
        float hd;

        if (Keyboard.isKeyDown(Keyboard.KEY_C)&& keyCheck) {
            cam_view_count++;
            if (cam_view_count > 7) {
                cam_view_count = 1;
            }
            keyCheck = false;
        }
        if (!Keyboard.isKeyDown(Keyboard.KEY_C)) {
            keyCheck = true;
        }

        switch (cam_view_count) {
            case 1:
                pitch = 0;
                distanceFromPlayer = 100;
                break;
            case 2:
                pitch = 60;
                calculatePitch();
                distanceFromPlayer = 100;
                break;
            case 3:
                pitch = 120;
                calculatePitch();
                distanceFromPlayer = 100;
                break;
            case 4:
                pitch = 180;
                calculatePitch();
                distanceFromPlayer = 100;
                break;
            case 5:
                pitch = 240;
                calculatePitch();
                distanceFromPlayer = 100;
                break;
            case 6:
                pitch = 300;
                calculatePitch();
                distanceFromPlayer = 100;
                break;
            case 7:
                pitch = 360;
                calculatePitch();
                distanceFromPlayer = 100;
                break;
        }

        float vd = calculateVerticalDistance();
        hd = calculateHorizontalDistance();
        calculateCameraPos(hd, vd);
//        if (current_drone) {
//        }
//
        this.yaw = 180 - (p1.getRotY() + angleAroundPlayer);
    }

    public void setPosition(Vector3f newPosition) {
        this.position.x = newPosition.x;
        this.position.y = newPosition.y;
        this.position.z = newPosition.z;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void lookAt(Vector3f target) {
        // Calculate the direction vector from camera to target
        float dx = target.x - position.x;
        float dy = target.y - position.y;
        float dz = target.z - position.z;

        // Calculate yaw (horizontal angle)
        this.yaw = (float) Math.toDegrees(Math.atan2(dx, dz));

        // Calculate pitch (vertical angle)
        float horizontalDistance = (float) Math.sqrt(dx * dx + dz * dz);
        this.pitch = (float) Math.toDegrees(Math.atan2(dy, horizontalDistance));
    }

    public void setDistanceFromPlayer(float distance) {
        this.distanceFromPlayer = distance;
    }

    public void setAngelAroundPlayer(float angle) {
        this.angleAroundPlayer = angle;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private void calculateCameraPos(float hd, float vd){
//        if(current_drone)
//        {
            float theta = p1.getRotY()  + angleAroundPlayer;
            float xOffSet = (float) (hd * Math.sin(Math.toRadians(theta)));
            float zOffSet = (float) (hd * Math.cos(Math.toRadians(theta)));
            position.x = p1.getPosition().x - xOffSet;
            position.z = (p1.getPosition().z - 8.430138f) - zOffSet;
            position.y = (p1.getPosition().y - 6.1f) + vd;
            this.yaw = 180 - theta;

//        }

//        float theta = p1.getRotY()  + angleAroundPlayer;
//        float offsetX = (float) (hd * Math.sin(Math.toRadians(theta)));
//        float offsetZ = (float) (hd * Math.cos(Math.toRadians(theta)));
//        position.x = p1.getPosition().x - offsetX;
//        position.z = p1.getPosition().z - offsetZ;
//        position.y = p1.getPosition().y + vd;
////        this.yaw = 180 - theta;
    }

    private float calculateHorizontalDistance(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateZoom(){
        float zoomLevel = Mouse.getDWheel() * 0.05f;
        distanceFromPlayer -= zoomLevel;
//
//        if (Keyboard.isKeyDown(Keyboard.KEY_UP)){
//            distanceFromPlayer -= 5f;
//        }
//        if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
//            distanceFromPlayer += 5f;
//        }
    }

    private void calculatePitch(){
//        if(Mouse.isButtonDown(1)){
//            float pitchChange = Mouse.getDY() * 0.1f;
//            pitch -= pitchChange;
//
//            float yawChange = Mouse.getDX() * 0.1f;
//            angleAroundPlayer -= yawChange ;
//        }
//        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
//            pitch -= 0.5f;
//        }
//        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
//            pitch += 0.5f;
//        }


//        if(Keyboard.isKeyDown(Keyboard.KEY_B)){
//            angleAroundPlayer -= 50f;
//        }




    }

    public static float calcRotation(){
       return ControlObject_Test.Run_speed * DisplayManager.getframetimeseconds();
    }

    public float getAngleAroundPlayer() {
        return angleAroundPlayer;
    }

    public void setAngleAroundPlayer(float angleAroundPlayer) {
        this.angleAroundPlayer = angleAroundPlayer;
    }

    private void calculateAngleAround(){

    }
}