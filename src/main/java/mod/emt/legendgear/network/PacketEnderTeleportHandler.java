package mod.emt.legendgear.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEnderTeleportHandler implements IMessageHandler<PacketEnderTeleport, IMessage>
{
    @Override
    public IMessage onMessage(PacketEnderTeleport message, MessageContext ctx)
    {
        LGClientPacketHandler.handleEnderTeleport(message);
        return null;
    }
}