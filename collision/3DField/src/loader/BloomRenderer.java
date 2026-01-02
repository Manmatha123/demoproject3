// package loader;



// import org.lwjgl.opengl.GL11;
// import org.lwjgl.opengl.GL13;
// import org.lwjgl.opengl.GL15;
// import org.lwjgl.opengl.GL20;
// import org.lwjgl.opengl.GL30;

// import loader.Loader;
// import models.RawModel;
// import shaders.BloomShader;

// public class BloomRenderer {

//     private RawModel quad;
//     private BloomShader shader;

//     public BloomRenderer(Loader loader) {
//         quad = loader.loadToVAO(
//                 new float[]{
//                         -1, 1,
//                         -1, -1,
//                         1, 1,
//                         1, -1
//                 },
//                 new float[]{
//                         0, 1,
//                         0, 0,
//                         1, 1,
//                         1, 0
//                 }
//         );

//         shader = new BloomShader();
//         shader.start();
//         shader.stop();
//     }

//     public void render(int texture) {
//         shader.start();

//         GL30.glBindVertexArray(quad.getVaoID());
//         GL20.glEnableVertexAttribArray(0);
//         GL20.glEnableVertexAttribArray(1);

//         GL13.glActiveTexture(GL13.GL_TEXTURE0);
//         GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);

//         GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());

//         GL20.glDisableVertexAttribArray(0);
//         GL20.glDisableVertexAttribArray(1);
//         GL30.glBindVertexArray(0);

//         shader.stop();
//     }
// }
