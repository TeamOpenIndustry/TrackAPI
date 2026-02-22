package trackapi.lib;

import net.minecraft.util.math.Vec3d;

/**
 * A simple vanilla Vec3d wrapper for universality, mutable
 */
public class Vec3 {
    private double x;
    private double y;
    private double z;

    public Vec3(Vec3d vec3d) {
        this(vec3d.x, vec3d.y, vec3d.z);
    }

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3 add(Vec3 vec3) {
        this.x += vec3.x;
        this.y += vec3.y;
        this.z += vec3.z;
        return this;
    }

    public Vec3 subtract(Vec3 vec3) {
        this.x -= vec3.x;
        this.y -= vec3.y;
        this.z -= vec3.z;
        return this;
    }

    public Vec3 scale(double factor) {
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
        return this;
    }

    public Vec3 normalize() {
        double length = this.length();
        return scale(1 / length);
    }

    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public Double distanceTo(Vec3 other) {
        return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y) + (z - other.z) * (z - other.z));
    }

    public Vec3d toVanilla() {
        return new Vec3d(this.x, this.y, this.z);
    }
}
