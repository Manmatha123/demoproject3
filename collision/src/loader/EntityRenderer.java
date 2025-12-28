package loader;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolBox.Maths;

public class EntityRenderer {



    public static StaticShader shader;

    public EntityRenderer(StaticShader shader,Matrix4f projectionMatrix){
        EntityRenderer.shader = shader;
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<TexturedModel, List<Entity>> entities){
        for(TexturedModel texturedModel:entities.keySet()){
            prepareTexturedModel(texturedModel);
            List<Entity> batch = entities.get(texturedModel);
            for(Entity entity:batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES,texturedModel.getRawModel().getVertexCount(),GL11.GL_UNSIGNED_INT,0);
            }
            unbindTexturedModel();
        }
    }

    public void prepareTexturedModel(TexturedModel texturedModel){
        RawModel model = texturedModel.getRawModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0); // Positions
        GL20.glEnableVertexAttribArray(1); // textureCoordinates
        GL20.glEnableVertexAttribArray(2); // normal Coordinates "N"
        ModelTexture texture = texturedModel.getTexture();
        if(texture.isHasTransparency()){
            MasterRenderer.disableCulling();
        }
        shader.loadFakeLighting(texture.isHasFakeLighting());
        shader.loadShineVariables(texture.getReflectivity(),texture.getShineDamper());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,texturedModel.getTexture().getTextureID());
    }

    private void unbindTexturedModel(){
        MasterRenderer.enableCulling();
        GL20.glDisableVertexAttribArray(0);          //Disable After Draw which is in Number 0
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
    private void prepareInstance(Entity entity){
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(),entity.getRotY(),entity.getRotZ(),entity.getScale());
        shader.loadTransformMatrix(transformationMatrix);
    }


}
