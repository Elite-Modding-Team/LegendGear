package mod.emt.legendgear.init;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.client.render.*;
import mod.emt.legendgear.config.LGConfig;
import mod.emt.legendgear.entity.*;
import mod.emt.legendgear.tileentity.LGTileEntityClayUrn;
import mod.emt.legendgear.tileentity.LGTileEntityInfusedStarstone;
import mod.emt.legendgear.tileentity.LGTileEntitySkybeam;
import mod.emt.legendgear.tileentity.LGTileEntitySwordPedestal;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGRegistry
{
    public static final SoundType CALTROPS = new SoundType(1.0F, 1.2F, LGSoundEvents.BLOCK_CALTROPS_LAND.getSoundEvent(), LGSoundEvents.BLOCK_CALTROPS_HIT.getSoundEvent(), LGSoundEvents.BLOCK_CALTROPS_LAND.getSoundEvent(), LGSoundEvents.BLOCK_CALTROPS_HIT.getSoundEvent(), LGSoundEvents.BLOCK_CALTROPS_HIT.getSoundEvent());
    public static final SoundType URN = new SoundType(1.0F, 1.0F, LGSoundEvents.BLOCK_URN_SHATTER.getSoundEvent(), LGSoundEvents.BLOCK_URN_STEP.getSoundEvent(), LGSoundEvents.BLOCK_URN_PLACE.getSoundEvent(), LGSoundEvents.BLOCK_URN_STEP.getSoundEvent(), LGSoundEvents.BLOCK_URN_STEP.getSoundEvent());

    private static int entityID = 1;

    @Nonnull
    public static <T extends IForgeRegistryEntry> T setup(@Nonnull final T entry, @Nonnull final String name)
    {
        return setup(entry, new ResourceLocation(LegendGear.MOD_ID, name));
    }

    @Nonnull
    public static <T extends IForgeRegistryEntry> T setup(@Nonnull final T entry, @Nonnull final ResourceLocation registryName)
    {
        Preconditions.checkNotNull(entry, "Entry to setup must not be null!");
        Preconditions.checkNotNull(registryName, "Registry name to assign must not be null!");

        entry.setRegistryName(registryName);
        if (entry instanceof Block) ((Block) entry).setTranslationKey(registryName.getNamespace() + "." + registryName.getPath());
        if (entry instanceof Item) ((Item) entry).setTranslationKey(registryName.getNamespace() + "." + registryName.getPath());
        return entry;
    }

    public static void registerEntity(String name, Class<? extends Entity> clazz, int eggColor1, int eggColor2)
    {
        EntityRegistry.registerModEntity(new ResourceLocation(LegendGear.MOD_ID, name), clazz, LegendGear.MOD_ID + "." + name, entityID++, LegendGear.instance, 64, 1, true, eggColor1, eggColor2);
    }

    public static void registerEntity(String name, Class<? extends Entity> clazz, int trackingRange, boolean sendsVelocityUpdates)
    {
        EntityRegistry.registerModEntity(new ResourceLocation(LegendGear.MOD_ID, name), clazz, LegendGear.MOD_ID + "." + name, entityID++, LegendGear.instance, trackingRange, 1, sendsVelocityUpdates);
    }

    public static void registerAdvancements()
    {
    }

    @SubscribeEvent
    public static void registerEnchantments(@Nonnull final RegistryEvent.Register<Enchantment> event)
    {
        LegendGear.LOGGER.info("Registering enchantments...");

        event.getRegistry().registerAll();
    }

    @SubscribeEvent
    public static void registerEntities(@Nonnull final RegistryEvent.Register<EntityEntry> event)
    {
        LegendGear.LOGGER.info("Registering entities...");

        registerEntity("arrow_storm", LGEntityArrowStorm.class, 64, false);
        registerEntity("bomb", LGEntityBomb.class, 64, true);
        registerEntity("earth_medallion", LGEntityEarthMedallion.class, 64, true);
        registerEntity("ender_bomb", LGEntityEnderBomb.class, 64, false);
        registerEntity("ender_medallion", LGEntityEnderMedallion.class, 64, true);
        registerEntity("falling_star", LGEntityFallingStar.class, 512, true);
        registerEntity("fire_blast", LGEntityFireBlast.class, 64, false);
        registerEntity("fire_medallion", LGEntityFireMedallion.class, 64, true);
        registerEntity("grind_rail", LGEntityGrindRail.class, 64, true);
        registerEntity("hookshot", LGEntityHookshot.class, 64, true);
        registerEntity("magic_boomerang", LGEntityMagicBoomerang.class, 64, true);
        registerEntity("ping", LGEntityPing.class, 512, false);
        registerEntity("quake", LGEntityQuake.class, 64, false);
        registerEntity("volatile_arrow", LGEntityVolatileArrow.class, 64, true);
        registerEntity("wind_medallion", LGEntityWindMedallion.class, 64, true);
    }

    public static void registerEntitySpawns()
    {
    }

    // Gets biomes from selected entity.
    public static Biome[] getEntityBiomes(Class<? extends Entity> spawn)
    {
        List<Biome> biomes = new ArrayList<>();

        for (Biome biome : Biome.REGISTRY)
        {
            List<Biome.SpawnListEntry> spawnList = biome.getSpawnableList(EnumCreatureType.MONSTER);

            for (Biome.SpawnListEntry list : spawnList)
                if (list.entityClass == spawn)
                {
                    biomes.add(biome);
                    break;
                }
        }

        return biomes.toArray(new Biome[0]);
    }

    @SubscribeEvent
    public static void registerPotions(@Nonnull final RegistryEvent.Register<Potion> event)
    {
        LegendGear.LOGGER.info("Registering potions...");

        event.getRegistry().registerAll();
    }

    @SubscribeEvent
    public static void registerRecipes(@Nonnull final RegistryEvent.Register<IRecipe> event)
    {
        LegendGear.LOGGER.info("Registering ore dictionary entries...");

        final IForgeRegistry<IRecipe> registry = event.getRegistry();

        GameRegistry.addSmelting(new ItemStack(LGBlocks.AZURITE_ORE), new ItemStack(LGItems.AZURITE), 1.0F);
        GameRegistry.addSmelting(new ItemStack(LGBlocks.RED_STARRY_SAND), new ItemStack(LGItems.STARGLASS_INGOT), 1.0F);
        GameRegistry.addSmelting(new ItemStack(LGBlocks.STARRY_SAND), new ItemStack(LGItems.STARGLASS_INGOT), 1.0F);

        OreDictionary.registerOre("gemAzurite", LGItems.AZURITE);
        OreDictionary.registerOre("ingotStarglass", LGItems.STARGLASS_INGOT);
        OreDictionary.registerOre("ingotStarsteel", LGItems.STARSTEEL_INGOT);

        OreDictionary.registerOre("blockAzurite", LGBlocks.AZURITE_BLOCK);
        if (LGConfig.GENERAL_SETTINGS.azuriteOre)
        {
            OreDictionary.registerOre("oreAzurite", LGBlocks.AZURITE_ORE);
        }
        OreDictionary.registerOre("sandStarry", LGBlocks.RED_STARRY_SAND);
        OreDictionary.registerOre("sandStarry", LGBlocks.STARRY_SAND);
    }

    @SubscribeEvent
    public static void registerSoundEvents(@Nonnull final RegistryEvent.Register<SoundEvent> event)
    {
        LegendGear.LOGGER.info("Registering sound events...");

        final IForgeRegistry<SoundEvent> registry = event.getRegistry();

        for (LGSoundEvents soundEvents : LGSoundEvents.values())
        {
            registry.register(soundEvents.getSoundEvent());
        }
    }

    public static void registerTileEntities()
    {
        LegendGear.LOGGER.info("Registering tile entities...");

        GameRegistry.registerTileEntity(LGTileEntityClayUrn.class, new ResourceLocation(LegendGear.MOD_ID, "clay_urn"));
        GameRegistry.registerTileEntity(LGTileEntityInfusedStarstone.class, new ResourceLocation(LegendGear.MOD_ID, "infused_starstone"));
        GameRegistry.registerTileEntity(LGTileEntitySkybeam.class, new ResourceLocation(LegendGear.MOD_ID, "skybeam"));
        GameRegistry.registerTileEntity(LGTileEntitySwordPedestal.class, new ResourceLocation(LegendGear.MOD_ID, "sword_pedestal"));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderers(@Nonnull final ModelRegistryEvent event)
    {
        LegendGear.LOGGER.info("Registering entity renderers...");

        ClientRegistry.bindTileEntitySpecialRenderer(LGTileEntityInfusedStarstone.class, new LGRenderInfusedStarstone());
        ClientRegistry.bindTileEntitySpecialRenderer(LGTileEntitySkybeam.class, new LGRenderSkybeam());
        ClientRegistry.bindTileEntitySpecialRenderer(LGTileEntitySwordPedestal.class, new LGRenderSwordPedestal());

        RenderingRegistry.registerEntityRenderingHandler(LGEntityArrowStorm.class, new LGRenderArrowStorm.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityBomb.class, new LGRenderBomb.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityEarthMedallion.class, new LGRenderEarthMedallion.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityEnderBomb.class, new LGRenderEnderBomb.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityEnderMedallion.class, new LGRenderEnderMedallion.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityFallingStar.class, new LGRenderFallingStar.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityFireBlast.class, new LGRenderFireBlast.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityFireMedallion.class, new LGRenderFireMedallion.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityGrindRail.class, new LGRenderGrindRail.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityHookshot.class, new LGRenderHookshot.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityMagicBoomerang.class, new LGRenderMagicBoomerang.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityPing.class, new LGRenderPing.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityQuake.class, new LGRenderQuake.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityVolatileArrow.class, new LGRenderVolatileArrow.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityWindMedallion.class, new LGRenderWindMedallion.Factory());

        // XP Orb Replacement
        if (LGConfig.GENERAL_SETTINGS.prismaticXP)
        {
            RenderingRegistry.registerEntityRenderingHandler(EntityXPOrb.class, LGRenderPrismaticXP::new);
        }
    }
}
