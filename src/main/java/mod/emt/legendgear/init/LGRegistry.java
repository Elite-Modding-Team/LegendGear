package mod.emt.legendgear.init;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import mod.emt.legendgear.client.render.*;
import mod.emt.legendgear.entity.LGEntityPing;
import mod.emt.legendgear.tileentity.LGTileEntitySkybeam;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.Item;
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
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.entity.LGEntityBomb;
import mod.emt.legendgear.entity.LGEntityMagicBoomerang;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGRegistry
{
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
        EntityRegistry.registerModEntity(new ResourceLocation(LegendGear.MOD_ID, name), clazz, LegendGear.MOD_ID + "." + name, entityID++, LegendGear.instance, 64, 1, sendsVelocityUpdates);
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

        registerEntity("bomb", LGEntityBomb.class, 64, true);
        registerEntity("magic_boomerang", LGEntityMagicBoomerang.class, 64, true);
        registerEntity("ping", LGEntityPing.class, 128, false);
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

        GameRegistry.registerTileEntity(LGTileEntitySkybeam.class, new ResourceLocation(LegendGear.MOD_ID, "skybeam"));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderers(@Nonnull final ModelRegistryEvent event)
    {
        LegendGear.LOGGER.info("Registering entity renderers...");

        ClientRegistry.bindTileEntitySpecialRenderer(LGTileEntitySkybeam.class, new LGRenderSkybeam());

        RenderingRegistry.registerEntityRenderingHandler(LGEntityBomb.class, new LGRenderBomb.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityMagicBoomerang.class, new LGRenderMagicBoomerang.Factory());
        RenderingRegistry.registerEntityRenderingHandler(LGEntityPing.class, new LGRenderPing.Factory());

        // XP Orb Replacement
        RenderingRegistry.registerEntityRenderingHandler(EntityXPOrb.class, LGRenderPrismaticXP::new);
    }
}
