package mod.emt.legendgear;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import mod.emt.legendgear.config.LGConfig;
import mod.emt.legendgear.init.LGRegistry;
import mod.emt.legendgear.network.LGPacketHandler;
import mod.emt.legendgear.util.LGCreativeTab;
import mod.emt.legendgear.worldgen.LGAzuriteGenerator;
import mod.emt.legendgear.worldgen.LGBombFlowerGenerator;
import mod.emt.legendgear.worldgen.LGShrubGenerator;

@Mod(modid = LegendGear.MOD_ID, name = LegendGear.NAME, version = LegendGear.VERSION, acceptedMinecraftVersions = LegendGear.ACCEPTED_VERSIONS, dependencies = LegendGear.DEPENDENCIES)
public class LegendGear
{
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String NAME = Tags.MOD_NAME;
    public static final String VERSION = Tags.VERSION;
    public static final String ACCEPTED_VERSIONS = "[1.12.2]";
    public static final String DEPENDENCIES = "after:baubles";
    public static final Logger LOGGER = LogManager.getLogger(NAME);
    public static final CreativeTabs TAB = new LGCreativeTab(CreativeTabs.CREATIVE_TAB_ARRAY.length, MOD_ID + ".tab");

    @Mod.Instance
    public static LegendGear instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER.info(NAME + " pre-initialized");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        if (LGConfig.WORLD_GEN_SETTINGS.azuriteOreFrequency > 0)
        {
            GameRegistry.registerWorldGenerator(new LGAzuriteGenerator(), 3);
        }
        if (LGConfig.WORLD_GEN_SETTINGS.bombFlowerFrequency > 0)
        {
            GameRegistry.registerWorldGenerator(new LGBombFlowerGenerator(), 100);
        }
        if (LGConfig.WORLD_GEN_SETTINGS.mysticShrubFrequency > 0)
        {
            GameRegistry.registerWorldGenerator(new LGShrubGenerator(), 100);
        }

        LGPacketHandler.init();
        LGRegistry.registerTileEntities();
        LOGGER.info(NAME + " initialized");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        LOGGER.info(NAME + " post-initialized");
    }
}
