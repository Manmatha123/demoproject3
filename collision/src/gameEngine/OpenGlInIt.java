package gameEngine;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class OpenGlInIt {

    public static void setupOpenGL(Canvas canvas) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 0, 0, 0, 1, 1);
        GL11.glViewport(0, 0, canvas.getWidth(), canvas.getHeight());
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glClearDepth(1.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glColor3f(1,0f, 0.0f);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0f, 0f, -5f);
    }
}
