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
	 * Used by rolling stocks to look up their next position (and related data).
	 * 
	 * @param currentPosition Current position of entity or bogey
	 * @param motion Current velocity of entity or bogey
	 * @return Next found position on the track
	 */
	Vec3d getNextPosition(Vec3d currentPosition, Vec3d motion);
}
