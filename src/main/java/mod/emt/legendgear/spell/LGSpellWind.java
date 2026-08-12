package mod.emt.legendgear.spell;

import mod.emt.legendgear.block.LGBlockMysticShrub;
import mod.emt.legendgear.client.particle.LGParticleSpell;
import mod.emt.legendgear.client.render.LGRenderSpellDecorator;
import mod.emt.legendgear.client.render.LGRenderSpellReticle;
import mod.emt.legendgear.entity.LGEntitySpellDecorator;
import mod.emt.legendgear.entity.LGEntitySpellEffect;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class LGSpellWind extends LGSpell {
    protected LGSpellWind(String id) {
        super(id);
    }

    public int getSpellLife()
    {
        return 20;
    }

    @Override
    public double getBaseCastRange() {
        return 15.0D;
    }

    @Override
    public double getBaseCastRadius() {
        return 4.0D;
    }

    @Override
    public double getBaseArcanePower() {
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
        this.doWhirlwind(spell, target);
        if (target instanceof EntitySheep) {
            EntitySheep sheep = (EntitySheep) target;
            if (!sheep.getSheared() && !sheep.isChild()) {
                sheep.setSheared(true);
                for (ItemStack wool : sheep.onSheared(new ItemStack(Items.SHEARS), target.world, sheep.getPosition(), 0)) {
                    EntityItem item = new EntityItem(target.world, sheep.posX, sheep.posY, sheep.posZ, wool);
                    item.motionY += target.world.rand.nextFloat() * 0.05F;
                    target.world.spawnEntity(item);
                }
            }
        }
    }

    public void onNonLivingEntityHit(LGEntitySpellEffect spell, Entity target)
    {
        this.doWhirlwind(spell, target);
    }

    public void doWhirlwind(LGEntitySpellEffect spell, Entity target) {
        Vec3d offset = target.getPositionVector().subtract(spell.getPositionVector());
        Vec3d horizontal = new Vec3d(offset.x, 0.0D, offset.z);
        double distance = horizontal.length();

        if (distance < 0.001D)
        {
            double angle = spell.world.rand.nextDouble() * Math.PI * 2.0D;
            horizontal = new Vec3d(Math.cos(angle), 0.0D, Math.sin(angle));
            distance = 0.0D;
        }
        else
        {
            horizontal = horizontal.scale(1.0D / distance);
        }

        Vec3d tangent = new Vec3d(-horizontal.z, 0.0D, horizontal.x);
        double orbitRadius = spell.radius * 0.30D;
        double radialError = distance - orbitRadius;
        double swirl = 3.0D;
        double correction = radialError * 1.2D;
        double desiredX = tangent.x * swirl - horizontal.x * correction;
        double desiredZ = tangent.z * swirl - horizontal.z * correction;
        target.motionX += (desiredX - target.motionX) * 0.35D;
        target.motionZ += (desiredZ - target.motionZ) * 0.35D;
        double desiredLift = 0.45D;
        target.motionY += (desiredLift - target.motionY) * 0.25D;
        target.fallDistance = 0.0F;
        target.velocityChanged = true;
    }

    @Override
    public void onBlockHit(LGEntitySpellEffect spell, BlockPos pos)
    {
        IBlockState state = spell.world.getBlockState(pos);
        Block block = state.getBlock();

        if (block.isLeaves(state, spell.world, pos))
        {
            spell.world.destroyBlock(pos, true);
            return;
        }

        if (!(block instanceof LGBlockMysticShrub) && block instanceof IPlantable && (state.getMaterial().isReplaceable() || state.getBlockHardness(spell.world, pos) == 0.0F))
        {
            spell.world.destroyBlock(pos, true);
        }
    }

    @Override
    public void generateParticles(LGEntitySpellDecorator decorator)
    {
        Random rand = decorator.world.rand;
        for (int i = 0; i < 50; i++)
        {
            LGParticleSpell particle = LGParticleSpell.radialParticle(rand, decorator.radius, 0.0D, 0.0D);
            particle.maxLife = 20;
            decorator.particles.add(particle);
        }
    }

    @Override
    public void renderParticle(LGRenderSpellDecorator renderer, LGParticleSpell particle, float partialTicks, double power)
    {
        double age = particle.renderAge;
        double waxwane = Math.sin(age * Math.PI);
        GlStateManager.color((float)(0.7D + 0.3D * waxwane), (float)(0.7D + 0.3D * waxwane), (float)(0.8D + 0.2D * waxwane), 1.0F);
        Vec3d toAxis = new Vec3d(-particle.x, 0.0D, -particle.z);
        GlStateManager.pushMatrix();
        GlStateManager.translate(toAxis.x, 0.0D, toAxis.z);
        double length = toAxis.length() * power * waxwane;
        LGRenderSpellReticle.drawScythe(waxwane, length, Math.atan2(particle.z, particle.x) * 180.0D / Math.PI + age * 360.0D * 4.0D);
        GlStateManager.popMatrix();
    }

    @Override
    public double getParticleScale()
    {
        return 1.5D;
    }

    @Override
    public SoundEvent getCastSound()
    {
        return LGSoundEvents.ITEM_SPELL_SCYTHEWIND.getSoundEvent();
    }
}
