package terrain;

import models.RawModel;

public class Terrain {

    private float [][] height;
    private RawModel rawModel;


    public Terrain(String heightMapFile){
        HeightmapLoader loader=new HeightmapLoader(heightMapFile);
        height=loader.getHeights();
        rawModel=loader.generateRawModel();
    }

    public float getHeight(int x, int z) {
        return height[x][z];
    }

    public RawModel getRawModel(){
        return this.rawModel;
    }
}
