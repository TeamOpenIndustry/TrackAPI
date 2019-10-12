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

	public Vec3d getTrackVector() {
		Vec3d north = new Vec3d(0, 0, 1);
		Vec3d south = new Vec3d(0, 0, -1);
		Vec3d east = new Vec3d(1, 0, 0);
		Vec3d west = new Vec3d(-1, 0, 0);
		Vec3d ascending = new Vec3d(0, 1, 0);


		switch (direction) {
			case ASCENDING_EAST:
				return east.add(ascending).normalize();
			case ASCENDING_NORTH:
				return north.add(ascending).normalize();
			case ASCENDING_SOUTH:
				return south.add(ascending).normalize();
			case ASCENDING_WEST:
				return west.add(ascending).normalize();
			case EAST_WEST:
				return east.normalize();
			case NORTH_EAST:
				return north.add(east).normalize();
			case NORTH_SOUTH:
				return north.normalize();
			case NORTH_WEST:
				return north.add(west).normalize();
			case SOUTH_EAST:
				return south.add(east).normalize();
			case SOUTH_WEST:
				return south.add(west).normalize();
            default:
				return null;
		}
	}

	public Vec3d getTrackCenter() {
		switch (direction) {
			case ASCENDING_EAST:
			case ASCENDING_NORTH:
			case ASCENDING_SOUTH:
			case ASCENDING_WEST:
				return new Vec3d(0.5, 0.5, 0.5);
			case EAST_WEST:
				return new Vec3d(0.5, 0.1, 0.5);
			case NORTH_EAST:
				return new Vec3d(0.75, 0.1, 0.25);
			case NORTH_SOUTH:
				return new Vec3d(0.5, 0.1, 0.5);
			case NORTH_WEST:
				return new Vec3d(0.25, 0.1, 0.25);
			case SOUTH_EAST:
				return new Vec3d(0.75, 0.1, 0.75);
			case SOUTH_WEST:
				return new Vec3d(0.25, 0.1, 0.75);
			default:
				return null;
		}
	}

	@Override
	public Vec3d getNextPosition(Vec3d currentPosition, Vec3d motion) {
		Vec3d trackMovement = getTrackVector();
		Vec3d trackCenter = getTrackCenter();

		Vec3d posRelativeToCenter = currentPosition.subtractReverse(new Vec3d(pos).add(trackCenter));
		double distanceToCenter = posRelativeToCenter.lengthVector();

		// Determine if trackMovement should be positive or negative as relative to block center
		boolean trackPosMotionInverted = posRelativeToCenter.distanceTo(trackMovement) < posRelativeToCenter.scale(-1).distanceTo(trackMovement);

		boolean trackMotionInverted = motion.distanceTo(trackMovement) > motion.scale(-1).distanceTo(trackMovement);

		Vec3d newPosition = new Vec3d(pos).add(trackCenter);
		//Correct new pos to track alignment
		newPosition = newPosition.add(trackMovement.scale(trackPosMotionInverted ? -distanceToCenter : distanceToCenter));
		// Move new pos along track alignment
		newPosition = newPosition.add(trackMovement.scale(trackMotionInverted ? -motion.lengthVector() : motion.lengthVector()));
		return newPosition;
	}

	public static boolean isRail(World world, BlockPos pos) {
		return BlockRailBase.isRailBlock(world, pos);
	}
	
}
