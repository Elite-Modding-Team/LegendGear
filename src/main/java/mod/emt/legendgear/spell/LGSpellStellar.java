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
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;

import java.util.Random;

public class LGSpellStellar extends LGSpell {
    protected LGSpellStellar(String id) {
        super(id);
    }

    @Override
    public double getBaseCastRange() {
        return 15.0D;
    }

    @Override
    public double getBaseCastRadius() {
        return 2.0D;
    }

    @Override
    public double getBaseArcanePower() {
        return 8.0D;
    }

    @Override
    public DamageSource getDamageSource(LGEntitySpellEffect spell)
    {
        return spell.caster != null ? DamageSource.causePlayerDamage(spell.caster) : DamageSource.MAGIC.setDamageBypassesArmor();
    }

    @Override
    public double getBaseCastTime() {
        return 0.75D;
    }

    @Override
    public double getBaseCritBonus() {
        return 0.0D;
    }

    @Override
    public void onLivingEntityHit(LGEntitySpellEffect spell, EntityLivingBase target)
    {
        if (spell.isCrit)
        {
            target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 8 * 20));
        }
    }

    @Override
    public void generateParticles(LGEntitySpellDecorator decorator)
    {
        Random rand = decorator.world.rand;
        for (int i = 0; i < 50; i++)
        {
            LGParticleSpell particle = LGParticleSpell.radialParticle(rand, decorator.radius, 0.0D, 0.0D);
            particle.maxLife = 5;
            particle.hibernateTime = rand.nextInt(15);
            decorator.particles.add(particle);
        }
    }

    @Override
    public void renderParticle(LGRenderSpellDecorator renderer, LGParticleSpell particle, float partialTicks, double power) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.5F - (float)particle.renderAge, 0.5F + (float)particle.renderAge, 1.0F);
        GlStateManager.glLineWidth(2.0F);
        LGRenderSpellReticle.drawCross(2, Math.sin(particle.renderAge * Math.PI) * (power - 1.0D) / 4.0D, (Math.PI / 2D));
    }

    @Override
    public double getParticleScale()
    {
        return 6.0D;
    }

    @Override
    public SoundEvent getCastSound()
    {
        return LGSoundEvents.ITEM_SPELL_TWINKLE.getSoundEvent();
    }
}
