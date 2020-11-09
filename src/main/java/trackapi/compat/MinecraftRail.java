package trackapi.compat;

import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.MinecartEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import trackapi.lib.Gauges;
import trackapi.lib.ITrack;

import java.util.HashMap;
import java.util.Map;

public class MinecraftRail implements ITrack {
	private static Map<RailShape, Vector3d> vectors = new HashMap<>();
	private static Map<RailShape, Vector3d> centers = new HashMap<>();
	static {
		Vector3d north = new Vector3d(0, 0, 1);
		Vector3d south = new Vector3d(0, 0, -1);
		Vector3d east = new Vector3d(1, 0, 0);
		Vector3d west = new Vector3d(-1, 0, 0);
		Vector3d ascending = new Vector3d(0, 1, 0);

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

        centers.put(RailShape.ASCENDING_EAST, new Vector3d(0.5, 0.5, 0.5));
        centers.put(RailShape.ASCENDING_NORTH, new Vector3d(0.5, 0.5, 0.5));
        centers.put(RailShape.ASCENDING_SOUTH, new Vector3d(0.5, 0.5, 0.5));
        centers.put(RailShape.ASCENDING_WEST, new Vector3d(0.5, 0.5, 0.5));
        centers.put(RailShape.EAST_WEST, new Vector3d(0.5, 0.1, 0.5));
        centers.put(RailShape.NORTH_EAST, new Vector3d(0.75, 0.1, 0.25));
        centers.put(RailShape.NORTH_SOUTH, new Vector3d(0.5, 0.1, 0.5));
        centers.put(RailShape.NORTH_WEST, new Vector3d(0.25, 0.1, 0.25));
        centers.put(RailShape.SOUTH_EAST, new Vector3d(0.75, 0.1, 0.75));
        centers.put(RailShape.SOUTH_WEST, new Vector3d(0.25, 0.1, 0.75));
	}


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
	public Vector3d getNextPosition(Vector3d currentPosition, Vector3d motion) {
		Vector3d trackMovement = vectors.get(direction);
		Vector3d trackCenter = centers.get(direction);

		Vector3d posRelativeToCenter = currentPosition.subtractReverse(Vector3d.copy(pos).add(trackCenter));
		double distanceToCenter = posRelativeToCenter.length();

		// Determine if trackMovement should be positive or negative as relative to block center
		boolean trackPosMotionInverted = posRelativeToCenter.distanceTo(trackMovement) < posRelativeToCenter.scale(-1).distanceTo(trackMovement);

		boolean trackMotionInverted = motion.distanceTo(trackMovement) > motion.scale(-1).distanceTo(trackMovement);

		Vector3d newPosition = Vector3d.copy(pos).add(trackCenter);
		//Correct new pos to track alignment
		newPosition = newPosition.add(trackMovement.scale(trackPosMotionInverted ? -distanceToCenter : distanceToCenter));
		// Move new pos along track alignment
		newPosition = newPosition.add(trackMovement.scale(trackMotionInverted ? -motion.length() : motion.length()));
		return newPosition;
	}

	public static boolean isRail(World world, BlockPos pos) {
		return AbstractRailBlock.isRail(world, pos);
	}
	
}
