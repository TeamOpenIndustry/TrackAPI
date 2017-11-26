package trackapi.lib;

import net.minecraft.util.math.Vec3d;

public interface ITrackTile {
	
	/**
	 * The distance between the rails measured in meters
	 * 
	 * @see Util#STANDARD_GAUGE
	 * @see Util#MINECRAFT_GAUGE
	 */
	public double getTrackGauge();
	
	/**
	 * Used by rolling stock to look up their next position.
	 * 
	 * @param currentPosition - Current entity or bogey position
	 * @param rotationYaw - Current entity rotation in degrees
	 * @param bogieYaw - Current bogey rotation in degrees (set to rotationYaw if unused)
	 * @param distance - Distanced traveled in meters
	 * @return The new position of the entity or bogey
	 */
	public Vec3d getNextPosition(Vec3d currentPosition, Vec3d motion);
}
