package trackapi.lib;

import net.minecraft.util.math.Vec3d;

/**
 * Mutable data storaging object used by stocks to query data
 */
public class PathingData {
    //Reserve position/pos name for mods that may not use MC's Vec3d
    private Vec3d vanillaPosition;
    private double roll;

    public PathingData(Vec3d vanillaPosition, double roll) {
        this.vanillaPosition = vanillaPosition;
        this.roll = roll;
    }

    public Vec3d getVanillaPos() {
        return vanillaPosition;
    }

    public PathingData setVanillaPosition(Vec3d vanillaPosition) {
        this.vanillaPosition = vanillaPosition;
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
