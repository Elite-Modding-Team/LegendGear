package mod.emt.legendgear.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.item.LGItemEmeraldPiece;
import mod.emt.legendgear.item.LGItemEmeraldShard;
import mod.emt.legendgear.item.LGItemRecoveryHeart;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGEntityItemPickupEvent
{
    @SubscribeEvent
    public static void onEntityItemPickup(EntityItemPickupEvent event)
    {
        Item item = event.getItem().getItem().getItem();
        EntityPlayer player = event.getEntityPlayer();
        World world = player.getEntityWorld();
        if (item == Items.EMERALD)
        {
            world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_EMERALD_DROP_PICKUP_LARGE.getSoundEvent(), SoundCategory.PLAYERS, 0.75F, 1F);
        }
        else if (item instanceof LGItemEmeraldPiece)
        {
            world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_EMERALD_DROP_PICKUP_MEDIUM.getSoundEvent(), SoundCategory.PLAYERS, 0.75F, 1F);
        }
        else if (item instanceof LGItemEmeraldShard)
        {
            world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_EMERALD_DROP_PICKUP_SMALL.getSoundEvent(), SoundCategory.PLAYERS, 0.75F, 1F);
        }
        else if (item instanceof LGItemRecoveryHeart)
        {
            world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_RECOVERY_HEART_PICKUP.getSoundEvent(), SoundCategory.PLAYERS, 0.75F, 1F);
        }
    }
}
