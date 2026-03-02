package trackapi.lib;

import net.minecraft.util.math.Vec3d;

/**
 * Mutable data storaging object used by stocks to query data
 */
public class PathingData {
    private Vec3d pos;
    private double roll;

    public PathingData(Vec3d pos, double roll) {
        this.pos = pos;
        this.roll = roll;
    }

    public Vec3d getPos() {
        return pos;
    }

    public PathingData setPos(Vec3d pos) {
        this.pos = pos;
        return this;
    }

    public double getRoll() {
        return roll;
    }

    public PathingData setRoll(double roll) {
        this.roll = roll;
        return this;
    }
}
