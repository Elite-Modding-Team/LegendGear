package mod.emt.legendgear.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import mod.emt.legendgear.LegendGear;

@Config(modid = LegendGear.MOD_ID, name = LegendGear.NAME)
public class LGConfig
{
    @Config.Name("Generate Bomb Flowers")
    @Config.Comment("Generate bomb flowers in the world")
    public static boolean generateBombFlowers = true;

    @Config.Name("Generate Mystic Shrubs")
    @Config.Comment("Generate mystic shrubs in the world")
    public static boolean generateMysticShrubs = true;

    @Config.Name("Falling Star Chance")
    @Config.Comment("Spawn chance for falling stars per second for each player under the open sky at night")
    public static double chanceFallingStar = 0.001D;

    @Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
    public static class EventHandler
    {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equals(LegendGear.MOD_ID)) ConfigManager.sync(LegendGear.MOD_ID, Config.Type.INSTANCE);
        }
    }
}
