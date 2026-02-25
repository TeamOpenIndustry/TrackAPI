package trackapi.lib;

import net.minecraft.util.math.Vec3d;

/**
 * Mutable data storaging object used by stocks to query data
 */
public class PathingData {
    //Reserve position/pos name for mods that may not use MC's Vec3d
    private Vec3d vanillaPos;
    private double roll;

    public PathingData(Vec3d vanillaPos, double roll) {
        this.vanillaPos = vanillaPos;
        this.roll = roll;
    }

    public Vec3d getVanillaPos() {
        return vanillaPos;
    }

    public PathingData setVanillaPos(Vec3d vanillaPos) {
        this.vanillaPos = vanillaPos;
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
