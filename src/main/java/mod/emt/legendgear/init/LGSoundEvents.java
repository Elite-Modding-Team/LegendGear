package mod.emt.legendgear.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import mod.emt.legendgear.LegendGear;

public enum LGSoundEvents
{
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
