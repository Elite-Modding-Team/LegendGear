package mod.emt.legendgear.network;

import mod.emt.legendgear.augment.IMedallionAugment;
import mod.emt.legendgear.augment.MedallionAugmentRegistry;
import mod.emt.legendgear.event.LGAugmentToolHandler;
import mod.emt.legendgear.util.MedallionAugmentHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketAugmentActivate implements IMessage
{
    @Override
    public void fromBytes(io.netty.buffer.ByteBuf buf)
    {
    }

    @Override
    public void toBytes(io.netty.buffer.ByteBuf buf)
    {
    }

    public static class Handler implements IMessageHandler<PacketAugmentActivate, IMessage>
    {
        @Override
        public IMessage onMessage(PacketAugmentActivate message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;

            player.getServerWorld().addScheduledTask(() ->
            {
                EnumHand hand = LGAugmentToolHandler.getActiveHand(player);
                if (hand == null) return;

                ItemStack stack = player.getHeldItem(hand);
                if (stack.isEmpty()) return;

                String medallion = MedallionAugmentHelper.getAugment(stack);
                if (medallion == null) return;

                MedallionAugmentHelper data = MedallionAugmentHelper.getPlayer(player);
                if (!data.isCharged()) return;

                IMedallionAugment augment = MedallionAugmentRegistry.get(medallion);
                if (augment == null) return;

                MedallionAugmentRegistry.trigger(player, hand, stack);
                stack.damageItem(augment.getDurabilityCost(), player);
                data.reset();
                LGPacketHandler.instance.sendTo(new PacketAugmentState(PacketAugmentState.State.IDLE), player);
            });

            return null;
        }
    }
}