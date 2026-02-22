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
     * Find next position and related data
     * @param currentPosition Current entity or bogey position
     * @param motion Current velocity of entity or bogey
     * @param gauge Gauge of the pathing stock
     * @param inputCtx The object contains other required input parameters
     * @return PathingContext
     */
    PathingContext getNextPosition(Vec3 currentPosition, Vec3 motion, double gauge, PathingContext inputCtx);

    //Overrides for forward compatibility, don't use
    @Override
    @Deprecated
    default double getTrackGauge() {
        return getTrackGauges()[0];
    }

    @Override
    @Deprecated
    default Vec3d getNextPosition(Vec3d currentPosition, Vec3d motion) {
        PathingContext ctx = new PathingContext(currentPosition, 0d);
        getNextPosition(new Vec3(currentPosition), new Vec3(motion), getTrackGauge(), ctx);
        return ctx.nextPos.toVanilla();
    }
}
