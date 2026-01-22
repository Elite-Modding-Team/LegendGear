package mod.emt.legendgear.init;

import javax.annotation.Nonnull;

import mod.emt.legendgear.item.base.*;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
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

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
@GameRegistry.ObjectHolder(LegendGear.MOD_ID)
public class LGItems
{
    public static final LGItemAeroAmulet AERO_AMULET = null;
    public static final LGItemCharm BLAST_CHARM = null;
    public static final LGItemBomb BOMB = null;
    public static final LGItemBombBag BOMB_BAG = null;
    public static final LGItemRockCandy CHORUS_FRUIT_ROCK_CANDY = null;
    public static final LGItemRockCandy DIAMOND_ROCK_CANDY = null;
    public static final LGItemDimensionalCatalyst DIMENSIONAL_CATALYST = null;
    public static final LGItemEarthMedallion EARTH_MEDALLION = null;
    public static final LGItemEmeraldPiece EMERALD_PIECE = null;
    public static final LGItemEnderMedallion ENDER_MEDALLION = null;
    public static final LGItemRockCandy EMERALD_ROCK_CANDY = null;
    public static final LGItemEmeraldShard EMERALD_SHARD = null;
    public static final LGItemCharm FEATHER_CHARM = null;
    public static final LGItemFireMedallion FIRE_MEDALLION = null;
    public static final LGItemFortuneCookie FORTUNE_COOKIE = null;
    public static final LGItemGeoAmulet GEO_AMULET = null;
    public static final LGItemRockCandy GLOWSTONE_ROCK_CANDY = null;
    public static final LGItemStardust INFUSED_STARDUST = null;
    public static final LGItemStarPiece INFUSED_STAR_PIECE = null;
    public static final LGItemRockCandy LAPIS_LAZULI_ROCK_CANDY = null;
    public static final LGItemMagicBoomerang MAGIC_BOOMERANG = null;
    public static final LGItemMagicMirror MAGIC_MIRROR = null;
    public static final LGItemFood MILK_CHOCOLATE = null;
    public static final LGItemMysticSeed MYSTIC_SEED = null;
    public static final LGItemCharm PHOENIX_CHARM = null;
    public static final LGItemPhoenixFeather PHOENIX_FEATHER = null;
    public static final LGItemPyroAmulet PYRO_AMULET = null;
    public static final LGItemQuiver QUIVER = null;
    public static final LGItemRockCandy QUARTZ_ROCK_CANDY = null;
    public static final LGItemRecord RECORD_DRAGONDOT = null;
    public static final LGItemRockCandy REDSTONE_ROCK_CANDY = null;
    public static final LGItemReedPipes REED_PIPES = null;
    public static final LGItemRecoveryHeart RECOVERY_HEART = null;
    public static final LGItemSlimeSword SLIME_SWORD = null;
    public static final LGItemSpottingScope SPOTTING_SCOPE = null;
    public static final LGItemStardust STARDUST = null;
    public static final LGItemBase STARGLASS_INGOT = null;
    public static final LGItemSword STARGLASS_SWORD = null;
    public static final LGItemStarPiece STAR_PIECE = null;
    public static final LGItemBase STARSTEEL_INGOT = null;
    public static final LGItemTitanBand TITAN_BAND = null;
    public static final LGItemValorHeadband VALOR_HEADBAND = null;
    public static final LGItemWhirlwindBoots WHIRLWIND_BOOTS = null;
    public static final LGItemWindMedallion WIND_MEDALLION = null;

    // TODO: Add materials to its own class
    public static Item.ToolMaterial TOOL_STARGLASS = EnumHelper.addToolMaterial("legendgear_tool_starglass", 2, 216, 15.0F, 2.0F, 20);

    public static ItemArmor.ArmorMaterial ARMOR_SPECIAL = EnumHelper.addArmorMaterial("legendgear_armor_special", "special", 5, new int[]{1, 0, 0, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);

    @SubscribeEvent
    public static void onRegisterItemsEvent(@Nonnull final RegistryEvent.Register<Item> event)
    {
        LegendGear.LOGGER.info("Registering items...");

        final IForgeRegistry<Item> registry = event.getRegistry();

        // ITEMS
        registry.registerAll
            (
                LGRegistry.setup(new LGItemStarPiece(), "star_piece").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemStarPiece(), "infused_star_piece").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemStardust(), "stardust").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemStardust(), "infused_stardust").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemBase(EnumRarity.UNCOMMON), "starglass_ingot").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemBase(EnumRarity.UNCOMMON), "starsteel_ingot").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemBomb(), "bomb").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemBombBag(), "bomb_bag").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemDimensionalCatalyst(), "dimensional_catalyst").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemFortuneCookie(), "fortune_cookie").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemRockCandy(), "chorus_fruit_rock_candy").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemRockCandy(), "diamond_rock_candy").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemRockCandy(), "emerald_rock_candy").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemRockCandy(), "glowstone_rock_candy").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemRockCandy(), "lapis_lazuli_rock_candy").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemRockCandy(), "quartz_rock_candy").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemRockCandy(), "redstone_rock_candy").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemEmeraldPiece(), "emerald_piece").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemEmeraldShard(), "emerald_shard").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemFood(2, 0.3F, false), "milk_chocolate").setAlwaysEdible().setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemMagicBoomerang(), "magic_boomerang").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemMagicMirror(), "magic_mirror").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemMysticSeed(LGBlocks.MYSTIC_SHRUB, Blocks.GRASS), "mystic_seed").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemPhoenixFeather(), "phoenix_feather").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemEarthMedallion(), "earth_medallion").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemEnderMedallion(), "ender_medallion").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemFireMedallion(), "fire_medallion").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemWindMedallion(), "wind_medallion").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemAeroAmulet(), "aero_amulet").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemGeoAmulet(), "geo_amulet").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemPyroAmulet(), "pyro_amulet").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemCharm(), "blast_charm").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemCharm(), "feather_charm").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemCharm(), "phoenix_charm").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemTitanBand(), "titan_band").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemValorHeadband(ARMOR_SPECIAL, 0), "valor_headband").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemWhirlwindBoots(ARMOR_SPECIAL, 0), "whirlwind_boots").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemBestBow(), "best_bow").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemQuiver(), "quiver").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemReedPipes(), "reed_pipes").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemRecoveryHeart(), "recovery_heart").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemSlimeSword(), "slime_sword").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemSpottingScope(), "spotting_scope").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemSword(TOOL_STARGLASS, EnumRarity.RARE), "starglass_sword").setCreativeTab(LegendGear.TAB),
                LGRegistry.setup(new LGItemRecord("dragondot", LGSoundEvents.RECORD_DRAGONDOT.getSoundEvent(), EnumRarity.EPIC), "record_dragondot").setCreativeTab(LegendGear.TAB)
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
