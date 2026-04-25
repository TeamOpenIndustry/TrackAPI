package trackapi.compat;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.state.properties.RailShape;
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


	private final RailShape direction;
	private final BlockPos pos;

	public MinecraftRail(World world, BlockPos pos) {
		this.pos = pos;
		BlockState state = world.getBlockState(pos);
        AbstractRailBlock blockrailbase = (AbstractRailBlock)state.getBlock();
        this.direction = blockrailbase.getRailDirection(state, world, pos, new MinecartEntity(world, pos.getX(), pos.getY(), pos.getZ()));
	}

	@Override
	public double[] getTrackGauges() {
		return new double[]{Gauges.MINECRAFT};
	}

	@Override
	public<D extends PathingData> void getNextPosition(D inputData, Vec3d motion, double gauge) {
		Vec3d currentPosition = inputData.getPos();

        Vec3d trackMovement = vectors.get(direction);
		Vec3d trackCenter = centers.get(direction);

		Vec3d pos = new Vec3d(this.pos).add(trackCenter);
		Vec3d posRelativeToCenter = currentPosition.subtractReverse(pos);
		double distanceToCenter = posRelativeToCenter.length();

		// Determine if trackMovement should be positive or negative as relative to block center
		boolean trackPosMotionInverted = posRelativeToCenter.distanceTo(trackMovement) < posRelativeToCenter.scale(-1).distanceTo(trackMovement);

		boolean trackMotionInverted = motion.distanceTo(trackMovement) > motion.scale(-1).distanceTo(trackMovement);

		Vec3d newPosition = pos;
		double factor =
				//Correct new pos to track alignment
				(trackPosMotionInverted ? -distanceToCenter : distanceToCenter)
				//And Move new pos along track alignment
				+ (trackMotionInverted ? -motion.length() : motion.length());
		newPosition = newPosition.add(trackMovement.scale(factor));
		inputData.setPos(newPosition).setRoll(0d);
	}

	public static boolean isRail(World world, BlockPos pos) {
		return AbstractRailBlock.isRail(world, pos);
	}
}
