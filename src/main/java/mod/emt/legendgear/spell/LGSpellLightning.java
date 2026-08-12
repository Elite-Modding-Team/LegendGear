package mod.emt.legendgear.spell;

import mod.emt.legendgear.client.particle.LGParticleSpell;
import mod.emt.legendgear.client.render.LGRenderSpellDecorator;
import mod.emt.legendgear.client.render.LGRenderSpellReticle;
import mod.emt.legendgear.entity.LGEntitySpellDecorator;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.SoundEvent;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class LGSpellLightning extends LGSpell
{
    protected LGSpellLightning(String id)
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
        return 2.0D;
    }

    @Override
    public double getBaseArcanePower()
    {
        return 8.0D;
    }

    @Override
    public double getKnockback(boolean critical)
    {
        return critical ? 0.5D : 0.45D;
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
    public void generateParticles(LGEntitySpellDecorator decorator)
    {
        Random rand = decorator.world.rand;
        for (int i = 0; i < 30; i++)
        {
            LGParticleSpell particle = LGParticleSpell.radialParticle(rand, decorator.radius, 0.0D, 0.0D);
            particle.uniqueness = rand.nextDouble();
            particle.maxLife = 15;
            particle.hibernateTime = rand.nextInt(5);
            decorator.particles.add(particle);
        }
    }

    @Override
    public void renderParticle(LGRenderSpellDecorator renderer, LGParticleSpell particle, float partialTicks, double power)
    {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.pushMatrix();
        double s = power * 0.15;
        if (particle.renderAge < 0.5D)
        {
            double f = Math.sin((particle.renderAge + particle.uniqueness) * 30.0D) * 0.5D + 0.5D;
            GlStateManager.color((float)(f * 2.0D), 1.0F, (float)(2.0F - f * 2.0D));
            LGRenderSpellReticle.diamondZig(s, s * 2.0D, Math.sin(particle.renderAge * Math.PI * 4.0D));
        } else
        {
            GlStateManager.glBegin(GL11.GL_LINES);
            double phase = particle.renderAge * 2.0D - 1.0D;
            double f = Math.sin((particle.renderAge + particle.uniqueness) * 30.0D) * 0.5D + 0.5D;
            GlStateManager.color((float)(f * 2.0D), 1.0F, (float)(2.0F - f * 2.0D));
            double h = Math.sin(phase * Math.PI) * 2.5D;
            GL11.glVertex3d(-particle.x, -particle.y, -particle.z);
            GL11.glVertex3d(particle.x * h - particle.x, particle.y * h - particle.y, particle.z * h - particle.z);
            GlStateManager.glEnd();
        }
        GlStateManager.popMatrix();
        GlStateManager.glLineWidth(1.0F);
    }

    @Override
    public double getParticleScale()
    {
        return 8.0D;
    }

    @Override
    public SoundEvent getCastSound()
    {
        return LGSoundEvents.ITEM_SPELL_JOLT.getSoundEvent();
    }
}
