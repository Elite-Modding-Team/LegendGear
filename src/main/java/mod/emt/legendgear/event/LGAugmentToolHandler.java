package mod.emt.legendgear.event;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.augment.AugmentEnderMedallion;
import mod.emt.legendgear.augment.IMedallionAugment;
import mod.emt.legendgear.augment.MedallionAugmentRegistry;
import mod.emt.legendgear.client.particle.LGParticleHandler;
import mod.emt.legendgear.network.*;
import mod.emt.legendgear.util.MedallionAugmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.util.Random;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGAugmentToolHandler
{
    private static boolean lastUseState = false;
    private static float visualCharge = 0;

    private static final Random RAND = new Random();
    private static final float SPARK_DISTANCE = 0.7F;
    private static final float DEG_TO_RAD = 0.017453292F;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END || event.player.world.isRemote)
        {
            return;
        }

        EntityPlayer player = event.player;
        ItemStack stack = getActiveTool(player);
        MedallionAugmentHelper data = MedallionAugmentHelper.getPlayer(player);
        IMedallionAugment ability = MedallionAugmentRegistry.get(MedallionAugmentHelper.getAugment(stack));

        // Ender Medallion recall
        if (ability instanceof AugmentEnderMedallion)
        {
            ((AugmentEnderMedallion) ability).update((EntityPlayerMP) player);
        }

        if (ability == null || !isTool(stack))
        {
            if (!data.isIdle())
            {
                data.reset();
                LGPacketHandler.INSTANCE.sendTo(new PacketAugmentState(PacketAugmentState.State.IDLE), (EntityPlayerMP) player);
            }
            return;
        }

        if (data.isCharging())
        {
            if (data.getCharge() < ability.getChargeTime())
            {
                data.incrementCharge(ability.getChargeRate());
                if (data.getCharge() >= ability.getChargeTime())
                {
                    data.becomeCharged(Integer.MAX_VALUE);
                    LGPacketHandler.INSTANCE.sendTo(new PacketAugmentState(PacketAugmentState.State.CHARGED), (EntityPlayerMP) player);
                }
            }
        } else if (data.isCharged())
        {
            if (!data.isHoldingCharge())
            {
                data.decrementEnergy();
                if (data.getEnergy() <= 0)
                {
                    data.reset();
                    LGPacketHandler.INSTANCE.sendTo(new PacketAugmentState(PacketAugmentState.State.IDLE), (EntityPlayerMP) player);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onClientTickInput(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null) return;

        // Cancel charging when any interface is opened
        if (mc.currentScreen != null)
        {
            if (lastUseState)
            {
                lastUseState = false;
                LGPacketHandler.INSTANCE.sendToServer(new PacketAugmentCharge(false));
            }
            return;
        }

        ItemStack stack = getActiveTool(mc.player);
        if (!isTool(stack)) return;
        if (MedallionAugmentHelper.getAugment(stack) == null) return;

        boolean holding = mc.gameSettings.keyBindUseItem.isKeyDown();
        if (holding != lastUseState)
        {
            lastUseState = holding;
            MedallionAugmentHelper data = MedallionAugmentHelper.getPlayer(mc.player);

            if (holding && data.isCharged() && !data.isHoldingCharge() && getActiveHand(mc.player) == EnumHand.OFF_HAND)
            {
                mc.player.swingArm(EnumHand.OFF_HAND);
            }

            LGPacketHandler.INSTANCE.sendToServer(new PacketAugmentCharge(holding));
        }

        MedallionAugmentHelper data = MedallionAugmentHelper.getPlayer(mc.player);
        if (data.getPortalTimer() > 0)
        {
            data.setPortalTimer(data.getPortalTimer() - 1);
            mc.player.timeInPortal = Math.min(1.0F, data.getPortalTimer() / 50.0F);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onMouseInput(InputEvent.MouseInputEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.player == null || mc.currentScreen != null)
        {
            return;
        }

        if (!Mouse.getEventButtonState() || Mouse.getEventButton() != 0)
        {
            return;
        }

        MedallionAugmentHelper data = MedallionAugmentHelper.getPlayer(mc.player);

        if (!data.isCharged() || data.isHoldingCharge())
        {
            return;
        }

        if (getActiveHand(mc.player) != EnumHand.MAIN_HAND)
        {
            return;
        }

        LGPacketHandler.INSTANCE.sendToServer(new PacketAugmentActivate());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onClientParticles(TickEvent.ClientTickEvent event)
    {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player == null || mc.world == null) return;

        ItemStack stack = getActiveTool(mc.player);
        if (!isTool(stack)) return;

        IMedallionAugment ability = MedallionAugmentRegistry.get(MedallionAugmentHelper.getAugment(stack));
        if (ability == null) return;

        MedallionAugmentHelper data = MedallionAugmentHelper.getPlayer(mc.player);

        if (data.isCharging())
        {
            visualCharge += ability.getChargeRate();

            if (visualCharge > ability.getChargeTime())
            {
                visualCharge = ability.getChargeTime();
            }

            spawnChargingParticles(mc.player, visualCharge, ability.getChargeTime());
        } else
        {
            visualCharge = 0.0F;
        }

        if (data.isCharged())
        {
            spawnActiveParticles(mc.player);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void spawnChargingParticles(EntityPlayer player, float charge, int maxChargeTime)
    {
        double[] pos = getHandPosition(player);
        double x = pos[0], y = pos[1], z = pos[2];
        double offx = MathHelper.cos(player.rotationYaw * DEG_TO_RAD) * SPARK_DISTANCE;
        double offz = MathHelper.sin(player.rotationYaw * DEG_TO_RAD) * SPARK_DISTANCE;
        float progress = MathHelper.clamp(charge / (float) maxChargeTime, 0.0F, 1.0F);
        double r = 1.0D - progress;
        double theta = r * Math.PI * 3.0D;
        double h = Math.cos(theta) * r;
        double v = Math.sin(theta) * r;
        offx *= h;
        offz *= h;
        double sparkSpan = 0.6D;
        LGParticleHandler.spawnSparkleFX(player.world, x + offx * sparkSpan, y + v * sparkSpan, z + offz * sparkSpan, 0.0D, 0.0D, 0.0D, 0.3F);
        LGParticleHandler.spawnSparkleFX(player.world, x - offx * sparkSpan, y - v * sparkSpan, z - offz * sparkSpan, 0.0D, 0.0D, 0.0D, 0.3F);
    }

    @SideOnly(Side.CLIENT)
    public static void spawnActiveParticles(EntityPlayer player)
    {
        double[] pos = getHandPosition(player);
        double x = pos[0] + RAND.nextGaussian() * 0.05D;
        double y = pos[1] + RAND.nextGaussian() * 0.05D;
        double z = pos[2] + RAND.nextGaussian() * 0.05D;
        LGParticleHandler.spawnSparkleFX(player.world, x, y, z, 0.0D, 0.0D, 0.0D, 0.6F);
    }

    @SideOnly(Side.CLIENT)
    private static double[] getHandPosition(EntityPlayer player)
    {
        Minecraft mc = Minecraft.getMinecraft();
        float partialTicks = mc.getRenderPartialTicks();
        double px = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
        double py = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
        double pz = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
        float yawRad = player.rotationYaw * DEG_TO_RAD;
        float pitchRad = player.rotationPitch * DEG_TO_RAD;
        double x = px - (MathHelper.sin(yawRad) * SPARK_DISTANCE) * MathHelper.cos(pitchRad);
        double y = py + player.getEyeHeight() - (MathHelper.sin(pitchRad) * SPARK_DISTANCE) - 0.5D;
        double z = pz + (MathHelper.cos(yawRad) * SPARK_DISTANCE) * MathHelper.cos(pitchRad);
        return new double[]{x, y, z};
    }

    public static EnumHand getActiveHand(EntityPlayer player)
    {
        ItemStack main = player.getHeldItemMainhand();

        if (isTool(main) && MedallionAugmentHelper.getAugment(main) != null)
        {
            return EnumHand.MAIN_HAND;
        }

        ItemStack off = player.getHeldItemOffhand();
        if (isTool(off) && MedallionAugmentHelper.getAugment(off) != null)
        {
            return EnumHand.OFF_HAND;
        }

        return null;
    }

    private static ItemStack getActiveTool(EntityPlayer player)
    {
        EnumHand hand = getActiveHand(player);
        return hand == null ? ItemStack.EMPTY : player.getHeldItem(hand);
    }

    private static boolean isTool(ItemStack stack)
    {
        if (stack.isEmpty()) return false;
        return stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool;
    }
}