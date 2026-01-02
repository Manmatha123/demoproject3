package entities;

import org.lwjgl.util.vector.Vector3f;

import collision.AABB;
import models.TexturedModel;

public class Entity {
    private TexturedModel model;
    private Vector3f position;
    private float scale;
    private float rotX, rotY, rotZ;

    // for collision
    private Vector3f size; // 👈 NEW
    private AABB aabb;

    public Entity(TexturedModel model, Vector3f position, float scale, float rotX, float rotY,
            float rotZ) {
        this.model = model;
        this.position = position;
        this.scale = scale;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;

        Vector3f min = model.getRawModel().getMin();
        Vector3f max = model.getRawModel().getMax();

        Vector3f sizeTree = new Vector3f(
                (max.x - min.x) * scale,
                (max.y - min.y) * scale,
                (max.z - min.z) * scale);

        // TRUNK ONLY
        sizeTree.x *= 0.068f;
        sizeTree.z *= 0.068f;

        // CORRECT CENTER
        float minY = min.y * scale;

        Vector3f center = new Vector3f(
                position.x,
                position.y + minY + sizeTree.y / 2f,
                position.z);
        this.size = sizeTree;

        this.aabb = new AABB(center, sizeTree);

    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;

        aabb.update(position, size); // 👈 UPDATE AABB
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
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

    public float getScale() {
        return scale;
    }

    public TexturedModel getModel() {
        return model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public AABB getAABB() {
        return aabb;
    }

    public Vector3f getSize() {
        return size;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
