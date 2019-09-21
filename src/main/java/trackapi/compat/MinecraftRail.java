package trackapi.compat;

import net.minecraft.block.BlockRailBase;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import trackapi.lib.Gauges;
import trackapi.lib.ITrack;

public class MinecraftRail implements ITrack {

	private int direction;
	private final int posX;
	private final int posY;
	private final int posZ;

	public MinecraftRail(World world, int posX, int posY, int posZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;

        this.direction = ((BlockRailBase)world.getBlock(posX, posY, posZ)).getBasicRailMetadata(world, null, posX, posY, posZ);
	}

	@Override
	public double getTrackGauge() {
		return Gauges.MINECRAFT;
	}

	@Override
	public Vec3 getNextPosition(Vec3 currentPosition, Vec3 motion) {
		//TODO fill in the rest of the states

		switch (direction) {
        case 2://ASCENDING_EAST:
			break;
        case 4://ASCENDING_NORTH:
			break;
        case 5://ASCENDING_SOUTH:
			break;
        case 3://ASCENDING_WEST:
			break;
        case 1: //EAST_WEST:
			return currentPosition.addVector(motion.xCoord > 0 ? motion.lengthVector() : -motion.lengthVector(), 0, posZ - currentPosition.zCoord + 0.5);
		case 9://NORTH_EAST:
			break;
        case 0: //NORTH_SOUTH:
			return currentPosition.addVector(posX - currentPosition.xCoord + 0.5, 0, motion.zCoord > 0 ? motion.lengthVector() : -motion.lengthVector());
        case 8://NORTH_WEST:
			break;
		case 6://SOUTH_EAST:
			break;
		case 7://SOUTH_WEST:
			break;
		}

		return currentPosition;
	}

	public static boolean isRail(World world, int posX, int posY, int posZ) {
		return world.getBlock(posX, posY, posZ) instanceof BlockRailBase;
	}
	
}
