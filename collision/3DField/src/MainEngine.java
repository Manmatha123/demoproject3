
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import displayManager.DisplayManager;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
// import loader.BloomRenderer;
import loader.FileType;
import loader.Loader;
import loader.MasterRenderer;
import loader.SceneFbo;
import models.RawModel;
import models.TexturedModel;
import objConverter.OBJFileLoader;
import shadows.guis.GuiRenderer;
import shadows.guis.GuiTexture;
import textures.ModelTexture;

public class MainEngine {

    static final float TILE_SIZE = 200f; // or 300
    static final int GRID_SIZE = 3;
    private static final String TEST_GUIS_PATH = "testGuis/";
    // SceneFbo sceneFbo;
    // BloomRenderer bloomRenderer;

    public static void main(String[] args) {
        try {
            DisplayManager.createDisplay();

            Loader loader = new Loader();

            RawModel fieldModel = OBJFileLoader.loadOBJ("planesurface", loader);
            TexturedModel fieldTexture = new TexturedModel(fieldModel,
                    new ModelTexture(loader.loadTexture("terrain/blendMap", FileType.PNG)).setHasTransparency(false)
                            .setShineDamper(1)
                            .setReflectivity(1));

            RawModel treeModel = OBJFileLoader.loadOBJ("lowPolyTree", loader);
            TexturedModel treeTexture = new TexturedModel(treeModel,
                    new ModelTexture(loader.loadTexture("lowPolyTree", FileType.PNG)).setHasTransparency(false)
                            .setShineDamper(1)
                            .setReflectivity(1));

            List<Entity> treesEntity = new ArrayList<>();

            for (int i = 0; i <= 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Entity treeEntity = new Entity(treeTexture,
                            new Vector3f((-200f * i) - 50.0f, 0, (-250f * j) - 50.0f), 2, 0, 0, 0);
                    treesEntity.add(treeEntity);
                }
            }

            RawModel ballModel = OBJFileLoader.loadOBJ("ball", loader);
            TexturedModel ballTexture = new TexturedModel(ballModel,
                    new ModelTexture(loader.loadTexture("human_texture", FileType.PNG)).setHasTransparency(false)
                            .setShineDamper(100)
                            .setReflectivity(1));

            RawModel sunModel = OBJFileLoader.loadOBJ("ball", loader);
            TexturedModel sunTexture = new TexturedModel(sunModel,
                    new ModelTexture(loader.loadTexture("13913_Sun_diff", FileType.JPG)).setHasTransparency(false)
                            .setShineDamper(100)
                            .setReflectivity(1));
            Entity sunEntity = new Entity(sunTexture, new Vector3f(-200, 30, 400), 9,
                    0, 0, 0);

            Light light = new Light(new Vector3f(-20, 10, 1000), new Vector3f(1, 1, 1),
                    new Vector3f(1.0f, 0.0001f, 0.00001f));
            Player player = new Player(
                    ballTexture,
                    new Vector3f(-200f, 10, -200f),
                    0, 0, 0,
                    4);

            Camera camera = new Camera(player);
            MasterRenderer masterrender = new MasterRenderer(camera);

            List<Entity> terrainTiles = new ArrayList<>();

            List<GuiTexture> guiTextures = new ArrayList<>();
            GuiRenderer guiRenderer = new GuiRenderer(loader);
            GuiTexture guitexture = new GuiTexture(masterrender.getShadowMapTexture(), new Vector2f(-6f, 0.7f),
                    new Vector2f(2f, 2f));
             guiTextures.add(guitexture);

            guiRenderer.render(guiTextures);

            for (int x = -GRID_SIZE / 2; x <= GRID_SIZE / 2; x++) {
                for (int z = -GRID_SIZE / 2; z <= GRID_SIZE / 2; z++) {

                    Entity tile = new Entity(
                            fieldTexture,
                            new Vector3f(x * TILE_SIZE, 0, z * TILE_SIZE),
                            30,
                            0, 0, 0);

                    terrainTiles.add(tile);
                }
            }
            guiTextures.addAll(setUpGUI(loader));

            List<Entity> shadowCasters = new ArrayList<>();
shadowCasters.addAll(treesEntity);
shadowCasters.add(player);

            // SceneFbo sceneFbo = new SceneFbo(
            // Display.getWidth(),
            // Display.getHeight());
            // BloomRenderer bloomRenderer = new BloomRenderer(loader);
            treesEntity.add(player);
            while (!Display.isCloseRequested()) {

                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

                // GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

                float delta = DisplayManager.getFrameTimeSeconds();

                Vector3f oldPos = new Vector3f(player.getPosition());

                player.move(delta);
                camera.move();
                guiRenderer.render(guiTextures);

                masterrender.renderShadowMap(shadowCasters, light);
                // GL11.glEnable(GL30.GL_CLIP_DISTANCE0);

                // boolean isCollide = false;
                // for (Entity tree : treesEntity) {
                // if (player.getAABB().intersects(tree.getAABB())) {
                // isCollide = true;
                // break;
                // }
                // }

                // if (isCollide) {
                // player.setPosition(oldPos);
                // player.getAABB().update(oldPos, player.getSize());
                // }

                for (Entity tree : treesEntity) {
                    masterrender.processEntity(tree);
                }

                for (Entity tile : terrainTiles) {
                    masterrender.processEntity(tile);
                }

                masterrender.processEntity(player);

                masterrender.render(light, camera);
                masterrender.renderSun(sunEntity, camera, light);
                DisplayManager.updateDisplay();
                // DisplayManager.updateDisplay();
            }
            DisplayManager.closeDisplay();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleGroundCollision(Player player) {
        Vector3f pos = player.getPosition();
        pos.y = 0; // ground height
        player.setPosition(pos);
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
        GuiTexture abFrame = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "abFrame", FileType.PNG),
                new Vector2f(0.0f, startingPosY), new Vector2f(0.33f, 0.07f));
        GuiTexture ab1 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab1", FileType.PNG),
                new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab2 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab2", FileType.PNG),
                new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab3 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab3", FileType.PNG),
                new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab4 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab4", FileType.PNG),
                new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab5 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab5", FileType.PNG),
                new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab6 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab6", FileType.PNG),
                new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab7 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab7", FileType.PNG),
                new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab8 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab8", FileType.PNG),
                new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab9 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab9", FileType.PNG),
                new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture ab0 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "ab0", FileType.PNG),
                new Vector2f(startingPosX + space, startingPosY), new Vector2f(0.03f, 0.058f));

        // FIREBALL ABILITY EXAMPLE
        GuiTexture fireAbility = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "fire", FileType.PNG),
                new Vector2f(-0.293f, startingPosY), new Vector2f(0.03f, 0.058f));
        GuiTexture cosmicAbility = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "cosmic", FileType.PNG),
                new Vector2f(-0.293f + 0.065f, startingPosY), new Vector2f(0.03f, 0.058f));

        // HEALTH AND MAGIC ENERGY BAR
        GuiTexture healthBar = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "abHealth", FileType.PNG),
                new Vector2f(0.0f, -0.72f), new Vector2f(0.33f, 0.03f));
        GuiTexture energyBar = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "abEnergy", FileType.PNG),
                new Vector2f(0.0f, -0.79f), new Vector2f(0.33f, 0.03f));

        // change variables
        space = 0f;
        spaceIncrement = 0.12f;
        startingPosX = 0.95f;
        startingPosY = -0.9f;

        // RIGHT SIDE INTERFACE (INVENTORY, ETC)
        GuiTexture rightFrame = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "fillerFrame", FileType.PNG),
                new Vector2f(startingPosX - 0.315f, -0.66f), new Vector2f(0.28f, 0.30f));
        GuiTexture fillerR1 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler", FileType.PNG),
                new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerR2 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler", FileType.PNG),
                new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerR3 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler", FileType.PNG),
                new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerR4 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler", FileType.PNG),
                new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerR5 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler", FileType.PNG),
                new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));

        // change variables
        space = 0f;
        spaceIncrement = 0.12f;
        startingPosX = -0.95f;
        startingPosY = -0.9f;

        // LEFT SIDE INTERFACE (INVENTORY, ETC)
        GuiTexture leftFrame = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "fillerFrame", FileType.PNG),
                new Vector2f(startingPosX + 0.315f, -0.66f), new Vector2f(0.28f, 0.30f));
        GuiTexture fillerL1 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler", FileType.PNG),
                new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerL2 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler", FileType.PNG),
                new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerL3 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler", FileType.PNG),
                new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerL4 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler", FileType.PNG),
                new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f));
        space += spaceIncrement;
        GuiTexture fillerL5 = new GuiTexture(loader.loadTexture(TEST_GUIS_PATH + "filler", FileType.PNG),
                new Vector2f(startingPosX, startingPosY + space), new Vector2f(0.03f, 0.058f)); // add to GUI
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
