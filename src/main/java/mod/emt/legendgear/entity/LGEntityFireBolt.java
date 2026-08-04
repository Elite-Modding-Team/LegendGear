package mod.emt.legendgear.entity;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.client.particle.LGParticleHandler;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class LGEntityFireBolt extends EntityThrowable implements IEntityAdditionalSpawnData
{
    private int throwerID;
    public double damage_per_hit;

    public LGEntityFireBolt(World world)
    {
        super(world);
    }

    public LGEntityFireBolt(World world, EntityLivingBase thrower, double damage)
    {
        super(world, thrower);
        this.throwerID = thrower.getEntityId();
        setLocationAndAngles(thrower.posX, thrower.posY + thrower.getEyeHeight(), thrower.posZ, thrower.rotationYaw, thrower.rotationPitch);
        float speed = 0.4F;
        motionX = -MathHelper.sin(rotationYaw * 0.017453292F) * MathHelper.cos(rotationPitch * 0.017453292F) * speed;
        motionY = -MathHelper.sin(rotationPitch * 0.017453292F) * speed;
        motionZ = MathHelper.cos(rotationYaw * 0.017453292F) * MathHelper.cos(rotationPitch * 0.017453292F) * speed;
        shoot(motionX, motionY, motionZ, 1.5F, 0.0F);
        damage_per_hit = damage;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (world.isRemote)
        {
            Vec3d forward = getLookVec();

            if (forward.lengthSquared() < 1.0E-4D)
            {
                forward = new Vec3d(0.0D, 0.0D, 1.0D);
            }

            forward = forward.normalize();
            Vec3d axis = Math.abs(forward.y) > 0.9D ? new Vec3d(1, 0, 0) : new Vec3d(0, 1, 0);
            Vec3d right = forward.crossProduct(axis).normalize();
            Vec3d up = right.crossProduct(forward).normalize();

            double theta = ticksExisted * -0.75D;
            double radiusOuter = 0.45D;
            double radiusInner = 0.22D;

            for (int i = 0; i < 2; i++)
            {
                double angle = theta + Math.sin(ticksExisted * 0.3D + i) * 0.2D;
                double radius = ticksExisted < 4 ? 0.35D - ticksExisted * 0.08D : 0.35D + Math.sin(ticksExisted * 0.45D) * 0.08D;
                double spiral = i * 0.18D;
                Vec3d out = forward.scale(spiral).add(right.scale(Math.cos(angle) * radius)).add(up.scale(Math.sin(angle) * radius));

                if (!isInWater())
                {
                    LGParticleHandler.spawnFireSwirlFX(world, posX + out.x * radiusOuter, posY + out.y * radiusOuter, posZ + out.z * radiusOuter,
                            out.x * 0.3D, out.y * 0.3D, out.z * 0.3D, 1.5F);
                    LGParticleHandler.spawnFireSwirlFX(world, posX + out.x * radiusOuter, posY + out.y * radiusOuter, posZ + out.z * radiusOuter,
                            out.x * 0.2D, out.y * 0.2D, out.z * 0.2D, 1.0F);
                } else
                {

                    world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX + out.x * radiusInner, posY + out.y * radiusInner, posZ + out.z * radiusInner,
                            out.x * 0.3D, out.y * 0.3D, out.z * 0.3D);
                }

                theta += (Math.PI * 2.0D) / 3.0D;
            }
        }

        for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(1.0D)))
        {
            if (entity.getEntityId() != throwerID && (entity instanceof EntityLivingBase || entity instanceof EntityItem || entity instanceof EntityXPOrb))
            {
                if (entity instanceof EntityLivingBase)
                {
                    EntityLivingBase living = (EntityLivingBase) entity;

                    if (living.getEntityId() != throwerID)
                    {
                        if (!isInWater()) living.setFire(10);
                        living.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, getThrower()), (float) damage_per_hit);
                    }
                }
                entity.velocityChanged = true;
            }
        }

        if (ticksExisted > 30)
        {
            spawnImpactParticles();
            this.world.playSound(null, this.posX, this.posY, this.posZ, LGSoundEvents.ITEM_AUGMENT_FIRE_BOLT.getSoundEvent(), SoundCategory.PLAYERS, 3.0F, 1.5F);
            setDead();
        }
    }

    @Override
    protected float getGravityVelocity()
    {
        return 0.0F;
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (result.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            spawnImpactParticles();
            this.world.playSound(null, this.posX, this.posY, this.posZ, LGSoundEvents.ITEM_AUGMENT_FIRE_BOLT.getSoundEvent(), SoundCategory.PLAYERS, 3.0F, 1.5F);
            setDead();
        }
    }

    private void spawnImpactParticles()
    {
        if (!world.isRemote) return;
        for (int i = 0; i < 20; i++)
        {
            double theta = world.rand.nextDouble() * Math.PI * 2.0D;
            double phi = (world.rand.nextDouble() - 0.5D) * Math.PI;
            double speed = 0.15D + world.rand.nextDouble() * 0.15D;
            double vx = Math.cos(theta) * Math.cos(phi) * speed;
            double vy = Math.sin(phi) * speed + 0.05D;
            double vz = Math.sin(theta) * Math.cos(phi) * speed;
            LGParticleHandler.spawnFireSwirlFX(this.world, this.posX, this.posY, this.posZ, vx, vy, vz, 1.5F);
            LGParticleHandler.spawnFireSwirlFX(this.world, this.posX, this.posY, this.posZ, vx * 0.4D, vy * 0.4D, vz * 0.4D, 2.0F);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        throwerID = compound.getInteger("throwerID");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setInteger("throwerID", throwerID);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeInt(throwerID);
    }

    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        throwerID = buffer.readInt();
    }
}