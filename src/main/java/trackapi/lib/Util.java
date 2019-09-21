package trackapi.lib;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import trackapi.compat.MinecraftRail;

public class Util {
	private static ITrack getInternalTileEntity(final World world, Vec3 pos, boolean acceptMinecraftRails) {
		int posX = (int) Math.floor(pos.xCoord);
		int posY = (int) Math.floor(pos.yCoord);
		int posZ = (int) Math.floor(pos.zCoord);

		Block bs = world.getBlock(posX, posY, posZ);
		if (bs instanceof ITrackBlock) {
			final ITrackBlock track = (ITrackBlock) bs;
			// Wrap block in ITrack
			
			return new ITrack() {
				@Override
				public double getTrackGauge() {
					return track.getTrackGauge(world, posX, posY, posZ);
				}
				@Override
				public Vec3 getNextPosition(Vec3 currentPosition, Vec3 motion) {
					return track.getNextPosition(world, posX, posY, posZ, currentPosition, motion);
				}
			};
		}
		
		TileEntity te = world.getTileEntity(posX, posY, posZ);
		if (te instanceof ITrack) {
			return (ITrack) te;
		}
		if (acceptMinecraftRails) {
			if (MinecraftRail.isRail(world, posX, posY, posZ)) {
				return new MinecraftRail(world, posX, posY, posZ);
			}
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
