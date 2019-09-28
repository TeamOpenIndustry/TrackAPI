package trackapi.lib;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import trackapi.compat.MinecraftRail;

public class Util {
	private static ITrack getInternalBlockEntity(final World world, Vec3d pos, boolean acceptMinecraftRails) {
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
				public Vec3d getNextPosition(Vec3d currentPosition, Vec3d motion) {
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
	
	public static ITrack getBlockEntity(World world, Vec3d pos, boolean acceptMinecraftRails) {
		ITrack track = getInternalBlockEntity(world, pos, acceptMinecraftRails);
		if (track != null) {
			return track;
		}
		// Allow a bit of vertical fuzziness
		track = getInternalBlockEntity(world, pos.add(0, 0.4, 0), acceptMinecraftRails);
		if (track != null) {
			return track;
		}
		track = getInternalBlockEntity(world, pos.add(0, -0.4, 0), acceptMinecraftRails);
		if (track != null) {
			return track;
		}
		return null;
	}
}
