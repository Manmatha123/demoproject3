package renderEngine;

import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class objLoaderList {


    public static List<RawModel> loadObjModel(String fileName, Loader loader) {
        FileReader fr = null;
        try {
            fr = new FileReader("resource/" + fileName + ".obj");
        } catch (FileNotFoundException e) {
            System.err.println("Couldn't load file!");
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fr);
        String line;
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();
        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] textureArray = null;
        int[] indicesArray = null;
        int faceCount = 0;
        int c = 0;

        List<RawModel> ourRawModel = new ArrayList<RawModel>();
        RawModel currentmodel;
        int useCount = 0;
        // Variables for CG calculation
        Vector3f cg = new Vector3f(0, 0, 0);
        int vertexCount = 0;

        Vector3f closestVertex = null;
        float closestDistance = Float.MAX_VALUE;

        try {

            while (true) {

                line = reader.readLine();
                if (line != null) {
//                    c++;
//                    System.out.println(c);
                    String[] currentLine = line.split(" ");
                    if (line.startsWith("v ")) {
                        Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
                                Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                        vertices.add(vertex);

//                        cg.x += vertex.x;
//                        cg.y += vertex.y;
//                        cg.z += vertex.z;
//
//                        vertexCount++;
//
//                        float dx = vertex.x - cg.x;
//                        float dy = vertex.y - cg.y;
//                        float dz = vertex.z - cg.z;
//                        float distance = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
//
//                        if (distance < closestDistance) {
//                            closestDistance = distance;
//                        }

                    } else if (line.startsWith("vt ")) {
                        Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
                                Float.parseFloat(currentLine[2]));
                        textures.add(texture);
                    } else if (line.startsWith("vn ")) {
                        Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
                                Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                        normals.add(normal);
                    } else if (line.startsWith("f ")) {
                        if (faceCount == 0) {

                            textureArray = new float[(vertices.size() * 2)*2];
                            normalsArray = new float[(vertices.size() * 3)*2];

                        }
                        faceCount++;

                        String[] currentLine2 = line.split(" ");
                        String[] vertex1 = currentLine2[1].split("/");
                        String[] vertex2 = currentLine2[2].split("/");
                        String[] vertex3 = currentLine2[3].split("/");

                        processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
                        processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
                        processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);



                    } else if (line.startsWith("o ") || line.startsWith("g ")) {
                        if (useCount > 0) {

                            verticesArray = new float[vertices.size()*3];
                            indicesArray = new int[indices.size()];

                            int vertexPointer = 0;
                            for(Vector3f vertex:vertices){
                                verticesArray[vertexPointer++] = vertex.x;
                                verticesArray[vertexPointer++] = vertex.y;
                                verticesArray[vertexPointer++] = vertex.z;
                            }

                            for(int i=0;i<indices.size();i++){
                                indicesArray[i] = indices.get(i);
                            }

                            currentmodel = loader.loadToVAO(vertices, verticesArray, textureArray, normalsArray, indicesArray);
                            currentmodel.filename = fileName ;
                            ourRawModel.add(currentmodel);
                        }
                        useCount++;
                        faceCount=0;
                        indices.clear();
                    }
                }
                else{
                    break;
                }
            }

            reader.close();

            if ( !indices.isEmpty()) {

                verticesArray = new float[vertices.size()*3];
                indicesArray = new int[indices.size()];

                int vertexPointer = 0;
                for(Vector3f vertex:vertices){
                    verticesArray[vertexPointer++] = vertex.x;
                    verticesArray[vertexPointer++] = vertex.y;
                    verticesArray[vertexPointer++] = vertex.z;
                }

                for(int i=0;i<indices.size();i++){
                    indicesArray[i] = indices.get(i);
                }

                currentmodel = loader.loadToVAO(vertices, verticesArray, textureArray, normalsArray, indicesArray);
                currentmodel.filename = fileName ;
                ourRawModel.add(currentmodel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ourRawModel;

    }
    public static double vol(List<Vector3f> vertices, List<Integer> indices) {
        double vol = 0.0;

        for (int i = 0; i < indices.size(); i++) {
            Vector3f v0 = vertices.get(indices.get(i));
            Vector3f v1 = vertices.get(indices.get(i + 1));
            Vector3f v2 = vertices.get(indices.get(i + 2));

            Vector3f cross = crossProduct(v1, v2);
            double v = dotProduct(v0, cross);
            vol += v;
        }
        vol = Math.abs(vol) / 6.0;
        return vol;
    }

    public static double volume(List<Vector3f> vertices){

        if(vertices.isEmpty()) return 0.0;
        float minX = Float.MAX_VALUE , minY = Float.MAX_VALUE, minZ = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE , maxY = -Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;

        for(Vector3f v: vertices){
            if(v.x <minX) minX = v.x;
            if(v.y <minY) minY = v.y;
            if(v.z < minZ) minZ = v.z;

            if(v.x >maxX) maxX = v.x;
            if(v.y >maxY) maxY = v.y;
            if(v.z > maxZ) maxZ = v.z;
        }
        float width = maxX -minX;
        float height = maxY -minY;
        float depth = maxZ -minZ;

        System.out.println("width = " + width);
        System.out.println("height = " + height);
        System.out.println("depth = " + depth);

        return width*height*depth;
    }

    public static Vector3f crossProduct(Vector3f a, Vector3f b){
        return new Vector3f(
                a.y*b.z - a.z*b.y,
                a.z*b.x - a.x*b.z,
                a.x*b.y - a.y*b.x
        );
    }
    public static double dotProduct(Vector3f a, Vector3f b){
        return a.x*b.x+a.y*b.y+a.z*b.z;
    }


    private static void processVertex(String[] vertexData, List<Integer> indices,
                                      List<Vector2f> textures, List<Vector3f> normals, float[] textureArray,
                                      float[] normalsArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);

        if (vertexData.length >= 2 && !vertexData[1].isEmpty()) {
            int textureIndex = Integer.parseInt(vertexData[1].split("/")[0]) - 1;
            if (textureIndex >= 0 && textureIndex < textures.size()) {
                Vector2f currentTex = textures.get(textureIndex);
                textureArray[currentVertexPointer * 2] = currentTex.x;
                textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
            }
        }

        if (vertexData.length >= 3 && !vertexData[2].isEmpty()) {
            int normalIndex = Integer.parseInt(vertexData[2].split("/")[0]) - 1;
            if (normalIndex >= 0 && normalIndex < normals.size()) {
                Vector3f currentNorm = normals.get(normalIndex);
                normalsArray[currentVertexPointer * 3] = currentNorm.x;
                normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
                normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
            }
        }
    }


}