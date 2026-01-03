package renderEngine;

import de.matthiasmann.twl.utils.PNGDecoder;
import entities.Entity;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;



public class DisplayManager {

    private static int WIDTH = 1280;
    private static int HEIGHT = 720;
    private static final int FPS_CAP = 120;
    private static long lastframeTime;
    private static float delta;

    public static List<Entity> entities = new ArrayList<>();

    public static void createDisplay(){
        try{
        Display.create(new PixelFormat());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        lastframeTime = getcurrenttime();
    }

    public static void createDisplay(int x, int y, boolean resizable, boolean fullscreen){
        ContextAttribs attribs = new ContextAttribs(3,2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            DisplayMode displayMode = new DisplayMode(WIDTH, HEIGHT);

            Display.setDisplayMode(displayMode);
            Display.setResizable(resizable);
            if (fullscreen) {
                Display.setFullscreen(true);
            } else {
                Display.create(new PixelFormat(), attribs);
            }

            Display.setTitle("VU Dynamics UAV Simulator");

            setCustomIcon("resource//img.png");
            setCustomIcon("resource//right.png");

        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        GL11.glViewport(x, y, WIDTH, HEIGHT);
        lastframeTime = getcurrenttime();

    }

    public static void updateDisplay(){
        Display.sync(FPS_CAP);
        Display.update();

        if (Display.wasResized()) {
            WIDTH = Display.getWidth();
            HEIGHT = Display.getHeight();
            GL11.glViewport(0, 0, WIDTH, HEIGHT);
        }

        long currentFrameTime = getcurrenttime();
        delta = (currentFrameTime - lastframeTime) / 1000f;
        lastframeTime = currentFrameTime;
    }

    public static void setCustomIcon(String path) {
        try (InputStream in = new FileInputStream(path)) {
            PNGDecoder decoder = new PNGDecoder(in);
            ByteBuffer buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buf.flip();

            Display.setIcon(new ByteBuffer[]{buf});
        } catch (IOException e) {
            System.err.println("Error loading custom icon: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static float getframetimeseconds(){
        return delta;
    }

    public static void closeDisplay(){
        Display.destroy();
    }

    private static long getcurrenttime(){
        return Sys.getTime()*1000/Sys.getTimerResolution();
    }




}