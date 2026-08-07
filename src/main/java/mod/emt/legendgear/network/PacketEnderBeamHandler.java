package mod.emt.legendgear.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEnderBeamHandler implements IMessageHandler<PacketEnderBeam, IMessage>
{
    @Override
    public IMessage onMessage(PacketEnderBeam message, MessageContext ctx)
    {
        LGClientPacketHandler.handleEnderBeam(message);
        return null;
    }
}