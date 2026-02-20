package trackapi.lib;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Compatibility layer between <code>ITrack</code> and blocks which only contain tracks
 */
public interface ITrackBlock {

	/**
	 * The distance between the rails measured in meters
	 * 
	 * @see Gauges
	 */
	double getTrackGauge(World world, BlockPos pos);
	
	/**
	 * Used by rolling stock to look up their next position.
	 *
	 * @param world World to query
	 * @param pos Position of the block
	 * @param currentPosition - Current entity or bogey position
	 * @param motion Current velocity of entity or bogey
	 * @return PathingContext object contains related data regarding next found point
	 */
	PathingContext getNextPosition(World world, BlockPos pos, Vec3d currentPosition, Vec3d motion);
}
