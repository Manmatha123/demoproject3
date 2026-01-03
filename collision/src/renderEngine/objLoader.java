package renderEngine;

import Collision.Face;
import entities.Triangle;
import models.RawModel;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class objLoader {

    public static List<Face> f = new ArrayList<>();

    private static float minX=Float.POSITIVE_INFINITY;
    private static float minY=Float.POSITIVE_INFINITY;
    private static float minZ=Float.POSITIVE_INFINITY;

    private static float maxX= Float.NEGATIVE_INFINITY;
    private static float maxY= Float.NEGATIVE_INFINITY;
    private static float maxZ= Float.NEGATIVE_INFINITY;

    private static List<Triangle> triangleList=new ArrayList<>();

    public static List<RawModel> loadObjModel(String fileName, Loader loader) {


        InputStream readerStream = null;
        try {
            readerStream = new FileInputStream(fileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Model file not found: " + e.getMessage());
        }

        if (readerStream == null) {
            System.err.println("[WARN] Resource not found in classpath, trying file path: " + fileName);
            try {
                readerStream = new FileInputStream(fileName);
            } catch (FileNotFoundException e) {
                System.err.println("[ERROR] File not found: " + fileName);
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println("[DEBUG] Successfully opened stream for: " + "/"+fileName);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(readerStream));

        String line;
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();
        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] textureArray = null;
        int[] indicesArray = null;
        int facecount = 0;
        int c = 0;

        List<RawModel> ourrawmodel = new ArrayList<RawModel>();
        RawModel currentmodel;
        int usecount = 0;

        // Variables for CG calculation
        Vector3f cg = new Vector3f(0, 0, 0);
        int vertexCount = 0;

        Vector3f closestVertex = null;

        try {

            while (true) {

                Face current_face = new Face();
                line = reader.readLine();
                if (line != null) {
//                    c++;
//                    System.out.println(c);
                    String[] currentLine = line.split(" ");
                    if (line.startsWith("v ")) {
                        float x=Float.parseFloat(currentLine[1]);
                        float y=Float.parseFloat(currentLine[2]);
                        float z=Float.parseFloat(currentLine[3]);
                        Vector3f vertex = new Vector3f(x,y,z);
                        vertices.add(vertex);

                        cg.x += vertex.x;
                        cg.y += vertex.y;
                        cg.z += vertex.z;
                        vertexCount++;

                        minX=Math.min(minX,x);
                        minY=Math.min(minY,y);
                        minZ=Math.min(minZ,z);

                        maxX=Math.max(maxX,x);
                        maxY=Math.max(maxY,y);
                        maxZ=Math.max(maxZ,z);

                    } else if (line.startsWith("vt ")) {
                        Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
                                Float.parseFloat(currentLine[2]));
                        textures.add(texture);
                    } else if (line.startsWith("vn ")) {
                        Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
                                Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                        normals.add(normal);
                    } else if (line.startsWith("f ")) {
                        if (facecount == 0) {

                            textureArray = new float[(vertices.size() * 2)*2];
                            normalsArray = new float[(vertices.size() * 3)*2];

                        }
                        facecount++;

                        String[] currentLine2 = line.split(" ");
                        String[] vertex1 = currentLine2[1].split("/");
                        String[] vertex2 = currentLine2[2].split("/");
                        String[] vertex3 = currentLine2[3].split("/");

                        processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
                        processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
                        processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);

                        int currentVertexPointer1 = Integer.parseInt(vertex1[0]) - 1;
                        int currentVertexPointer2 = Integer.parseInt(vertex2[0]) - 1;
                        int currentVertexPointer3 = Integer.parseInt(vertex3[0]) - 1;

                        current_face.addface(vertices.get(currentVertexPointer1), vertices.get(currentVertexPointer2), vertices.get(currentVertexPointer3));
                        current_face.setUpAABB(new Vector3f(150,340,-1000),new Vector3f(0.02f,0.02f,0.02f));
                        f.add(current_face);


                    } else if (line.startsWith("o ") || line.startsWith("g ")) {
                        if (usecount > 0) {

                            verticesArray = new float[vertices.size()*3];
                            indicesArray = new int[indices.size()];

                            int vertexPointer = 0;
                            for(Vector3f vertex:vertices){
                                verticesArray[vertexPointer++] = vertex.x;
                                verticesArray[vertexPointer++] = vertex.y;
                                verticesArray[vertexPointer++] = vertex.z;
                            }

                            for(int i=0;i<indices.size();i+=3){
                                Triangle t=new Triangle();
                                t.v1=vertices.get(indices.get(i));
                                t.v2=vertices.get(indices.get(i+1));
                                t.v3=vertices.get(indices.get(i+2));
                                triangleList.add(t);
                            }


                            for(int i=0;i<indices.size();i++){
                                indicesArray[i] = indices.get(i);
                            }


                            Vector3f min=new Vector3f(minX, minY, minZ);
                            Vector3f max=new Vector3f(maxX, maxY, maxZ);
                            currentmodel = loader.loadToVAO(vertices, verticesArray, textureArray, normalsArray, indicesArray, f,fileName,min,max, triangleList);

                            currentmodel.filename = fileName ;
                            ourrawmodel.add(currentmodel);



                        }
                        usecount++;
                        facecount=0;
                        indices.clear();
                        f.clear();
                    }
                }
                else{
                    break;
                }
            }

            cg.x /= vertexCount;
            cg.y /= vertexCount;
            cg.z /= vertexCount;

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
                Vector3f min=new Vector3f(minX, minY, minZ);
                Vector3f max=new Vector3f(maxX, maxY, maxZ);
                currentmodel = loader.loadToVAO(vertices, verticesArray, textureArray, normalsArray, indicesArray,f,fileName,min,max,triangleList);
//                currentmodel = loader.loadToVAO(vertices, verticesArray, textureArray, normalsArray, indicesArray);
                currentmodel.filename = fileName ;
                ourrawmodel.add(currentmodel);
                f.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ourrawmodel;

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
