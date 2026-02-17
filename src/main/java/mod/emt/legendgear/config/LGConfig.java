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
    @Config.LangKey("config.legendgear.general")
    public static final GeneralSettings GENERAL_SETTINGS = new GeneralSettings();
    @Config.LangKey("config.legendgear.world_gen")
    public static final WorldGenSettings WORLD_GEN_SETTINGS = new WorldGenSettings();
    @Config.LangKey("config.legendgear.starbeam_torch")
    public static final StarbeamTorchSettings STARBEAM_TORCH_SETTINGS = new StarbeamTorchSettings();

    public static class GeneralSettings
    {
        @Config.Name("Azurite Ore")
        @Config.Comment("Enables azurite ore")
        public boolean azuriteOre = true;

        @Config.Name("Bomb Bag Capacity")
        @Config.Comment("Amount of bombs a bomb bag can hold")
        @Config.RangeInt(min = 1)
        public int bombBagCapacity = 50;

        @Config.Name("Bomb Damage")
        @Config.Comment("Damage bombs deal to entities on detonation")
        @Config.RangeDouble(min = 0)
        public double bombDamage = 5.0D;

        @Config.Name("Bomb Terrain Damage")
        @Config.Comment("Bombs deal terrain damage on detonation like regular explosions")
        public boolean bombDamageTerrain = false;

        @Config.Name("Dimensional Catalyst Azurite Extraction")
        @Config.Comment("Using a dimensional catalyst on azurite ore will extract three azurite from it")
        public boolean dimensionalCatalystAzuriteExtraction = true;

        @Config.Name("Earth Medallion Damage")
        @Config.Comment("Damage dealt by the earth medallion")
        @Config.RangeDouble(min = 0)
        public double earthMedallionDamage = 70.0D;

        @Config.Name("Earth Medallion Horizontal Knockback")
        @Config.Comment("Horizontal knockback dealt by the earth medallion")
        @Config.RangeDouble(min = 0)
        public double earthMedallionHorizontalKnockback = 4.0D;

        @Config.Name("Earth Medallion Vertical Knockback")
        @Config.Comment("Vertical knockback dealt by the earth medallion")
        @Config.RangeDouble(min = 0)
        public double earthMedallionVerticalKnockback = 2.0D;

        @Config.Name("Emerald Exchange Rate")
        @Config.Comment("Exchange rate for shards -> pieces and pieces -> emeralds")
        @Config.RangeInt(min = 1)
        public int emeraldExchangeRate = 8;

        @Config.Name("Ender Medallion Damage")
        @Config.Comment("Damage dealt by the ender medallion")
        @Config.RangeDouble(min = 0)
        public double enderMedallionDamage = 80.0D;

        @Config.Name("Ender Medallion Radius")
        @Config.Comment("The radius of the affected area by the ender medallion")
        @Config.RangeDouble(min = 0)
        public double enderMedallionRadius = 10.0D;

        @Config.Name("Fallen Star Lifetime")
        @Config.Comment("The time in ticks a fallen star will exist before dwindling away")
        @Config.RangeInt(min = 100)
        public int fallenStarLifetime = 440;

        @Config.Name("Fire Medallion Damage")
        @Config.Comment("Damage dealt by the fire medallion")
        @Config.RangeDouble(min = 0)
        public double fireMedallionDamage = 80.0D;

        @Config.Name("Fire Medallion Burn Duration")
        @Config.Comment("The amount of seconds the fire medallion will burn targets")
        @Config.RangeInt(min = 0)
        public int fireMedallionBurnDuration = 20;

        @Config.Name("Lightning-Struck Blocks")
        @Config.Comment("Allows lightning conversions of grass, dirt and sand into lightning-struck variants")
        public boolean lightningStruckBlocks = true;

        @Config.Name("Magic Boomerang Damage")
        @Config.Comment("Damage magic boomerangs deal to hit entities")
        @Config.RangeDouble(min = 0)
        public double magicBoomerangDamage = 6.0D;

        @Config.Name("Prismatic XP")
        @Config.Comment("Replace regular experience orb textures with custom rainbow experience orb textures")
        public boolean prismaticXP = true;

        @Config.Name("Quiver Capacity")
        @Config.Comment("Amount of arrows a quiver can hold")
        @Config.RangeInt(min = 1)
        public int quiverCapacity = 200;

        @Config.Name("Special Drops")
        @Config.Comment("Enables additional loot tables for slain mobs (emerald shards, emerald pieces, and hearts)")
        public boolean specialDrops = true;

        @Config.Name("Starfall Rarity")
        @Config.Comment("Spawn chance for falling stars per second for each player under the open night sky")
        @Config.RangeDouble(min = 0)
        public double starfallRarity = 0.001D;

        @Config.Name("Sugar Cubes")
        @Config.Comment("Enables sugar cubes")
        public boolean sugarCubes = true;

        @Config.Name("Wind Medallion Damage")
        @Config.Comment("Damage dealt by the wind medallion's arrows")
        @Config.RangeDouble(min = 0)
        public double windMedallionDamage = 4.0D;

        @Config.Name("Wind Medallion Ignores Invincibility Frames")
        @Config.Comment("Wind medallion's arrows will ignore invincibility frames")
        public boolean windMedallionIgnoresIF = true;
    }

    public static class WorldGenSettings
    {
        @Config.Name("Azurite Ore Generation Frequency")
        @Config.RangeInt(min = 0)
        public int azuriteOreFrequency = 1;

        @Config.Name("Azurite Ore Generation Min Altitude")
        public int azuriteOreAltitudeMin = 100;

        @Config.Name("Azurite Ore Generation Max Altitude")
        public int azuriteOreAltitudeMax = 110;

        @Config.Name("Azurite Ore Generation Density")
        @Config.RangeDouble(min = 0)
        public double azuriteOreDensity = 0.5D;

        @Config.Name("Bomb Flower Generation Frequency")
        @Config.RangeInt(min = 0)
        public int bombFlowerFrequency = 8;

        @Config.Name("Mystic Shrub Generation Frequency")
        @Config.RangeInt(min = 0)
        public int mysticShrubFrequency = 8;
    }

    public static class StarbeamTorchSettings
    {
        @Config.Name("Grind Rail Start Speed")
        @Config.Comment("Initial speed for grinding on starbeam torch rails")
        @Config.RangeDouble(min = 0)
        public double grindRailSpeedStart = 0.3D;

        @Config.Name("Grind Rail Minimum Speed")
        @Config.Comment("Minimum speed for grinding on starbeam torch rails")
        @Config.RangeDouble(min = 0)
        public double grindRailSpeedMin = 0.02D;

        @Config.Name("Grind Rail Drag")
        @Config.Comment("Slowdown when grinding on starbeam torch rails")
        @Config.RangeDouble(min = 0)
        public double grindRailDrag = 0.001D;

        @Config.Name("Grind Rail Node Link Distance")
        @Config.Comment("Maximum distance for linking starbeam torch nodes")
        @Config.RangeInt(min = 1)
        public int grindRailNodeLinkDistance = 8;
    }

    @Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
    public static class EventHandler
    {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if (event.getModID().equals(LegendGear.MOD_ID))
            {
                ConfigManager.sync(LegendGear.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}
