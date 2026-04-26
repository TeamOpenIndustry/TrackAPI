package trackapi.lib;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import trackapi.compat.MinecraftRail;

public class Util {

	/**
	 * Used for finding acceptable track in given world
	 * @param world World to query
	 * @param pos Current position of stock or bogey
	 * @param acceptMinecraftRails Should we take vanilla rails into consideration?
	 * @return Potential ITrack, or null if failed to find a valid one
	 */

	public static <T extends ITrack> T findTrackBlocks(Level world, Vec3 pos, boolean acceptMinecraftRails, Class<T> type) {
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
	public static ITrack getTileEntity(Level world, Vec3 pos, boolean acceptMinecraftRails) {
		return findTrackBlocks(world, pos, acceptMinecraftRails, ITrack.class);
	}

	private static <T extends ITrack> T getInternalTileEntity(final Level world, Vec3 pos, boolean acceptMinecraftRails, Class<T> type) {
		final BlockPos bp = new BlockPos((int) Math.floor(pos.x), (int) Math.floor(pos.y), (int) Math.floor(pos.z));
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
				public Vec3 getNextPosition(Vec3 currentPosition, Vec3 motion) {
					return track.getNextPosition(world, bp, currentPosition, motion);
				}
			});
		}

		BlockEntity te = world.getBlockEntity(bp);
		if (type.isInstance(te)) {
			return type.cast(te);
		}
		if (acceptMinecraftRails && type.isAssignableFrom(MinecraftRail.class) && MinecraftRail.isRail(world, bp)) {
			return type.cast(new MinecraftRail(world, bp));
		}
		return null;
	}
}
