package mod.emt.legendgear.spell;

import mod.emt.legendgear.client.particle.LGParticleSpell;
import mod.emt.legendgear.client.render.LGRenderSpellDecorator;
import mod.emt.legendgear.client.render.LGRenderSpellReticle;
import mod.emt.legendgear.entity.LGEntitySpellDecorator;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.SoundEvent;

import java.util.Random;

public class LGSpellPhoenix extends LGSpell
{
    protected LGSpellPhoenix(String id)
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
        return 4.0D;
    }

    @Override
    public double getBaseArcanePower()
    {
        return 8.0D;
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
        for (int i = 0; i < 25; i++)
        {
            double theta = i * Math.PI * 2.0D / 25.0D;
            double r = i / 23.0D * decorator.radius;
            for (int j = 0; j < 3; j++)
            {
                LGParticleSpell particle = LGParticleSpell.radialParticle(rand, decorator.radius, 0.0D, 0.0D);
                particle.x = Math.cos(theta) * r;
                particle.z = Math.sin(theta) * r;
                particle.y = 0.0D;
                particle.maxLife = 5;
                particle.hibernateTime = i / 2;
                decorator.particles.add(particle);
                theta += 2.0D * Math.PI / 3.0D;
            }
        }
    }

    @Override
    public void renderParticle(LGRenderSpellDecorator renderer, LGParticleSpell particle, float partialTicks, double power)
    {
        double age = particle.renderAge;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.color(1.0F, (float)(1.5D - age * 1.5D), (float)(age * 1.5D), 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0D, power, 1.0D);
        LGRenderSpellReticle.drawPinchDiamond(age, 1.5D, 0.0D);
        GlStateManager.popMatrix();
    }

    @Override
    public double getParticleScale()
    {
        return 4.0D;
    }

    @Override
    public SoundEvent getCastSound()
    {
        return LGSoundEvents.ITEM_SPELL_RAYFIRE.getSoundEvent();
    }
}
