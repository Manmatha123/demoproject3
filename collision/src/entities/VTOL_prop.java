package entities;//package entities;
//
//
//import models.TextureModel;
//import org.lwjgl.input.Keyboard;
//import org.lwjgl.input.Mouse;
//import org.lwjgl.util.vector.Vector3f;
//
//public class VTOL_prop extends ControlObject_Test {
//
//    private ControlObject_Test aircraft;
//    private Vector3f position = new Vector3f(150f,60f,-461.35f);
//
//    public VTOL_prop(TextureModel model, Vector3f position, Vector3f offset, float rotX, float rotY, float rotZ, Vector3f scale, ControlObject_Test aircraft){
//        super(model, new Vector3f(aircraft.getPosition().x, aircraft.getPosition().y,aircraft.getPosition().z ),offset, rotX, rotY, rotZ, new Vector3f(scale.getX(),scale.getY(),scale.getZ()));
//        this.aircraft = aircraft;
//        this.position = aircraft.getPosition();
//    }
//
//
//    public void move(){
//
//        checkInputs();
//
//        this.setRotX(aircraft.getRotX());
////        this.setRotY(aircraft.getRotY());
//
//        this.setRotZ(aircraft.getRotZ());
//
//
//        increaseRotation(0,10,0,"cowl");
//
//    }
//
//    public void checkInputs(){
//
//   if(Mouse.isButtonDown(1)) {
//       if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
//           changeOffSet(0.00001f, 0, 0);
//       }
//       if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
//           changeOffSet(-0.00001f, 0, 0);
//       }
//       if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
//           changeOffSet(0, 0.00001f, 0);
//       }
//       if (Keyboard.isKeyDown(Keyboard.KEY_K)) {
//           changeOffSet(0, -0.00001f, 0);
//       }
//       if (Keyboard.isKeyDown(Keyboard.KEY_N)) {
//           changeOffSet(0, 0, 0.00001f);
//       }
//       if (Keyboard.isKeyDown(Keyboard.KEY_M)) {
//           changeOffSet(0, 0, -0.00001f);
//       }
//   }
//    }
//
//    public void reset_offset(){
//        this.offset = new Vector3f(0,0,0);
//    }
//
//    public void reset_angle_offset(){
//        this.angleOffSet = new Vector3f(0,0,0);
//    }
//
//
//
//    public Vector3f getPosition() {
//        return position;
//    }
//
//}
