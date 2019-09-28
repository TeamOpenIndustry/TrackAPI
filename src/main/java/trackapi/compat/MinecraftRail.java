package trackapi.compat;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import trackapi.lib.Gauges;
import trackapi.lib.ITrack;

public class MinecraftRail implements ITrack {

	private RailShape direction;
	private BlockPos pos;

	public MinecraftRail(World world, BlockPos pos) {
		this.pos = pos;
		BlockState state = world.getBlockState(pos);
        AbstractRailBlock blockrailbase = (AbstractRailBlock)state.getBlock();
        this.direction = blockrailbase.getRailDirection(state, world, pos, new MinecartEntity(world, pos.getX(), pos.getY(), pos.getZ()));
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
			return currentPosition.add(motion.x > 0 ? motion.length() : -motion.length(), 0, pos.getZ() - currentPosition.z + 0.5);
		case NORTH_EAST:
			break;
		case NORTH_SOUTH:
			return currentPosition.add(pos.getX() - currentPosition.x + 0.5, 0, motion.z > 0 ? motion.length() : -motion.length());
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
		return AbstractRailBlock.isRail(world, pos);
	}
	
}
