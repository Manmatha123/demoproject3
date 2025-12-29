
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

import displayManager.DisplayManager;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import loader.FileType;
import loader.Loader;
import loader.MasterRenderer;
import models.RawModel;
import models.TexturedModel;
import objConverter.OBJFileLoader;
import textures.ModelTexture;

public class MainEngine {

    static final float TILE_SIZE = 200f; // or 300
    static final int GRID_SIZE = 3;

    public static void main(String[] args) {
        try {
            DisplayManager.createDisplay();

            Loader loader = new Loader();

            RawModel fieldModel = OBJFileLoader.loadOBJ("uploads_files_2708212_terrain", loader);
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
            Entity sunEntity = new Entity(sunTexture, new Vector3f(-200, 100, -200), 6,
                    0, 0, 0);

            Player player = new Player(
                    ballTexture,
                    new Vector3f(-200f, 10, -200f),
                    0, 0, 0,
                    4);

            Camera camera = new Camera(player);

            List<Entity> terrainTiles = new ArrayList<>();

            for (int x = -GRID_SIZE / 2; x <= GRID_SIZE / 2; x++) {
                for (int z = -GRID_SIZE / 2; z <= GRID_SIZE / 2; z++) {

                    Entity tile = new Entity(
                            fieldTexture,
                            new Vector3f(x * TILE_SIZE, 0, z * TILE_SIZE),
                            25,
                            0, 0, 0);

                    terrainTiles.add(tile);
                }
            }

            Light light = new Light(new Vector3f(-200, 1000000000, -200), new Vector3f(1, 1, 1),
                    new Vector3f(1.0f, 0.0001f, 0.00001f));

            MasterRenderer masterrender = new MasterRenderer();

            while (!Display.isCloseRequested()) {
                // GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                float delta = DisplayManager.getFrameTimeSeconds();

                Vector3f oldPos = new Vector3f(player.getPosition());

                player.move(delta);
                camera.move();

                boolean isCollide = false;
                for (Entity tree : treesEntity) {
                    if (player.getAABB().intersects(tree.getAABB())) {
                        isCollide = true;
                        break;
                    }
                }

                if (isCollide) {
                    player.setPosition(oldPos);
                    player.getAABB().update(oldPos, player.getSize());
                }

                for (Entity tree : treesEntity) {
                    masterrender.processEntity(tree);
                }

                for (Entity tile : terrainTiles) {
                    masterrender.processEntity(tile);
                }

                masterrender.processEntity(player);

                masterrender.render(light, camera);
                masterrender.renderSun(sunEntity, camera);
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

    private static void updateTerrain(List<Entity> tiles, Entity player) {
        for (Entity tile : tiles) {

            float dx = tile.getPosition().x - player.getPosition().x;
            float dz = tile.getPosition().z - player.getPosition().z;

            if (dx > TILE_SIZE)
                tile.getPosition().x -= TILE_SIZE * GRID_SIZE;
            if (dx < -TILE_SIZE)
                tile.getPosition().x += TILE_SIZE * GRID_SIZE;

            if (dz > TILE_SIZE)
                tile.getPosition().z -= TILE_SIZE * GRID_SIZE;
            if (dz < -TILE_SIZE)
                tile.getPosition().z += TILE_SIZE * GRID_SIZE;
        }
    }

}
