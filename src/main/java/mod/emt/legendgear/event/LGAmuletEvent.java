package mod.emt.legendgear.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

import baubles.api.cap.BaublesCapabilities;
import baubles.api.inv.BaublesInventoryWrapper;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.client.particle.LGParticleHandler;
import mod.emt.legendgear.entity.LGEntityQuake;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.item.LGItemAeroAmulet;
import mod.emt.legendgear.item.LGItemGeoAmulet;
import mod.emt.legendgear.item.LGItemPyroAmulet;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGAmuletEvent
{
    @SubscribeEvent
    public static void onAmuletEffectAttack(LivingAttackEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            DamageSource source = event.getSource();
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            World world = player.getEntityWorld();
            BaublesInventoryWrapper inv = new BaublesInventoryWrapper(player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null));
            ItemStack stack = inv.getStackInSlot(0);
            if (stack.getItem() instanceof LGItemAeroAmulet && source.isProjectile())
            {
                Entity projectile = source.getImmediateSource();
                if (projectile instanceof EntityArrow)
                {
                    EntityArrow arrow = (EntityArrow) projectile;
                    if (!world.isRemote)
                    {
                        Vec3d look = player.getLookVec();
                        double speed = arrow.motionX * arrow.motionX + arrow.motionY * arrow.motionY + arrow.motionZ * arrow.motionZ;
                        speed = Math.sqrt(speed);
                        speed *= 0.1F;
                        arrow.motionX = look.x * speed;
                        arrow.motionY = look.y * speed;
                        arrow.motionZ = look.z * speed;
                        arrow.rotationYaw = (float) (Math.atan2(arrow.motionX, arrow.motionZ) * 180.0D / Math.PI);
                        arrow.rotationPitch = (float) (Math.atan2(arrow.motionY, speed) * 180.0D / Math.PI);
                        if (player instanceof EntityPlayerMP)
                            ((EntityPlayerMP) player).connection.sendPacket(new SPacketEntityVelocity(arrow));
                        arrow.shootingEntity = player;
                        arrow.motionX /= -0.10000000149011612D;
                        arrow.motionY /= -0.10000000149011612D;
                        arrow.motionZ /= -0.10000000149011612D;
                        double power = arrow.getDamage();
                        stack.damageItem((int) power, player);
                        arrow.setDamage(power * 1.5D);
                        arrow.setIsCritical(true);
                        world.playSound(null, arrow.getPosition(), LGSoundEvents.ITEM_AMULET_REPEL.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 0.8F + world.rand.nextFloat() * 0.4F);
                    }
                    else
                    {
                        // TODO: Improve particles to be similar to 1.5.2
                        for (int j = 0; j < 3; j++)
                        {
                            double azimuth = 5 * 0.3D + j * Math.PI * 2.0D / 3.0D;
                            double r = 2.0D;
                            double elevation = Math.sin(5 * 0.13D + j * Math.PI * 2.0D / 3.0D);
                            double y = Math.sin(elevation) * r + player.posY;
                            double out = Math.cos(elevation);
                            double x = Math.sin(azimuth) * out * r + player.posX;
                            double z = Math.cos(azimuth) * out * r + player.posZ;
                            LGParticleHandler.spawnMagicRuneFX(player.world, x, y, z, player.getRNG().nextGaussian() * 0.002D, player.getRNG().nextGaussian() * 0.002D, player.getRNG().nextGaussian() * 0.002D, 1.0F);
                        }

                        LGParticleHandler.spawnMagicScrambleFX(player.world, arrow.posX, arrow.posY, arrow.posZ, arrow.motionX * 0.2D, arrow.motionY * 0.2D, arrow.motionZ * 0.2D, 6.0F);
                    }

                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onAmuletEffectHurt(LivingHurtEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            DamageSource source = event.getSource();
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            World world = player.getEntityWorld();
            BaublesInventoryWrapper inv = new BaublesInventoryWrapper(player.getCapability(BaublesCapabilities.CAPABILITY_BAUBLES, null));
            ItemStack stack = inv.getStackInSlot(0);
            if (stack.getItem() instanceof LGItemGeoAmulet && source == DamageSource.FALL)
            {
                stack.damageItem((int) event.getAmount(), event.getEntityLiving());
                if (!world.isRemote)
                {
                    float scale = Math.min(event.getAmount() / 20.0F, 1.0F);
                    double radius = 3.0D + 12.0D * scale;
                    LGEntityQuake quake = new LGEntityQuake(world, player.posX, player.posY, player.posZ, player, true, radius);
                    quake.damage_per_hit = (int) event.getAmount();
                    world.spawnEntity(quake);
                    world.playSound(null, quake.getPosition(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.4F + 3.0F * scale, 1.2F - scale * 0.8F);
                }
                event.setCanceled(true);
            }
            else if (stack.getItem() instanceof LGItemPyroAmulet && source.isFireDamage())
            {
                if (!world.isRemote)
                {
                    stack.damageItem((int) event.getAmount(), event.getEntityLiving());
                    player.heal(event.getAmount());
                    world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_RECOVERY_HEART_PICKUP.getSoundEvent(), SoundCategory.PLAYERS, 0.2F, 1.0F);
                    player.hurtResistantTime = player.maxHurtResistantTime;
                    player.extinguish();
                }
                event.setCanceled(true);
            }
        }
    }
}