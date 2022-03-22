package trackapi.lib;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Compatibility layer for block only tracks
 *
 */
public interface ITrackBlock {

	/**
	 * The distance between the rails measured in meters
	 * 
	 * @see Gauges#STANDARD
	 * @see Gauges#MINECRAFT
	 */
	public double getTrackGauge(Level world, BlockPos pos);
	
	/**
	 * Used by rolling stock to look up their next position.
	 * 
	 * @param currentPosition - Current entity or bogey position
	 * @param rotationYaw - Current entity rotation in degrees
	 * @param bogieYaw - Current bogey rotation in degrees (set to rotationYaw if unused)
	 * @param distance - Distanced traveled in meters
	 * @return The new position of the entity or bogey
	 */
	public Vec3 getNextPosition(Level world, BlockPos pos, Vec3 currentPosition, Vec3 motion);
}
