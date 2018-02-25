package trackapi.compat;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailBase.EnumRailDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import trackapi.lib.Gauges;
import trackapi.lib.ITrack;

public class MinecraftRail implements ITrack {

	private EnumRailDirection direction;
	private BlockPos pos;

	public MinecraftRail(World world, BlockPos pos) {
		this.pos = pos;
		IBlockState state = world.getBlockState(pos);
        BlockRailBase blockrailbase = (BlockRailBase)state.getBlock();
        this.direction = blockrailbase.getRailDirection(world, pos, state, null);
	}

	@Override
	public double getTrackGauge() {
		return Gauges.MINECRAFT;
	}

	@Override
	public Vec3d getNextPosition(Vec3d currentPosition, Vec3d motion) {
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
			return currentPosition.addVector(motion.xCoord > 0 ? motion.lengthVector() : -motion.lengthVector(), 0, pos.getZ() - currentPosition.zCoord + 0.5);
		case NORTH_EAST:
			break;
		case NORTH_SOUTH:
			return currentPosition.addVector(pos.getX() - currentPosition.xCoord + 0.5, 0, motion.zCoord > 0 ? motion.lengthVector() : -motion.lengthVector());
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
