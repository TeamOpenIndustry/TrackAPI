package trackapi.lib;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import trackapi.compat.MinecraftRail;

public class Util {
	private static ITrackTile getInternalTileEntity(World world, Vec3d pos, boolean acceptMinecraftRails) {
		BlockPos bp = new BlockPos(Math.floor(pos.xCoord), Math.floor(pos.yCoord), Math.floor(pos.zCoord));
		TileEntity te = world.getTileEntity(bp);
		if (te instanceof ITrackTile) {
			return (ITrackTile) te;
		}
		if (acceptMinecraftRails) {
			if (MinecraftRail.isRail(world, bp)) {
				return new MinecraftRail(world, bp);
			}
		}
		return null;
	}
	
	public static ITrackTile getTileEntity(World world, Vec3d pos, boolean acceptMinecraftRails) {
		ITrackTile track = getInternalTileEntity(world, pos, acceptMinecraftRails);
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
