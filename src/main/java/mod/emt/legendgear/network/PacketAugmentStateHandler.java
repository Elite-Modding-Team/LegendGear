package mod.emt.legendgear.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketAugmentStateHandler implements IMessageHandler<PacketAugmentState, IMessage>
{
    @Override
    public IMessage onMessage(PacketAugmentState message, MessageContext ctx)
    {
        LGClientPacketHandler.handleAugmentState(message);
        return null;
    }
}