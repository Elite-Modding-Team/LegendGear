package mod.emt.legendgear.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.entity.LGEntityBomb;
import mod.emt.legendgear.init.LGItems;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGBombEvent
{
    @SubscribeEvent
    public static void onBombPickup(EntityItemPickupEvent event)
    {
        ItemStack stack = event.getItem().getItem();
        if (stack.getItem() != LGItems.BOMB) return;
        EntityPlayer player = event.getEntityPlayer();
        int bombCount = stack.getCount();
        ItemStack bag = findNonFullBombBag(event.getEntityPlayer());
        if (!bag.isEmpty())
        {
            int bombCountBag = Math.max(bag.getItemDamage() - bombCount, 0);
            int bombCountItem = Math.max(bombCount - bag.getItemDamage(), 0);
            bag.setItemDamage(bombCountBag);
            bag.setAnimationsToGo(5);
            stack.setCount(bombCountItem);
            player.getEntityWorld().playSound(null, player.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.4F);
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public static void onSpawnBombArrow(EntityJoinWorldEvent event)
    {
        if (!event.getWorld().isRemote && event.getEntity() instanceof EntityArrow)
        {
            EntityArrow arrow = ((EntityArrow) event.getEntity());
            if (arrow.shootingEntity instanceof EntityPlayer)
            {
                EntityPlayer shooter = ((EntityPlayer) arrow.shootingEntity);
                if (shooter.isSneaking())
                {
                    ItemStack bombBag = findNonEmptyBombBag(shooter);
                    if (bombBag != ItemStack.EMPTY)
                    {
                        if (!shooter.capabilities.isCreativeMode) bombBag.setItemDamage(bombBag.getItemDamage() + 1);
                        LGEntityBomb eb = new LGEntityBomb(shooter.world, shooter, 80);
                        shooter.world.spawnEntity(eb);
                        eb.startRiding(arrow);
                        arrow.setFire(100);
                        arrow.addVelocity(-arrow.motionX * 0.4D, -arrow.motionY * 0.4D, -arrow.motionZ * 0.4D);
                        arrow.velocityChanged = true;
                    }
                }
            }
        }
    }

    private static ItemStack findNonFullBombBag(EntityPlayer player)
    {
        NonNullList<ItemStack> inv = player.inventory.mainInventory;
        for (ItemStack stack : inv)
        {
            if (!stack.isEmpty() && stack.getItem() == LGItems.BOMB_BAG && stack.getItemDamage() > 0) return stack;
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack findNonEmptyBombBag(EntityPlayer player)
    {
        NonNullList<ItemStack> inv = player.inventory.mainInventory;
        for (ItemStack stack : inv)
        {
            if (!stack.isEmpty() && stack.getItem() == LGItems.BOMB_BAG && stack.getItemDamage() < stack.getMaxDamage()) return stack;
        }
        return ItemStack.EMPTY;
    }
}
