//package renderEngine;
//
//import java.io.FileInputStream;
//import java.io.FileReader;
//import java.io.InputStream;
//import java.nio.ByteBuffer;
//import java.nio.FloatBuffer;
//import java.nio.IntBuffer;
//import java.util.ArrayList;
//import java.util.List;
//
//import models.RawModel;
//
//import org.lwjgl.BufferUtils;
//import org.lwjgl.opengl.*;
//import org.newdawn.slick.opengl.PNGDecoder;
//import org.newdawn.slick.opengl.Texture;
//import org.newdawn.slick.opengl.TextureLoader;
//import textures.TextureData;
//
//import static org.newdawn.slick.opengl.PNGDecoder.RGBA;
//
//public class Loader {
//
//    private List<Integer> vaos = new ArrayList<Integer>();
//    private List<Integer> vbos = new ArrayList<Integer>();
//    private List<Integer> textures = new ArrayList<Integer>();
//
//    public RawModel loadToVAO(float[] positions,float[] textureCoords,float[] normals,int[] indices){
//        int vaoID = createVAO(); //create empty VAO
//        bindIndicesBuffer(indices);
//        storeDataInAttributeList(0,3,positions);
//        storeDataInAttributeList(1,2,textureCoords);
//        storeDataInAttributeList(2,3,normals);
//        unbindVAO();
//        return new RawModel(vaoID,indices.length);
//    }
//
//    public RawModel loadToVAO(float [] positions, int dimensions){
//        int vaoID = createVAO();
//        this.storeDataInAttributeList(0,dimensions,positions);
//        unbindVAO();
//        return new RawModel(vaoID, positions.length/dimensions);
//    }
//    public int loadTexture(String fileName) {
//        Texture texture = null;
//        try {
//            texture = TextureLoader.getTexture("PNG", new FileInputStream("resource/" + fileName + ".png"));
//            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
//            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
//            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("Tried to load texture " + fileName + ".png , didn't work");
//            System.exit(-1);
//        }
//        textures.add(texture.getTextureID());
//        return texture.getTextureID();
//    }
//
//    public void cleanUp(){
//        for(int vao:vaos){
//            GL30.glDeleteVertexArrays(vao);
//        }
//        for(int vbo:vbos){
//            GL15.glDeleteBuffers(vbo);
//        }
//        for(int texture:textures){
//            GL11.glDeleteTextures(texture);
//        } // deletes all buffers, vbos and vaos
//    }
//
//    public int loadcubemap(String[] textureFiles){
//        int textID = GL11.glGenTextures();
//        GL13.glActiveTexture(GL13.GL_TEXTURE0);
//        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textID);
//
//        for(int i = 0; i < textureFiles.length; i++){
//            TextureData data = decodeTextureFile("resource/" + textureFiles[i] + ".png");
//            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE, data.getBuffer());
//        }
//        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
//        textures.add(textID);
//        return textID;
//    }
//
//    private TextureData decodeTextureFile(String fileName){
//        int width = 0;
//        int height = 0;
//        ByteBuffer buffer = null;
//        try {
//            FileInputStream in = new FileInputStream(fileName);
//            PNGDecoder decoder = new PNGDecoder(in);
//            width = decoder.getWidth();
//            height = decoder.getHeight();
//            buffer = ByteBuffer.allocateDirect(4 * width * height);
//            decoder.decode(buffer, width * 4, RGBA);
//            buffer.flip();
//            in.close();
//        }catch (Exception e){
//            e.printStackTrace();
//            System.err.println("Tried to load texture " + fileName + ", didn't work");
//            System.exit(-1);
//        }
//        return new TextureData(buffer, width, height);
//    }
//    private int createVAO(){
//        int vaoID = GL30.glGenVertexArrays(); // creates empty VAo and returns its ID
//        vaos.add(vaoID);
//        GL30.glBindVertexArray(vaoID);
//        return vaoID;
//    }
//
//    private void storeDataInAttributeList(int attributeNumber, int coordinateSize,float[] data){
//        int vboID = GL15.glGenBuffers();//creates empty VBO and returns ID
//        vbos.add(vboID);
//        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
//        FloatBuffer buffer = storeDataInFloatBuffer(data);
//        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);//storing data
//        GL20.glVertexAttribPointer(attributeNumber,coordinateSize,GL11.GL_FLOAT,false,0,0);// takes in number of attribute list, length od vectors, type of data, normalised data or not, distance between vertex
//        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
//    }
//
//    private void unbindVAO(){
//        GL30.glBindVertexArray(0);
//    }
//
//    private void bindIndicesBuffer(int[] indices){
//        int vboID = GL15.glGenBuffers();
//        vbos.add(vboID);
//        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
//        IntBuffer buffer = storeDataInIntBuffer(indices);
//        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
//    }
//
//    private IntBuffer storeDataInIntBuffer(int[] data){
//        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
//        buffer.put(data);
//        buffer.flip();
//        return buffer;
//    }
//
//    private FloatBuffer storeDataInFloatBuffer(float[] data){
//        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length); //creates empty float buffer
//        buffer.put(data);
//        buffer.flip();//finishes writing on the buffer and prepares it to read
//        return buffer;
//    }
//
//}

package renderEngine;

import Collision.Face;
import entities.Triangle;
import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.PNGDecoder;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import textures.TextureData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static org.newdawn.slick.opengl.PNGDecoder.RGBA;

public class Loader {
    public List<Integer> temp = new ArrayList<Integer>();
    private List<Integer> vaos = new ArrayList<Integer>();
    private List<Integer> vbos = new ArrayList<Integer>();
    public Texture texture;

    public List<Integer> textures=new ArrayList<>();

    public FloatBuffer vertex;
    public FloatBuffer normal;
    public FloatBuffer text;
    public FloatBuffer color;

    public RawModel loadToVAO( float[] positions,float[] textureCoords,float[] normals,int[] indices){
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,2,textureCoords);
        storeDataInAttributeList(2,3,normals);
        unbindVAO();
        return new RawModel(vaoID,indices.length);
    }


    public int loadToVAO( float[] positions, float[] textureCoords){
        int vaoID = createVAO();
        storeDataInAttributeList(0,2,positions);
        storeDataInAttributeList(1,2,textureCoords);
        unbindVAO();
        return vaoID;
    }

    public RawModel loadToVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();
        this.storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();
        return new RawModel(vaoID, positions.length / dimensions);
    }


    public RawModel loadToVAO(List<Vector3f> vertices, float[] positions, float[] textureCoords, float[] normals, int[] indices){
        int vaoID = createVAO(); //create empty VAO
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,2,textureCoords);
        storeDataInAttributeList(2,3,normals);
        unbindVAO();
        return new RawModel(vaoID,indices.length, vertices);
    }

    public RawModel loadToVAO(List<Vector3f> vertices, float[] positions, float[] textureCoords, float[] normals, int[] indices, List<Face> f){
        int vaoID = createVAO(); //create empty VAO
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,2,textureCoords);
        storeDataInAttributeList(2,3,normals);
        unbindVAO();
        return new RawModel(vaoID,indices.length, vertices, f);
    }


    public RawModel loadToVAO(List<Vector3f> vertices, float[] positions, float[] textureCoords, float[] normals, int[] indices, List<Face> f, String filename, Vector3f min, Vector3f max, List<Triangle> triangleList){
        int vaoID = createVAO(); //create empty VAO
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,2,textureCoords);
        storeDataInAttributeList(2,3,normals);
        unbindVAO();
        return new RawModel(vaoID,indices.length, vertices, f,filename,min, max, triangleList);
    }




    public int loadTexture(String fileName) {
        Texture texture = null;

        try {
            if (fileName.contains("jpg") || fileName.contains("jpeg")) {
                texture = TextureLoader.getTexture("jpg", new FileInputStream("resource/" +fileName));// with ex:  img1.jpg
            } else if (fileName.contains("png")) {
                texture = TextureLoader.getTexture("PNG", new FileInputStream("resource/" +fileName));
            } else if (fileName.contains("gif")) {
                texture = TextureLoader.getTexture("GIF", new FileInputStream("resource/" +fileName));
            } else {
                texture = TextureLoader.getTexture("png", new FileInputStream("resource/" + fileName + ".png"));
            }

            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR); // Use mipmaps

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,GL11. GL_LINEAR);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);


            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work");
            System.exit(-1);
        }
     textures.add(texture.getTextureID());
        return texture.getTextureID();
    }

    public int loadTextureUsingMemoryMappedFile(String fileName, String memoryFileName , int width, int height) {
        File memoryFile = new File(memoryFileName);

        try {
            // Check if the memory-mapped file exists
            if (!memoryFile.exists()) {

                if(new File(fileName).exists()){
                    saveTextureToMemoryMappedFile(fileName, memoryFileName);
                }
            } else {
//                 System.out.println("memory file exists");
            }


            // Output the memory-mapped file size

            // Map the memory-mapped file into a buffer
            FileChannel channel = new RandomAccessFile(memoryFileName, "r").getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

            // Load the texture data from the memory-mapped file (not from image)
            byte[] textureData = new byte[buffer.remaining()];
            buffer.get(textureData);  // Copy the data from the buffer into the texture data array

            // Now use the texture data as before
//            int width = 8192 /* Get width from metadata or pre-saved value */;
//            int height = 8192 /* Get height from metadata or pre-saved value */;

            // Create a direct ByteBuffer for OpenGL
            ByteBuffer directBuffer = ByteBuffer.allocateDirect(textureData.length);
            directBuffer.put(textureData);
            directBuffer.flip();  // Prepare the buffer for OpenGL usage

            // Create the texture in OpenGL and bind it
            int textureID = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

            // Upload the texture data to OpenGL
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, directBuffer);

            // Generate mipmaps for the texture
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            // Set texture parameters for filtering and wrapping
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

            // Close the channel
            channel.close();

            return textureID;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;  // Return an invalid texture ID on failure
        }
    }


    public void saveTextureToMemoryMappedFile(String fileName, String memoryFileName) {
        try {

            byte[] textureData = null;  // Hypothetical method to get raw pixel data

            if (textureData == null) {
//                 System.out.println("texture data is null ");
                // If the texture is not already in RGBA format, convert it
                BufferedImage img = ImageIO.read(new File(fileName));
                int width = img.getWidth();
                int height = img.getHeight();
                textureData = convertRGBtoRGBA(img, width, height);
            }

            // Load the image
            BufferedImage img = ImageIO.read(new File(fileName));
            int width = img.getWidth();
            int height = img.getHeight();

            int expectedSize = width * height * 4;  // RGBA has 4 bytes per pixel
            if (textureData.length != expectedSize)
            {
                System.err.println("Error: Texture data size mismatch. Expected: " + expectedSize + " bytes, but got: " + textureData.length);
                return;
            }
            else{
//                 System.out.println("Texture data size mismatched .Expected: " + expectedSize + " bytes,  Got: " + textureData.length);
            }

            // Create a memory-mapped file with the correct size
            FileChannel channel = new RandomAccessFile(memoryFileName, "rw").getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, textureData.length);

            // Write the texture data to the memory-mapped file
            buffer.put(textureData);

            // Close the channel
            channel.close();
//             System.out.println("Texture data written to memory-mapped file.");

            new File(fileName).delete();




        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to save texture to memory-mapped file.");
        }
    }

    public byte[] convertRGBtoRGBA(BufferedImage img, int width, int height) {
        byte[] rgbaData = new byte[width * height * 4];
        int index = 0;

        // Iterate through the image pixels and convert each RGB pixel to RGBA
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);
                // Extract RGB values
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb) & 0xFF;
                // Add RGBA values to the byte array (alpha = 255)
                rgbaData[index++] = (byte) r;
                rgbaData[index++] = (byte) g;
                rgbaData[index++] = (byte) b;
                rgbaData[index++] = (byte) 255;  // Fully opaque
            }
        }
        return rgbaData;
    }


    public void deleteTexture(int textureID) {
        try {
            // Unbind any active texture
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

            // Delete the texture from OpenGL memory
            GL11.glDeleteTextures(textureID);


        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to delete texture " + textureID);
        }
    }


    public void cleanUp(){
        for(int vao:vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo:vbos){
            GL15.glDeleteBuffers(vbo);
        }
        for(int texture:textures){
            GL11.glDeleteTextures(texture);
        }
    }

    public int loadcubemap(String[] textureFiles){
        int textID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textID);

        for(int i = 0; i < textureFiles.length; i++){
            TextureData data = decodeTextureFile("resource/" + textureFiles[i] + ".png");
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
//        textures.add(textID);
        return textID;
    }

    private TextureData decodeTextureFile(String fileName){
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, RGBA);
            buffer.flip();
            in.close();
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work");
            System.exit(-1);
        }
        return new TextureData(buffer, width, height);
    }
    private int createVAO(){
        int vaoID = GL30.glGenVertexArrays(); // creates empty VAo and returns its ID
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }


    private void storeDataInAttributeList(int attributeNumber, int coordinateSize,float[] data){
        int vboID = GL15.glGenBuffers();//creates empty VBO and returns ID
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);//storing data
        GL20.glVertexAttribPointer(attributeNumber,coordinateSize,GL11.GL_FLOAT,false,0,0);// takes in number of attribute list, length od vectors, type of data, normalised data or not, distance between vertex
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVAO(){
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices){
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length); //creates empty float buffer
        buffer.put(data);
        buffer.flip();//finishes writing on the buffer and prepares it to read
        return buffer;
    }

}
