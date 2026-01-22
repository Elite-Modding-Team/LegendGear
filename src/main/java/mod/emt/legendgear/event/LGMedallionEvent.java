package mod.emt.legendgear.event;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.util.InventoryHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// TODO: Maybe a better way of recharging the Ender Medallion
@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGMedallionEvent
{
    @SubscribeEvent
    public static void chargeMedallionHurtEvent(LivingHurtEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (!player.world.isRemote)
            {
                if (event.getSource().damageType.equals("fall"))
                {
                    chargeMedallion(player, LGItems.EARTH_MEDALLION, event.getAmount());
                }
                if (event.getSource().damageType.equals("arrow"))
                {
                    chargeMedallion(player, LGItems.WIND_MEDALLION, event.getAmount());
                }
                if (event.getSource().isFireDamage())
                {
                    chargeMedallion(player, LGItems.FIRE_MEDALLION, event.getAmount());
                }
                if (event.getSource().getTrueSource() instanceof EntityEnderman
                        || event.getSource().getTrueSource() instanceof EntityEndermite
                        || event.getSource().getTrueSource() instanceof EntityShulker
                        || event.getSource().getTrueSource() instanceof EntityDragon)
                {
                    chargeMedallion(player, LGItems.ENDER_MEDALLION, event.getAmount());
                }
            }
        }
    }

    public static void chargeMedallion(EntityPlayer player, Item item, float amount)
    {
        ItemStack stack = InventoryHelper.getDamagedItemStack(player, item);
        if (stack != null)
        {
            stack.setItemDamage((int) Math.max(0, stack.getItemDamage() - amount));
            player.world.playSound(null, player.getPosition(), stack.getItemDamage() == 0 ? LGSoundEvents.ITEM_MEDALLION_CHARGE_FULL.getSoundEvent() : LGSoundEvents.ITEM_MEDALLION_CHARGE_PARTIAL.getSoundEvent(), SoundCategory.NEUTRAL, 0.2F, 1.0F);
        }
    }
}
