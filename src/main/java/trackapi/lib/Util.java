package trackapi.lib;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import trackapi.compat.MinecraftRail;

public class Util {
	private static ITrack getInternalTileEntity(final Level world, Vec3 pos, boolean acceptMinecraftRails) {
		final BlockPos bp = new BlockPos(Math.floor(pos.x), Math.floor(pos.y), Math.floor(pos.z));
		BlockState bs = world.getBlockState(bp);
		
		if (bs.getBlock() instanceof ITrackBlock) {
			final ITrackBlock track = (ITrackBlock) bs.getBlock();
			// Wrap block in ITrack
			
			return new ITrack() {
				@Override
				public double getTrackGauge() {
					return track.getTrackGauge(world, bp);
				}
				@Override
				public Vec3 getNextPosition(Vec3 currentPosition, Vec3 motion) {
					return track.getNextPosition(world, bp, currentPosition, motion);
				}
			};
		}
		
		BlockEntity te = world.getBlockEntity(bp);
		if (te instanceof ITrack) {
			return (ITrack) te;
		}
		if (acceptMinecraftRails) {
			if (MinecraftRail.isRail(world, bp)) {
				return new MinecraftRail(world, bp);
			}
		}
		return null;
	}
	
	public static ITrack getTileEntity(Level world, Vec3 pos, boolean acceptMinecraftRails) {
		ITrack track = getInternalTileEntity(world, pos, acceptMinecraftRails);
		if (track != null) {
			return track;
		}
		// Allow a bit of vertical fuzziness
		track = getInternalTileEntity(world, pos.add(0, 0.4, 0), acceptMinecraftRails);
		if (track != null) {
			return track;
		}
		track = getInternalTileEntity(world, pos.add(0, -0.4, 0), acceptMinecraftRails);
		if (track != null) {
			return track;
		}
		return null;
	}
}
