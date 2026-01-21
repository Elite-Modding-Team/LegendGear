package mod.emt.legendgear.init;

import javax.annotation.Nonnull;

import mod.emt.legendgear.block.LGBlockSkybeam;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.block.LGBlockBombFlower;
import mod.emt.legendgear.block.LGBlockMysticShrub;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
@GameRegistry.ObjectHolder(LegendGear.MOD_ID)
public class LGBlocks
{
    public static final LGBlockBombFlower BOMB_FLOWER = null;
    public static final LGBlockMysticShrub MYSTIC_SHRUB = null;
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
                LGRegistry.setup(new LGBlockMysticShrub(), "mystic_shrub").setCreativeTab(LegendGear.TAB),
                    LGRegistry.setup(new LGBlockSkybeam(), "skybeam_block").setCreativeTab(LegendGear.TAB)
            );
    }
}
