package shader;

import entities.Camera;
import entities.light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Maths;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class StaticShader extends  ShaderProgram{

    private static  final String VERTEX_FILE="src/shader/VertexShader.txt";
    private static  final String FRAGMENT_FILE="src/shader/FragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightposition;
    private int location_lightcolour;

    private int location_shinedamper;
    private int location_reflectivity;

    private int location_skycolour;

    private int location_offset;

    private int location_shadowMap;
    private int location_toShadowMapSpace;
    private int location_usesSpecularMap;


    private int location_modelMatrix;
    private int location_depthBiasMatrix;
    private int location_ambient;
    private int location_lightRange;
    private int location_attenConstant;
    private int location_attenLinear;
    private int location_attenExponent;
    private int location_specularMap;
    private int location_modelTexture;
    private int location_plane;

    public StaticShader(){
       super(VERTEX_FILE,FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightcolour = super.getUniformLocation("lightcolour");
        location_shinedamper = super.getUniformLocation("shinedamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_skycolour = super.getUniformLocation("skycolour");
        location_offset = super.getUniformLocation("offset");

        location_lightposition = super.getUniformLocation("lightposition");

        location_modelMatrix = super.getUniformLocation("modelMatrix");
        location_depthBiasMatrix = super.getUniformLocation("depthBiasMatrix");
        location_ambient = super.getUniformLocation("ambient");
        location_lightRange = super.getUniformLocation("lightRange");
        location_attenConstant = super.getUniformLocation("attenConstant");
        location_attenLinear = super.getUniformLocation("attenLinear");
        location_attenExponent = super.getUniformLocation("attenExponent");


        location_plane = super.getUniformLocation("plane");
        location_shadowMap = super.getUniformLocation("shadowMap");
        location_usesSpecularMap = super.getUniformLocation("attenExponent");

        location_toShadowMapSpace = super.getUniformLocation("toShadowMapSpace");

        location_specularMap = super.getUniformLocation("specularMap");
        location_modelTexture = super.getUniformLocation("modelTexture");

    }


    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoordinates");
        super.bindAttribute(2,"normal");
    }

    public void connectTextureUnits() {
        super.loadInt(location_shadowMap, 5);
        super.loadInt(location_modelTexture, 0);
        super.loadInt(location_specularMap, 1);
    }

    public void loadoffset(float x, float y){
        super.load2DVector(location_offset, new Vector2f(x,y));
    }

    public void loadskycolour(float r, float g, float b){
        super.loadVector(location_skycolour, new Vector3f(r,g,b));
    }

    public void loadshinevariables(float damper, float reflectivity){
        super.loadFloat(location_shinedamper, damper);
        super.loadFloat(location_reflectivity,reflectivity);
    }
    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadClipPlane(Vector4f plane){
        super.loadVector(location_plane, plane);
    }


    public void loadLightRange(float range){
        super.loadFloat(location_lightRange,range);
    }
    public void loadAttenConstant(float attenConstant){
        super.loadFloat(location_ambient,attenConstant);
    }
    public void loadAttenLinear(float attenLinear){
        super.loadFloat(location_attenLinear,attenLinear);
    }
    public void loadAttenExponent(float attenExponent){
        super.loadFloat(location_attenExponent,attenExponent);
    }

    public void loadAmbient(float ambient){
        super.loadFloat(location_ambient,ambient);
    }





    public void loadlight(light l){
        super.loadVector(location_lightposition, l.getPosition());
        super.loadVector(location_lightcolour, l.getColour());
        super.loadFloat(location_attenConstant,l.getAttenuation());
        super.loadFloat(location_ambient, l.getAmbient());
        super.loadFloat(location_lightRange, l.getRange());
        super.loadFloat(location_attenLinear, l.getAttenLinear());
        super.loadFloat(location_attenExponent, l.getAttenExponent());
    }

    public void loadShadowmap(int val){

    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(location_projectionMatrix, projection);
    }

    public void loadToShadowSpaceMatrix(Matrix4f matrix) {
       super.loadMatrix(location_toShadowMapSpace, matrix);
    }


    public void loadUseSpecularMap(boolean useMap) {
        super.loadBoolean(location_usesSpecularMap, useMap);
    }
}
