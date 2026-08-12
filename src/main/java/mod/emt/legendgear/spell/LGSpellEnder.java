package mod.emt.legendgear.spell;

import mod.emt.legendgear.client.particle.LGParticleSpell;
import mod.emt.legendgear.client.render.LGRenderSpellDecorator;
import mod.emt.legendgear.client.render.LGRenderSpellReticle;
import mod.emt.legendgear.entity.LGEntitySpellDecorator;
import mod.emt.legendgear.entity.LGEntitySpellEffect;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.network.LGPacketHandler;
import mod.emt.legendgear.network.PacketEnderTeleport;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class LGSpellEnder extends LGSpell
{
    protected LGSpellEnder(String id)
    {
        super(id);
    }

    @Override
    public double getBaseCastRange()
    {
        return 15.0D;
    }

    @Override
    public double getBaseCastRadius()
    {
        return 3.0D;
    }

    @Override
    public double getBaseArcanePower()
    {
        return 0.0D;
    }

    @Override
    public boolean canAffectCaster()
    {
        return true;
    }

    @Override
    public double getKnockback(boolean critical)
    {
        return 0.0D;
    }

    @Override
    public double getBaseCastTime()
    {
        return 0.75D;
    }

    @Override
    public double getBaseCritBonus()
    {
        return 0.0D;
    }

    @Override
    public void onLivingEntityHit(LGEntitySpellEffect spell, EntityLivingBase target)
    {
        World world = spell.world;
        if (target.isRiding())
        {
            target.dismountRidingEntity();
        }

        if (tryTeleport(world, target, 24.0D, 64.0D, 8))
        {
            return;
        }

        if (tryTeleport(world, target, 12.0D, 24.0D, 4))
        {
            return;
        }

        tryTeleport(world, target, 4.0D, 12.0D, 4);
    }

    private boolean tryTeleport(World world, EntityLivingBase target, double minDistance, double maxDistance, int attempts)
    {
        for (int i = 0; i < attempts; i++)
        {
            double angle = target.getRNG().nextDouble() * Math.PI * 2.0D;
            double distance = minDistance + target.getRNG().nextDouble() * (maxDistance - minDistance);
            double x = target.posX + Math.cos(angle) * distance;
            double z = target.posZ + Math.sin(angle) * distance;
            double y = MathHelper.clamp(target.posY + target.getRNG().nextInt(32) - 16, 0.0D, world.getActualHeight() - 1);

            if (!target.attemptTeleport(x, y, z))
            {
                continue;
            }

            PacketEnderTeleport packet = new PacketEnderTeleport(target.posX, target.posY, target.posZ, false, false);
            if (target instanceof EntityPlayerMP)
            {
                EntityPlayerMP player = (EntityPlayerMP) target;
                LGPacketHandler.INSTANCE.sendTo(packet, player);
                LGPacketHandler.INSTANCE.sendToAllTracking(packet, player);
            }
            else
            {
                LGPacketHandler.INSTANCE.sendToAllTracking(packet, target);
            }

            return true;
        }

        return false;
    }

    @Override
    public void generateParticles(LGEntitySpellDecorator decorator)
    {
        Random rand = decorator.world.rand;
        for(int i = 0; i < 20; ++i)
        {
            LGParticleSpell particle = LGParticleSpell.radialParticle(rand, decorator.radius, 0.0F, 0.0F);
            particle.maxLife = 10;
            particle.hibernateTime = rand.nextInt(8);
            decorator.particles.add(particle);
        }
    }

    @Override
    public void renderParticle(LGRenderSpellDecorator renderer, LGParticleSpell particle, float partialTicks, double power)
    {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.glLineWidth(5.0F);
        GlStateManager.color((float)(1.5F - particle.renderAge * 1.5D), (float)(particle.renderAge * 1.5D), 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        LGRenderSpellReticle.drawPolyOutline(4, Math.sin(particle.renderAge * Math.PI) * power, 0.0D);
        GlStateManager.popMatrix();
    }

    @Override
    public double getParticleScale()
    {
        return 2.0D;
    }

    @Override
    public SoundEvent getCastSound()
    {
        return LGSoundEvents.ITEM_SPELL_EXEUNT.getSoundEvent();
    }
}
