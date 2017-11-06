package trackapi.compat;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailBase.EnumRailDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import trackapi.lib.ITrackTile;
import trackapi.lib.Util;

public class MinecraftRail implements ITrackTile {

	private EnumRailDirection direction;

	public MinecraftRail(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
        BlockRailBase blockrailbase = (BlockRailBase)state.getBlock();
        this.direction = blockrailbase.getRailDirection(world, pos, state, null);
	}

	@Override
	public double getTrackGauge() {
		return Util.MINECRAFT_GAUGE;
	}

	@Override
	public double getTrackSlope() {
		return	direction == EnumRailDirection.ASCENDING_EAST ||
				direction == EnumRailDirection.ASCENDING_WEST ||
				direction == EnumRailDirection.ASCENDING_NORTH ||
				direction == EnumRailDirection.ASCENDING_SOUTH ? 1 : 0;
	}

	@Override
	public Vec3d getNextPosition(Vec3d currentPosition, float rotationYaw, float bogeyYaw, double distance) {
		if (distance < 0) {
			distance = -distance;
			rotationYaw = (rotationYaw + 180) % 360;
			bogeyYaw = (bogeyYaw + 180) % 360;
		}
		
		//TODO fill in the rest of the states
		
		switch (direction) {
		case ASCENDING_EAST:
			break;
		case ASCENDING_NORTH:
			break;
		case ASCENDING_SOUTH:
			break;
		case ASCENDING_WEST:
			break;
		case EAST_WEST:
			break;
		case NORTH_EAST:
			break;
		case NORTH_SOUTH:
			if (rotationYaw / 180 == 0) {
				return currentPosition.addVector(0, 0, distance);
			}
			return currentPosition.addVector(0, 0, -distance);
		case NORTH_WEST:
			break;
		case SOUTH_EAST:
			break;
		case SOUTH_WEST:
			break;
		}
		
		return currentPosition;
	}

	public static boolean isRail(World world, BlockPos pos) {
		return BlockRailBase.isRailBlock(world, pos);
	}
	
}
