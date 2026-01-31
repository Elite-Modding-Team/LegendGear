package mod.emt.legendgear.init;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.block.*;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
@GameRegistry.ObjectHolder(LegendGear.MOD_ID)
public class LGBlocks
{
    public static final LGBlockBombFlower BOMB_FLOWER = null;
    public static final LGBlockCaltrops CALTROPS = null;
    public static final LGBlockInfusedStarstone INFUSED_STARSTONE_BLOCK = null;
    public static final LGBlockMysticShrub MYSTIC_SHRUB = null;
    public static final LGBlockFalling RED_STARRY_SAND = null;
    public static final LGBlockSkybeam SKYBEAM_BLOCK = null;
    public static final LGBlockStarbeamTorch STARBEAM_TORCH = null;
    public static final LGBlockStarstone STARSTONE_BLOCK = null;
    public static final LGBlockFalling STARRY_SAND = null;
    public static final LGBlockSugarCube SUGAR_CUBE = null;

    @SubscribeEvent
    public static void onRegisterBlocksEvent(@Nonnull final RegistryEvent.Register<Block> event)
    {
        LegendGear.LOGGER.info("Registering blocks...");

        final IForgeRegistry<Block> registry = event.getRegistry();

        // BLOCKS
        registry.registerAll
            (
                LGRegistry.setup(new LGBlockFalling(MapColor.SAND, -2370656), "starry_sand").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGBlockFalling(MapColor.ADOBE, -5679071), "red_starry_sand").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGBlockBombFlower(), "bomb_flower").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGBlockCaltrops(), "caltrops").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGBlockMysticShrub(), "mystic_shrub").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGBlockStarstone(3000.0F), "starstone_block"),
                LGRegistry.setup(new LGBlockInfusedStarstone(6000.0F), "infused_starstone_block"),
                LGRegistry.setup(new LGBlockSkybeam(), "skybeam_block").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGBlockStarbeamTorch(), "starbeam_torch").setCreativeTab(LegendGear.TAB)
            );

        // Optional - These blocks can be disabled by the config file
        registry.register(LGRegistry.setup(new LGBlockSugarCube(), "sugar_cube").setCreativeTab(LegendGear.TAB));
    }
}
