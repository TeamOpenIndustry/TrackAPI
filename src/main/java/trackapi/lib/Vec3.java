package trackapi.lib;

import net.minecraft.util.math.Vec3d;

/**
 * A simple vanilla Vec3d wrapper for universality
 */
public class Vec3 {
    private final Vec3d internal;

    public Vec3(double x, double y, double z) {
        this(new Vec3d(x, y, z));
    }

    public Vec3(Vec3d vec3d) {
        this.internal = vec3d;
    }

    public Vec3d toVanilla() {
        return internal;
    }
}
