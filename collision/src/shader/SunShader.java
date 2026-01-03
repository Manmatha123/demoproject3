package shader;

import entities.Camera;
import entities.light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import shader.ShaderProgram;
import toolbox.Maths;

public class SunShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/shader/sunVertex.txt";
    private static final String FRAGMENT_FILE = "src/shader/sunFragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_glowColour;
    private int location_attenuation;
    private int location_lightColour;
    private int location_lightPosition;
    private int location_skyColor;
    private int location_shinedamper;
    private int location_reflectivity;


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
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_glowColour = super.getUniformLocation("glowColour");

        location_attenuation = super.getUniformLocation("attenuation");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColour = super.getUniformLocation("lightColour");
        location_skyColor = super.getUniformLocation("skycolour");
        location_shinedamper = super.getUniformLocation("shinedamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadProjection(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadskycolour(float r, float g, float b){
        super.loadVector(location_skyColor, new Vector3f(r,g,b));
    }


    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createSunViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }


    public void loadGlowColour(Vector3f colour){
        super.loadVector(location_glowColour, colour);
    }

    public void loadshinevariables(float damper, float reflectivity){
        super.loadFloat(location_shinedamper, damper);
        super.loadFloat(location_reflectivity,reflectivity);
    }

    public void loadLight(light light){
        super.loadVector(location_lightPosition, light.getPosition());
        super.loadVector(location_lightColour, light.getColour());
    }

}

