package mod.emt.legendgear.event;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGItems;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID, value = Side.CLIENT)
public class LGCraftEvent
{
    @SubscribeEvent
    public static void dimensionalCatalystCraftingSound(PlayerEvent.ItemCraftedEvent event)
    {
        if (!event.player.world.isRemote)
        {
            return;
        }

        if (!event.crafting.isEmpty() && event.crafting.getItem() == LGItems.DIMENSIONAL_CATALYST)
        {
            event.player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.5F, 1.0F);
            return;
        }

        IInventory inv = event.craftMatrix;
        for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == LGItems.DIMENSIONAL_CATALYST)
            {
                event.player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.5F, 1.0F);
                break;
            }
        }
    }
}
