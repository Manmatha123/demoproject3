package Collision;

import models.RawModel;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class AABB
{
    public Vector3f min;
    public Vector3f max;

//    public Vector3f facemin;
//    public Vector3f facemax;

    private RawModel rawModel;

    public static List<RawModel> totaldata = new ArrayList<>();

    List<Vector3f> vertices = new ArrayList<>();

    public Vector3f scale(Vector3f value, Vector3f scalevalue){

        Vector3f newvalue = new Vector3f();

        newvalue.x = value.x * scalevalue.getX();
        newvalue.y = value.y * scalevalue.getY();
        newvalue.z = value.z * scalevalue.getZ();

        return  newvalue;
    }

    public  void show(){
        System.out.println("max x = "+max.x+"min x = "+min.x+"max y = "+max.y+"min.y = "+min.y+"max z = "+max.z+"min z = "+min.z);
    }

    public AABB(RawModel curmodel, Vector3f pos, Vector3f scale){

        this.rawModel = curmodel;

        vertices = curmodel.vertex;

        this.min = new Vector3f(vertices.get(0).x, vertices.get(0).y, vertices.get(0).z);
        this.max = new Vector3f(vertices.get(0).x, vertices.get(0).y, vertices.get(0).z);

        for(Vector3f ver : vertices){

            this.min = new Vector3f(
                    Math.min(this.min.x, ver.x),
                    Math.min(this.min.y, ver.y),
                    Math.min(this.min.z, ver.z)
            );

            this.max = new Vector3f(
                    Math.max(this.max.x, ver.x),
                    Math.max(this.max.y, ver.y),
                    Math.max(this.max.z, ver.z)
            );

        }
        min = scale(min, scale);
        max = scale(max,scale);

        this.min = new Vector3f(min.x + pos.x,min.y + pos.y,min.z+pos.z);
        this.max = new Vector3f(max.x + pos.x,max.y+pos.y,max.z+pos.z);
    }

    public AABB(List<RawModel> curmodel, Vector3f pos, Vector3f scale){

        this.min = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
        this.max = new Vector3f(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);

        for(RawModel curmodels : curmodel){
            this.rawModel = curmodels;

            vertices = curmodels.vertex;

            for(Vector3f ver : vertices){

                this.min = new Vector3f(
                        Math.min(this.min.x, ver.x),
                        Math.min(this.min.y, ver.y),
                        Math.min(this.min.z, ver.z)
                );

                this.max = new Vector3f(
                        Math.max(this.max.x, ver.x),
                        Math.max(this.max.y, ver.y),
                        Math.max(this.max.z, ver.z)
                );

            }

        }

        min = scale(min, scale);
        max = scale(max,scale);

        this.min = new Vector3f(min.x + pos.x,min.y + pos.y,min.z+pos.z);
        this.max = new Vector3f(max.x + pos.x,max.y+pos.y,max.z+pos.z);


    }

    public AABB(RawModel curmodel, Vector3f pos, Vector3f scale, boolean first){

        this.rawModel = curmodel;

        vertices = curmodel.vertex;
        this.min = new Vector3f(vertices.get(0).x, vertices.get(0).y, vertices.get(0).z);
        this.max = new Vector3f(vertices.get(0).x, vertices.get(0).y, vertices.get(0).z);

        for(Vector3f ver : vertices){

            this.min = new Vector3f(
                    Math.min(this.min.x, ver.x),
                    Math.min(this.min.y, ver.y),
                    Math.min(this.min.z, ver.z)
            );

            this.max = new Vector3f(
                    Math.max(this.max.x, ver.x),
                    Math.max(this.max.y, ver.y),
                    Math.max(this.max.z, ver.z)
            );

        }
        min = scale(min, scale);
        max = scale(max,scale);

        if(first)
        {
            totaldata.add(curmodel);
        }
        this.min = new Vector3f(min.x + pos.x,min.y + pos.y,min.z+pos.z);
        this.max = new Vector3f(max.x + pos.x,max.y+pos.y,max.z+pos.z);
    }

    public AABB(Face f, Vector3f scale)
    {
        for(int i = 0; i < f.vertices.size(); i++)
        {
            Vector3f point = f.vertices.get(i);

            if(i == 0)
            {
                this.min = new Vector3f(point.x, point.y, point.z);
                this.max = new Vector3f(point.x, point.y, point.z);
            }
            else
            {
                this.min = new Vector3f(
                        Math.min(this.min.x, point.x),
                        Math.min(this.min.y, point.y),
                        Math.min(this.min.z, point.z)
                );

                this.max = new Vector3f(
                        Math.max(this.max.x, point.x),
                        Math.max(this.max.y, point.y),
                        Math.max(this.max.z, point.z)
                );
            }

            min = scale(min, scale);
            max = scale(max,scale);
        }
    }

    public AABB(Vector3f position, Vector3f size){
        this.min = new Vector3f(position);
       this.max=Vector3f.add(position,size,null);
    }

    public AABB(Face f, Vector3f pos, Vector3f scale)
    {

        this.min = new Vector3f(f.vertices.get(0).x, f.vertices.get(0).y, f.vertices.get(0).z);
        this.max = new Vector3f(f.vertices.get(0).x, f.vertices.get(0).y, f.vertices.get(0).z);

        for(int i = 0; i < f.vertices.size(); i++) {
            Vector3f point = f.vertices.get(i);

            if (i == 0) {
                this.min = new Vector3f(point.x, point.y, point.z);
                this.max = new Vector3f(point.x, point.y, point.z);
            } else {
                this.min = new Vector3f(
                        Math.min(this.min.x, point.x),
                        Math.min(this.min.y, point.y),
                        Math.min(this.min.z, point.z)
                );

                this.max = new Vector3f(
                        Math.max(this.max.x, point.x),
                        Math.max(this.max.y, point.y),
                        Math.max(this.max.z, point.z)
                );
            }
        }
            min = scale(min, scale);
            max = scale(max,scale);

            this.min = new Vector3f(min.x + pos.x,min.y + pos.y,min.z+pos.z);
            this.max = new Vector3f(max.x + pos.x,max.y+pos.y,max.z+pos.z);

    }

    // Check if this AABB intersects with a ray
    public boolean intersects(Ray ray) {
        float tmin = (min.x - ray.origin.x) / ray.direction.x;
        float tmax = (max.x - ray.origin.x) / ray.direction.x;

        if (tmin > tmax) {
            float temp = tmin;
            tmin = tmax;
            tmax = temp;
        }

        float tymin = (min.y - ray.origin.y) / ray.direction.y;
        float tymax = (max.y - ray.origin.y) / ray.direction.y;

        if (tymin > tymax) {
            float temp = tymin;
            tymin = tymax;
            tymax = temp;
        }

        if ((tmin > tymax) || (tymin > tmax)) {
            return false;
        }

        if (tymin > tmin) {
            tmin = tymin;
        }

        if (tymax < tmax) {
            tmax = tymax;
        }

        float tzmin = (min.z - ray.origin.z) / ray.direction.z;
        float tzmax = (max.z - ray.origin.z) / ray.direction.z;

        if (tzmin > tzmax) {
            float temp = tzmin;
            tzmin = tzmax;
            tzmax = temp;
        }

        if ((tmin > tzmax) || (tzmin > tmax)) {
            return false;
        }

        if (tzmin > tmin) {
            tmin = tzmin;
        }

        if (tzmax < tmax) {
            tmax = tzmax;
        }

        return true;
    }


    public boolean intersects(AABB aabb) {

        return (
                        this.min.x < aabb.max.x &&
                        this.max.x > aabb.min.x &&
                        this.min.y < aabb.max.y &&
                        this.max.y > aabb.min.y &&
                        this.min.z < aabb.max.z &&
                        this.max.z > aabb.min.z
                );
    }


    public void update(Vector3f position, Vector3f size){
        this.min.set(position);
        Vector3f.add(position,size,this.max);
    }

    @Override
    public String toString(){
        return "AABB(Min("+this.min+"), Max("+this.max+"))";
    }
}
