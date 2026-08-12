package mod.emt.legendgear.spell;

import mod.emt.legendgear.client.particle.LGParticleSpell;
import mod.emt.legendgear.client.render.LGRenderSpellDecorator;
import mod.emt.legendgear.entity.LGEntitySpellDecorator;
import mod.emt.legendgear.entity.LGEntitySpellEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class LGSpell {
    private final String id;

    protected LGSpell(String id)
    {
        this.id = id;
    }

    public final String getId()
    {
        return id;
    }

    public final LGEntitySpellEffect createEffect(World world, EntityPlayer caster, Vec3d location, double radius, double power, boolean crit)
    {
        return new LGEntitySpellEffect(world, caster, this, location, radius, power, crit);
    }

    public int getSpellLife()
    {
        return 1;
    }

    public int getParticleLife()
    {
        return 100;
    }

    public abstract double getBaseCastRange();

    public abstract double getBaseCastRadius();

    public abstract double getBaseArcanePower();

    public DamageSource getDamageSource(LGEntitySpellEffect spell)
    {
        return spell.caster != null ? DamageSource.causePlayerDamage(spell.caster) : DamageSource.MAGIC;
    }

    public boolean canAffectCaster()
    {
        return false;
    }

    public double getKnockback(boolean critical)
    {
        return critical ? 0.2D : 0.15D;
    }

    public abstract double getBaseCastTime();

    public abstract double getBaseCritBonus();

    public boolean hitsWater()
    {
        return false;
    }

    public void onLivingEntityHit(LGEntitySpellEffect spell, EntityLivingBase target)
    {
    }

    public void onNonLivingEntityHit(LGEntitySpellEffect spell, Entity target)
    {
    }

    public void onBlockHit(LGEntitySpellEffect spell, BlockPos pos)
    {
    }

    public void generateParticles(LGEntitySpellDecorator decorator)
    {
    }

    public abstract void renderParticle(LGRenderSpellDecorator renderer, LGParticleSpell particle, float partialTicks, double power);

    public double getParticleScale()
    {
        return getBaseArcanePower();
    }

    public abstract SoundEvent getCastSound();
}
