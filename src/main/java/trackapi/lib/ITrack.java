package trackapi.lib;

import net.minecraft.util.math.Vec3d;

public interface ITrack {
	
	/**
	 * The distance between the rails measured in meters
	 * 
	 * @see Gauges#STANDARD
	 * @see Gauges#MINECRAFT
	 */
	double getTrackGauge();
	
	/**
	 * Used by rolling stock to look up their next position.
	 * 
	 * @param currentPosition - Current entity or bogey position
	 * @return PathingContext object contains related data regarding next point found
	 */
	PathingContext getNextPosition(Vec3d currentPosition, Vec3d motion);
}
