package collision;

import org.lwjgl.util.vector.Vector3f;

public class AABB {

    private Vector3f min;
    private Vector3f max;

    public AABB(Vector3f position, Vector3f size) {
        this.min = new Vector3f(position);
        this.max = Vector3f.add(position, size, null);
    }

    public void update(Vector3f position, Vector3f size) {
        this.min.set(position);
        Vector3f.add(position, size, this.max);
    }

    public boolean intersects(AABB other) {
        return (this.max.x > other.min.x &&
                this.min.x < other.max.x &&
                this.max.y > other.min.y &&
                this.min.y < other.max.y &&
                this.max.z > other.min.z &&
                this.min.z < other.max.z);
    }

    public Vector3f getMin() { return min; }
    public Vector3f getMax() { return max; }
}
