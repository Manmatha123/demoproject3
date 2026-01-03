package textures;

public class ModelTexture {

    private int textureID;
    private  float shinedamper =1;
    private float reflectivity = 0;

    private boolean hastransparency = false;

    private int numberofrows = 1;


    public int getNumberofrows() {
        return numberofrows;
    }

    public void setNumberofrows(int numberofrows) {
        this.numberofrows = numberofrows;
    }

    public boolean isHastransparency() {
        return hastransparency;
    }

    public ModelTexture(int texture){
        this.textureID = texture;
    }

    public int getID(){
        return textureID;
    }

    public float getShinedamper() {
        return shinedamper;
    }

    public void setShinedamper(float shinedamper) {
        this.shinedamper = shinedamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }
}