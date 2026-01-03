package gameEngine;

import Collision.AABB;
import entities.*;
import models.RawModel;
import models.TextureModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import shader.ShaderProgram;
import shader.StaticShader;
import shadows.guis.GuiRenderer;
import shadows.guis.GuiTexture;
import terrain.Terrain;
import textures.ModelTexture;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import static org.lwjgl.opengl.GL11.*;
import static org.newdawn.slick.opengl.renderer.SGL.GL_DEPTH_BUFFER_BIT;

public class GameLoop extends JPanel {

    private static final int DATA_RATE_HZ = 100;
    private static final int TIMER_INTERVAL_MS = 10000 / DATA_RATE_HZ;
    private final Canvas canvas;
    private Thread gameThread;
    public  static  Loader loader ;
    public Camera camera;
   public static List<Entity>  treesArr=new ArrayList<>();
    public Entity sunEntity;
    private static final String TEST_GUIS_PATH = "testGuis/";


    public GameLoop(int width, int height) {
        JPanel field3DPane = new JPanel(null);
        field3DPane.setBackground(Color.BLACK); // set black color on simulation screen
        canvas = createCanvas(); // create canvas (openGL window)
        canvas.setBounds(0,0,width, height); // set OpenGL window size and x,y coordinates
        field3DPane.add(canvas, BorderLayout.CENTER);
        setLayout(new BorderLayout());
        add(field3DPane, BorderLayout.CENTER);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setNeedValidation();
            }
        });
        startRendering();
    }


public void startRendering(){
           gameThread=new Thread(()->{
               try{
                  DisplayManager.createDisplay();

                   Display.setParent(canvas);
                   OpenGlInIt.setupOpenGL(canvas);
                   loader = new Loader();


                   List<RawModel> treeModels = objLoader.loadObjModel("E:\\lwjgl\\3DFieldShadow\\3DField\\resource\\lowPolyTree.obj",loader);
                   List<RawModel> terrienModel = objLoader.loadObjModel("E:\\lwjgl\\3DFieldShadow\\3DField\\resource\\3dplane.obj",loader);
                   List<RawModel> ballModel = objLoader.loadObjModel("E:\\lwjgl\\3DFieldShadow\\3DField\\resource\\ball.obj",loader);
                   List<RawModel> sunModels = objLoader.loadObjModel("E:\\lwjgl\\3DFieldShadow\\3DField\\resource\\ball.obj",loader);

                   TextureModel ballTexture = new TextureModel(ballModel.get(0), new ModelTexture(loader.loadTexture("ball")),"ball");
                   ControlObject_Test p = new ControlObject_Test(ballTexture, new Vector3f(0,105.0f, 0.0f), new Vector3f(0,0,0), 0, 0, 0, new Vector3f(2f, 2f, 2f));

                   ControlThread COT = new ControlThread(p);
                   COT.start();
                   CameraThread ct = new CameraThread(p);
                   ct.start();
                   this.camera = new Camera(p);


                   TextureModel sunModeTexture = new TextureModel(sunModels.get(0), new ModelTexture(loader.loadTexture("2k_sun.jpg")),"sun");
                   TextureModel treeTexture=new TextureModel(treeModels.get(0), new ModelTexture(loader.loadTexture("lowPolyTree")), "tree");
                   TextureModel terrianTexture=new TextureModel(terrienModel.get(0), new ModelTexture(loader.loadTexture("grassFlowers")), "grassFlowers");

                  masterrenderer renderer = new masterrenderer(loader, camera);
                   light light = new light(new Vector3f(0,900, 100.0f),new Vector3f(-0.1f,-0.9f,-1.0f), new Vector3f(1.0f, 0.99f, 0.88f),1.2f, 65, 1f, 0.01f, 0.00001f);

//                   List<GuiTexture> guiTextures = new ArrayList<>();
//                   guiTextures.add(new GuiTexture(renderer.getShadowMapTexture(), new Vector2f(0,-245.0f), new Vector2f(5f, 5f)));
//                   GuiRenderer guiRenderer = new GuiRenderer(loader);
//                   guiRenderer.render(guiTextures);
//
//
//                   guiTextures.addAll(setUpGUI(loader));

                   List<Entity>  terrianList=new ArrayList<>();

                   for(int i=0;i<5;i++){
                       for(int j=0;j<5;j++){
                            Entity terrian=new Entity(terrianTexture, new Vector3f((i*600),0,(j*600)+50),new Vector3f(0,0,0), 0,0,0, new Vector3f(120f, 120f, 120f));
                            terrianList.add(terrian);
                       }
                   }
                   sunEntity=new Entity(sunModeTexture, new Vector3f(100,200.0f, 1000.0f), 1, 0, 0, new Vector3f(11.5f, 11.5f, 11.5f));


                   for(int i=1;i<=10;i++){
                       for(int j=1;j<=10;j++){
                           float randomX=(float) (Math.random() * 40);
                           float randomZ=(float) (Math.random() * 40);
                           Entity tree=new Entity(new TextureModel(treeTexture),new Vector3f((50*randomX),50.0f,(100*randomZ)), new Vector3f(0,0,0), 0,0,0, new Vector3f(2f, 2f, 2f));
                           treesArr.add(tree);
                       }
                   }

                   List<Entity> renderingEntity=new ArrayList<>();
                   renderingEntity.addAll(treesArr);
                   renderingEntity.addAll(terrianList);
                   renderingEntity.add(p);



                    while(!Display.isCloseRequested()){
//                        DisplayManager.updateDisplay();
//                        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                        Vector3f odlPlayerPos=new Vector3f(p.position.x, p.position.y, p.position.z);

                        camera.move();
                        p.move(DisplayManager.getframetimeseconds());

//                        for(Triangle triangle:terrianList.get(0).model.getRawModel().getTriangles()){
//                            if(isInsideTriangle(p.position.x, p.position.z,triangle)){
//                                float height=getTriangleHeight(p.position.x, p.position.z, triangle);
//                                p.position.y=height;
//                            }
//                        }


                        boolean isCollide=false;

                        for (Entity tree : treesArr) {
                            if(p.getAabb().intersects(tree.getAabb())){
                                isCollide=true;
                                break;
                            }
                        }

                        if(isCollide){
                            p.setPosition(odlPlayerPos);
                            p.getAabb().update(odlPlayerPos,p.getSize());
                        }

                        renderer.renderShadowMap(renderingEntity, light);
                     



                        renderer.processEntity(p);
                        for(Entity terrian:terrianList){
                            renderer.processEntity(terrian);
                        }
                        for (Entity entity : treesArr) {
                            renderer.processEntity(entity);
                        }
                        GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
                        renderer.renderSunEntity(sunEntity,camera, light);
                        renderer.render(light, camera );
//                        guiRenderer.render(guiTextures);

                      DisplayManager.updateDisplay();

                    }


//                   renderer.cleanup();
                   loader.cleanUp();
                   DisplayManager.closeDisplay();
               }catch (Exception e) {
                 e.printStackTrace();
               }
           });

           gameThread.start();
}




    private Canvas createCanvas() {
        Canvas canvas = new Canvas() {
            public void removeNotify() {
                stopRendering();
            }
        };
        canvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setNeedValidation();
            }
        });
        canvas.setIgnoreRepaint(true);
        return canvas;
    }
    private void setNeedValidation() {
    }
    private void stopRendering() {
        System.out.println("StopRendering");
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private boolean isInsideTriangle(float x, float z, Triangle t){

        Vector2f p=new Vector2f(x,z);
        Vector2f a=new Vector2f(t.v1.x, t.v1.z);
        Vector2f b=new Vector2f(t.v2.x, t.v2.z);
        Vector2f c=new Vector2f(t.v3.x, t.v3.z);

        float area=sign(p,a,b)+ sign(p,a,c) + sign(p,b,c);
        return Math.abs(area) < 0.001f;
    }


    private float sign(Vector2f p1,Vector2f p2, Vector2f p3){
        return ((p1.x - p3.x) * (p2.y - p3.y) -
                (p2.x - p3.x) - (p1.y - p3.y));
    }

    private float getTriangleHeight(float x, float z, Triangle t){

        Vector3f p1=t.v1;
        Vector3f p2=t.v2;
        Vector3f p3=t.v3;

        float del=(p2.z- p3.z)* (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1=((p2.z- p3.z)* (x - p3.x) + (p3.x - p2.x) * (z - p3.z))/del;
        float l2=((p3.z- p1.z)* (x - p3.x) + (p1.x - p3.x) * (z - p3.z))/del;
        float l3= 1.0f - l1 -l2;
        return l1 * p1.y + l2 * p2.y + l3* p3.y;
    }

    public static List<GuiTexture> setUpGUI(Loader loader) {

        // List for use in this method
        List<GuiTexture> list = new ArrayList<>();

        // Variables to be modified depending on the GUI area being set up
        float space = 0f;
        float spaceIncrement = 0.065f;
        float startingPosX = -0.293f;
        float startingPosY = -0.9f;

        // ACTION BARS
        GuiTexture abFrame = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "abFrame" ), new Vector2f(0.0f, startingPosY), new Vector2f(0.33f, 0.07f));
        GuiTexture ab1 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab1"), new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab2 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab2"), new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab3 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab3"), new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab4 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab4"), new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab5 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab5"), new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab6 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab6"), new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab7 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab7"), new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab8 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab8"), new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab9 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab9"), new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab0 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab0"), new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));

        // FIREBALL ABILITY EXAMPLE
        GuiTexture fireAbility = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "fire"), new Vector2f(-0.293f, startingPosY), new Vector2f(0.03f, 0.058f));
        GuiTexture cosmicAbility = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "cosmic"), new Vector2f(-0.293f + 0.065f, startingPosY), new Vector2f(0.03f, 0.058f));

        // HEALTH AND MAGIC ENERGY BAR
        GuiTexture healthBar = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "abHealth"), new Vector2f(0.0f, -0.72f), new Vector2f(0.33f, 0.03f));
        GuiTexture energyBar = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "abEnergy"), new Vector2f(0.0f, -0.79f), new Vector2f(0.33f, 0.03f));

        // change variables
        space = 0f;
        spaceIncrement = 0.12f;
        startingPosX = 0.95f;
        startingPosY = -0.9f;

        // RIGHT SIDE INTERFACE (INVENTORY, ETC)
        GuiTexture rightFrame = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "fillerFrame"), new Vector2f(startingPosX - 0.315f, -0.66f), new Vector2f(0.28f, 0.30f));
        GuiTexture fillerR1 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler"), new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerR2 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler"), new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerR3 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler"), new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerR4 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler"), new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerR5 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler"), new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));

        // change variables
        space = 0f;
        spaceIncrement = 0.12f;
        startingPosX = -0.95f;
        startingPosY = -0.9f;

        // LEFT SIDE INTERFACE (INVENTORY, ETC)
        GuiTexture leftFrame = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "fillerFrame"), new Vector2f(startingPosX + 0.315f, -0.66f), new Vector2f(0.28f, 0.30f));
        GuiTexture fillerL1 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler"), new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerL2 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler"), new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerL3 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler"), new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerL4 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler"), new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerL5 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler"), new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));        // add to GUI
        list.add(abFrame);
        list.add(fireAbility);
        list.add(cosmicAbility);
        list.add(ab1);
        list.add(ab2);
        list.add(ab3);
        list.add(ab4);
        list.add(ab5);
        list.add(ab6);
        list.add(ab7);
        list.add(ab8);
        list.add(ab9);
        list.add(ab0);
        return list;
    }
    
}
