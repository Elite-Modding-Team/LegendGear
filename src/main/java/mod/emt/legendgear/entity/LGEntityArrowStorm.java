package mod.emt.legendgear.entity;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.config.LGConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

import java.util.List;

public class LGEntityArrowStorm extends Entity implements IEntityAdditionalSpawnData
{
    public static int MAX_LIFESPAN = 100;
    public static double RADIUS = 8.0D;
    public static EntityLivingBase thrower;
    public int lifetime;

    public LGEntityArrowStorm(World world, double x, double y, double z, EntityLivingBase responsible)
    {
        super(world);
        setSize(0.25F, 0.25F);
        setPosition(x, y, z);
        noClip = true;
        lifetime = 0;
        thrower = responsible;
    }

    public LGEntityArrowStorm(World world)
    {
        super(world);
        setSize(0.25F, 0.25F);
        noClip = true;
        lifetime = 0;
    }

    public void doArrowStorm()
    {
        List<Entity> targets = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(RADIUS));
        for (Entity target : targets)
        {
            if (target instanceof EntityLivingBase)
            {
                double targetX = target.posX + (world.rand.nextGaussian() * target.width);
                double targetY = target.posY + target.height + 1.0D;
                double targetZ = target.posZ + (world.rand.nextGaussian() * target.width);
                createVolatileArrow(targetX, targetY, targetZ);
            }
        }
        for (int i = 0; i < 3; i++)
        {
            createVolatileArrow(posX + world.rand.nextGaussian() * RADIUS * 0.5D, posY + 5.0D, posZ + world.rand.nextGaussian() * RADIUS * 0.5D);
        }
        world.playSound(null, thrower.getPosition(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.NEUTRAL, 0.1F, 1.0F / (rand.nextFloat() * 0.4F + 1.2F) + 0.5F);
    }

    public void createVolatileArrow(double xPos, double yPos, double zPos)
    {
        LGEntityVolatileArrow arrow = new LGEntityVolatileArrow(world, thrower);
        arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        arrow.setDamage(LGConfig.GENERAL_SETTINGS.windMedallionDamage);
        arrow.setPositionAndRotation(xPos, yPos, zPos, 2.5F, 12.0F);
        world.spawnEntity(arrow);
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        spawnParticles();
        if (!world.isRemote && thrower != null)
        {
            doArrowStorm();
        }
        lifetime++;
        if (lifetime > MAX_LIFESPAN)
        {
            setDead();
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1)
    {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1)
    {
    }

    private void spawnParticles()
    {
        if (world.isRemote)
        {
            if (lifetime == 0)
            {
                for (int i = 0; i < 15; i++)
                {
                    world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, posX + world.rand.nextGaussian() * 0.3D, posY, posZ + world.rand.nextGaussian() * 0.3D, 0.0D, world.rand.nextDouble(), 0.0D);
                }
            }
            for (int i = 0; i < 5; i++)
            {
                world.spawnParticle(EnumParticleTypes.CLOUD, posX + world.rand.nextGaussian() * LGEntityArrowStorm.RADIUS * 0.5D, posY + 3.0D, posZ + world.rand.nextGaussian() * LGEntityArrowStorm.RADIUS * 0.5D, 0.0D, -0.1D, 0.0D);
            }
        }
    }

    @Override
    public void writeSpawnData(ByteBuf data)
    {
        data.writeInt(thrower != null ? thrower.getEntityId() : -1);
    }

    @Override
    public void readSpawnData(ByteBuf data)
    {
        final Entity shooter = world.getEntityByID(data.readInt());

        if (shooter instanceof EntityLivingBase)
        {
            this.thrower = (EntityLivingBase) thrower;
        }
    }
}
