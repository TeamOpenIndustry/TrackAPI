package trackapi.lib;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Compatibility layer between <code>ITrack</code> and blocks which only contain tracks
 * @deprecated Use <code>ITrackV2</code> instead for forward compatibility, instances of this class will only be returned when querying <code>ITrack</code>
 */
@Deprecated
public interface ITrackBlock {

	/**
	 * The distance between the rails measured in meters
	 * 
	 * @see Gauges
	 */
	double getTrackGauge(Level world, BlockPos pos);
	
	/**
	 * Used by rolling stock to look up their next position (and related data).
	 *
	 * @param world World to query
	 * @param pos Position of the block
	 * @param currentPosition Current entity or bogey position
	 * @param motion Current velocity of entity or bogey
	 * @return Next found position on the track
	 */
	Vec3 getNextPosition(Level world, BlockPos pos, Vec3 currentPosition, Vec3 motion);
}
