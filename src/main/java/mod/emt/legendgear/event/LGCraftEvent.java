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
    private static long lastCraftSoundTime = 0L;

    @SubscribeEvent
    public static void dimensionalCatalystCraftingSound(PlayerEvent.ItemCraftedEvent event)
    {
        if (lastCraftSoundTime != event.player.world.getWorldTime())
        {
            final ItemStack result = event.crafting;

            if (!result.isEmpty() && result.getItem() == LGItems.DIMENSIONAL_CATALYST)
            {
                event.player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.5F, 1.0F);
                lastCraftSoundTime = event.player.world.getWorldTime();
                return;
            }

            final IInventory inv = event.craftMatrix;

            for (int slots = inv.getSizeInventory(), i = 0; i < slots; ++i)
            {
                if (inv.getStackInSlot(i).isEmpty() && inv.getStackInSlot(i).getItem() == LGItems.DIMENSIONAL_CATALYST)
                {
                    event.player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.5F, 1.0F);
                    lastCraftSoundTime = event.player.world.getWorldTime();
                    break;
                }
            }
        }
    }
}
