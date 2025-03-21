package mod.emt.legendgear.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.Mod;

import mod.emt.legendgear.LegendGear;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGLootTables
{
    public static final ResourceLocation MYSTIC_SHRUB = createLootTable("mystic_shrub");
    public static final ResourceLocation MYSTIC_SHRUB_CHARGED = createLootTable("mystic_shrub_charged");

    private static ResourceLocation createLootTable(String path)
    {
        return LootTableList.register(new ResourceLocation(LegendGear.MOD_ID, path));
    }
}
