package trackapi.compat;

import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailBase.EnumRailDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import trackapi.lib.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Wrapper for vanilla rail
 */
public class MinecraftRail implements ITrackV2 {
	private static final Map<EnumRailDirection, Vec3d> vectors = new HashMap<>();
	private static final Map<EnumRailDirection, Vec3d> centers = new HashMap<>();
	static {
		Vec3d north = new Vec3d(0, 0, 1);
		Vec3d south = new Vec3d(0, 0, -1);
		Vec3d east = new Vec3d(1, 0, 0);
		Vec3d west = new Vec3d(-1, 0, 0);
		Vec3d ascending = new Vec3d(0, 1, 0);

        vectors.put(EnumRailDirection.ASCENDING_EAST, east.add(ascending).normalize());
        vectors.put(EnumRailDirection.ASCENDING_NORTH, north.add(ascending).normalize());
        vectors.put(EnumRailDirection.ASCENDING_SOUTH, south.add(ascending).normalize());
        vectors.put(EnumRailDirection.ASCENDING_WEST, west.add(ascending).normalize());
        vectors.put(EnumRailDirection.EAST_WEST, east.normalize());
        vectors.put(EnumRailDirection.NORTH_EAST, north.add(east).normalize());
        vectors.put(EnumRailDirection.NORTH_SOUTH, north.normalize());
        vectors.put(EnumRailDirection.NORTH_WEST, north.add(west).normalize());
        vectors.put(EnumRailDirection.SOUTH_EAST, south.add(east).normalize());
        vectors.put(EnumRailDirection.SOUTH_WEST, south.add(west).normalize());

        centers.put(EnumRailDirection.ASCENDING_EAST, new Vec3d(0.5, 0.5, 0.5));
        centers.put(EnumRailDirection.ASCENDING_NORTH, new Vec3d(0.5, 0.5, 0.5));
        centers.put(EnumRailDirection.ASCENDING_SOUTH, new Vec3d(0.5, 0.5, 0.5));
        centers.put(EnumRailDirection.ASCENDING_WEST, new Vec3d(0.5, 0.5, 0.5));
        centers.put(EnumRailDirection.EAST_WEST, new Vec3d(0.5, 0.1, 0.5));
        centers.put(EnumRailDirection.NORTH_EAST, new Vec3d(0.75, 0.1, 0.25));
        centers.put(EnumRailDirection.NORTH_SOUTH, new Vec3d(0.5, 0.1, 0.5));
        centers.put(EnumRailDirection.NORTH_WEST, new Vec3d(0.25, 0.1, 0.25));
        centers.put(EnumRailDirection.SOUTH_EAST, new Vec3d(0.75, 0.1, 0.75));
        centers.put(EnumRailDirection.SOUTH_WEST, new Vec3d(0.25, 0.1, 0.75));
	}


	private final EnumRailDirection direction;
	private final BlockPos pos;

	public MinecraftRail(World world, BlockPos pos) {
		this.pos = pos;
		IBlockState state = world.getBlockState(pos);
        BlockRailBase blockrailbase = (BlockRailBase)state.getBlock();
        this.direction = blockrailbase.getRailDirection(world, pos, state, null);
	}

	@Override
	public double[] getTrackGauges() {
		return new double[]{Gauges.MINECRAFT};
	}

	@Override
	public WheelData getNextPosition(WheelData inputData, Vec3 motion, double gauge) {
		Vec3d currentPositionWrapped = inputData.position.toVanilla();
		Vec3d motionWrapped = motion.toVanilla();

		Vec3d trackMovement = vectors.get(direction);
		Vec3d trackCenter = centers.get(direction);

		Vec3d pos = new Vec3d(this.pos).add(trackCenter);
		Vec3d posRelativeToCenter = currentPositionWrapped.subtractReverse(pos);
		double distanceToCenter = posRelativeToCenter.length();

		// Determine if trackMovement should be positive or negative as relative to block center
		boolean trackPosMotionInverted = posRelativeToCenter.distanceTo(trackMovement) < posRelativeToCenter.scale(-1).distanceTo(trackMovement);

		boolean trackMotionInverted = motionWrapped.distanceTo(trackMovement) > motionWrapped.scale(-1).distanceTo(trackMovement);

		Vec3d newPosition = pos;
		//Correct new pos to track alignment
		newPosition = newPosition.add(trackMovement.scale(trackPosMotionInverted ? -distanceToCenter : distanceToCenter));
		// Move new pos along track alignment
		newPosition = newPosition.add(trackMovement.scale(trackMotionInverted ? -motionWrapped.length() : motionWrapped.length()));
		return new WheelData(newPosition, 0d).fromPrev(inputData);
	}

	public static boolean isRail(World world, BlockPos pos) {
		return BlockRailBase.isRailBlock(world, pos);
	}
}
