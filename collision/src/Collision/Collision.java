package Collision;

import org.lwjgl.util.vector.Vector3f;

public class Collision {

    public static boolean sphereAABB(Vector3f center, float radious, AABB box){

        float x=Math.max(box.min.x, Math.max(center.x, box.max.x));
        float y=Math.max(box.min.y, Math.max(center.y, box.max.y));
        float z=Math.max(box.min.z, Math.max(center.z, box.max.z));

        float dx=center.x -x;
        float dy=center.y -y;
        float dz=center.z -z;

        return (dx*dx + dy*dy + dz*dz) < (radious * radious);
    }
}
