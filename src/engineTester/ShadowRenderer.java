public class ShadowRenderer {

    private int shadowFBO;
    private int shadowMap;
    private Matrix4f lightViewMatrix;
    private Matrix4f lightProjectionMatrix;

    public ShadowRenderer() {
        createShadowFBO();
        setupLightMatrices();
    }

    public void renderShadowMap(List<Entity> entities, Light light) {
        glBindFramebuffer(GL_FRAMEBUFFER, shadowFBO);
        glViewport(0, 0, 1024, 1024);
        glClear(GL_DEPTH_BUFFER_BIT);

        glColorMask(false, false, false, false);

        for (Entity e : entities) {
            e.renderDepth(lightViewMatrix, lightProjectionMatrix);
        }

        glColorMask(true, true, true, true);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}




    // shadowRenderer.renderShadowMap(entities, light); call this at the While loop of the main class


    
