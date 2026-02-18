package mod.emt.legendgear.entity;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.config.LGConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

import java.util.List;
import mod.emt.legendgear.client.particle.LGParticleHandler;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.util.damagesource.DamageSourceFirestorm;

public class LGEntityFireBlast extends Entity implements IEntityAdditionalSpawnData
{
    public static int MAX_LIFESPAN = 70;
    public static int DETONATION_TIME = 60;
    public static double DETONATION_RADIUS = 8.0D;
    public static Entity thrower;
    public float damage_per_hit = (float) LGConfig.GENERAL_SETTINGS.fireMedallionDamage;
    public boolean noFF;
    public int lifetime;

    public LGEntityFireBlast(World par1World, double par2, double par4, double par6, Entity responsible, boolean safeToUser)
    {
        super(par1World);
        noFF = safeToUser;
        setSize(0.0F, 0.0F);
        setPosition(par2, par4, par6);
        noClip = true;
        lifetime = 0;
        thrower = responsible;
    }

    public LGEntityFireBlast(World par1World, double par2, double par4, double par6, Entity responsible)
    {
        this(par1World, par2, par4, par6, responsible, false);
    }

    public LGEntityFireBlast(World par1World)
    {
        super(par1World);
        setSize(0.25F, 0.25F);
        noClip = true;
        lifetime = 0;
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
        if (lifetime == 0)
        {
            world.playSound(null, this.getPosition(), LGSoundEvents.ENTITY_MEDALLION_FIRE_START.getSoundEvent(), SoundCategory.NEUTRAL, 3.0F, 1.0F);
        }
        if (lifetime == DETONATION_TIME)
        {
            world.playSound(null, this.getPosition(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.NEUTRAL, 3.0F, 0.8F);
            world.playSound(null, this.getPosition(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.NEUTRAL, 3.0F, 1.0F);
            world.playSound(null, this.getPosition(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.NEUTRAL, 3.0F, 1.2F);
            burnThings(DETONATION_RADIUS);
        }
        lifetime++;
        if (lifetime >= MAX_LIFESPAN)
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

    private void burnThings(double radius)
    {
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(radius));
        for (Entity entity : entities)
        {
            if (entity instanceof EntityLiving)
            {
                EntityLiving el = (EntityLiving) entity;
                if (el.getDistance(this) <= radius && (!el.equals(thrower) || !noFF))
                {
                    DamageSourceFirestorm damageSourceFirestorm = new DamageSourceFirestorm("inFire", this, thrower);
                    el.setFire(LGConfig.GENERAL_SETTINGS.fireMedallionBurnDuration);
                    el.attackEntityFrom(damageSourceFirestorm, damage_per_hit);
                }
            }
        }
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
            if (lifetime < LGEntityFireBlast.DETONATION_TIME)
            {
                double progress = 1.0D * lifetime / LGEntityFireBlast.DETONATION_TIME;
                double decel = 1.0D - progress;
                decel = decel * decel * decel;
                decel = 1.0D - decel;
                double r = LGEntityFireBlast.DETONATION_RADIUS * decel;
                double theta = decel * Math.PI * 4.0D;
                for (int i = 0; i < 3; i++)
                {
                    double ox = Math.cos(theta) * r;
                    double oz = Math.sin(theta) * r;
                    LGParticleHandler.spawnFireSwirlFX(world, posX + ox, posY, posZ + oz, 0.0D, 0.05D, 0.0D, 3.0F);
                    theta += 2.0943951023931953D;
                }
            }
            if (lifetime > LGEntityFireBlast.DETONATION_TIME)
            {
                for (int i = 0; i < 5; i++)
                {
                    LGParticleHandler.spawnFireSwirlFX(world, posX + world.rand.nextGaussian() * LGEntityFireBlast.DETONATION_RADIUS * 0.5D, posY, posZ + world.rand.nextGaussian() * LGEntityFireBlast.DETONATION_RADIUS * 0.5D, 0.0D, 0.5D, 0.0D, 10.0F);
                    world.spawnParticle(EnumParticleTypes.LAVA, posX + world.rand.nextGaussian() * LGEntityFireBlast.DETONATION_RADIUS * 0.5D, posY, posZ + world.rand.nextGaussian() * LGEntityFireBlast.DETONATION_RADIUS * 0.5D, 0.0D, 0.5D, 0.0D);
                }
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
