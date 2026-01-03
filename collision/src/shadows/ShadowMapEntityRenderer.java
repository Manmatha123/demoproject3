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

import entities.Entity;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.opengl.GL13;
import renderEngine.masterrenderer;
import toolbox.Maths;

public class ShadowMapEntityRenderer {

    private Matrix4f projectionViewMatrix;
    private ShadowShader shader;


    protected ShadowMapEntityRenderer(ShadowShader shader, Matrix4f projectionViewMatrix) {
        this.shader = shader;
        this.projectionViewMatrix = projectionViewMatrix;
    }


    protected void render(Map<TextureModel, List<Entity>> entities) {
        for (TextureModel model : entities.keySet()) {
            RawModel rawModel = model.getRawModel();
            bindModel(rawModel);
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
            if (model.getTexture().isHastransparency()) {
                masterrenderer.disableCulling();
            }
            for (Entity entity : entities.get(model)) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);
            }
            if (model.getTexture().isHastransparency()) {
                masterrenderer.enableCulling();
            }
        }
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }


    private void bindModel(RawModel rawModel) {
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
    }


    private void prepareInstance(Entity entity) {
        Matrix4f modelMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        Matrix4f mvpMatrix = Matrix4f.mul(projectionViewMatrix, modelMatrix, null);
        shader.loadMvpMatrix(mvpMatrix);
    }
}
