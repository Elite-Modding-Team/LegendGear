package mod.emt.legendgear;

import mod.emt.legendgear.proxy.CommonProxy;
import net.minecraftforge.fml.common.SidedProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import mod.emt.legendgear.util.LGCreativeTab;

@Mod(modid = LegendGear.MOD_ID, name = LegendGear.NAME, version = LegendGear.VERSION, acceptedMinecraftVersions = LegendGear.ACCEPTED_VERSIONS, dependencies = LegendGear.DEPENDENCIES)
public class LegendGear
{
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String NAME = Tags.MOD_NAME;
    public static final String VERSION = Tags.VERSION;
    public static final String ACCEPTED_VERSIONS = "[1.12.2]";
    public static final String DEPENDENCIES = "required-after:baubles";
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    public static final String CLIENT_PROXY = "mod.emt.legendgear.proxy.ClientProxy";
    public static final String COMMON_PROXY = "mod.emt.legendgear.proxy.CommonProxy";

    public static final CreativeTabs TAB = new LGCreativeTab(CreativeTabs.CREATIVE_TAB_ARRAY.length, MOD_ID + ".tab");

    @Mod.Instance
    public static LegendGear instance;

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = COMMON_PROXY)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER.info(NAME + " pre-initialized");
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        LOGGER.info(NAME + " initialized");
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        LOGGER.info(NAME + " post-initialized");
        proxy.postInit();
    }
}
