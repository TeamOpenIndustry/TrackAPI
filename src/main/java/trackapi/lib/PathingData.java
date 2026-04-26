package trackapi.lib;

import net.minecraft.util.math.vector.Vector3d;

/**
 * Mutable data storaging object used by stocks to query data
 */
public class PathingData {
    private Vector3d pos;
    private double roll;

    public PathingData(Vector3d pos, double roll) {
        this.pos = pos;
        this.roll = roll;
    }

    public Vector3d getPos() {
        return pos;
    }

    public PathingData setPos(Vector3d pos) {
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
