package mod.emt.legendgear.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import mod.emt.legendgear.LegendGear;

public enum LGSoundEvents
{
    ENTITY_MAGIC_BOOMERANG_FLY("entity.magic_boomerang.fly"),
    ITEM_BEST_BOW_SHOOT("item.best_bow.shoot"),
    ITEM_EMERALD_DROP_PICKUP_LARGE("item.emerald_drop.pickup_large"),
    ITEM_EMERALD_DROP_PICKUP_MEDIUM("item.emerald_drop.pickup_medium"),
    ITEM_EMERALD_DROP_PICKUP_SMALL("item.emerald_drop.pickup_small"),
    ITEM_EMERALD_EXCHANGE("item.emerald.exchange"),
    ITEM_FLUTE_ATTACK("item.flute.attack"),
    ITEM_FLUTE_SUSTAIN("item.flute.sustain"),
    ITEM_FORTUNE_COOKIE_USE("item.fortune_cookie.use"),
    ITEM_MAGIC_MIRROR_INACTIVE("item.magic_mirror.inactive"),
    ITEM_MAGIC_MIRROR_USE("item.magic_mirror.use"),
    ITEM_PHOENIX_FEATHER_ACTIVATE("item.phoenix_feather.activate"),
    ITEM_PHOENIX_FEATHER_REVIVE("item.phoenix_feather.revive"),
    ITEM_PHOENIX_FEATHER_USE("item.phoenix_feather.use"),
    ITEM_RECOVERY_HEART_PICKUP("item.recovery_heart.pickup"),
    ITEM_SPOTTING_SCOPE_MARK("item.spotting_scope.mark"),
    ITEM_SPOTTING_SCOPE_UNZOOM("item.spotting_scope.unzoom"),
    ITEM_SPOTTING_SCOPE_ZOOM("item.spotting_scope.zoom"),
    ITEM_STAR_PIECE_CHARGE_END("item.star_piece.charge_end"),
    ITEM_STAR_PIECE_CHARGE_START("item.star_piece.charge_start"),
    ITEM_STAR_PIECE_SPARKLE("item.star_piece.sparkle"),
    RANDOM_MYSTERY_SPARKLE("random.mystery_sparkle"),
    RANDOM_SWORD_SLASH("random.sword_slash"),
    RECORD_DRAGONDOT("record.dragondot");

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
