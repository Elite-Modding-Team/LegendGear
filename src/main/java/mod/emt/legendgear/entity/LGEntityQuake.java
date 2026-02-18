package mod.emt.legendgear.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

import io.netty.buffer.ByteBuf;
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
    public double damage_per_hit = 7.0D;
    public boolean noFF;
    public int lifetime;
    public boolean oneshot = false;
    public double oneshot_radius = 5.0D;

    public LGEntityQuake(World par1World, double par2, double par4, double par6, Entity responsible, boolean safeToUser, double radius)
    {
        this(par1World, par2, par4, par6, responsible, safeToUser);
        oneshot = true;
        oneshot_radius = radius;
        damage_per_hit = 7.0D;
        VERTICAL_LAUNCH = 0.5D;
        HORIZONTAL_LAUNCH = 0.3D;

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

    public LGEntityQuake(World par1World, double par2, double par4, double par6, Entity responsible, double damage, double verticalLaunch, double horizontalLaunch)
    {
        this(par1World, par2, par4, par6, responsible, false);
        damage_per_hit = damage;
        VERTICAL_LAUNCH = verticalLaunch;
        HORIZONTAL_LAUNCH = horizontalLaunch;
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
                    el.attackEntityFrom(damage, (float) damage_per_hit);
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

    @Override
    protected void entityInit()
    {
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (oneshot)
        {
            doPulse(oneshot_radius);
            setDead();
        }
        else
        {
            if (lifetime % PULSE_INTERVAL == 0) doPulse(FIRST_PULSE_RADIUS + RADIUS_PER_TICK * lifetime);
            lifetime++;
            if (lifetime > MAX_LIFESPAN) setDead();
        }
        spawnParticles();
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
            double radius = oneshot_radius;
            if (!oneshot)
            {
                radius = LGEntityQuake.FIRST_PULSE_RADIUS + LGEntityQuake.RADIUS_PER_TICK * lifetime;
            }
            if (ticksExisted % 5 == 0)
            {
                for (int i = 0; i < 12; i++)
                {
                    double th = i * Math.PI * 2.0D / 12.0D;
                    double vx = Math.cos(th);
                    double vz = Math.sin(th);
                    world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX + vx * radius, posY, posZ + vz * radius, vx * 0.05D, 0.2D, vz * 0.05D);
                }
            }
            if (lifetime <= 1 && !oneshot)
            {
                for (int i = 0; i < 15; i++)
                {
                    world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, posX + world.rand.nextGaussian() * 0.3D, posY, posZ + world.rand.nextGaussian() * 0.3D, 0.0D, world.rand.nextDouble(), 0.0D);
                }
            }
        }
    }
}
