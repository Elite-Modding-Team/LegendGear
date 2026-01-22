package mod.emt.legendgear.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.util.List;

public class LGEntityArrowStorm extends Entity
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
            createVolatileArrow(posX + world.rand.nextGaussian() * RADIUS * 0.5D, posY + 3.0D, posZ + world.rand.nextGaussian() * RADIUS * 0.5D);
        }
        world.playSound(null, thrower.getPosition(), SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, SoundCategory.NEUTRAL, 0.1F, 1.0F / (rand.nextFloat() * 0.4F + 1.2F) + 0.5F);
    }

    public void createVolatileArrow(double xPos, double yPos, double zPos)
    {
        LGEntityVolatileArrow arrow = new LGEntityVolatileArrow(world, thrower);
        arrow.pickupStatus = EntityArrow.PickupStatus.DISALLOWED;
        arrow.setDamage(5.0D);
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
}
