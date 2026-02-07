package mod.emt.legendgear.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import mod.emt.legendgear.LegendGear;

public enum LGSoundEvents
{
    BLOCK_CALTROPS_HIT("block.caltrops.hit"),
    BLOCK_CALTROPS_LAND("block.caltrops.land"),
    BLOCK_GRIND_RAIL_CHOIR("block.grind_rail_choir"),
    BLOCK_GRIND_RAIL("block.grind_rail"),
    BLOCK_SWORD_PEDESTAL_PLACE("block.sword_pedestal.place"),
    BLOCK_SWORD_PEDESTAL_TAKE("block.sword_pedestal.take"),
    BLOCK_URN_PLACE("block.urn.place"),
    BLOCK_URN_SHATTER("block.urn.shatter"),
    BLOCK_URN_STEP("block.urn.step"),
    ENTITY_FALLING_STAR_CAUGHT("entity.falling_star.caught"),
    ENTITY_FALLING_STAR_DESPAWN("entity.falling_star.despawn"),
    ENTITY_FALLING_STAR_EXPLODE("entity.falling_star.explode"),
    ENTITY_FALLING_STAR_FALL("entity.falling_star.fall"),
    ENTITY_FALLING_STAR_LAND("entity.falling_star.land"),
    ENTITY_FALLING_STAR_TWINKLE("entity.falling_star.twinkle"),
    ENTITY_MAGIC_BOOMERANG_FLY("entity.magic_boomerang.fly"),
    ENTITY_MEDALLION_FIRE_START("entity.medallion.fire_start"),
    ITEM_AMULET_REPEL("item.amulet.repel"),
    ITEM_BAUBLE_EQUIP("item.bauble.equip"),
    ITEM_BAUBLE_UNEQUIP("item.bauble.unequip"),
    ITEM_BEST_BOW_SHOOT("item.best_bow.shoot"),
    ITEM_CHARM_REPEL("item.charm.repel"),
    ITEM_EMERALD_DROP_PICKUP_LARGE("item.emerald_drop.pickup_large"),
    ITEM_EMERALD_DROP_PICKUP_MEDIUM("item.emerald_drop.pickup_medium"),
    ITEM_EMERALD_DROP_PICKUP_SMALL("item.emerald_drop.pickup_small"),
    ITEM_EMERALD_EXCHANGE("item.emerald.exchange"),
    ITEM_FLUTE_ATTACK("item.flute.attack"),
    ITEM_FLUTE_SUSTAIN("item.flute.sustain"),
    ITEM_FORTUNE_COOKIE_USE("item.fortune_cookie.use"),
    ITEM_MAGIC_MIRROR_INACTIVE("item.magic_mirror.inactive"),
    ITEM_MAGIC_MIRROR_USE("item.magic_mirror.use"),
    ITEM_MAGIC_POWDER_SPRINKLE("item.magic_powder.sprinkle"),
    ITEM_MAGIC_POWDER_TRANSFORM("item.magic_powder.transform"),
    ITEM_MEDALLION_CHARGE_FULL("item.medallion.charge_full"),
    ITEM_MEDALLION_CHARGE_PARTIAL("item.medallion.charge_partial"),
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
    ITEM_TITAN_BAND_LIFT("item.titan_band.lift"),
    ITEM_TITAN_BAND_THROW("item.titan_band.throw"),
    RANDOM_DAMAGE_BOOST("random.damage_boost"),
    RANDOM_DASH("random.dash"),
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
