package trackapi.lib;


import net.minecraft.util.Vec3;

/**
 * Mutable data storaging object used by stocks to query data
 */
public class PathingData {
    private Vec3 pos;
    private double roll;

    public PathingData(Vec3 pos, double roll) {
        this.pos = pos;
        this.roll = roll;
    }

    public Vec3 getPos() {
        return pos;
    }

    public PathingData setPos(Vec3 pos) {
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
