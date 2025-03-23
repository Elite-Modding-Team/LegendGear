package mod.emt.legendgear.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGItems;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGQuiverEvent
{
    @SubscribeEvent
    public static void onDrawBow(ArrowNockEvent event)
    {
        EntityPlayer ep = event.getEntityPlayer();
        if (ep.capabilities.isCreativeMode) return;
        Item bow = event.getBow().getItem();
        if (bow instanceof ItemBow && ((ItemBow) bow).findAmmo(ep) == ItemStack.EMPTY)
        {
            drawArrowFromQuiver(ep);
        }
    }

    @SubscribeEvent
    public static void onArrowPickup(EntityItemPickupEvent event)
    {
        ItemStack stack = event.getItem().getItem();
        if (stack.getItem() != Items.ARROW) return;
        EntityPlayer player = event.getEntityPlayer();
        int arrowCount = stack.getCount();
        ItemStack quiver = findNonFullQuiver(event.getEntityPlayer());
        if (!quiver.isEmpty())
        {
            int arrowCountBag = Math.max(quiver.getItemDamage() - arrowCount, 0);
            int arrowCountItem = Math.max(arrowCount - quiver.getItemDamage(), 0);
            quiver.setItemDamage(arrowCountBag);
            quiver.setAnimationsToGo(5);
            stack.setCount(arrowCountItem);
            player.getEntityWorld().playSound(null, player.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, 0.4F);
            event.setResult(Event.Result.ALLOW);
        }
    }

    private static ItemStack findNonFullQuiver(EntityPlayer player)
    {
        NonNullList<ItemStack> inv = player.inventory.mainInventory;
        for (ItemStack stack : inv)
        {
            if (!stack.isEmpty() && stack.getItem() == LGItems.QUIVER && stack.getItemDamage() > 0) return stack;
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack findNonEmptyQuiver(EntityPlayer player)
    {
        NonNullList<ItemStack> inv = player.inventory.mainInventory;
        for (ItemStack stack : inv)
        {
            if (!stack.isEmpty() && stack.getItem() == LGItems.QUIVER && stack.getItemDamage() < stack.getMaxDamage()) return stack;
        }
        return ItemStack.EMPTY;
    }

    private static void drawArrowFromQuiver(EntityPlayer ep)
    {
        ItemStack quiver = findNonEmptyQuiver(ep);
        if (quiver != ItemStack.EMPTY)
        {
            ep.inventory.addItemStackToInventory(new ItemStack(Items.ARROW));
            quiver.setItemDamage(quiver.getItemDamage() + 1);
            quiver.setAnimationsToGo(5);
            ep.playSound(SoundEvents.ENTITY_IRONGOLEM_ATTACK, 0.4F, 0.9F + (ep.getRNG().nextFloat() / 2));
            ep.playSound(SoundEvents.ITEM_FLINTANDSTEEL_USE, 0.4F, 0.1F + (ep.getRNG().nextFloat() / 2));
        }
    }
}
