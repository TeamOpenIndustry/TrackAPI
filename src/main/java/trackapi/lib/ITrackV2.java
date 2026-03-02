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
     * @param inputData Mutable PathingData contains input parameters, like position and roll, and will be overridden with output data
     * @param motion Current velocity of entity or bogey
     * @param gauge Gauge of the pathing stock
     * @return True if inputData is successfully overridden, false if cannot find next path
     */
     <D extends PathingData> boolean getNextPosition(D inputData, Vec3d motion, double gauge);

    //Overrides for forward compatibility, don't use
    @Override
    @Deprecated
    default double getTrackGauge() {
        return getTrackGauges()[0];
    }

    @Override
    @Deprecated
    default Vec3d getNextPosition(Vec3d currentPosition, Vec3d motion) {
        //Create another PathingData impl may confuse user and that action is discouraged, so don't process V1 logic in V2
        PathingData data = new PathingData(currentPosition, 0d);
        if (getNextPosition(data, motion, getTrackGauge())) {
            return data.getPos();
        }
        return currentPosition;
    }
}
