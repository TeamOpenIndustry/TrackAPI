package trackapi.lib;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import trackapi.compat.MinecraftRail;

public class Util {

	/**
	 * Used for finding acceptable track in given world
	 * @param world World to query
	 * @param pos Current position of stock or bogey
	 * @param acceptMinecraftRails Should we take vanilla rails into consideration?
	 * @return Potential ITrack, or null if failed to find a valid one
	 */

	public static <T extends ITrack> T findTrackBlocks(World world, Vec3d pos, boolean acceptMinecraftRails, Class<T> type) {
        T track = getInternalTileEntity(world, pos, acceptMinecraftRails, type);
		if (track != null) {
			return track;
		}
		// Allow a bit of vertical fuzziness
		track = getInternalTileEntity(world, pos.addVector(0, 0.4, 0), acceptMinecraftRails, type);
		if (track != null) {
			return track;
		}
		track = getInternalTileEntity(world, pos.addVector(0, -0.4, 0), acceptMinecraftRails, type);
		if (track != null) {
			return track;
		}
		return null;
	}

	//Compatibility
	@Deprecated
	public static ITrack getTileEntity(World world, Vec3d pos, boolean acceptMinecraftRails) {
		return findTrackBlocks(world, pos, acceptMinecraftRails, ITrack.class);
	}

	private static <T extends ITrack> T getInternalTileEntity(final World world, Vec3d pos, boolean acceptMinecraftRails, Class<T> type) {
		int posX = (int) Math.floor(pos.xCoord);
		int posY = (int) Math.floor(pos.yCoord);
		int posZ = (int) Math.floor(pos.zCoord);

		Block bs = world.getBlock(posX, posY, posZ);
		if (bs instanceof ITrackBlock && type == ITrack.class) {
			final ITrackBlock track = (ITrackBlock) bs;
			// Wrap block in ITrack

			return type.cast(new ITrack() {
				@Override
				public double getTrackGauge() {
					return track.getTrackGauge(world, posX, posY, posZ);
				}
				@Override
				public Vec3 getNextPosition(Vec3 currentPosition, Vec3 motion) {
					return track.getNextPosition(world, posX, posY, posZ, currentPosition, motion);
				}
			});
		}
		
		TileEntity te = world.getTileEntity(posX, posY, posZ);
		if (type.isInstance(te)) {
			return type.cast(te);
		}
		if (acceptMinecraftRails && type.isAssignableFrom(MinecraftRail.class) && MinecraftRail.isRail(world, posX, posY, posZ)) {
			return type.cast(new MinecraftRail(world, posX, posY, posZ));
		}
		return null;
	}
	
	public static ITrack getTileEntity(World world, Vec3 pos, boolean acceptMinecraftRails) {
		ITrack track = getInternalTileEntity(world, pos, acceptMinecraftRails);
		if (track != null) {
			return track;
		}
		// Allow a bit of vertical fuzziness
		track = getInternalTileEntity(world, pos.addVector(0, 0.4, 0), acceptMinecraftRails);
		if (track != null) {
			return track;
		}
		track = getInternalTileEntity(world, pos.addVector(0, -0.4, 0), acceptMinecraftRails);
		if (track != null) {
			return track;
		}
		return null;
	}
}
