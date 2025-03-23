package mod.emt.legendgear.init;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.item.*;
import mod.emt.legendgear.item.base.*;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
@GameRegistry.ObjectHolder(LegendGear.MOD_ID)
public class LGItems
{
    public static final LGItemBomb BOMB = null;
    public static final LGItemEmeraldPiece EMERALD_PIECE = null;
    public static final LGItemEmeraldShard EMERALD_SHARD = null;
    public static final LGItemFood MILK_CHOCOLATE = null;
    public static final LGItemMysticSeed MYSTIC_SEED = null;
    public static final LGItemQuiver QUIVER = null;
    public static final LGItemRecoveryHeart RECOVERY_HEART = null;

    @SubscribeEvent
    public static void onRegisterItemsEvent(@Nonnull final RegistryEvent.Register<Item> event)
    {
        LegendGear.LOGGER.info("Registering items...");

        final IForgeRegistry<Item> registry = event.getRegistry();

        // ITEMS
        registry.registerAll
            (
                LGRegistry.setup(new LGItemBomb(), "bomb").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemEmeraldPiece(), "emerald_piece").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemEmeraldShard(), "emerald_shard").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemFood(2, 0.3F, false), "milk_chocolate").setAlwaysEdible().setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemMysticSeed(LGBlocks.MYSTIC_SHRUB, Blocks.GRASS), "mystic_seed").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemQuiver(), "quiver").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemRecoveryHeart(), "recovery_heart").setCreativeTab(LegendGear.TAB)
            );

        // ITEM BLOCKS
        ForgeRegistries.BLOCKS.getValues().stream()
            .filter(block -> block.getRegistryName().getNamespace().equals(LegendGear.MOD_ID))
            .forEach(block -> registry.register(LGRegistry.setup(new ItemBlock(block), block.getRegistryName())));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRegisterModelsEvent(@Nonnull final ModelRegistryEvent event)
    {
        LegendGear.LOGGER.info("Registering item models...");

        // ITEM MODELS
        for (final Item item : ForgeRegistries.ITEMS.getValues())
        {
            if (item.getRegistryName().getNamespace().equals(LegendGear.MOD_ID))
            {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
            }
        }
    }
}
