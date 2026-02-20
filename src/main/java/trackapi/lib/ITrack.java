package trackapi.lib;

import net.minecraft.util.math.Vec3d;

public interface ITrack {
	
	/**
	 * The distance between the rails measured in meters
	 * 
	 * @see Gauges
	 */
	double getTrackGauge();
	
	/**
	 * Used by rolling stocks to look up their next position (and relative data).
	 * 
	 * @param currentPosition - Current position of entity or bogey
	 * @param motion Current velocity of entity or bogey
	 * @return PathingContext object contains related data regarding next found point
	 */
	PathingContext getNextPosition(Vec3d currentPosition, Vec3d motion);
}
