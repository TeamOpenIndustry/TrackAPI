package trackapi.compat;

import net.minecraft.world.phys.Vec3;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import trackapi.lib.Gauges;
import trackapi.lib.ITrack;

import java.util.HashMap;
import java.util.Map;

public class MinecraftRail implements ITrack {
	private static Map<RailShape, Vec3> vectors = new HashMap<>();
	private static Map<RailShape, Vec3> centers = new HashMap<>();
	static {
		Vec3 north = new Vec3(0, 0, 1);
		Vec3 south = new Vec3(0, 0, -1);
		Vec3 east = new Vec3(1, 0, 0);
		Vec3 west = new Vec3(-1, 0, 0);
		Vec3 ascending = new Vec3(0, 1, 0);

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

        centers.put(RailShape.ASCENDING_EAST, new Vec3(0.5, 0.5, 0.5));
        centers.put(RailShape.ASCENDING_NORTH, new Vec3(0.5, 0.5, 0.5));
        centers.put(RailShape.ASCENDING_SOUTH, new Vec3(0.5, 0.5, 0.5));
        centers.put(RailShape.ASCENDING_WEST, new Vec3(0.5, 0.5, 0.5));
        centers.put(RailShape.EAST_WEST, new Vec3(0.5, 0.1, 0.5));
        centers.put(RailShape.NORTH_EAST, new Vec3(0.75, 0.1, 0.25));
        centers.put(RailShape.NORTH_SOUTH, new Vec3(0.5, 0.1, 0.5));
        centers.put(RailShape.NORTH_WEST, new Vec3(0.25, 0.1, 0.25));
        centers.put(RailShape.SOUTH_EAST, new Vec3(0.75, 0.1, 0.75));
        centers.put(RailShape.SOUTH_WEST, new Vec3(0.25, 0.1, 0.75));
	}


	private RailShape direction;
	private BlockPos pos;

	public MinecraftRail(Level world, BlockPos pos) {
		this.pos = pos;
		BlockState state = world.getBlockState(pos);
        BaseRailBlock blockrailbase = (BaseRailBlock)state.getBlock();
        this.direction = blockrailbase.getRailDirection(state, world, pos, new Minecart(world, pos.getX(), pos.getY(), pos.getZ()));
	}

	@Override
	public double getTrackGauge() {
		return Gauges.MINECRAFT;
	}

	@Override
	public Vec3 getNextPosition(Vec3 currentPosition, Vec3 motion) {
		Vec3 trackMovement = vectors.get(direction);
		Vec3 trackCenter = centers.get(direction);

		Vec3 posRelativeToCenter = currentPosition.vectorTo(Vec3.atLowerCornerOf(pos).add(trackCenter));
		double distanceToCenter = posRelativeToCenter.length();

		// Determine if trackMovement should be positive or negative as relative to block center
		boolean trackPosMotionInverted = posRelativeToCenter.distanceTo(trackMovement) < posRelativeToCenter.scale(-1).distanceTo(trackMovement);

		boolean trackMotionInverted = motion.distanceTo(trackMovement) > motion.scale(-1).distanceTo(trackMovement);

		Vec3 newPosition = Vec3.atLowerCornerOf(pos).add(trackCenter);
		//Correct new pos to track alignment
		newPosition = newPosition.add(trackMovement.scale(trackPosMotionInverted ? -distanceToCenter : distanceToCenter));
		// Move new pos along track alignment
		newPosition = newPosition.add(trackMovement.scale(trackMotionInverted ? -motion.length() : motion.length()));
		return newPosition;
	}

	public static boolean isRail(Level world, BlockPos pos) {
		return BaseRailBlock.isRail(world, pos);
	}
	
}
