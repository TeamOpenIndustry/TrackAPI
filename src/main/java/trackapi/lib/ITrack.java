package trackapi.lib;

import net.minecraft.util.math.vector.Vector3d;

public interface ITrack {
	
	/**
	 * The distance between the rails measured in meters
	 * 
	 * @see Gauges
	 */
	double getTrackGauge();
	
	/**
	 * Used by rolling stocks to look up their next position.
	 * 
	 * @param currentPosition Current position of entity or bogey
	 * @param motion Current velocity of entity or bogey
	 * @return Next found position on the track
	 */
	Vector3d getNextPosition(Vector3d currentPosition, Vector3d motion);
}
