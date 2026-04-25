package trackapi.lib;

import net.minecraft.util.Vec3;
import net.minecraft.world.World;

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
	double getTrackGauge(World world, int posX, int posY, int posZ);
	
	/**
	 * Used by rolling stock to look up their next position (and related data).
	 *
	 * @param world World to query
	 * @param posX X coordinate of the block
	 * @param posY Y coordinate of the block
	 * @param posZ Z coordinate of the block
	 * @param currentPosition Current entity or bogey position
	 * @param motion Current velocity of entity or bogey
	 * @return Next found position on the track
	 */
	Vec3 getNextPosition(World world, int posX, int posY, int posZ, Vec3 currentPosition, Vec3 motion);
}
