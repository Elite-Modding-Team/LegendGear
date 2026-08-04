package mod.emt.legendgear.network;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.augment.AugmentEnderMedallion;
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

public class PacketAugmentCharge implements IMessage
{
    private boolean charging;

    public PacketAugmentCharge()
    {
    }

    public PacketAugmentCharge(boolean charging)
    {
        this.charging = charging;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        charging = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(charging);
    }

    public static class Handler implements IMessageHandler<PacketAugmentCharge, IMessage>
    {
        @Override
        public IMessage onMessage(PacketAugmentCharge message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() ->
            {
                MedallionAugmentHelper data = MedallionAugmentHelper.getPlayer(player);

                if (message.charging)
                {
                    EnumHand hand = LGAugmentToolHandler.getActiveHand(player);
                    if (hand == null) return;
                    ItemStack stack = player.getHeldItem(hand);
                    IMedallionAugment ability = MedallionAugmentRegistry.get(MedallionAugmentHelper.getAugment(stack));
                    if (ability == null) return;

                    // Ender Medallion recall
                    if (data.getRecallTime() > 0)
                    {
                        AugmentEnderMedallion.bamf(player, data.getRecallX(), data.getRecallY(), data.getRecallZ(), false, false);
                        data.clearRecall();
                        return;
                    }

                    // Offhand support
                    if (hand == EnumHand.OFF_HAND && data.isCharged() && !data.isHoldingCharge())
                    {
                        MedallionAugmentRegistry.trigger(player, hand, stack);
                        stack.damageItem(ability.getDurabilityCost(), player);
                        data.reset();
                        LGPacketHandler.instance.sendTo(new PacketAugmentState(PacketAugmentState.State.IDLE), player);
                        return;
                    }

                    if (data.isIdle())
                    {
                        data.beginCharging();
                        LGPacketHandler.instance.sendTo(new PacketAugmentState(PacketAugmentState.State.CHARGING), player);
                    }
                } else
                {
                    EnumHand hand = LGAugmentToolHandler.getActiveHand(player);
                    if (hand == null) return;
                    ItemStack stack = player.getHeldItem(hand);
                    IMedallionAugment ability = MedallionAugmentRegistry.get(MedallionAugmentHelper.getAugment(stack));
                    if (ability == null) return;

                    if (data.isCharging())
                    {
                        data.reset();
                        LGPacketHandler.instance.sendTo(new PacketAugmentState(PacketAugmentState.State.IDLE), player);
                        return;
                    }

                    if (data.isCharged() && data.isHoldingCharge())
                    {
                        data.setHoldingCharge(false);
                        data.setEnergy(ability.getActivationWindow());
                    }
                }
            });
            return null;
        }
    }
}