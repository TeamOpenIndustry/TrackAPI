package trackapi.lib;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
	public double getTrackGauge(World world, BlockPos pos);
	
	/**
	 * Used by rolling stock to look up their next position.
	 * 
	 * @param currentPosition - Current entity or bogey position
	 * @param rotationYaw - Current entity rotation in degrees
	 * @param bogieYaw - Current bogey rotation in degrees (set to rotationYaw if unused)
	 * @param distance - Distanced traveled in meters
	 * @return The new position of the entity or bogey
	 */
	public Vec3d getNextPosition(World world, BlockPos pos, Vec3d currentPosition, Vec3d motion);
}
