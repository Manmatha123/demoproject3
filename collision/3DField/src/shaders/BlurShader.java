package shaders;
public class BlurShader extends ShaderProgram {

    private static final String VERTEX = "src/shaders/bloomVertex.txt";
    private static final String FRAGMENT = "src/shaders/bloomFragment.txt";

    private int location_sceneTex;
    private int location_bloomTex;

    public BlurShader() {
        super(VERTEX, FRAGMENT);
    }

    @Override
    public void getAllUniformLocations() {
        location_sceneTex = getUniformlocation("sceneTex");
        location_bloomTex = getUniformlocation("bloomTex");
    }

    public void connectTextures() {
        loadInteger(location_sceneTex, 0);
        loadInteger(location_bloomTex, 1);
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }
}
