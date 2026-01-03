package toolbox;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Maths {

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
                                                      float rz, Vector3f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.getX(),scale.getY(),scale.getZ()), matrix, matrix);
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
        return matrix;
    }


    public static Matrix4f createTransformationMatrix(Vector3f translation,Vector3f offset, float rx, float ry,
                                                      float rz, Vector3f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
//        Matrix4f.translate(translation, matrix, matrix);

        Vector3f finaloffset = calculateP2(offset, 0);

//        System.out.println("intial translation : X: "+translation.x+" Y: "+translation.y+" Z: "+translation.z);
//
//        System.out.println("propeller offset : X: "+offset.x+" Y: "+offset.y+" Z: "+offset.z);
//
//        System.out.println("heading angle : "+servercreater.roty);
//
        //System.out.println("final offset : X: "+finaloffset.x+" Y: "+finaloffset.y+" Z: "+finaloffset.z);


        Matrix4f.translate(new Vector3f(finaloffset.x+translation.x,finaloffset.y+translation.y,finaloffset.z+translation.z),matrix,matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
        Matrix4f.translate(new Vector3f(-finaloffset.x,-finaloffset.y,-finaloffset.z),matrix,matrix);

        Matrix4f.scale(new Vector3f(scale.getX(),scale.getY(),scale.getZ()), matrix, matrix);

        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation,Vector3f offset,Vector3f angleoffset, float rx, float ry,
                                                      float rz, Vector3f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
//        Matrix4f.translate(translation, matrix, matrix);


//        System.out.println("intial translation : X: "+translation.x+" Y: "+translation.y+" Z: "+translation.z);

//        System.out.println("propeller offset : X: "+offset.x+" Y: "+offset.y+" Z: "+offset.z);

//        System.out.println("heading angle : "+servercreater.roty);
//
//        System.out.println("final offset : X: "+offset.x+" Y: "+offset.y+" Z: "+offset.z);
//
//        System.out.println("angle offset : X: "+angleoffset.x+" Y: "+angleoffset.y+" Z: "+angleoffset.z);


        Matrix4f.translate(new Vector3f(offset.x+translation.x,offset.y+translation.y,offset.z+translation.z),matrix,matrix);


        Matrix4f.rotate((float) Math.toRadians(ry+angleoffset.y), new Vector3f(0,1,0), matrix, matrix);
//        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);

        Matrix4f.rotate((float) Math.toRadians(rz+angleoffset.z), new Vector3f(0,0,1), matrix, matrix);

        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);

        Matrix4f.rotate((float) Math.toRadians(-angleoffset.z), new Vector3f(0,0,1), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(-angleoffset.y), new Vector3f(0,1,0), matrix, matrix);





        Matrix4f.translate(new Vector3f(-offset.x,-offset.y,-offset.z),matrix,matrix);

        Matrix4f.scale(new Vector3f(scale.getX(),scale.getY(),scale.getZ()), matrix, matrix);

        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector3f translation,Vector3f offset,Vector3f angleoffset, float rx, float ry,
                                                      float rz, Vector3f scale,String name) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
//        Matrix4f.translate(translation, matrix, matrix);


//        System.out.println("intial translation : X: "+translation.x+" Y: "+translation.y+" Z: "+translation.z);

//        System.out.println("propeller offset : X: "+offset.x+" Y: "+offset.y+" Z: "+offset.z);

//        System.out.println("heading angle : "+servercreater.roty);

//        System.out.println("final offset : X: "+offset.x+" Y: "+offset.y+" Z: "+offset.z);
//
//        System.out.println("angle offset : X: "+angleoffset.x+" Y: "+angleoffset.y+" Z: "+angleoffset.z);


        Matrix4f.translate(new Vector3f(offset.x+translation.x,offset.y+translation.y,offset.z+translation.z),matrix,matrix);



//        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);

        Matrix4f.rotate((float) Math.toRadians(rz+angleoffset.z), new Vector3f(0,0,1), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rx+angleoffset.x), new Vector3f(1,0,0), matrix, matrix);

        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);

        Matrix4f.rotate((float) Math.toRadians(-angleoffset.z), new Vector3f(0,0,1), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(-angleoffset.x), new Vector3f(1,0,0), matrix, matrix);
//        Matrix4f.rotate((float) Math.toRadians(-angleoffset.y), new Vector3f(0,1,0), matrix, matrix);





        Matrix4f.translate(new Vector3f(-offset.x,-offset.y,-offset.z),matrix,matrix);

        Matrix4f.scale(new Vector3f(scale.getX(),scale.getY(),scale.getZ()), matrix, matrix);

        return matrix;
    }

    public static Vector3f calculateP2( Vector3f p1, double theta) {
        double xc = 0;
        double yc = 0;
        double x1 = p1.x;
        double y1 = p1.z;

        // Calculate the radius (distance between center and p1)
        double r = Math.sqrt((x1 - xc) * (x1 - xc) + (y1 - yc) * (y1 - yc));

        // Calculate the angle of p1 (alpha1) with respect to the positive x-axis
        double alpha1 = Math.atan2(y1 - yc, x1 - xc);

        // Calculate the angle of p2 (alpha2)
        double alpha2 = alpha1 + theta; // Assuming counterclockwise rotation

        // Calculate p2 coordinates using polar to Cartesian conversion
        float x2 = (float) (xc + r * Math.cos(alpha2));
        float y2 = (float) (yc + r * Math.sin(alpha2));

        return new Vector3f(x2,p1.y,y2);
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix,
                viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }

    public static Matrix4f createSunViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix,
                viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
        return viewMatrix;
    }


    public static Matrix4f createViewProjectionMatrix(Matrix4f viewMatrix, Matrix4f projectionMatrix) {
        Matrix4f viewProjectionMatrix = new Matrix4f();
        Matrix4f.mul(projectionMatrix, viewMatrix, viewProjectionMatrix);
        return viewProjectionMatrix;
    }





}