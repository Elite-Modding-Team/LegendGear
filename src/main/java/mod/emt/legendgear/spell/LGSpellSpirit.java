package mod.emt.legendgear.spell;

import mod.emt.legendgear.client.particle.LGParticleSpell;
import mod.emt.legendgear.client.render.LGRenderSpellDecorator;
import mod.emt.legendgear.client.render.LGRenderSpellReticle;
import mod.emt.legendgear.entity.LGEntitySpellDecorator;
import mod.emt.legendgear.entity.LGEntitySpellEffect;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;

public class LGSpellSpirit extends LGSpell
{
    protected LGSpellSpirit(String id)
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
        target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 8 * 20, 1));
        target.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, 1 * 20, 5));
        target.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 8 * 20, 0));
    }

    @Override
    public void generateParticles(LGEntitySpellDecorator decorator)
    {
        for (int i = 0; i < 12; i++)
        {
            LGParticleSpell particle = new LGParticleSpell();
            particle.maxLife = 14;
            particle.hibernateTime = i * 2;
            decorator.particles.add(particle);
        }
    }

    @Override
    public void renderParticle(LGRenderSpellDecorator renderer, LGParticleSpell particle, float partialTicks, double power)
    {
        double age = particle.renderAge;
        double radius = power * (0.25D + 0.5D * Math.pow(age, 1.0D));
        double core = power * (0.12D + 0.06D * Math.sin(age * Math.PI));
        boolean showOuter = radius > core * 2.0D;
        float alpha;

        if (age < 0.75D)
        {
            alpha = 1.0F;
        }
        else
        {
            alpha = (float)((1.0D - age) / 0.25D);
        }

        float t = (float)Math.pow(age, 1.1D);
        float r = 1.0F - 0.50F * t;
        float g = 1.0F;
        float b = 1.0F - 0.15F * t;
        double tilt = age * Math.PI * 0.5D;

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.depthMask(false);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0D, 0.0D, -0.002D);
        GlStateManager.color(r, g, b, alpha * 0.25F);
        LGRenderSpellReticle.drawPolySolid(32, core, 0.0D);
        GlStateManager.popMatrix();

        if (showOuter)
        {
            float lineWidth = Math.max(4.0F - (float)(age * 8.0D), 2.5F);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0D, 0.0D, -0.001D);
            GlStateManager.glLineWidth(lineWidth + 1.0F);
            GlStateManager.color(0.10F, 0.16F, 0.28F, alpha * 0.20F);
            LGRenderSpellReticle.drawPolyOutline(32, radius, tilt);
            GlStateManager.glLineWidth(lineWidth);
            GlStateManager.color(r, g, b, alpha);
            LGRenderSpellReticle.drawPolyOutline(32, radius, tilt);
            GlStateManager.glLineWidth(1.0F);
            GlStateManager.popMatrix();
        }
        GlStateManager.depthMask(true);
    }

    @Override
    public double getParticleScale()
    {
        return 4.0D;
    }

    @Override
    public SoundEvent getCastSound()
    {
        return LGSoundEvents.ITEM_SPELL_SPIRIT_DRAIN.getSoundEvent();
    }
}
