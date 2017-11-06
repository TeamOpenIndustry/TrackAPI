package trackapi;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = TrackAPI.MODID, version = TrackAPI.VERSION, acceptedMinecraftVersions = "[1.12,1.13)")
public class TrackAPI
{
    public static final String MODID = "trackapi";
    public static final String VERSION = "0.1";
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	System.out.println("TrackAPI Activated");
    }
}
