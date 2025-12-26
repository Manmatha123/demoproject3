package engineTester;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Light;
import shaders.ShaderProgram;

public public class DepthShader extends ShaderProgram {

    private int location_model;
    private int location_lightView;
    private int location_lightProjection;

    @Override
    protected void getAllUniformLocations() {
        location_model = getUniformLocation("model");
        location_lightView = getUniformLocation("lightView");
        location_lightProjection = getUniformLocation("lightProjection");
    }

    public void loadMatrices(Entity entity,
                             Matrix4f lightView,
                             Matrix4f lightProjection) {
        loadMatrix(location_model, entity.getTransformationMatrix());
        loadMatrix(location_lightView, lightView);
        loadMatrix(location_lightProjection, lightProjection);
    }
}
 {
    
}





// add this to the entity claass to  object transform + model referance
public void renderDepth(Matrix4f lightView, Matrix4f lightProjection) {
    depthShader.start();
    depthShader.loadMatrices(this, lightView, lightProjection);
    model.render(); // only positions
    depthShader.stop();
}


// Purpose: normal scene rendering

public void render(List<Entity> entities, Camera camera, Light light) {

    glActiveTexture(GL_TEXTURE1);
    glBindTexture(GL_TEXTURE_2D, shadowRenderer.getShadowMap());

    for (Entity e : entities) {
        shader.loadShadowData(
            shadowRenderer.getLightViewMatrix(),
            shadowRenderer.getLightProjectionMatrix()
        );
        renderEntity(e);
    }
}


uniform sampler2D shadowMap;
uniform mat4 lightView;
uniform mat4 lightProjection;


shader.loadInt(location_shadowMap, 1);


// depthVertex.txt
gl_Position = lightProjection * lightView * model * vec4(position, 1.0);

// depthFragment.txt

keep empty its ok