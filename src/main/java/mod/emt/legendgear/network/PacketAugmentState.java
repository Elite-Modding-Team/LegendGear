package mod.emt.legendgear.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketAugmentState implements IMessage
{
    private State state;

    public enum State
    {
        IDLE,
        CHARGING,
        CHARGED
    }

    public PacketAugmentState()
    {
    }

    public PacketAugmentState(State state)
    {
        this.state = state;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        int ordinal = buf.readInt();
        if (ordinal < 0 || ordinal >= State.values().length)
        {
            state = State.IDLE;
        } else
        {
            state = State.values()[ordinal];
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(state.ordinal());
    }

    public State getState()
    {
        return state;
    }
}