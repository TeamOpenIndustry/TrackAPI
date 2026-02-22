package trackapi.lib;

import net.minecraft.util.math.Vec3d;

public interface ITrackV2 extends ITrack {

    /**
     * Find available gauges within this track
     *
     * @return All available gauges
     */
    double[] getTrackGauges();

    /**
     * Used by rolling stocks to look up their next position and related data
     * @param inputData PathingData contains required input parameters like current position and roll
     * @param motion Current velocity of entity or bogey
     * @param gauge Gauge of the pathing stock
     * @return PathingData contains required output data for stock
     */
    PathingData getNextPosition(PathingData inputData, Vec3 motion, double gauge);

    //Overrides for forward compatibility, don't use
    @Override
    @Deprecated
    default double getTrackGauge() {
        return getTrackGauges()[0];
    }

    @Override
    @Deprecated
    default Vec3d getNextPosition(Vec3d currentPosition, Vec3d motion) {
        PathingData ctx = new PathingData(currentPosition, 0d);
        return getNextPosition(ctx, new Vec3(motion), getTrackGauge()).position.toVanilla();
    }
}
