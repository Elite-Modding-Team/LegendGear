package mod.emt.legendgear.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import java.util.List;

public class LGEntityQuake extends Entity implements IEntityAdditionalSpawnData
{
    public static int MAX_LIFESPAN = 60;
    public static int PULSE_INTERVAL = 7;
    public static double FIRST_PULSE_RADIUS = 1.0D;
    public static double RADIUS_PER_TICK = 0.2D;
    public static double VERTICAL_LAUNCH = 0.5D;
    public static double HORIZONTAL_LAUNCH = 0.3D;
    public static Entity thrower;
    public int damage_per_hit = 7;
    public boolean noFF;
    public int lifetime;
    public boolean oneshot = false;
    public double oneshot_radius = 5.0D;

    public LGEntityQuake(World par1World, double par2, double par4, double par6, Entity responsible, boolean safeToUser, double radius)
    {
        this(par1World, par2, par4, par6, responsible, safeToUser);
        oneshot = true;
        oneshot_radius = radius;
    }

    public LGEntityQuake(World par1World, double par2, double par4, double par6, Entity responsible, boolean safeToUser)
    {
        super(par1World);
        noFF = safeToUser;
        setSize(0.0F, 0.0F);
        setPosition(par2, par4, par6);
        noClip = true;
        lifetime = 0;
        thrower = responsible;
    }

    public LGEntityQuake(World par1World, double par2, double par4, double par6, Entity responsible)
    {
        this(par1World, par2, par4, par6, responsible, false);
    }

    public LGEntityQuake(World par1World)
    {
        super(par1World);
        setSize(0.25F, 0.25F);
        noClip = true;
        lifetime = 0;
    }

    public void doPulse(double radius)
    {
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(radius));
        for (Entity entity : entities)
        {
            if (entity instanceof EntityLiving)
            {
                EntityLiving el = (EntityLiving) entity;
                if (el.onGround && el.getDistance(this) <= radius) if (!el.equals(thrower) || !noFF)
                {
                    DamageSource damage = DamageSource.causeIndirectMagicDamage(this, thrower);
                    el.attackEntityFrom(damage, damage_per_hit);
                    el.addVelocity(rand.nextGaussian() * HORIZONTAL_LAUNCH, VERTICAL_LAUNCH, rand.nextGaussian() * HORIZONTAL_LAUNCH);
                    world.playSound(null, el.getPosition(), SoundEvents.ENTITY_IRONGOLEM_ATTACK, SoundCategory.NEUTRAL, 1.0F, 0.2F);
                }
            }
        }
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeBoolean(oneshot);
        buffer.writeDouble(oneshot_radius);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        oneshot = additionalData.readBoolean();
        oneshot_radius = additionalData.readDouble();
    }

    protected void entityInit()
    {
    }

    public void onUpdate()
    {
        super.onUpdate();
        if (oneshot)
        {
            doPulse(oneshot_radius);
            setDead();
        } else
        {
            if (lifetime % PULSE_INTERVAL == 0) doPulse(FIRST_PULSE_RADIUS + RADIUS_PER_TICK * lifetime);
            lifetime++;
            if (lifetime > MAX_LIFESPAN) setDead();
        }
    }

    protected void readEntityFromNBT(NBTTagCompound var1)
    {
    }

    protected void writeEntityToNBT(NBTTagCompound var1)
    {
    }
}
