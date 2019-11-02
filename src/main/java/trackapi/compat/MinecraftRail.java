package trackapi.compat;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import trackapi.lib.Gauges;
import trackapi.lib.ITrack;

import java.util.HashMap;
import java.util.Map;

public class MinecraftRail implements ITrack {
	private static Map<RailShape, Vec3d> vectors = new HashMap<>();
	private static Map<RailShape, Vec3d> centers = new HashMap<>();
	static {
		Vec3d north = new Vec3d(0, 0, 1);
		Vec3d south = new Vec3d(0, 0, -1);
		Vec3d east = new Vec3d(1, 0, 0);
		Vec3d west = new Vec3d(-1, 0, 0);
		Vec3d ascending = new Vec3d(0, 1, 0);

        vectors.put(RailShape.ASCENDING_EAST, east.add(ascending).normalize());
        vectors.put(RailShape.ASCENDING_NORTH, north.add(ascending).normalize());
        vectors.put(RailShape.ASCENDING_SOUTH, south.add(ascending).normalize());
        vectors.put(RailShape.ASCENDING_WEST, west.add(ascending).normalize());
        vectors.put(RailShape.EAST_WEST, east.normalize());
        vectors.put(RailShape.NORTH_EAST, north.add(east).normalize());
        vectors.put(RailShape.NORTH_SOUTH, north.normalize());
        vectors.put(RailShape.NORTH_WEST, north.add(west).normalize());
        vectors.put(RailShape.SOUTH_EAST, south.add(east).normalize());
        vectors.put(RailShape.SOUTH_WEST, south.add(west).normalize());

        centers.put(RailShape.ASCENDING_EAST, new Vec3d(0.5, 0.5, 0.5));
        centers.put(RailShape.ASCENDING_NORTH, new Vec3d(0.5, 0.5, 0.5));
        centers.put(RailShape.ASCENDING_SOUTH, new Vec3d(0.5, 0.5, 0.5));
        centers.put(RailShape.ASCENDING_WEST, new Vec3d(0.5, 0.5, 0.5));
        centers.put(RailShape.EAST_WEST, new Vec3d(0.5, 0.1, 0.5));
        centers.put(RailShape.NORTH_EAST, new Vec3d(0.75, 0.1, 0.25));
        centers.put(RailShape.NORTH_SOUTH, new Vec3d(0.5, 0.1, 0.5));
        centers.put(RailShape.NORTH_WEST, new Vec3d(0.25, 0.1, 0.25));
        centers.put(RailShape.SOUTH_EAST, new Vec3d(0.75, 0.1, 0.75));
        centers.put(RailShape.SOUTH_WEST, new Vec3d(0.25, 0.1, 0.75));
	}


	private RailShape direction;
	private BlockPos pos;

	public MinecraftRail(World world, BlockPos pos) {
		this.pos = pos;
		BlockState state = world.getBlockState(pos);
        AbstractRailBlock blockrailbase = (AbstractRailBlock)state.getBlock();
        this.direction = state.get(blockrailbase.getShapeProperty());
	}

	@Override
	public double getTrackGauge() {
		return Gauges.MINECRAFT;
	}

	@Override
	public Vec3d getNextPosition(Vec3d currentPosition, Vec3d motion) {
		Vec3d trackMovement = vectors.get(direction);
		Vec3d trackCenter = centers.get(direction);

		Vec3d posRelativeToCenter = currentPosition.reverseSubtract(new Vec3d(pos).add(trackCenter));
		double distanceToCenter = posRelativeToCenter.length();

		// Determine if trackMovement should be positive or negative as relative to block center
		boolean trackPosMotionInverted = posRelativeToCenter.distanceTo(trackMovement) < posRelativeToCenter.multiply(-1).distanceTo(trackMovement);

		boolean trackMotionInverted = motion.distanceTo(trackMovement) > motion.multiply(-1).distanceTo(trackMovement);

		Vec3d newPosition = new Vec3d(pos).add(trackCenter);
		//Correct new pos to track alignment
		newPosition = newPosition.add(trackMovement.multiply(trackPosMotionInverted ? -distanceToCenter : distanceToCenter));
		// Move new pos along track alignment
		newPosition = newPosition.add(trackMovement.multiply(trackMotionInverted ? -motion.length() : motion.length()));
		return newPosition;
	}

	public static boolean isRail(World world, BlockPos pos) {
		return AbstractRailBlock.isRail(world, pos);
	}
	
}
