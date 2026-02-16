package mod.emt.legendgear.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

import java.util.List;
import mod.emt.legendgear.client.particle.LGParticleHandler;
import mod.emt.legendgear.config.LGConfig;

public class LGEntityEnderBomb extends Entity
{
    public int lifespan_timer;
    public int EXPAND_TIME = 110;
    public int COLLAPSE_TIME = 10;
    public static Entity thrower;
    public double radius;
    public boolean portal;
    public double xCoord;
    public double yCoord;
    public double zCoord;
    public float xSpeed;
    public float ySpeed;
    public float zSpeed;

    public LGEntityEnderBomb(World world)
    {
        super(world);
        lifespan_timer = 0;
        setSize(10.0F, 10.0F);
        portal = false;
    }

    public LGEntityEnderBomb(World world, double x, double y, double z)
    {
        this(world);
        setPosition(x, y, z);
        portal = false;
    }

    public boolean teleportEntityTo(Entity e, double x, double y, double z)
    {
        double oldX = e.posX;
        double oldY = e.posY;
        double oldZ = e.posZ;
        e.posX = x;
        e.posY = y;
        e.posZ = z;
        boolean solid = false;
        int ix = MathHelper.floor(e.posX);
        int iy = MathHelper.floor(e.posY);
        int iz = MathHelper.floor(e.posZ);
        boolean embedSolved = e.world.isBlockLoaded(new BlockPos(ix, iy, iz));
        while (!embedSolved && iy > 0)
        {
            IBlockState blockState = e.world.getBlockState(new BlockPos(ix, iy - 1, iz));
            if (blockState.getMaterial().blocksMovement())
            {
                embedSolved = true;
                continue;
            }
            e.posY--;
            iy--;
        }
        if (embedSolved)
        {
            e.setPosition(e.posX, e.posY, e.posZ);
            if (e.world.getCollisionBoxes(e, e.getEntityBoundingBox()).isEmpty() && !e.world.containsAnyLiquid(e.getEntityBoundingBox())) solid = true;
        }
        if (!solid)
        {
            e.setPosition(oldX, oldY, oldZ);
            return false;
        }
        short s = 128;
        for (int i = 0; i < s; i++)
        {
            double d = i / (s - 1.0D);
            xSpeed = (rand.nextFloat() - 0.5F) * 0.2F;
            ySpeed = (rand.nextFloat() - 0.5F) * 0.2F;
            zSpeed = (rand.nextFloat() - 0.5F) * 0.2F;
            xCoord = oldX + (e.posX - oldX) * d + (rand.nextDouble() - 0.5D) * e.width * 2.0D;
            yCoord = oldY + (e.posY - oldY) * d + rand.nextDouble() * e.height;
            zCoord = oldZ + (e.posZ - oldZ) * d + (rand.nextDouble() - 0.5D) * e.width * 2.0D;
            portal = true;
        }
        world.playSound(oldX, oldY, oldZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.NEUTRAL, 1.0F, 1.0F, false);
        playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        return true;
    }

    @Override
    public void entityInit()
    {
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        spawnParticles();
        if (lifespan_timer < EXPAND_TIME)
        {
            if (lifespan_timer == 0)
            {
                world.playSound(null, getPosition(), SoundEvents.ENTITY_ENDERMEN_STARE, SoundCategory.NEUTRAL, 3.0F, 2.0F);
            }
            radius = lifespan_timer * 1.0D / EXPAND_TIME * LGConfig.GENERAL_SETTINGS.enderMedallionRadius;
        }
        else
        {
            radius = LGConfig.GENERAL_SETTINGS.enderMedallionRadius * (COLLAPSE_TIME - lifespan_timer - EXPAND_TIME) * 1.0D / COLLAPSE_TIME;
            List<EntityLiving> entities = world.getEntitiesWithinAABB(EntityLiving.class, getEntityBoundingBox().grow(LGConfig.GENERAL_SETTINGS.enderMedallionRadius));
            for (EntityLiving entity : entities)
            {
                double dist = entity.getDistance(this);
                if (dist <= LGConfig.GENERAL_SETTINGS.enderMedallionRadius && dist >= radius)
                {
                    entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 100, 6));
                    entity.attackEntityFrom(DamageSource.MAGIC, (float) LGConfig.GENERAL_SETTINGS.enderMedallionDamage);
                    double randX = entity.posX + rand.nextGaussian() * 20.0D;
                    double randZ = entity.posZ + rand.nextGaussian() * 20.0D;
                    double randY = entity.posY + 18.0D;
                    teleportEntityTo(entity, randX, randY, randZ);
                }
            }
        }
        if (lifespan_timer > EXPAND_TIME + COLLAPSE_TIME)
        {
            portal = false;
            setDead();
        }
        lifespan_timer++;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound var1)
    {
        lifespan_timer = var1.getInteger("life");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound var1)
    {
        var1.setInteger("life", lifespan_timer);
    }

    private void spawnParticles()
    {
        if (FMLLaunchHandler.side().isClient() && world.isRemote)
        {
            if (lifespan_timer == 0)
            {
                for (int i = 0; i < 15; i++)
                {
                    world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, posX + rand.nextGaussian() * 0.3D, posY, posZ + rand.nextGaussian() * 0.3D, 0.0D, rand.nextDouble(), 0.0D);
                }
            }
            if (portal)
            {
                LGParticleHandler.spawnMagicScrambleFX(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, 1.0F);
            }
            if (lifespan_timer < (EXPAND_TIME - 35))
            {
                for (int i = 0; i < 15; i++)
                {
                    double theta = i * Math.PI * radius;
                    double ox = Math.cos(theta) * radius;
                    double oz = Math.sin(theta) * radius;
                    world.spawnParticle(EnumParticleTypes.PORTAL, posX + ox, posY, posZ + oz, 0.0D, 0.05D, 0.0D);
                }
            }
        }
    }
}
