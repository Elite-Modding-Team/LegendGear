package mod.emt.legendgear.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LGPacketJump implements IMessage, IMessageHandler<LGPacketJump, IMessage>
{
    private int entityId;

    public LGPacketJump() {}
    public LGPacketJump(int id)
    {
        this.entityId = id;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.entityId);
    }

    @Override
    public IMessage onMessage(LGPacketJump message, MessageContext ctx)
    {
        FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
            EntityPlayer player = ctx.getServerHandler().player;
            Entity target = player.world.getEntityByID(message.entityId);

            if (target != null)
            {
                target.getEntityData().setBoolean("clientJustJumped", true);
            }
        });

        return null;
    }
}
