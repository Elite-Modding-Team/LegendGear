package mod.emt.legendgear.event;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.block.LGBlockLightningStruck;
import mod.emt.legendgear.block.LGBlockLightningStruckFalling;
import mod.emt.legendgear.init.LGItems;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGHarvestDropsEvent
{
    @SubscribeEvent
    public static void onFulguriteHarvest(BlockEvent.HarvestDropsEvent event)
    {
        // Light-struck blocks dug by shovels will drop Fulgurite
        if (event.getHarvester() != null && (event.getHarvester().getHeldItemMainhand().getItem().getToolClasses(event.getHarvester().getHeldItemMainhand()).contains("shovel") && !event.isSilkTouching()))
        {
            if (event.getState().getBlock() instanceof LGBlockLightningStruck || event.getState().getBlock() instanceof LGBlockLightningStruckFalling)
            {
                List<ItemStack> to_be_removed = new ArrayList<ItemStack>();
                List<ItemStack> to_be_added = new ArrayList<ItemStack>();

                to_be_added.add(new ItemStack(LGItems.FULGURITE));

                // If block drops something remove it in favor of the new item
                if (!event.getDrops().isEmpty())
                {
                    to_be_removed.addAll(event.getDrops());
                }

                event.getDrops().addAll(to_be_added);
                event.getDrops().removeAll(to_be_removed);
            }
        }
    }
}
