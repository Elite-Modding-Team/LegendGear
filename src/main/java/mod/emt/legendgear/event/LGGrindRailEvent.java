package mod.emt.legendgear.event;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.network.LGPacketHandler;
import mod.emt.legendgear.network.LGPacketJump;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGGrindRailEvent {
    @SubscribeEvent
    public static void handleJumpEdge(LivingEvent.LivingUpdateEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer ep = (EntityPlayer) event.getEntityLiving();
            NBTTagCompound data = ep.getEntityData();

            if (data.getBoolean("clientJustJumped"))
            {
                data.setBoolean("clientJustJumped", false);
                data.setBoolean("justJumped", true);
            } else
            {
                data.setBoolean("justJumped", false);
            }

            if (ep.isJumping)
            {
                boolean wasJumping = data.getBoolean("wasJumping");
                if (!wasJumping && ep.isJumping)
                {
                    data.setBoolean("justJumped", true);

                    if (ep.world.isRemote)
                    {
                        LGPacketHandler.INSTANCE.sendToServer(new LGPacketJump(ep.getEntityId()));
                    }
                }
            }

            data.setBoolean("wasJumping", ep.isJumping);
        }
    }
}
