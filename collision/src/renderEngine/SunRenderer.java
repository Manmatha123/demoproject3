package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shader.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;
import shader.SunShader;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTranslatef;

public class SunRenderer {

    private SunShader shader;


    public SunRenderer(SunShader shader, Matrix4f projectionMatrix){
        this.shader =shader;
        shader.start();
        shader.loadProjection(projectionMatrix);
        shader.stop();
    }



    public void render(Entity sun){
        TextureModel model=sun.getModel();
            prepareTexturedModel(model);
                prepareInstance(sun);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTexturedModel();
        }




    private void prepareTexturedModel(TextureModel model){
        RawModel rawModel = model.getRawModel();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        ModelTexture texture = model.getTexture();

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
    }

    private void unbindTexturedModel(){
//        masterrenderer.enableculling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
    private void prepareInstance(Entity entity){

        Matrix4f transformationMatrix = new Matrix4f();

        if(entity.objectType == 1){
            transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),entity.offset,
                    entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        }
        else if(entity.objectType == 2 && entity.getModel().name.equals("rightelevatortail")){
            transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),entity.offset,entity.angleOffSet,
                    entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale(),"rightelevatortail");
        }
        else if(entity.objectType == 2 && entity.getModel().name.equals("leftelevatortail")){
            transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),entity.offset,entity.angleOffSet,
                    entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale(),"leftelevatortail");
        }else if(entity.objectType == 2 ){
            transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),entity.offset,entity.angleOffSet,
                    entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        }
        else{
            transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                    entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        }

        shader.loadTransformationMatrix(transformationMatrix);
//        shader.loadoffset(entity.getTextureXoffSet(), entity.getTextureYoffSet());
    }

}
