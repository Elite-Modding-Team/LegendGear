package mod.emt.legendgear.entity;

import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.util.damagesource.DamageSourceFirestorm;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

import java.util.List;

public class LGEntityFireBlast extends Entity
{
    public static int MAX_LIFESPAN = 70;
    public static int DETONATION_TIME = 60;
    public static double DETONATION_RADIUS = 8.0D;
    public static Entity thrower;
    public int damage_per_hit = 15;
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
                    el.setFire(10);
                    el.attackEntityFrom(damageSourceFirestorm, damage_per_hit);
                }
            }
        }
    }
}
