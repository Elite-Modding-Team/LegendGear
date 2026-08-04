package mod.emt.legendgear.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketEnderBeam implements IMessage
{
    private double x;
    private double y;
    private double z;

    public PacketEnderBeam()
    {
    }

    public PacketEnderBeam(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public static class Handler implements IMessageHandler<PacketEnderBeam, IMessage>
    {
        @Override
        public IMessage onMessage(PacketEnderBeam message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                World world = Minecraft.getMinecraft().world;
                if (world == null) return;
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, message.x, message.y, message.z, 0.0D, 0.0D, 0.0D);
            });
            return null;
        }
    }
}