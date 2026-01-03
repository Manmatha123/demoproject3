package models;

import renderEngine.Loader;
import textures.ModelTexture;

import static renderEngine.objLoaderList.volume;

public class TextureModel {
    private RawModel rawModel;
    private ModelTexture texture;
    public String name;

    public TextureModel(RawModel model, ModelTexture texture){
        this.rawModel = model;
        this.texture = texture;
    }
    public TextureModel(TextureModel model){
        this.rawModel = model.getRawModel();
        this.texture = model.texture;
        this.name = model.name;
    }

    public TextureModel(RawModel model, ModelTexture texture,String name){
        this.rawModel = model;
        this.texture = texture;
        this.name = name;


        synchronized (model.vertex) {
            System.out.println("vertex count of raw model from texture model : " + model.vertex.size());
            System.out.println("volume of "+ model.filename + " : " +volume(model.vertex));
          //  System.out.println("volume of "+ model.filename + " : " +vol(model.vertex, model.indices));
            System.out.println( );
        }
    }


    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getTexture() {
        return texture;
    }

    public void update_texture(String path , Loader loader) {
        this.texture = new ModelTexture(loader.loadTexture(path));
    }

}
