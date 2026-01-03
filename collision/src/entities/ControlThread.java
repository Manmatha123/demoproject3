package entities;

public class ControlThread extends Thread {
        private ControlObject_Test controlObject;

        public ControlThread(ControlObject_Test controlObject) {
            this.controlObject = controlObject;

        }

        @Override
        public void run() {
            while (true) {
//                controlObject.checkInputs();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
}

