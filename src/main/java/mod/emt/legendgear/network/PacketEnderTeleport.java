package mod.emt.legendgear.network;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.util.MedallionAugmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Random;

public class PacketEnderTeleport implements IMessage
{
    private static final Random RAND = new Random();
    private double x;
    private double y;
    private double z;
    private boolean startOverlay;

    public PacketEnderTeleport()
    {
    }

    public PacketEnderTeleport(double x, double y, double z, boolean startOverlay) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.startOverlay = startOverlay;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        startOverlay = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeBoolean(startOverlay);
    }

    public static class Handler implements IMessageHandler<PacketEnderTeleport, IMessage>
    {
        @Override
        public IMessage onMessage(PacketEnderTeleport message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                World world = Minecraft.getMinecraft().world;
                if (world == null) return;

                for (int i = 0; i < 16; i++)
                {
                    world.spawnParticle(EnumParticleTypes.PORTAL, message.x + RAND.nextGaussian(), message.y + 1.0D + RAND.nextGaussian(), message.z + RAND.nextGaussian(), RAND.nextGaussian() * 0.1D, RAND.nextGaussian() * 0.1D, RAND.nextGaussian() * 0.1D);
                }

                MedallionAugmentHelper data = MedallionAugmentHelper.getPlayer(Minecraft.getMinecraft().player);

                if (message.startOverlay)
                {
                    data.setPortalTimer(50);
                } else
                {
                    data.setPortalTimer(0);
                    Minecraft mc = Minecraft.getMinecraft();
                    mc.player.timeInPortal = 0.0F;
                    mc.player.prevTimeInPortal = 0.0F;
                }
            });
            return null;
        }
    }
}