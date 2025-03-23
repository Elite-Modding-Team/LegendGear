package mod.emt.legendgear.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import mod.emt.legendgear.LegendGear;

public enum LGSoundEvents
{
    ITEM_EMERALD_DROP_PICKUP_LARGE("item.emerald_drop.pickup_large"),
    ITEM_EMERALD_DROP_PICKUP_MEDIUM("item.emerald_drop.pickup_medium"),
    ITEM_EMERALD_DROP_PICKUP_SMALL("item.emerald_drop.pickup_small"),
    ITEM_EMERALD_EXCHANGE("item.emerald.exchange"),
    ITEM_FLUTE_ATTACK("item.flute.attack"),
    ITEM_FLUTE_SUSTAIN("item.flute.sustain"),
    ITEM_RECOVERY_HEART_PICKUP("item.recovery_heart.pickup");

    private final SoundEvent soundEvent;

    LGSoundEvents(String path)
    {
        ResourceLocation resourceLocation = new ResourceLocation(LegendGear.MOD_ID, path);
        this.soundEvent = new SoundEvent(resourceLocation);
        this.soundEvent.setRegistryName(resourceLocation);
    }

    public SoundEvent getSoundEvent()
    {
        return this.soundEvent;
    }
}
