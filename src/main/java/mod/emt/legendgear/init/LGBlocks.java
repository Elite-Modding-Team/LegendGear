package mod.emt.legendgear.init;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.block.*;
import mod.emt.legendgear.config.LGConfig;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
@GameRegistry.ObjectHolder(LegendGear.MOD_ID)
public class LGBlocks
{
    public static final LGBlockAzurite AZURITE_BLOCK = null;
    public static final LGBlockAzuriteOre AZURITE_ORE = null;
    public static final LGBlockBombFlower BOMB_FLOWER = null;
    public static final LGBlockCaltrops CALTROPS = null;
    public static final LGBlockClayUrn CLAY_URN = null;
    public static final LGBlockInfusedStarstone INFUSED_STARSTONE_BLOCK = null;
    public static final LGBlockLightningStruck LIGHTNING_STRUCK_COARSE_DIRT = null;
    public static final LGBlockLightningStruck LIGHTNING_STRUCK_DIRT = null;
    public static final LGBlockLightningStruckFalling LIGHTNING_STRUCK_RED_SAND = null;
    public static final LGBlockLightningStruckFalling LIGHTNING_STRUCK_SAND = null;
    public static final LGBlockMysticShrub MYSTIC_SHRUB = null;
    public static final LGBlockSwordPedestal SWORD_PEDESTAL = null;
    public static final LGBlockSwordPedestalTechnical SWORD_PEDESTAL_TECHNICAL = null;
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
                // LegendGear 1
                LGRegistry.setup(new LGBlockMysticShrub(), "mystic_shrub").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGBlockClayUrn(), "clay_urn").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGBlockBombFlower(), "bomb_flower").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGBlockCaltrops(), "caltrops").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGBlockSwordPedestal(), "sword_pedestal").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGBlockSwordPedestalTechnical(), "sword_pedestal_technical"),
                LGRegistry.setup(new LGBlockStarbeamTorch(), "starbeam_torch").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGBlockSkybeam(), "skybeam_block").setCreativeTab(LegendGear.TAB),
                // LegendGear 2
                LGRegistry.setup(new LGBlockFalling(Material.SAND, SoundType.SAND, MapColor.SAND, -2370656), "starry_sand").setHardness(0.5F).setLightLevel(0.4F).setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGBlockFalling(Material.SAND, SoundType.SAND, MapColor.ADOBE, -5679071), "red_starry_sand").setHardness(0.5F).setLightLevel(0.4F).setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGBlockStarstone(3000.0F), "starstone_block"),
                LGRegistry.setup(new LGBlockInfusedStarstone(6000.0F), "infused_starstone_block"),
                LGRegistry.setup(new LGBlockAzurite(), "azurite_block").setCreativeTab(LegendGear.TAB)
            );

        // Optional - These blocks can be disabled by the config file
        // LegendGear 1
        if (LGConfig.GENERAL_SETTINGS.sugarCubes)
        {
            registry.register(LGRegistry.setup(new LGBlockSugarCube(), "sugar_cube").setCreativeTab(LegendGear.TAB));
        }
        // LegendGear 2
        if (LGConfig.GENERAL_SETTINGS.lightningStruckBlocks)
        {
            registry.register(LGRegistry.setup(new LGBlockLightningStruck(Material.GROUND, SoundType.GROUND, MapColor.DIRT, Blocks.DIRT, 0), "lightning_struck_dirt").setHardness(0.5F).setCreativeTab(LegendGear.TAB));
            registry.register(LGRegistry.setup(new LGBlockLightningStruck(Material.GROUND, SoundType.GROUND, MapColor.DIRT, Blocks.DIRT, 1), "lightning_struck_coarse_dirt").setHardness(0.5F).setCreativeTab(LegendGear.TAB));
            registry.register(LGRegistry.setup(new LGBlockLightningStruckFalling(Material.SAND, SoundType.SAND, MapColor.SAND, Blocks.SAND, 0, -2370656), "lightning_struck_sand").setHardness(0.5F).setCreativeTab(LegendGear.TAB));
            registry.register(LGRegistry.setup(new LGBlockLightningStruckFalling(Material.SAND, SoundType.SAND, MapColor.ADOBE, Blocks.SAND, 1, -5679071), "lightning_struck_red_sand").setHardness(0.5F).setCreativeTab(LegendGear.TAB));
        }
        if (LGConfig.GENERAL_SETTINGS.azuriteOre)
        {
            registry.register(LGRegistry.setup(new LGBlockAzuriteOre(), "azurite_ore").setCreativeTab(LegendGear.TAB));
        }
    }
}
