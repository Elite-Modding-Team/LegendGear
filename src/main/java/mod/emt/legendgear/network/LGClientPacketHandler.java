package mod.emt.legendgear.network;

import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.util.MedallionAugmentHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class LGClientPacketHandler {
    private static final Random RAND = new Random();

    public static void handleEnderBeam(PacketEnderBeam packet)
    {
        Minecraft.getMinecraft().addScheduledTask(() ->
        {
            World world = Minecraft.getMinecraft().world;
            if (world == null)
            {
                return;
            }
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, packet.getX(), packet.getY(), packet.getZ(), 0.0D, 0.0D, 0.0D);
        });
    }

    public static void handleEnderTeleport(PacketEnderTeleport packet)
    {
        Minecraft.getMinecraft().addScheduledTask(() ->
        {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.world == null || mc.player == null) {
                return;
            }

            EntityPlayerSP player = mc.player;
            player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);

            if (packet.shouldPlaySlashSound())
            {
                player.playSound(LGSoundEvents.RANDOM_SWORD_SLASH.getSoundEvent(), 1.0F, 1.3F + mc.world.rand.nextFloat() * 0.4F);
            }

            for (int i = 0; i < 16; i++)
            {
                mc.world.spawnParticle(EnumParticleTypes.PORTAL, packet.getX() + RAND.nextGaussian(), packet.getY() + 1D + RAND.nextGaussian(), packet.getZ() + RAND.nextGaussian(), RAND.nextGaussian() * .1, RAND.nextGaussian() * .1, RAND.nextGaussian() * .1);
            }

            MedallionAugmentHelper data = MedallionAugmentHelper.getPlayer(player);

            if (packet.shouldStartOverlay())
            {
                data.setPortalTimer(50);
            } else {
                data.setPortalTimer(0);
                player.timeInPortal = 0F;
                player.prevTimeInPortal = 0F;
            }
        });
    }

    public static void handleAugmentState(PacketAugmentState packet)
    {
        Minecraft.getMinecraft().addScheduledTask(() ->
        {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.player == null || mc.world == null) {
                return;
            }
            MedallionAugmentHelper data = MedallionAugmentHelper.getPlayer(mc.player);
            switch (packet.getState())
            {
                case CHARGING:
                    data.setState(MedallionAugmentHelper.State.CHARGING);
                    break;
                case CHARGED:
                    data.setState(MedallionAugmentHelper.State.CHARGED);
                    mc.world.playSound(mc.player.posX, mc.player.posY, mc.player.posZ, LGSoundEvents.ITEM_AUGMENT_FULL_CHARGE.getSoundEvent(), SoundCategory.PLAYERS, 0.3F, 2.0F, false);
                    break;
                case IDLE:
                default:
                    data.reset();
                    break;
            }
        });
    }
}
