package trackapi.compat;

import net.minecraft.block.BlockRailBase;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import trackapi.lib.Gauges;
import trackapi.lib.ITrack;

import java.util.HashMap;
import java.util.Map;

public class MinecraftRail implements ITrack {

	public enum EnumRailDirection {
		NORTH_SOUTH, //(0),
		EAST_WEST, //(1),
        ASCENDING_EAST, //(2),
		ASCENDING_WEST, //(3),
        ASCENDING_NORTH, //(4),
        ASCENDING_SOUTH, //(5),
		SOUTH_EAST, //(6),
		SOUTH_WEST, //(7),
		NORTH_WEST, //(8),
		NORTH_EAST, //(9),
	}

	public static Vec3 add(Vec3 a, Vec3 b) {
		return a.addVector(b.xCoord, b.yCoord, b.zCoord);
	}

	public static Vec3 subtract(Vec3 a, Vec3 b) {
		return b.subtract(a);
	}

	public static Vec3 subtractReverse(Vec3 a, Vec3 b) {
		return a.subtract(b);
	}

	public static Vec3 scale(Vec3 a, double b) {
		return Vec3.createVectorHelper(a.xCoord * b, a.yCoord * b, a.zCoord * b);
	}

	private static Map<EnumRailDirection, Vec3> vectors = new HashMap<>();
	private static Map<EnumRailDirection, Vec3> centers = new HashMap<>();
	static {
		Vec3 north = Vec3.createVectorHelper(0, 0, 1);
		Vec3 south = Vec3.createVectorHelper(0, 0, -1);
		Vec3 east = Vec3.createVectorHelper(1, 0, 0);
		Vec3 west = Vec3.createVectorHelper(-1, 0, 0);
		Vec3 ascending = Vec3.createVectorHelper(0, 1, 0);

        vectors.put(EnumRailDirection.ASCENDING_EAST, add(east, ascending).normalize());
        vectors.put(EnumRailDirection.ASCENDING_NORTH, add(north, ascending).normalize());
        vectors.put(EnumRailDirection.ASCENDING_SOUTH, add(south, ascending).normalize());
        vectors.put(EnumRailDirection.ASCENDING_WEST, add(west, ascending).normalize());
        vectors.put(EnumRailDirection.EAST_WEST, east.normalize());
        vectors.put(EnumRailDirection.NORTH_EAST, add(north, east).normalize());
        vectors.put(EnumRailDirection.NORTH_SOUTH, north.normalize());
        vectors.put(EnumRailDirection.NORTH_WEST, add(north, west).normalize());
        vectors.put(EnumRailDirection.SOUTH_EAST, add(south, east).normalize());
        vectors.put(EnumRailDirection.SOUTH_WEST, add(south, west).normalize());

        centers.put(EnumRailDirection.ASCENDING_EAST, Vec3.createVectorHelper(0.5, 0.5, 0.5));
        centers.put(EnumRailDirection.ASCENDING_NORTH, Vec3.createVectorHelper(0.5, 0.5, 0.5));
        centers.put(EnumRailDirection.ASCENDING_SOUTH, Vec3.createVectorHelper(0.5, 0.5, 0.5));
        centers.put(EnumRailDirection.ASCENDING_WEST, Vec3.createVectorHelper(0.5, 0.5, 0.5));
        centers.put(EnumRailDirection.EAST_WEST, Vec3.createVectorHelper(0.5, 0.1, 0.5));
        centers.put(EnumRailDirection.NORTH_EAST, Vec3.createVectorHelper(0.75, 0.1, 0.25));
        centers.put(EnumRailDirection.NORTH_SOUTH, Vec3.createVectorHelper(0.5, 0.1, 0.5));
        centers.put(EnumRailDirection.NORTH_WEST, Vec3.createVectorHelper(0.25, 0.1, 0.25));
        centers.put(EnumRailDirection.SOUTH_EAST, Vec3.createVectorHelper(0.75, 0.1, 0.75));
        centers.put(EnumRailDirection.SOUTH_WEST, Vec3.createVectorHelper(0.25, 0.1, 0.75));
	}


	private EnumRailDirection direction;
	private final int posX;
	private final int posY;
	private final int posZ;

	public MinecraftRail(World world, int posX, int posY, int posZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;

        this.direction = EnumRailDirection.values()[((BlockRailBase)world.getBlock(posX, posY, posZ)).getBasicRailMetadata(world, null, posX, posY, posZ)];
	}

	@Override
	public double getTrackGauge() {
		return Gauges.MINECRAFT;
	}

	@Override
	public Vec3 getNextPosition(Vec3 currentPosition, Vec3 motion) {
		Vec3 trackMovement = vectors.get(direction);
		Vec3 trackCenter = centers.get(direction);

		Vec3 posRelativeToCenter = subtractReverse(currentPosition, add(Vec3.createVectorHelper(posX, posY, posZ), trackCenter));
		double distanceToCenter = posRelativeToCenter.lengthVector();

		// Determine if trackMovement should be positive or negative as relative to block center
		boolean trackPosMotionInverted = posRelativeToCenter.distanceTo(trackMovement) < scale(posRelativeToCenter, -1).distanceTo(trackMovement);

		boolean trackMotionInverted = motion.distanceTo(trackMovement) > scale(motion, -1).distanceTo(trackMovement);

		Vec3 newPosition = add(Vec3.createVectorHelper(posX, posY, posZ), trackCenter);
		//Correct new pos to track alignment
		newPosition = add(newPosition, scale(trackMovement, trackPosMotionInverted ? -distanceToCenter : distanceToCenter));
		// Move new pos along track alignment
		newPosition = add(newPosition, scale(trackMovement, trackMotionInverted ? -motion.lengthVector() : motion.lengthVector()));
		return newPosition;
	}

	public static boolean isRail(World world, int posX, int posY, int posZ) {
		return world.getBlock(posX, posY, posZ) instanceof BlockRailBase;
	}
	
}
