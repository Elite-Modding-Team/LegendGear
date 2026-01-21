package mod.emt.legendgear.item.base;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;

public class LGItemRecord extends ItemRecord {
    private final EnumRarity rarity;

    public LGItemRecord(String name, SoundEvent soundEvent, EnumRarity rarity) {
        super(name, soundEvent);
        this.rarity = rarity;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return rarity;
    }
}
