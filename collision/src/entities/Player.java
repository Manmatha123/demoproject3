package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import collision.AABB;
import displayManager.DisplayManager;
import models.TexturedModel;

public class Player extends Entity {

    private static final float RUN_SPEED = 120;
    private static final float TURN_SPEED = 120;
    private static boolean isInTheAir = false;
    private static float GRAVITY = -20.0f;
    private static float upwardsSpeed = 0.0f;
    private static float JUMP_POWER = 80.0f;
    private static Vector3f basePosition;

    public Player(TexturedModel model, Vector3f position,
            float rotX, float rotY, float rotZ, float scale) {
        super(model, position, scale, rotX, rotY, rotZ);
        basePosition=new Vector3f(position.x,position.y,position.z);
    }

    public void move(float delta) {
        checkInputs(delta);

        if(isInTheAir){
        upwardsSpeed += GRAVITY * DisplayManager.getFrameTimeSeconds();
        super.increasePosition(0, upwardsSpeed *
        DisplayManager.getFrameTimeSeconds(), 0);
        }

        if (super.getPosition().getY() < 0.0f + basePosition.y) {
        upwardsSpeed = 0;
        super.getPosition().y = 0.0f + basePosition.y;
        isInTheAir = false;
        }
    }

    private void checkInputs(float delta) {

        float distance = RUN_SPEED * delta;
        float rotation = TURN_SPEED * delta;

        // Move forward
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            float dx = (float) (distance * Math.sin(Math.toRadians(getRotY())));
            float dz = (float) (distance * Math.cos(Math.toRadians(getRotY())));
            increasePosition(dx, 0, dz);
        }

        // Move backward
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            float dx = (float) (distance * Math.sin(Math.toRadians(getRotY())));
            float dz = (float) (distance * Math.cos(Math.toRadians(getRotY())));
            increasePosition(-dx, 0, -dz);
        }

        // Rotate left
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            increaseRotation(0, rotation, 0);
        }

        // Rotate right
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            increaseRotation(0, -rotation, 0);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            // basePosition=new Vector3f(super.getPosition().x,super.getPosition().y,super.getPosition().z);
            jump();
        }
    }

    private void jump() {
        if (!isInTheAir) {
            upwardsSpeed = JUMP_POWER;
            isInTheAir = true;
        }

    }
}
