package mod.emt.legendgear.init;

import javax.annotation.Nonnull;

import mod.emt.legendgear.block.*;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import mod.emt.legendgear.LegendGear;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
@GameRegistry.ObjectHolder(LegendGear.MOD_ID)
public class LGBlocks
{
    public static final LGBlockBombFlower BOMB_FLOWER = null;
    public static final LGBlockInfusedStarstone INFUSED_STARSTONE_BLOCK = null;
    public static final LGBlockMysticShrub MYSTIC_SHRUB = null;
    public static final LGBlockStarstone STARSTONE_BLOCK = null;
    public static final LGBlockSkybeam SKYBEAM_BLOCK = null;

    @SubscribeEvent
    public static void onRegisterBlocksEvent(@Nonnull final RegistryEvent.Register<Block> event)
    {
        LegendGear.LOGGER.info("Registering blocks...");

        final IForgeRegistry<Block> registry = event.getRegistry();

        // BLOCKS
        registry.registerAll
            (
                    LGRegistry.setup(new LGBlockBombFlower(), "bomb_flower").setCreativeTab(LegendGear.TAB),
                    LGRegistry.setup(new LGBlockInfusedStarstone(6000.0F), "infused_starstone_block"),
                    LGRegistry.setup(new LGBlockMysticShrub(), "mystic_shrub").setCreativeTab(LegendGear.TAB),
                    LGRegistry.setup(new LGBlockStarstone(3000.0F), "starstone_block"),
                    LGRegistry.setup(new LGBlockSkybeam(), "skybeam_block").setCreativeTab(LegendGear.TAB)
            );
    }
}
