package mod.emt.legendgear.network;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.util.MedallionAugmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.SoundEvents;
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
    private boolean slashSound;

    public PacketEnderTeleport()
    {
    }

    public PacketEnderTeleport(double x, double y, double z, boolean startOverlay, boolean slashSound) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.startOverlay = startOverlay;
        this.slashSound = slashSound;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        startOverlay = buf.readBoolean();
        slashSound = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeBoolean(startOverlay);
        buf.writeBoolean(slashSound);
    }

    public static class Handler implements IMessageHandler<PacketEnderTeleport, IMessage>
    {
        @Override
        public IMessage onMessage(PacketEnderTeleport message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                World world = Minecraft.getMinecraft().world;
                if (world == null) return;
                EntityPlayerSP player = Minecraft.getMinecraft().player;
                if (player == null) return;

                player.setPositionAndUpdate(message.x, message.y, message.z);
                player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);

                if (message.slashSound) {
                    player.playSound(LGSoundEvents.RANDOM_SWORD_SLASH.getSoundEvent(), 1.0F, 1.3F + world.rand.nextFloat() * 0.4F);
                }

                for (int i = 0; i < 16; i++)
                {
                    world.spawnParticle(EnumParticleTypes.PORTAL, message.x + RAND.nextGaussian(), message.y + 1.0D + RAND.nextGaussian(), message.z + RAND.nextGaussian(), RAND.nextGaussian() * 0.1D, RAND.nextGaussian() * 0.1D, RAND.nextGaussian() * 0.1D);
                }

                MedallionAugmentHelper data = MedallionAugmentHelper.getPlayer(player);

                if (message.startOverlay)
                {
                    data.setPortalTimer(50);
                }
                else
                {
                    data.setPortalTimer(0);
                    player.timeInPortal = 0.0F;
                    player.prevTimeInPortal = 0.0F;
                }
            });
            return null;
        }
    }
}