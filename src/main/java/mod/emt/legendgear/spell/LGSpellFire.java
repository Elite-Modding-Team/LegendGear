package mod.emt.legendgear.spell;

import mod.emt.legendgear.client.particle.LGParticleSpell;
import mod.emt.legendgear.client.render.LGRenderSpellDecorator;
import mod.emt.legendgear.client.render.LGRenderSpellReticle;
import mod.emt.legendgear.entity.LGEntitySpellDecorator;
import mod.emt.legendgear.entity.LGEntitySpellEffect;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class LGSpellFire extends LGSpell
{
    protected LGSpellFire(String id)
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
        target.setFire(spell.isCrit ? 8 : 4);
    }

    @Override
    public void onBlockHit(LGEntitySpellEffect spell, BlockPos pos)
    {
        World world = spell.world;
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == Blocks.ICE || state.getBlock() == Blocks.FROSTED_ICE)
        {
            BlockPos meltPos = pos.toImmutable();
            spell.queueBlockAction(() -> world.setBlockState(meltPos, Blocks.FLOWING_WATER.getDefaultState()));
        }
    }

    @Override
    public void generateParticles(LGEntitySpellDecorator decorator)
    {
        Random rand = decorator.world.rand;
        for (int i = 0; i < 30; i++)
        {
            LGParticleSpell particle = LGParticleSpell.radialParticle(rand, decorator.radius * 0.75D, 0.1D, -0.002D);
            particle.maxLife = 15 + rand.nextInt(10);
            particle.ay = 0.03D;
            particle.hibernateTime = rand.nextInt(5);
            particle.drag = 0.8D;
            particle.uniqueness = rand.nextDouble();
            decorator.particles.add(particle);
        }
    }

    @Override
    public void renderParticle(LGRenderSpellDecorator renderer, LGParticleSpell particle, float partialTicks, double power)
    {
        double age = particle.age + partialTicks / (double) particle.maxLife;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 2.0F - (float)(age * 2.0D), 1.0F - (float)(age * 2.0D), 1.0F);
        double size = Math.sin(age * Math.PI * 3.0D / 4.0D + Math.PI / 4.0D) * power / 8.0D;
        LGRenderSpellReticle.drawPolySolid(4, size, Math.PI / 2.0D + age * Math.PI * 2.0D);
    }

    @Override
    public double getParticleScale()
    {
        return 8.0D;
    }

    @Override
    public SoundEvent getCastSound()
    {
        return LGSoundEvents.ITEM_SPELL_EMBER.getSoundEvent();
    }
}
