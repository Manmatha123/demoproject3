package entities;

    public class CameraThread extends Thread {
        private Camera camera;

        public CameraThread(ControlObject_Test p) {
            camera = new Camera(p);
        }

        @Override
        public void run() {
            while (true) {
                camera.move();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }

//        public static void camTest(ControlObject p) {
//            CameraThread cameraThread = new CameraThread(p);
//            cameraThread.start();
//        }
    }

