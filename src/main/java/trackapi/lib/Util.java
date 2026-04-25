package trackapi.lib;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
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

	public static <T extends ITrack> T findTrackBlocks(World world, Vector3d pos, boolean acceptMinecraftRails, Class<T> type) {
        T track = getInternalTileEntity(world, pos, acceptMinecraftRails, type);
		if (track != null) {
			return track;
		}
		// Allow a bit of vertical fuzziness
		track = getInternalTileEntity(world, pos.add(0, 0.4, 0), acceptMinecraftRails, type);
		if (track != null) {
			return track;
		}
		track = getInternalTileEntity(world, pos.add(0, -0.4, 0), acceptMinecraftRails, type);
		if (track != null) {
			return track;
		}
		return null;
	}

	//Compatibility
	@Deprecated
	public static ITrack getTileEntity(World world, Vector3d pos, boolean acceptMinecraftRails) {
		return findTrackBlocks(world, pos, acceptMinecraftRails, ITrack.class);
	}

	private static <T extends ITrack> T getInternalTileEntity(final World world, Vector3d pos, boolean acceptMinecraftRails, Class<T> type) {
		final BlockPos bp = new BlockPos(Math.floor(pos.x), Math.floor(pos.y), Math.floor(pos.z));
		BlockState bs = world.getBlockState(bp);

		if (bs.getBlock() instanceof ITrackBlock) {
			final ITrackBlock track = (ITrackBlock) bs.getBlock();
			// Wrap block in ITrack

			return type.cast(new ITrack() {
				@Override
				public double getTrackGauge() {
					return track.getTrackGauge(world, bp);
				}
				@Override
				public Vector3d getNextPosition(Vector3d currentPosition, Vector3d motion) {
					return track.getNextPosition(world, bp, currentPosition, motion);
				}
			});
		}

		TileEntity te = world.getBlockEntity(bp);
		if (type.isInstance(te)) {
			return type.cast(te);
		}
		if (acceptMinecraftRails && type.isAssignableFrom(MinecraftRail.class) && MinecraftRail.isRail(world, bp)) {
			return type.cast(new MinecraftRail(world, bp));
		}
		return null;
	}
}
