package entities;


import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;
import models.TextureModel;


public class Player extends Entity {

    private static final float RUN_SPEED = 880;
    private static final float TURN_SPEED = 120;

    public Player(TextureModel model, Vector3f position,
                  float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, new Vector3f(scale,scale,scale));
    }

    public void move(float delta) {
        checkInputs(delta);
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

        // Rotate using R key
        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            increaseRotation(0, rotation, 0);
        }
    }
}
