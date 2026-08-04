package mod.emt.legendgear.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class LGEntityWhirlwind extends EntityThrowable implements IEntityAdditionalSpawnData
{
    private int throwerID;

    public LGEntityWhirlwind(World world)
    {
        super(world);
    }

    public LGEntityWhirlwind(World world, EntityLivingBase thrower)
    {
        super(world, thrower);
        this.throwerID = thrower.getEntityId();
        setLocationAndAngles(thrower.posX, thrower.posY + thrower.getEyeHeight(), thrower.posZ, thrower.rotationYaw, thrower.rotationPitch);
        float speed = 0.4F;
        motionX = -MathHelper.sin(rotationYaw * 0.017453292F) * MathHelper.cos(rotationPitch * 0.017453292F) * speed;
        motionY = -MathHelper.sin(rotationPitch * 0.017453292F) * speed;
        motionZ = MathHelper.cos(rotationYaw * 0.017453292F) * MathHelper.cos(rotationPitch * 0.017453292F) * speed;
        shoot(motionX, motionY, motionZ, 1.5F, 0.0F);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (world.isRemote)
        {
            Vec3d forward = new Vec3d(motionX, motionY, motionZ);

            if (forward.lengthSquared() < 1.0E-4D)
            {
                forward = new Vec3d(0.0D, 0.0D, 1.0D);
            }

            forward = forward.normalize();
            Vec3d axis = Math.abs(forward.y) > 0.9D ? new Vec3d(1.0D, 0.0D, 0.0D) : new Vec3d(0.0D, 1.0D, 0.0D);
            Vec3d right = forward.crossProduct(axis).normalize();
            Vec3d up = right.crossProduct(forward).normalize();

            double theta = ticksExisted * -0.4D;
            double radiusOuter = 1.0D;
            double radiusInner = 0.5D;

            for (int i = 0; i < 3; i++) {
                double lx = Math.cos(theta);
                double ly = Math.sin(theta);
                Vec3d out = right.scale(lx).add(up.scale(ly));

                if (!isInWater())
                {
                    world.spawnParticle(EnumParticleTypes.SPELL, posX + out.x * radiusOuter, posY + out.y * radiusOuter, posZ + out.z * radiusOuter,
                            out.x * 0.3D, out.y * 0.3D, out.z * 0.3D
                    );
                    world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + out.x * radiusInner, posY + out.y * radiusInner, posZ + out.z * radiusInner,
                            out.x * 0.3D, out.y * 0.3D, out.z * 0.3D
                    );
                } else {
                    world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, posX + out.x * radiusInner, posY + out.y * radiusInner, posZ + out.z * radiusInner,
                            out.x * 0.3D, out.y * 0.3D, out.z * 0.3D
                    );
                }

                theta += (Math.PI * 2.0D) / 3.0D;
            }
        }

        for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(2.5D)))
        {
            if (entity.getEntityId() != throwerID && (entity instanceof EntityLivingBase || entity instanceof EntityItem || entity instanceof EntityXPOrb))
            {
                entity.motionX += motionX;
                entity.motionY += motionY;
                entity.motionZ += motionZ;
                entity.velocityChanged = true;
            }
        }

        if (ticksExisted > 30)
        {
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
            setDead();
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