package terrain;

import models.RawModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HeightmapLoader {

    private BufferedImage image;
    private float [][] heights;
    private int VERTEX_COUNT;

    public  HeightmapLoader(String file){
        try{
            image= ImageIO.read(new File(file));
            heights=new float[image.getWidth()][image.getHeight()];
            VERTEX_COUNT= image.getWidth();
        }catch (Exception e){
          e.printStackTrace();
        }
    }

    public float [][] getHeights(){
        return this.heights;
    }

    public float getHeight(int x, int z) {
        int rgb=image.getRGB(x,z);
        int r=(rgb>>16) & 0xFF;
        return (r/255.0f) * 10;
    }


    public RawModel generateRawModel(){
        Loader loader=new Loader();

        float [] verticesArray=new float[VERTEX_COUNT * VERTEX_COUNT *3];
        float [] textureCoordsArray=new float[VERTEX_COUNT * VERTEX_COUNT *2];
        float [] normalsArray=new float[VERTEX_COUNT * VERTEX_COUNT *3];
        int   [] indicesArray=new int[6 * (VERTEX_COUNT-1) * (VERTEX_COUNT-1)];

        int vertexPointer=0;

        for(int z=0;z<VERTEX_COUNT;z++){
            for(int x=0;x<VERTEX_COUNT;x++){

                float height=getHeight(x,z);
                heights[x][z]=height;

                verticesArray[vertexPointer *3]=x;
                verticesArray[vertexPointer *3 +1]=height;
                verticesArray[vertexPointer *3 +2]=z;

                normalsArray[vertexPointer *3]=0;
                normalsArray[vertexPointer *3 +1]=1;
                normalsArray[vertexPointer *3 +2]=0;

                textureCoordsArray[vertexPointer *2]=(float) x/ (VERTEX_COUNT-1);
                textureCoordsArray[vertexPointer *2 +1]=(float)z / (VERTEX_COUNT-1);
                vertexPointer++;
            }
        }

        int pointer=0;

        for(int z=0;z<VERTEX_COUNT-1;z++){
            for(int x=0;x<VERTEX_COUNT-1;x++){

                int topLeft= (int) ((z* VERTEX_COUNT)+x);
                int topRight=topLeft+1;
                int bottomleft=(int) (((z+1) * VERTEX_COUNT)+x);
                int bottomRight=bottomleft+1;

                indicesArray[pointer++]=topLeft;
                indicesArray[pointer++]=bottomleft;
                indicesArray[pointer++]=topRight;

                indicesArray[pointer++]=topRight;
                indicesArray[pointer++]=bottomleft;
                indicesArray[pointer++]=bottomRight;
            }
        }

        return  loader.loadToVAO(verticesArray,textureCoordsArray, normalsArray, indicesArray);
    }


}
