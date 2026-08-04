package mod.emt.legendgear.network;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.util.MedallionAugmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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

    public static class Handler implements IMessageHandler<PacketAugmentState, IMessage>
    {
        @Override
        public IMessage onMessage(PacketAugmentState message, MessageContext ctx)
        {
            Minecraft.getMinecraft().addScheduledTask(() ->
            {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.player == null || mc.world == null) return;
                MedallionAugmentHelper data = MedallionAugmentHelper.getPlayer(mc.player);

                switch (message.getState())
                {
                    case CHARGING:
                        data.setState(MedallionAugmentHelper.State.CHARGING);
                        break;
                    case CHARGED:
                        data.setState(MedallionAugmentHelper.State.CHARGED);
                        mc.world.playSound(mc.player.posX, mc.player.posY, mc.player.posZ, LGSoundEvents.ITEM_STAR_PIECE_SPARKLE.getSoundEvent(), SoundCategory.PLAYERS, 0.7F, 2.0F, false);
                        break;
                    case IDLE:
                        data.reset();
                        break;
                }
            });
            return null;
        }
    }
}