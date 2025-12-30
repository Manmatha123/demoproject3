package loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.SunShader;

public class MasterRenderer {

    private static final float fieldOfView = 90; // FOV
    private static final float nearPlane = 0.1f;
    private static final float farPlane = 3000;
    private static final float RED = 0;
    private static final float GREEN = 0.5f;
    private static final float BLUE = 0.5f;

    private SunShader sunShader = new SunShader();
    private SunRenderer sunRenderer;

    private StaticShader shader = new StaticShader();
    private EntityRenderer entityRenderer;

    private Matrix4f projectionMatrix;

    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public MasterRenderer() {
        enableCulling();
        createProjectionMatrix();
        entityRenderer = new EntityRenderer(shader, projectionMatrix);
        sunRenderer = new SunRenderer(sunShader, projectionMatrix);

    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0,0,0, 1);
    }

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

public void renderSun(Entity sun, Camera camera, Light light) {
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE); // Additive blending
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glDepthMask(false); // Optimization: Don't write to depth buffer for glow

    sunShader.start();
    sunShader.loadLight(light);
    sunShader.loadViewMatrix(camera);
    sunShader.loadCameraPosition(camera);

    float originalScale = sun.getScale();

    // Pass 1: The Blinding Core (Almost pure white)
    sunShader.loadGlowColour(new Vector3f(1.5f, 1.5f, 1.2f)); 
    sunRenderer.render(sun);

    // Pass 2: Intense Inner Halo (Bright Yellow)
    sun.setScale(originalScale * 1.2f);
    sunShader.loadGlowColour(new Vector3f(1.0f, 0.8f, 0.3f));
    sunRenderer.render(sun);

    // Pass 3: The Wide Glow (Soft Orange)
    sun.setScale(originalScale * 1.6f);
    sunShader.loadGlowColour(new Vector3f(0.6f, 0.3f, 0.1f));
    sunRenderer.render(sun);

    // Pass 4: The Atmospheric Aura (Very large, very faint red/orange)
    sun.setScale(originalScale * 1.8f);
    sunShader.loadGlowColour(new Vector3f(0.15f, 0.05f, 0.01f));
    sunRenderer.render(sun);

    sun.setScale(originalScale); // Reset
    sunShader.stop();
    
    GL11.glDepthMask(true);
    GL11.glDisable(GL11.GL_BLEND);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
}

    public void render(Light sun, Camera camera) {
        prepare();
        shader.start();
        shader.loadSkyColour(RED, GREEN, BLUE);
        shader.loadLight(sun);
        shader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        shader.stop();

        entities.clear();
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }

    }

    public void cleanUP() {
        shader.cleanUp();
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(fieldOfView / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = farPlane - nearPlane;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale; // x
        projectionMatrix.m11 = y_scale; // y
        projectionMatrix.m22 = -((farPlane + nearPlane) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustum_length);
        projectionMatrix.m33 = 0; // z

    }

}
