package trackapi.lib;

import net.minecraft.world.phys.Vec3;

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
     */
     <D extends PathingData> void getNextPosition(D inputData, Vec3 motion, double gauge);

    //Overrides for forward compatibility, don't use
    @Override
    @Deprecated
    default double getTrackGauge() {
        return getTrackGauges()[0];
    }

    @Override
    @Deprecated
    default Vec3 getNextPosition(Vec3 currentPosition, Vec3 motion) {
        PathingData data = new PathingData(currentPosition, 0d);
        getNextPosition(data, motion, getTrackGauge());
        return data.getPos();
    }
}
