package mod.emt.legendgear.init;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.augment.*;
import mod.emt.legendgear.recipe.RecipeMedallionAugment;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGAugments {
    public static final String EARTH_MEDALLION = "earth_medallion";
    public static final String ENDER_MEDALLION = "ender_medallion";
    public static final String FIRE_MEDALLION = "fire_medallion";
    public static final String WIND_MEDALLION = "wind_medallion";

    public static void registerAugments()
    {
        MedallionAugmentRegistry.register(EARTH_MEDALLION, new AugmentEarthMedallion());
        MedallionAugmentRegistry.register(ENDER_MEDALLION, new AugmentEnderMedallion());
        MedallionAugmentRegistry.register(FIRE_MEDALLION, new AugmentFireMedallion());
        MedallionAugmentRegistry.register(WIND_MEDALLION, new AugmentWindMedallion());
    }

    @SubscribeEvent
    public static void registerRecipes(@Nonnull final RegistryEvent.Register<IRecipe> event)
    {
        RecipeMedallionAugment.registerAugmentItem(LGItems.EARTH_MEDALLION, EARTH_MEDALLION);
        RecipeMedallionAugment.registerAugmentItem(LGItems.ENDER_MEDALLION, ENDER_MEDALLION);
        RecipeMedallionAugment.registerAugmentItem(LGItems.FIRE_MEDALLION, FIRE_MEDALLION);
        RecipeMedallionAugment.registerAugmentItem(LGItems.WIND_MEDALLION, WIND_MEDALLION);
        RecipeMedallionAugment.registerRemovalItem(LGItems.NULL_MEDALLION);
    }
}
