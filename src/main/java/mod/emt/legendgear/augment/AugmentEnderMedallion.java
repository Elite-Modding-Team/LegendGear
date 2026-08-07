package mod.emt.legendgear.augment;

import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.network.LGPacketHandler;
import mod.emt.legendgear.network.PacketEnderBeam;
import mod.emt.legendgear.network.PacketEnderTeleport;
import mod.emt.legendgear.util.MedallionAugmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AugmentEnderMedallion implements IMedallionAugment
{
    @Override
    public void executeAbility(EntityPlayer player, EnumHand hand, ItemStack tool)
    {
        if (player.world.isRemote) return;

        MedallionAugmentHelper data = MedallionAugmentHelper.getPlayer(player);
        if (data.getRecallTime() > 0)
        {
            bamf((EntityPlayerMP) player, data.getRecallX(), data.getRecallY(), data.getRecallZ(), false, false);
            data.clearRecall();
            return;
        }

        EntityLivingBase target = beamScan(player, 64.0D);
        if (target == null)
        {
            player.world.playSound(null, player.posX, player.posY, player.posZ, LGSoundEvents.ITEM_AUGMENT_ENDER_BEAM.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 0.7F);
            return;
        }

        data.beginRecall(player);
        double angle = Math.atan2(target.posZ - player.posZ, target.posX - player.posX);
        double backoff = 3.0D;
        bamf((EntityPlayerMP) player, target.posX - Math.cos(angle) * backoff, target.posY, target.posZ - Math.sin(angle) * backoff, true, false);
    }

    public void update(EntityPlayerMP player)
    {
        MedallionAugmentHelper data = MedallionAugmentHelper.getPlayer(player);

        if (data.getRecallTime() <= 0)
        {
            return;
        }

        data.setRecallTime(data.getRecallTime() - 1);

        if (data.getRecallTime() <= 0)
        {
            bamf(player, data.getRecallX(), data.getRecallY(), data.getRecallZ(), false, false);
            data.clearRecall();
        }
    }

    public static void bamf(EntityPlayerMP player, double x, double y, double z, boolean startOverlay, boolean slashSound)
    {
        if (!player.connection.getNetworkManager().isChannelOpen() || player.isPlayerSleeping())
        {
            return;
        }

        if (player.isRiding())
        {
            player.dismountRidingEntity();
        }

        player.fallDistance = 0.0F;
        player.connection.setPlayerLocation(x, y, z, player.rotationYaw, player.rotationPitch);
        player.world.playSound(null, x, y, z, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);

        if (slashSound)
        {
            player.world.playSound(null, player.posX, player.posY, player.posZ, LGSoundEvents.RANDOM_SWORD_SLASH.getSoundEvent(), SoundCategory.PLAYERS, 0.6F, 1.3F + player.world.rand.nextFloat() * 0.4F);
            PacketEnderTeleport packet = new PacketEnderTeleport(x, y, z, startOverlay, true);
            LGPacketHandler.INSTANCE.sendTo(packet, player);
            LGPacketHandler.INSTANCE.sendToAllTracking(packet, player);
        } else {
            PacketEnderTeleport packet = new PacketEnderTeleport(x, y, z, startOverlay, false);
            LGPacketHandler.INSTANCE.sendTo(packet, player);
            LGPacketHandler.INSTANCE.sendToAllTracking(packet, player);
        }
    }

    private EntityLivingBase beamScan(EntityPlayer player, double range)
    {
        AxisAlignedBB box = new AxisAlignedBB(player.posX, player.posY + player.getEyeHeight(), player.posZ, player.posX, player.posY + player.getEyeHeight(), player.posZ).grow(1.0D);
        Vec3d step = player.getLookVec();

        for (int i = 0; i < range; i++)
        {
            List<EntityLivingBase> hits = player.world.getEntitiesWithinAABB(EntityLivingBase.class, box);
            for (EntityLivingBase entity : hits)
            {
                if (entity != player && entity.isEntityAlive() && entity.canBeCollidedWith())
                {
                    return entity;
                }
            }

            box = box.offset(step);
            Vec3d center = new Vec3d((box.minX + box.maxX) * 0.5D, (box.minY + box.maxY) * 0.5D, (box.minZ + box.maxZ) * 0.5D);
            PacketEnderBeam packet = new PacketEnderBeam(center.x, center.y, center.z);
            LGPacketHandler.INSTANCE.sendTo(packet, (EntityPlayerMP) player);
            LGPacketHandler.INSTANCE.sendToAllTracking(packet, player);
        }

        return null;
    }

    @Override
    public int getChargeTime()
    {
        return 20;
    }

    @Override
    public int getActivationWindow()
    {
        return 20;
    }
}