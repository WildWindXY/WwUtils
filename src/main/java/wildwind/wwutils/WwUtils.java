package wildwind.wwutils;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import wildwind.wwutils.optimizations.SimpleMouseTweaks;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, clientSideOnly = true, acceptedMinecraftVersions = "[1.8,1.9)")
public class WwUtils {

    @Instance(Reference.MOD_ID)
    public static WwUtils instance;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        new SimpleMouseTweaks();
//        new ScrollableTooltips();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
