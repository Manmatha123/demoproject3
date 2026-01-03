/* 
 * The MIT License
 *
 * Copyright 2018 Charly Lamothe.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package shadows;

import java.util.List;
import java.util.Map;

import  entities.light;
import entities.Camera;
import entities.Entity;
import models.TextureModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;


public class ShadowMapMasterRenderer {

    private static final int SHADOW_MAP_SIZE = 5096;

    private ShadowFrameBuffer shadowFbo;
    private ShadowShader shader;
    private ShadowBox shadowBox;
    private Matrix4f projectionMatrix = new Matrix4f();
    private Matrix4f lightViewMatrix = new Matrix4f();
    private Matrix4f projectionViewMatrix = new Matrix4f();
    private Matrix4f offset = createOffset();

    private ShadowMapEntityRenderer entityRenderer;


    public ShadowMapMasterRenderer(Camera camera) {
        shader = new ShadowShader();
        shadowBox = new ShadowBox(lightViewMatrix, camera);
        shadowFbo = new ShadowFrameBuffer(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE);
        entityRenderer = new ShadowMapEntityRenderer(shader, projectionViewMatrix);
    }


    public void render(Map<TextureModel, List<Entity>> entities, light sun) {
        shadowBox.update();
        Vector3f sunPosition = sun.getPosition();
        Vector3f lightDirection = new Vector3f(sun.getDirection());
        prepare(lightDirection, shadowBox);
        entityRenderer.render(entities);
        finish();
    }


    public Matrix4f getToShadowMapSpaceMatrix() {
        return Matrix4f.mul(offset, projectionViewMatrix, null);
    }


    public void cleanUp() {
        shader.cleanUp();
        shadowFbo.cleanUp();
    }


    public int getShadowMap() {
        return shadowFbo.getShadowMap();
    }


    protected Matrix4f getLightSpaceTransform() {
        return lightViewMatrix;
    }


    private void prepare(Vector3f lightDirection, ShadowBox box) {
        updateOrthoProjectionMatrix(box.getWidth(), box.getHeight(), box.getLength());
        updateLightViewMatrix(lightDirection, box.getCenter());
        Matrix4f.mul(projectionMatrix, lightViewMatrix, projectionViewMatrix);
        shadowFbo.bindFrameBuffer();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        shader.start();
    }


    private void finish() {
        shader.stop();
        shadowFbo.unbindFrameBuffer();
    }


    private void updateLightViewMatrix(Vector3f direction, Vector3f center) {
        direction.normalise();
        center.negate();
        lightViewMatrix.setIdentity();
        float pitch = (float) Math.acos(new Vector2f(direction.x, direction.z).length());
        Matrix4f.rotate(pitch, new Vector3f(1, 0, 0), lightViewMatrix, lightViewMatrix);
        float yaw = (float) Math.toDegrees(((float) Math.atan(direction.x / direction.z)));
        yaw = direction.z > 0 ? yaw - 180 : yaw;
        Matrix4f.rotate((float) -Math.toRadians(yaw), new Vector3f(0, 1, 0), lightViewMatrix,
                lightViewMatrix);
        Matrix4f.translate(center, lightViewMatrix, lightViewMatrix);
    }


    private void updateOrthoProjectionMatrix(float width, float height, float length) {
        projectionMatrix.setIdentity();
        projectionMatrix.m00 = 2f / width;
        projectionMatrix.m11 = 2f / height;
        projectionMatrix.m22 = -2f / length;
        projectionMatrix.m33 = 1;
    }


    private static Matrix4f createOffset() {
        Matrix4f offset = new Matrix4f();
        offset.translate(new Vector3f(0.5f, 0.5f, 0.5f));
        offset.scale(new Vector3f(0.5f, 0.5f, 0.5f));
        return offset;
    }
}
