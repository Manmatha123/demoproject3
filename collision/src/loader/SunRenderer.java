package loader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.SunShader;
import toolBox.Maths;

public class SunRenderer {

    private SunShader shader;

    public SunRenderer(SunShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.start();
        shader.loadProjection(projectionMatrix);
        shader.stop();
    }

    public void render(Entity sun) {

        TexturedModel model = sun.getModel();
        RawModel rawModel = model.getRawModel();

        // Bind VAO
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        // Bind texture (CORRECT ORDER)
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(
                GL11.GL_TEXTURE_2D,
                model.getTexture().getTextureID()
        );

        // Load transform
        shader.loadTransformation(
                Maths.createTransformationMatrix(
                        sun.getPosition(),
                        sun.getRotX(),
                        sun.getRotY(),
                        sun.getRotZ(),
                        sun.getScale()
                )
        );

        // Draw
        GL11.glDrawElements(
                GL11.GL_TRIANGLES,
                rawModel.getVertexCount(),
                GL11.GL_UNSIGNED_INT,
                0
        );

        // Cleanup
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
}
