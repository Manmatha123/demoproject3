package entities;

import org.lwjgl.util.vector.Vector3f;

public class light {
    private Vector3f position;
    private Vector3f colour;

    private Vector3f direction;

    public Vector3f getDirection() {
        return direction;
    }

    private float attenConstant;
    private float ambient;
    private float range;
    private float attenLinear;
    private float attenExponent;

    public float getAttenConstant() {
        return attenConstant;
    }

    public float getAmbient() {
        return ambient;
    }

    public float getRange() {
        return range;
    }

    public float getAttenLinear() {
        return attenLinear;
    }

    public float getAttenExponent() {
        return attenExponent;
    }

    public light(Vector3f position, Vector3f direction, Vector3f colour, float attenConstant, float ambient, float range, float attenLinear, float attenExponent){
        this.position = position;
        this.colour = colour;
        this.direction=direction;
        this.attenConstant=attenConstant;
        this.ambient=ambient;
        this.range=range;
        this.attenLinear=attenLinear;
        this.attenExponent=attenExponent;
    }


    public void setAttenuation(Vector3f attenuation){
        this.attenConstant=attenConstant;
    }
    public float getAttenuation(){
        return this.attenConstant;
    }
    public Vector3f getPosition(){
        return position;
    }
    public void setPosition(Vector3f position){
        this.position = position;
    }
    public Vector3f getColour(){
        return colour;
    }
    public void setColour(Vector3f colour){
        this.colour = colour;
    }
}
