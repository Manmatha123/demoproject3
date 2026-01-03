package toolbox;
import org.lwjgl.util.vector.Vector3f;

public class Matrix3x3 {
        public final double R1C1, R1C2, R1C3, R2C1, R2C2, R2C3, R3C1, R3C2, R3C3;
        private static float[][] elements;


        public Matrix3x3(double r1c1, double r1c2, double r1c3, double r2c1, double r2c2, double r2c3, double r3c1, double r3c2, double r3c3)
        {
            R1C1 = r1c1;    R1C2 = r1c2;    R1C3 = r1c3;
            R2C1 = r2c1;    R2C2 = r2c2;    R2C3 = r2c3;
            R3C1 = r3c1;    R3C2 = r3c2;    R3C3 = r3c3;
        }

        public double getDeterminant()
        {
            return R1C1*(R2C2*R3C3-R2C3*R3C2)-R1C2*(R2C1*R3C3-R2C3*R3C1)+R1C3*(R2C1*R3C2-R2C2*R3C1);
        }


        public Matrix3x3 getCofactorMatrix()
        {
            return new Matrix3x3
                    (
                            R2C2*R3C3-R2C3*R3C2, -(R2C1*R3C3-R2C3*R3C1), R2C1*R3C2-R2C2*R3C1,
                            -(R1C2*R3C3-R1C3*R3C2), R1C1*R3C3-R1C3*R3C1, -(R1C1*R3C2-R1C2*R3C1),
                            R1C2*R2C3-R1C3*R2C2, -(R1C1*R2C3-R1C3*R2C1), R1C1*R2C2-R1C2*R2C1
                    );
        }


        public Matrix3x3 getAdjugateMatrix()
        {
            return new Matrix3x3
                    (
                            R2C2*R3C3-R2C3*R3C2, -(R1C2*R3C3-R1C3*R3C2), R1C2*R2C3-R1C3*R2C2,
                            -(R2C1*R3C3-R2C3*R3C1), R1C1*R3C3-R1C3*R3C1, -(R1C1*R2C3-R1C3*R2C1),
                            R2C1*R3C2-R2C2*R3C1, -(R1C1*R3C2-R1C2*R3C1), R1C1*R2C2-R1C2*R2C1
                    );
        }

        public Matrix3x3 getInverse()
        {
            return Matrix3x3.multiply(getAdjugateMatrix(), 1/getDeterminant());
        }

        public static Matrix3x3 multiply(Matrix3x3 m1, Matrix3x3 m2)
        {
            return new Matrix3x3(
                    m1.R1C1*m2.R1C1 + m1.R1C2*m2.R2C1 + m1.R1C3*m2.R3C1,    m1.R1C1*m2.R1C2 + m1.R1C2*m2.R2C2 + m1.R1C3*m2.R3C2,    m1.R1C1*m2.R1C3 + m1.R1C2*m2.R2C3 + m1.R1C3*m2.R3C3,
                    m1.R2C1*m2.R1C1 + m1.R2C2*m2.R2C1 + m1.R2C3*m2.R3C1,    m1.R2C1*m2.R1C2 + m1.R2C2*m2.R2C2 + m1.R2C3*m2.R3C2,    m1.R2C1*m2.R1C3 + m1.R2C2*m2.R2C3 + m1.R2C3*m2.R3C3,
                    m1.R3C1*m2.R1C1 + m1.R3C2*m2.R2C1 + m1.R3C3*m2.R3C1,    m1.R3C1*m2.R1C2 + m1.R3C2*m2.R2C2 + m1.R3C3*m2.R3C2,    m1.R3C1*m2.R1C3 + m1.R3C2*m2.R2C3 + m1.R3C3*m2.R3C3);
        }

        public static Matrix3x3 multiply(Matrix3x3 matrix, double scalar)
        {
            return new Matrix3x3
                    (
                            matrix.R1C1*scalar, matrix.R1C2*scalar, matrix.R1C3*scalar,
                            matrix.R2C1*scalar, matrix.R2C2*scalar, matrix.R2C3*scalar,
                            matrix.R3C1*scalar, matrix.R3C2*scalar, matrix.R3C3*scalar
                    );
        }

    public void initializeElements() {
        elements = new float[3][3];
        elements[0][0] = (float) R1C1;
        elements[0][1] = (float) R1C2;
        elements[0][2] = (float) R1C3;
        elements[1][0] = (float) R2C1;
        elements[1][1] = (float) R2C2;
        elements[1][2] = (float) R2C3;
        elements[2][0] = (float) R3C1;
        elements[2][1] = (float) R3C2;
        elements[2][2] = (float) R3C3;
//        System.out.println("Matrix : ");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(elements[i][j] + " ");
            }
            System.out.println();
        }
    }

        public static Matrix3x3 rotationMatrixAxisX(double angle)
        {
            double cosAngle = Math.cos(angle);
            double sinAngle = Math.sin(angle);
            return new Matrix3x3
                    (
                            1, 0, 0,
                            0, cosAngle, -sinAngle,
                            0, sinAngle, cosAngle
                    );

        }

        public static Matrix3x3 rotationMatrixAxisY(double angle)
        {

            double cosAngle = Math.cos(angle);
            double sinAngle = Math.sin(angle);

            return new Matrix3x3
                    (
                            cosAngle, 0, sinAngle,
                            0, 1, 0,
                            -sinAngle, 0, cosAngle
                    );
        }

        public static Matrix3x3 rotationMatrixAxisZ(double angle)
        {
            double cosAngle = Math.cos(angle);
            double sinAngle = Math.sin(angle);

            return new Matrix3x3
                    (
                            cosAngle, -sinAngle, 0,
                            sinAngle, cosAngle, 0,
                            0, 0, 1
                    );
        }
    public static Vector3f transform(Matrix3x3 left, Vector3f right, Vector3f dest) {
        if (dest == null) {
            dest = new Vector3f();
        }

        float x = (float) (left.R1C1 * right.x + left.R1C2 * right.y + left.R1C3 * right.z);
        float y = (float) (left.R2C1 * right.x + left.R2C2 * right.y + left.R2C3 * right.z);
        float z = (float) (left.R3C1 * right.x + left.R3C2 * right.y + left.R3C3 * right.z);
        dest.x = x;
        dest.y = y;
        dest.z = z;
        return dest;
    }

}
