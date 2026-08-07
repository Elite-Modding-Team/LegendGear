package mod.emt.legendgear.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEnderTeleport implements IMessage
{
    private double x;
    private double y;
    private double z;
    private boolean startOverlay;
    private boolean slashSound;

    public PacketEnderTeleport()
    {
    }

    public PacketEnderTeleport(double x, double y, double z, boolean startOverlay, boolean slashSound)
    {
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

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }

    public boolean shouldStartOverlay()
    {
        return startOverlay;
    }

    public boolean shouldPlaySlashSound()
    {
        return slashSound;
    }
}