// package loader;

// import org.lwjgl.opengl.GL11;
// import org.lwjgl.opengl.GL13;
// import org.lwjgl.opengl.GL15;
// import org.lwjgl.opengl.GL20;
// import org.lwjgl.opengl.GL30;
// import org.lwjgl.util.vector.Vector3f;

// import entities.Camera;
// import entities.Entity;
// import shaders.BlurShader;
// import shaders.SunShader;

// public class PostProcessing {

//     private int sunFBO;
//     private int sunTexture;
//     private BlurShader blurShader;

//     private int horizontalBlurTexture;
//     private int verticalBlurTexture;

//     public PostProcessing(int width, int height) {
//         // 1. Create FBO for sun
//         sunFBO = GL30.glGenFramebuffers();
//         GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, sunFBO);

//         sunTexture = GL11.glGenTextures();
//         GL11.glBindTexture(GL11.GL_TEXTURE_2D, sunTexture);
//         GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, null);
//         GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
//         GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

//         GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, sunTexture, 0);
//         GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

//         blurShader = new BlurShader();

//         // Optional: create textures for horizontal & vertical blur passes
//         horizontalBlurTexture = createEmptyTexture(width, height);
//         verticalBlurTexture = createEmptyTexture(width, height);
//     }

//     // Utility to create empty textures
//     private int createEmptyTexture(int width, int height){
//         int texture = GL11.glGenTextures();
//         GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
//         GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, null);
//         GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
//         GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//         return texture;
//     }

//         public static void enableCulling() {
//         GL11.glEnable(GL11.GL_CULL_FACE);
//         GL11.glCullFace(GL11.GL_BACK);
//     }

//     public static void disableCulling() {
//         GL11.glDisable(GL11.GL_CULL_FACE);
//     }

//     // Render sun to FBO
//     public void renderSunToFBO(SunRenderer sunRenderer, Entity sun, Camera camera, SunShader sunShader){
//         GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, sunFBO);
//         GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

//         GL11.glDisable(GL11.GL_DEPTH_TEST);
//         disableCulling();

//         sunShader.start();
//         GL11.glEnable(GL11.GL_BLEND);
//         GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

//         sunShader.loadViewMatrix(camera);
//         sunShader.loadGlowColour(new Vector3f(1.0f, 0.9f, 0.6f));
//         sunRenderer.render(sun);

//         GL11.glDisable(GL11.GL_BLEND);
//         sunShader.stop();

//         enableCulling();
//         GL11.glEnable(GL11.GL_DEPTH_TEST);

//         GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
//     }

//     // Apply bloom effect
//     public void applyBloom() {
//         // Horizontal blur
//         blurShader.start();
//         blurShader.loadDirection(1.0f, 0.0f);
//         renderFullscreenQuad(sunTexture, horizontalBlurTexture); // render FBO to horizontal texture
//         blurShader.stop();

//         // Vertical blur
//         blurShader.start();
//         blurShader.loadDirection(0.0f, 1.0f);
//         renderFullscreenQuad(horizontalBlurTexture, verticalBlurTexture); // render horizontal -> vertical
//         blurShader.stop();

//         // Additive blend to main scene
//         GL11.glEnable(GL11.GL_BLEND);
//         GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
//         renderFullscreenQuad(verticalBlurTexture); // final blurred sun
//         GL11.glDisable(GL11.GL_BLEND);
//     }

//     // Render a full-screen quad with a texture
//     private void renderFullscreenQuad(int textureID){
//         GL13.glActiveTexture(GL13.GL_TEXTURE0);
//         GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

//         // Draw quad code here (VAO + VBO for screen quad)
//     }

//     // Overloaded to render from one texture to another FBO
//     private void renderFullscreenQuad(int srcTexture, int destTexture){
//         GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, destTexture); // use destTexture's FBO
//         GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
//         renderFullscreenQuad(srcTexture);
//         GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
//     }
// }
