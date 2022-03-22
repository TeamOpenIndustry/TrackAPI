package trackapi.lib;

import net.minecraft.world.phys.Vec3;

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
	 * @param rotationYaw - Current entity rotation in degrees
	 * @param bogieYaw - Current bogey rotation in degrees (set to rotationYaw if unused)
	 * @param distance - Distanced traveled in meters
	 * @param currentPosition - Current entity or bogey position
	 * @param motion
	 * @return The new position of the entity or bogey
	 */
	public Vec3 getNextPosition(Vec3 currentPosition, Vec3 motion);
}
