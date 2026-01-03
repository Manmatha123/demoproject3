package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.light;
import models.TextureModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import shader.StaticShader;
import shadows.ShadowMapMasterRenderer;
import toolbox.Maths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class masterrenderer {

    public static final float FOV = 70;
    public static final float NEAR_PLANE = 0.1f;
    public static final float FAR_PLANE = 100000;

//    private static final float RED = 0.5444f;
//    private static final float GREEN = 0.62f;
//    private static final float BLUE = 0.69f;
    private static final float RED = 1.0f;
    private static final float GREEN = 1.0f;
    private static final float BLUE = 1.0f;
    private Matrix4f projectionMatrix;
    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;
    private ShadowMapMasterRenderer shadowMapRenderer;

    private shader.SunShader sunShader=new shader.SunShader();
    private SunRenderer sunRenderer;


    private Map<TextureModel, List<Entity>> entities = new HashMap<TextureModel, List<Entity>>();
    private Map<TextureModel, List<Entity>> normalMapEntities = new HashMap<TextureModel, List<Entity>>();

    public Map<TextureModel, List<Entity>> getEntities() {
        return entities;
    }

    public masterrenderer(Loader loader,Camera camera){
        enableCulling();
        createProjectionMatrix();
        renderer = new EntityRenderer(shader,projectionMatrix);
        sunRenderer=new SunRenderer(sunShader,projectionMatrix);
        this.shadowMapRenderer=new ShadowMapMasterRenderer(camera);
    }

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }


    public int getShadowMapTexture() {
        return shadowMapRenderer.getShadowMap();
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }


    public void renderShadowMap(List<Entity> entityList, light sun) {
        for (Entity entity : entityList) {
            processEntity(entity);
        }
        shadowMapRenderer.render(entities, sun);
        entities.clear();
    }

    public void renderSunEntity(Entity sunEntity, Camera camera, light sun){
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        disableCulling();
        prepare();
        sunShader.start();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        shader.loadlight(sun);
        sunShader.loadViewMatrix(camera);
        sunShader.loadGlowColour(new Vector3f(1.0f, 0.5f, 0.3f));
        shader.connectTextureUnits();
//        shader.loadToShadowSpaceMatrix(shadowMapRenderer.getToShadowMapSpaceMatrix());
        sunRenderer.render(sunEntity);
        GL11.glDisable(GL11.GL_BLEND);
        sunShader.stop();
        enableCulling();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public void render(light sun, Camera c){
        shader.start();
//        prepare();
//        GL13.glActiveTexture(GL11.GL_TEXTURE);
        shader.loadClipPlane(new Vector4f(0, -1, 100, 100000));
//        shader.loadShadowMap(5);
        shader.connectTextureUnits();
        shader.loadskycolour(RED,GREEN, BLUE);
        shader.loadlight(sun);
        shader.loadViewMatrix(c);
        shader.loadToShadowSpaceMatrix(shadowMapRenderer.getToShadowMapSpaceMatrix());
        renderer.render(entities);
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);
        shader.stop();


        entities.clear();

    }




    public void processEntity(Entity entity){
        TextureModel entitymodel = entity.getModel();
        List<Entity> batch = entities.get(entitymodel);
        if(batch != null){
            batch.add(entity);
        }else{
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entitymodel, newBatch);
        }
    }

    public void cleanup(){
        shader.cleanUp();
    }

    public void prepare() {

//        glFrontFace(GL_CW);
//        glEnable(GL_CULL_FACE);
//        glCullFace(GL_BACK);

        glEnable(GL_DEPTH_TEST);
        GL11.glClearColor(RED,GREEN,BLUE,1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_CLAMP);
        glEnable(GL_BLEND);
        GL13.glActiveTexture(GL13.GL_TEXTURE5);
        GL11.glBindTexture(GL_TEXTURE_2D,getShadowMapTexture());

    }

    private void createProjectionMatrix(){
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }


    public List<Entity> update(Camera camera, TextureModel treeTexture, List<Entity> treesArr, float randomX, float randomZ) {


//            int currentChunk = (int) Math.floor(camera.getPosition().z /105);
//            System.out.println(currentChunk);
//        for (int m = currentChunk - 1; m <= currentChunk + 1; m++) {
            for(int i=1;i<=5;i++){
                for(int j=1;j<=5;j++){
                    randomX+=(float) (Math.random() * 40);
                    randomZ+=(float) (Math.random() * 40);
                    Entity tempTreeEntity=new Entity(new TextureModel(treeTexture),new Vector3f((50*randomX),-10,(100*randomZ)), new Vector3f(0,0,0), 0,0,0, new Vector3f(1f, 1f, 1f));
                    treesArr.add(tempTreeEntity);
                }
            }
//        }


        return treesArr;
    }
}
