package mod.emt.legendgear.proxy;

import mod.emt.legendgear.config.LGConfig;
import mod.emt.legendgear.event.LGAugmentToolHandler;
import mod.emt.legendgear.init.LGAugments;
import mod.emt.legendgear.init.LGRegistry;
import mod.emt.legendgear.network.LGPacketHandler;
import mod.emt.legendgear.worldgen.LGAzuriteGenerator;
import mod.emt.legendgear.worldgen.LGBombFlowerGenerator;
import mod.emt.legendgear.worldgen.LGShrubGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy
{
    public void preInit()
    {
    }

    public void init()
    {
        if (LGConfig.WORLD_GEN_SETTINGS.azuriteOreFrequency > 0 && LGConfig.GENERAL_SETTINGS.azuriteOre)
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

        LGAugments.registerAugments();
        LGPacketHandler.init();
        LGRegistry.registerOreDictionaries();
        LGRegistry.registerTileEntities();

        MinecraftForge.EVENT_BUS.register(new LGAugmentToolHandler());
    }

    public void postInit()
    {
    }
}
