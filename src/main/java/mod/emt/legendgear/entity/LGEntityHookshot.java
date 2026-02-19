package mod.emt.legendgear.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.item.LGItemHookshot;

public class LGEntityHookshot extends EntityThrowable implements IEntityAdditionalSpawnData
{
    public static final DataParameter<Boolean> ANCHORED = EntityDataManager.createKey(LGEntityHookshot.class, DataSerializers.BOOLEAN);

    public EntityPlayer shooter;
    private int travelTime = 15;

    public LGEntityHookshot(World world)
    {
        super(world);
        setSize(0.25F, 0.25F);
    }

    public LGEntityHookshot(World world, EntityLivingBase thrower)
    {
        super(world, thrower);
        setSize(0.25F, 0.25F);
        if (thrower instanceof EntityPlayer)
        {
            shooter = (EntityPlayer) thrower;
        }
    }

    @Override
    public void setDead()
    {
        if (shooter != null)
        {
            if (dataManager.get(ANCHORED))
            {
                shooter.motionX = shooter.motionY = shooter.motionZ = 0.0D;
                shooter.fallDistance = 0.0F;
            }
            LGItemHookshot.setCast(shooter.inventory.getCurrentItem(), false);
            world.playSound(null, shooter.posX, shooter.posY, shooter.posZ, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.PLAYERS, 0.8F, 2.0F);
        }
        super.setDead();
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeInt(shooter != null ? shooter.getEntityId() : 0);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        int id = additionalData.readInt();
        if (id > 0)
        {
            Entity e = world.getEntityByID(id);
            if (e instanceof EntityPlayer) shooter = (EntityPlayer) e;
        }
    }

    public void shootHookshot(Entity shooter, float pitch, float yaw, float velocity, float inaccuracy)
    {
        float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float f1 = -MathHelper.sin(pitch * 0.017453292F);
        float f2 = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        this.shoot(f, f1, f2, velocity, inaccuracy);
        this.motionX += shooter.motionX;
        this.motionZ += shooter.motionZ;

        if (!shooter.onGround)
        {
            this.motionY += shooter.motionY;
        }
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        dataManager.register(ANCHORED, false);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (shooter == null || shooter.inventory.getCurrentItem().isEmpty() || shooter.inventory.getCurrentItem().getItem() != LGItems.HOOKSHOT)
        {
            setDead();
            return;
        }

        travelTime--;

        boolean anchored = dataManager.get(ANCHORED);
        if (anchored)
        {
            if (!world.isRemote && (!isValidAnchor(posX, posY, posZ) || getDistance(shooter) < 3.0D || shooter.isRiding()))
            {
                setDead();
            }

            Vec3d toMe = new Vec3d(posX - shooter.posX, posY - shooter.posY - shooter.getEyeHeight(), posZ - shooter.posZ).normalize();
            shooter.motionX = toMe.x;
            shooter.motionY = toMe.y + 0.25D;
            shooter.motionZ = toMe.z;
            shooter.fallDistance = 0.0F;

            if (this.travelTime % 2 == 0 && !this.world.isRemote)
            {
                world.playSound(null, shooter.posX, shooter.posY, shooter.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 0.2F, 2.0F);
            }
        }
        else
        {
            if (this.travelTime % 2 == 0 && !this.world.isRemote)
            {
                world.playSound(null, shooter.posX, shooter.posY, shooter.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 0.2F, 1.5F);
            }

            if (travelTime <= -15)
            {
                setDead();
            }
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
        if (result.typeOfHit == RayTraceResult.Type.BLOCK && !dataManager.get(ANCHORED) && travelTime > -20)
        {
            BlockPos pos = result.getBlockPos();
            if (!world.isRemote && isValidAnchor(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D))
            {
                dataManager.set(ANCHORED, true);
                motionX = motionY = motionZ = 0.0D;
                posX = pos.getX() + 0.5D;
                posY = pos.getY();
                posZ = pos.getZ() + 0.5D;
                velocityChanged = true;
                world.playSound(null, shooter.posX, shooter.posY, shooter.posZ, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.PLAYERS, 0.8F, 2.0F);
            }
            travelTime = Math.min(travelTime, 0);

            Block block = world.getBlockState(pos).getBlock();
            world.playSound(null, posX, posY, posZ, block.getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);

            for (int i = 0; i < 10; i++)
            {
                world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, posX, posY, posZ, rand.nextGaussian(), rand.nextGaussian(), rand.nextGaussian(), Block.getStateId(world.getBlockState(pos)));
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        if (shooter != null)
        {
            compound.setString("shooterName", shooter.getName());
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        String name = compound.getString("shooterName");
        if (!name.isEmpty())
        {
            shooter = world.getPlayerEntityByName(name);
        }
    }

    private boolean isValidAnchor(double x, double y, double z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        Material mat = world.getBlockState(pos).getMaterial();
        return mat.isSolid();
    }
}
