package toolbox;


import Collision.AABB;
import entities.Camera;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;


public class MousePicker {


    private static final int RECURSION_COUNT = 200;
    private static final float RAY_RANGE = 600;


    private Vector3f currentRay = new Vector3f();


    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;


    public MousePicker(Camera cam, Matrix4f projection) {
        camera = cam;
        projectionMatrix = projection;
        viewMatrix = Maths.createViewMatrix(camera);
    }


    public Vector3f getCurrentRay() {
        return currentRay;
    }


    public void update( Camera camera) {
        viewMatrix = Maths.createViewMatrix(camera);
        currentRay = calculateMouseRay();

    }



    public boolean isRayIntersectingAABB(Vector3f rayOrigin, Vector3f rayDirection, AABB bbox) {
        float tMin = (bbox.min.x - rayOrigin.x) / rayDirection.x;
        float tMax = (bbox.max.x - rayOrigin.x) / rayDirection.x;

        if (tMin > tMax) {
            float temp = tMin;
            tMin = tMax;
            tMax = temp;
        }

        float tyMin = (bbox.min.y - rayOrigin.y) / rayDirection.y;
        float tyMax = (bbox.max.y - rayOrigin.y) / rayDirection.y;

        if (tyMin > tyMax) {
            float temp = tyMin;
            tyMin = tyMax;
            tyMax = temp;
        }

        if ((tMin > tyMax) || (tyMin > tMax)) {
            return false;
        }

        if (tyMin > tMin) {
            tMin = tyMin;
        }

        if (tyMax < tMax) {
            tMax = tyMax;
        }

        float tzMin = (bbox.min.z - rayOrigin.z) / rayDirection.z;
        float tzMax = (bbox.max.z - rayOrigin.z) / rayDirection.z;

        if (tzMin > tzMax) {
            float temp = tzMin;
            tzMin = tzMax;
            tzMax = temp;
        }

        if ((tMin > tzMax) || (tzMin > tMax)) {
            return false;
        }

        if (tzMin > tMin) {
            tMin = tzMin;
        }

        if (tzMax < tMax) {
            tMax = tzMax;
        }

        return true;
    }


    private Vector3f calculateMouseRay() {
        float mouseX = Mouse.getX();
        float mouseY = Mouse.getY();
        Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
        Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
        Vector4f eyeCoords = toEyeCoords(clipCoords);
        Vector3f worldRay = toWorldCoords(eyeCoords);
        return worldRay;
    }


    private Vector3f toWorldCoords(Vector4f eyeCoords) {
        Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
        Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoords, null);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalise();
        return mouseRay;
    }


    private Vector4f toEyeCoords(Vector4f clipCoords) {
        Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
        Vector4f eyeCoords = Matrix4f.transform(invertedProjection, clipCoords, null);
        return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f);
    }


    private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
        float x = (2.0f * mouseX) / Display.getWidth() - 1f;
        float y = (2.0f * mouseY) / Display.getHeight() - 1f;
        return new Vector2f(x, y);
    }
}