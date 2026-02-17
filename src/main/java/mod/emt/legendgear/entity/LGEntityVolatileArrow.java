package mod.emt.legendgear.entity;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.config.LGConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class LGEntityVolatileArrow extends EntityArrow implements IEntityAdditionalSpawnData
{
    public LGEntityVolatileArrow(World worldIn)
    {
        super(worldIn);
    }

    public LGEntityVolatileArrow(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public LGEntityVolatileArrow(World worldIn, EntityLivingBase shooter)
    {
        super(worldIn, shooter);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (ticksExisted > 140)
        {
            setDead();
            world.playSound(null, getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.1F, 1.0F / (rand.nextFloat() * 0.4F + 1.2F) + 0.5F);
            world.spawnParticle(EnumParticleTypes.CLOUD, posX, posY, posZ, 0.0D, 0.1D, 0.0D);
        }
    }

    @Override
    protected void onHit(RayTraceResult raytraceResultIn)
    {
        if (raytraceResultIn.entityHit != null && !this.world.isRemote)
        {
            Entity target = raytraceResultIn.entityHit;
            double motionX = target.motionX;
            double motionY = target.motionY;
            double motionZ = target.motionZ;

            target.attackEntityFrom(DamageSource.causeArrowDamage(this, shootingEntity), (float) this.getDamage());

            if (LGConfig.GENERAL_SETTINGS.windMedallionIgnoresIF)
            {
                raytraceResultIn.entityHit.hurtResistantTime = 0;
            }

            target.motionX = motionX;
            target.motionY = motionY;
            target.motionZ = motionZ;
            target.isAirBorne = false;

            this.setDead();
        }
        else
        {
            super.onHit(raytraceResultIn);
        }
    }

    @Override
    protected ItemStack getArrowStack()
    {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public void writeSpawnData(ByteBuf data)
    {
        data.writeInt(shootingEntity != null ? shootingEntity.getEntityId() : -1);
    }

    @Override
    public void readSpawnData(ByteBuf data)
    {
        final Entity shooter = world.getEntityByID(data.readInt());

        if (shooter instanceof EntityLivingBase)
        {
            this.shootingEntity = (EntityLivingBase) shootingEntity;
        }
    }
}
