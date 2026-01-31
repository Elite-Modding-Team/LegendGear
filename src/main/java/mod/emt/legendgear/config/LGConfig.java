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
    public static final GeneralSettings GENERAL_SETTINGS = new GeneralSettings();
    public static final WorldGenSettings WORLD_GEN_SETTINGS = new WorldGenSettings();
    public static final StarbeamTorchSettings STARBEAM_TORCH_SETTINGS = new StarbeamTorchSettings();

    public static class GeneralSettings
    {
        @Config.Name("Falling Star Chance")
        @Config.Comment("Spawn chance for falling stars per second for each player under the open sky at night")
        public double chanceFallingStar = 0.001D;
    }

    public static class WorldGenSettings
    {
        @Config.Name("Generate Bomb Flowers")
        @Config.Comment("Generate bomb flowers in the world")
        public boolean generateBombFlowers = true;

        @Config.Name("Generate Mystic Shrubs")
        @Config.Comment("Generate mystic shrubs in the world")
        public boolean generateMysticShrubs = true;
    }

    public static class StarbeamTorchSettings
    {
        @Config.Name("Grind Rail Start Speed")
        @Config.Comment("Initial speed for grinding on starbeam torch rails")
        public double grindRailSpeedStart = 0.3D;

        @Config.Name("Grind Rail Minimum Speed")
        @Config.Comment("Minimum speed for grinding on starbeam torch rails")
        public double grindRailSpeedMin = 0.02D;

        @Config.Name("Grind Rail Drag")
        @Config.Comment("Slowdown when grinding on starbeam torch rails")
        public double grindRailDrag = 0.001D;

        @Config.Name("Grind Rail Node Link Distance")
        @Config.Comment("Maximum distance for linking starbeam torch nodes")
        public int grindRailNodeLinkDistance = 8;
    }

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
