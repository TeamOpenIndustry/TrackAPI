package trackapi.lib;

import net.minecraft.util.Vec3;

public interface ITrack {
	
	/**
	 * The distance between the rails measured in meters
	 * 
	 * @see Gauges#STANDARD
	 * @see Gauges#MINECRAFT
	 */
	public double getTrackGauge();
	
	/**
	 * Used by rolling stock to look up their next position.
	 * 
	 * @param currentPosition - Current entity or bogey position
	 * @param motion - Motion over the last tick (velocity)
	 */
	public Vec3 getNextPosition(Vec3 currentPosition, Vec3 motion);
}
