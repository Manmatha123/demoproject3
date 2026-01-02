
package shadows.guis;

import org.lwjgl.util.vector.Matrix4f;

import shaders.ShaderProgram;


public class GuiShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/shadows/guis/guiVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shadows/guis/guiFragmentShader.txt";
    private int location_transformationMatrix;

    public GuiShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformation(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    @Override
    public void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformlocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
