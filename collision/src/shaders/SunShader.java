package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import toolBox.Maths;

public class SunShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/shaders/sunVertexShader.txt";
    private static final String FRAGMENT_FILE = "src/shaders/sunFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_glowColour;
    private int location_attenuation;
    private int location_lightColour;
    private int location_lightPosition;


    public SunShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        
    }

    @Override
   public  void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformlocation("transformationMatrix");
        location_projectionMatrix = super.getUniformlocation("projectionMatrix");
        location_viewMatrix = super.getUniformlocation("viewMatrix");
        location_glowColour = super.getUniformlocation("glowColour");
        location_attenuation = super.getUniformlocation("attenuation");
        location_lightPosition = super.getUniformlocation("lightPosition");
        location_lightColour = super.getUniformlocation("lightColour");
    }

    public void loadTransformation(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjection(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }



    public void loadViewMatrix(Camera camera) {
    Matrix4f viewMatrix = Maths.createViewMatrix(camera);
    super.loadMatrix(location_viewMatrix, viewMatrix);
}


    public void loadGlowColour(Vector3f colour){
        super.loadVector(location_glowColour, colour);
    }

    public void loadLight(Light light){
    super.loadVector(location_lightPosition, light.getPosition());
    super.loadVector(location_lightColour, light.getColour());
    super.loadVector(location_attenuation, light.getAttenuation());
}

}
