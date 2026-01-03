//package renderEngine;
//
//import Collision.Face;
//import material.Material;
//import models.RawModel;
//import org.lwjgl.util.vector.Vector2f;
//import org.lwjgl.util.vector.Vector3f;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
////import utility.Model;
//
//
//public class ObjLoader5 {
//    static List<Material> finalmats = new ArrayList<Material>();
//    static List<String> usemtlnames = new ArrayList<String>();
//    private static String lastLoadedTexture = null;
//
//    public static String getLastLoadedTexture() {
//        return lastLoadedTexture;
//    }
//
//
//    public static Double offset ;
//    public static List<Face> f = new ArrayList<>();
//    public static List<RawModel> loadObjModel(String fileName, Loader loader,double offsetx, double offsetz) {
//
//        usemtlnames.clear();
//        FileReader fr = null;
//        try {
//
//
//            fr = new FileReader( fileName);
//
//        } catch (FileNotFoundException e) {
//            System.err.println("Couldn't load file!");
//            e.printStackTrace();
//        }
//        BufferedReader reader = new BufferedReader(fr);
//        String line;
//        List<Material> mats = new ArrayList<Material>();
//        List<Vector3f> vertices = new ArrayList<Vector3f>();
//        List<Vector2f> textures = new ArrayList<Vector2f>();
//        List<Vector3f> normals = new ArrayList<Vector3f>();
//        List<Integer> indices = new ArrayList<Integer>();
//        float[] verticesArray = null;
//        float[] normalsArray = null;
//        float[] textureArray = null;
//        int[] indicesArray = null;
//
//
//        int again = 0;
//
//        List<Face> facesgroup = new ArrayList<>();
//
//
//
//        List<RawModel> ourrawmodel = new ArrayList<RawModel>();
//        RawModel currentmodel;
//        int usecount = 0;
//        int facecount = 0;
//        try {
//
//            while (true) {
//                Face current_face = new Face();
//                line = reader.readLine();
//                if (line != null) {
//                    String[] currentLine = line.split(" ");
//                    if (line.startsWith("v ")) {
//                        if(again == 1){
//
//                            verticesArray = new float[vertices.size()*3];
//                            indicesArray = new int[indices.size()];
//
//                            int vertexPointer = 0;
//                            for(Vector3f vertex:vertices){
//                                verticesArray[vertexPointer++] = vertex.x;
//                                verticesArray[vertexPointer++] = vertex.y;
//                                verticesArray[vertexPointer++] = vertex.z;
//                            }
//
//                            for(int i=0;i<indices.size();i++){
//                                indicesArray[i] = indices.get(i);
//                            }
//
//                            currentmodel = loader.loadToVAO(vertices,verticesArray, textureArray, normalsArray, indicesArray,facesgroup, fileName);
//                            indices.clear();
//                            ourrawmodel.add(currentmodel);
//                            facecount = 0;
//                            usecount = 0;
//                            again = 0;
//                        }
//                        Vector3f vertex = new Vector3f((float) (Float.parseFloat(currentLine[1]) - offsetx),
//                                Float.parseFloat(currentLine[2]), (float) (Float.parseFloat(currentLine[3]) - offsetz));
//                        vertices.add(vertex);
//                    } else if (line.startsWith("vt ")) {
//                        Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
//                                Float.parseFloat(currentLine[2]));
//                        textures.add(texture);
//                    } else if (line.startsWith("vn ")) {
//                        Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
//                                Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
//                        normals.add(normal);
//                    } else if (line.startsWith("usemtl ")) {
//                        usemtlnames.add(line.split(" ")[1]);
//
//                        if(usecount>0){
//
//
//                            verticesArray = new float[vertices.size()*3];
//                            indicesArray = new int[indices.size()];
//
//                            int vertexPointer = 0;
//                            for(Vector3f vertex:vertices){
//                                verticesArray[vertexPointer++] = vertex.x;
//                                verticesArray[vertexPointer++] = vertex.y;
//                                verticesArray[vertexPointer++] = vertex.z;
//                            }
//
//                            for(int i=0;i<indices.size();i++){
//                                indicesArray[i] = indices.get(i);
//                            }
//
//
//                            currentmodel = loader.loadToVAO(vertices,verticesArray, textureArray, normalsArray, indicesArray,facesgroup, fileName);
//
//                            indices.clear();
//                            ourrawmodel.add(currentmodel);
//                            facecount = 0;
//                        }
//                        usecount++;
//
//
//                    } else if (line.startsWith("mtllib ")) {
//
//                         System.out.println("line = "+line);
//                        String mtlf = line.split("\\s")[1] ;
//
//
////                        String superior = fileName.split("\\\\")[1];
////                         System.out.println("superior : "+superior);
//////
////                        String junior = line.split(" ")[1].split("_")[0] ;
////                         System.out.println("junior : "+junior);
////
////                        if(!superior.equals(junior)){
////                            mtlf = mtlf.replace(junior,superior);
////                             System.out.println("mtlf = "+mtlf);
////                        }
//
//                        File mtlFileName = new File(mtlf);
//                        mats.addAll(parseMTLFile(mtlFileName, loader,fileName));
//                        finalmats = mats;
//                    }
//
//                    else if (line.startsWith("f ")){
//
//
//
//                        if(facecount==0){
//                            again = 1;
//                            textureArray = new float[vertices.size() * 3];
//                            normalsArray = new float[vertices.size() * 4];
//
//                        }
//                        facecount++;
////                        String[] currentLine = line.split(" ");
//                        String[] vertex1 = currentLine[1].split("/");
//                        String[] vertex2 = currentLine[2].split("/");
//                        String[] vertex3 = currentLine[3].split("/");
////
//
//                        Face f1 = new Face();
//                        f1.addface(vertices.get(Integer.parseInt(vertex1[0])-1),vertices.get(Integer.parseInt(vertex2[0])-1),vertices.get(Integer.parseInt(vertex3[0])-1));
//
//                        facesgroup.add(f1);
//
//                        processVertex(vertex1,indices,textures,normals,textureArray,normalsArray);
//                        processVertex(vertex2,indices,textures,normals,textureArray,normalsArray);
//                        processVertex(vertex3,indices,textures,normals,textureArray,normalsArray);
//
//                        int currentVertexPointer1 = Integer.parseInt(vertex1[0]) - 1;
//                        int currentVertexPointer2 = Integer.parseInt(vertex2[0]) - 1;
//                        int currentVertexPointer3 = Integer.parseInt(vertex3[0]) - 1;
//
////                        current_face.addface(vertices.get(currentVertexPointer1), vertices.get(currentVertexPointer2), vertices.get(currentVertexPointer3));
////                        current_face.setUpAABB(new Vector3f(0,0,0),1f);
////                        f.add(current_face);
//                    }
//                }
//                else{
//                    break;
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        verticesArray = new float[vertices.size()*3];
//        indicesArray = new int[indices.size()];
//
//        int vertexPointer = 0;
//        for(Vector3f vertex:vertices){
//            verticesArray[vertexPointer++] = vertex.x;
//            verticesArray[vertexPointer++] = vertex.y;
//            verticesArray[vertexPointer++] = vertex.z;
//        }
//
//        for(int i=0;i<indices.size();i++){
//            indicesArray[i] = indices.get(i);
//        }
//
//        currentmodel = loader.loadToVAO(vertices,verticesArray, textureArray, normalsArray, indicesArray, facesgroup, fileName);
//
//        indices.clear();
//
//        ourrawmodel.add(currentmodel);
//
////        System.out.println("ourrawmodel size from objloader5 = "+ourrawmodel.size());
//
//        return ourrawmodel;
//
//    }
//
//    public static List<Material> getmats(){
//        return finalmats;
//    }
//
//    public static List<String> mtlnames(){
//        return usemtlnames;
//    }
//
//    public static List<Material> parseMTLFile(File mtlFileName,Loader m, String filename){
//        List<Material> mats = new ArrayList<Material>();
//        int matNum = 0;
//        try{
//
//            File objfile = new File(filename);
//            BufferedReader mtlReader = new BufferedReader(new FileReader(new File(objfile.getParent()+"\\"+mtlFileName)));
//            Material mtl = null;
//            String line;
//            while((line = mtlReader.readLine())!=null)
//            {
//                if(line.startsWith("newmtl "))
//                {
//                    if(mtl != null)
//                        mats.add(mtl);
//
//                    //Make mtl a new material and set name and id
//                    mtl = new Material();
//                    mtl.name = line.split(" ")[1];
//                    mtl.id = -1;
//                }
//                //Specifying ambient color of material
//                else if(line.startsWith("Ka "))
//                {
//                    //Just split line up and set the ambient
//                    mtl.ambient = new Vector3f(
//                            Float.valueOf(line.split(" ")[1]),
//                            Float.valueOf(line.split(" ")[2]),
//                            Float.valueOf(line.split(" ")[3])
//
//                    );
//                }
//                //Specifying diffuse color of material
//                else if(line.startsWith("Kd "))
//                {
//                    mtl.diffuse = new Vector3f(
//                            Float.valueOf(line.split(" ")[1]),
//                            Float.valueOf(line.split(" ")[2]),
//                            Float.valueOf(line.split(" ")[3])
//                    );
//
//                }
//                //Specifying specular color of material
//                else if(line.startsWith("Ks "))
//                {
//                    mtl.specular = new Vector3f(
//                            Float.valueOf(line.split(" ")[1]),
//                            Float.valueOf(line.split(" ")[2]),
//                            Float.valueOf(line.split(" ")[3])
//                    );
//                }
//                else if(line.startsWith("Ke "))
//                {
//                    mtl.emission = new Vector3f(
//                            Float.valueOf(line.split(" ")[1]),
//                            Float.valueOf(line.split(" ")[2]),
//                            Float.valueOf(line.split(" ")[3])
//                    );
//                }
//                //Specifying coefficiant of specular color
//                //TODO add support for specular coefficiants
//                else if(line.startsWith("Ns "))
//                {
//
//                }
//                //Specifying ambient texture map
//                //TODO add support for ambient texture map
//                else if(line.startsWith("map_Ka "))
//                {
////                    matNum++;
////					System.out.println("matNum"+matNum);
//                    //Set the material's texture to specified texture
////					System.out.println("location_MAPka = "+f.getParentFile());
////					System.out.println("Working");
////                    mtl.texture = MyTextureLoader.getTexture(f.getParentFile() + File.separator + line.split(" ")[1]);
////					System.out.println("mtlt:"+f.getParentFile() + File.separator + line.split(" ")[1]);
////					System.out.println("model textures="+m.texture+","+"material texture"+mtl.texture);
////                    m.textures.add(mtl.texture);
////                    m.texture = mtl.texture;
////                    mtl.id = matNum;
////                    m.temp.add(matNum);
//                }
//                //Specifying diffuse texture map
//                else if(line.startsWith("map_Kd "))
//                {
//                    matNum++;
//                    //Set the material's texture to specified texture
////					System.out.println("location_kD = "+f.getParentFile() + File.separator + line.split(" ")[1]);
////                   mtl.texture = MyTextureLoader.getTexture( line.split(" ")[1]);
//                    mtl.imgname = line.split(" ")[1];
//                    lastLoadedTexture = line.split(" ")[1]; // Save the last texture name parsed from MTL
//
////					System.out.println("mtl texture = "+mtl.t  exture);
////					System.out.println("working  line = "+line);
////                    m.textures.add(mtl.texture);
////                    m.texture = mtl.texture;
//                    mtl.id = matNum;
//                    m.temp.add(matNum);
//                }
//                //Specifying specular texture map
//                //TODO add support for specular texture map
//                else if(line.startsWith("map_Ks "))
//                {
//
//                }
//                //Specifying highlight component map
//                //TODO add support for highlight component map
//                else if(line.startsWith("map_Ns "))
//                {
//
//                }
//            }
//            mtlReader.close();
//
//            mats.add(mtl);
//        }
//        catch(FileNotFoundException e){e.printStackTrace();}
//        catch (IOException e) {e.printStackTrace();}
//
//        return mats;
//
//    }
//    private static void processVertex(String[] vertexData, List<Integer> indices,
//                                      List<Vector2f> textures, List<Vector3f> normals, float[] textureArray,
//                                      float[] normalsArray) {
//        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
//        indices.add(currentVertexPointer);
//
//        if (vertexData.length >= 2) {
//            int textureIndex = Integer.parseInt(vertexData[1]) - 1;
//            if (textureIndex >= 0 && textureIndex < textures.size()) {
//                Vector2f currentTex = textures.get(textureIndex);
//                textureArray[currentVertexPointer * 2] = currentTex.x;
//                textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
//            }
//        }
//
//        if (vertexData.length >= 3) {
//            int normalIndex = Integer.parseInt(vertexData[2]) - 1;
//            if (normalIndex >= 0 && normalIndex < normals.size()) {
//                Vector3f currentNorm = normals.get(normalIndex);
//                normalsArray[currentVertexPointer * 3] = currentNorm.x;
//                normalsArray[currentVertexPointer * 3 + 1] = currentNorm.y;
//                normalsArray[currentVertexPointer * 3 + 2] = currentNorm.z;
//            }
//        }
//    }
//
//}
