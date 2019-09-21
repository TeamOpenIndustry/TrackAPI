package trackapi.lib;

import net.minecraft.util.Vec3;
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
	public double getTrackGauge(World world, int posX, int posY, int posZ);
	
	/**
	 * Used by rolling stock to look up their next position.
	 * 
	 * @param currentPosition - Current entity or bogey position
	 * @param motion - Motion over the last tick (velocity)
	 * @return The new position of the entity or bogey
	 */
	public Vec3 getNextPosition(World world, int posX, int posY, int posZ, Vec3 currentPosition, Vec3 motion);
}
