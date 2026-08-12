package mod.emt.legendgear.spell;

import mod.emt.legendgear.client.particle.LGParticleSpell;
import mod.emt.legendgear.client.render.LGRenderSpellDecorator;
import mod.emt.legendgear.client.render.LGRenderSpellReticle;
import mod.emt.legendgear.entity.LGEntitySpellDecorator;
import mod.emt.legendgear.entity.LGEntitySpellEffect;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class LGSpellIce extends LGSpell
{
    protected LGSpellIce(String id)
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
        target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 8 * 20, 5));
        target.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 8 * 20, 5));
    }

    @Override
    public void onBlockHit(LGEntitySpellEffect spell, BlockPos pos)
    {
        World world = spell.world;
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == Blocks.WATER && state.getValue(BlockLiquid.LEVEL) == 0)
        {
            BlockPos freezePos = pos.toImmutable();
            spell.queueBlockAction(() ->
            {
                world.setBlockState(freezePos, Blocks.FROSTED_ICE.getDefaultState());
                world.scheduleUpdate(freezePos, Blocks.FROSTED_ICE, MathHelper.getInt(world.rand, 60, 120));
            });
        }
    }

    @Override
    public void generateParticles(LGEntitySpellDecorator decorator)
    {
        Random rand = decorator.world.rand;
        for (int i = 0; i < 20; i++)
        {
            LGParticleSpell particle = LGParticleSpell.radialParticle(rand, decorator.radius, 0.0D, 0.0D);
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
        GlStateManager.color((float)(0.5D + (1.0D - Math.sin(particle.renderAge * Math.PI)) * 0.5D), (float)(0.5D * (1.0D - Math.sin(particle.renderAge * Math.PI)) + 0.5D), 1.0F, 1.0F);
        double size = power / 4.0D;
        if (particle.renderAge < 0.5D)
        {
            LGRenderSpellReticle.drawCross(3, size * (1.0D - particle.renderAge * 2.0D) * 2.0D, (Math.PI / 2D));
        } else if (particle.renderAge < 0.75D)
        {
            LGRenderSpellReticle.drawPolySolid(6, size * (particle.renderAge - 0.5D) * 4.0D, (Math.PI / 2D));
        } else
        {
            LGRenderSpellReticle.drawPolyOutline(6, size, (Math.PI / 2D));
        }
    }

    @Override
    public double getParticleScale()
    {
        return 8.0D;
    }

    @Override
    public SoundEvent getCastSound()
    {
        return LGSoundEvents.ITEM_SPELL_FROST.getSoundEvent();
    }
}
