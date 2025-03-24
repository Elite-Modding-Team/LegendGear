package mod.emt.legendgear.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import io.netty.buffer.ByteBuf;
import java.util.List;
import mod.emt.legendgear.init.LGItems;

@SuppressWarnings("deprecation")
public class LGEntityBomb extends Entity implements IEntityAdditionalSpawnData
{
    public EntityPlayer thrower;
    public int fuse;
    public boolean primed = false;
    public double pulse = 0.0D;
    public boolean wasOnArrow;
    public boolean unblastable;

    public LGEntityBomb(World world)
    {
        super(world);
        this.noClip = false;
        setSize(0.5F, 0.5F);
    }

    public LGEntityBomb(World world, EntityPlayer player, int fuseTime)
    {
        this(world, player.posX, player.posY, player.posZ, fuseTime);
        this.thrower = player;
        setLocationAndAngles(player.posX, player.posY + player.getEyeHeight(), player.posZ, player.rotationYaw, player.rotationPitch);
        this.posX -= (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
        setPosition(this.posX, this.posY, this.posZ);
        float var3 = 0.4F;
        this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * var3);
        this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * var3);
        this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F) * var3);
        setThrowableHeading(this.motionX, this.motionY, this.motionZ, 0.5F, 0.0F);
    }

    public LGEntityBomb(World world, double posX, double posY, double posZ, int fuseTime)
    {
        super(world);
        setPosition(posX, posY, posZ);
        this.fuse = fuseTime;
        this.noClip = false;
        setSize(0.5F, 0.5F);
        this.unblastable = (this.fuse <= 10);
    }

    public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8)
    {
        float var9 = MathHelper.sqrt(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= var9;
        par3 /= var9;
        par5 /= var9;
        par1 += this.rand.nextGaussian() * 0.007499999832361937D * par8;
        par3 += this.rand.nextGaussian() * 0.007499999832361937D * par8;
        par5 += this.rand.nextGaussian() * 0.007499999832361937D * par8;
        par1 *= par7;
        par3 *= par7;
        par5 *= par7;
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
        float var10 = MathHelper.sqrt(par1 * par1 + par5 * par5);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, var10) * 180.0D / Math.PI);
    }

    public void fizzle()
    {
        setDead();
        if (!this.world.isRemote)
        {
            LGEntityItemNoStacking bombDrop = new LGEntityItemNoStacking(this.world, this.posX, this.posY, this.posZ, new ItemStack(LGItems.BOMB));
            this.world.spawnEntity(bombDrop);
            this.world.playSound(null, this.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.7F);
        }
        else
        {
            for (int i = 0; i < 3; i++)
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        }
    }

    public void detonate()
    {
        if (!this.world.isRemote && this.getRidingEntity() != null)
        {
            this.posX = this.getRidingEntity().posX;
            this.posY = this.getRidingEntity().posY;
            this.posZ = this.getRidingEntity().posZ;
            this.getRidingEntity().setDead();
        }
        if (this.world.isRemote)
        {
            for (int j = 0; j < 8; j++)
            {
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + this.rand.nextGaussian(), this.posY + this.rand.nextGaussian(), this.posZ + this.rand.nextGaussian(), 0.0D, 0.0D, 0.0D);
                this.world.spawnParticle(EnumParticleTypes.LAVA, this.posX, this.posY + 0.25D, this.posZ, this.rand.nextGaussian(), this.rand.nextGaussian(), this.rand.nextGaussian());
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand.nextGaussian(), this.posY + this.rand.nextGaussian(), this.posZ + this.rand.nextGaussian(), 0.0D, 0.0D, 0.0D);
            }
        }
        List<Entity> entities = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().grow(4.0D, 4.0D, 4.0D));
        DamageSource damage = DamageSource.causeThrownDamage(this, this.thrower).setExplosion();
        double blastRadius = 3.5D;
        for (Entity e : entities)
        {
            Vec3d away = new Vec3d(e.posX - this.posX, e.posY - this.posY, e.posZ - this.posZ);
            double dist = away.length();
            if (dist < blastRadius)
            {
                away.normalize();
                double blastForce = 4.0D / (2.0D + dist);
                if (e instanceof EntityLiving) e.attackEntityFrom(damage, 5.0F);
                if (!(e instanceof LGEntityBomb) || !((LGEntityBomb) e).unblastable)
                {
                    e.motionX = away.x * blastForce;
                    e.motionY = away.y < 0.6D ? 0.6D * blastForce : away.y * blastForce;
                    e.motionZ = away.z * blastForce;
                }
                e.fallDistance = 0.0F;
            }
        }
        if (!this.world.isRemote) this.world.createExplosion(this, this.posX, this.posY + (this.height / 16.0F), this.posZ, 4.0F, false);
        setDead();
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeInt(this.fuse);
        if (this.getRidingEntity() != null)
        {
            buffer.writeInt(this.getRidingEntity().getEntityId());
        }
        else
        {
            buffer.writeInt(0);
        }
        buffer.writeBoolean(this.unblastable);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        this.fuse = additionalData.readInt();
        int arrowID = additionalData.readInt();
        if (arrowID != 0 && this.getRidingEntity() == null)
        {
            startRiding(this.world.getEntityByID(arrowID));
            this.wasOnArrow = true;
        }
        this.unblastable = additionalData.readBoolean();
    }

    @Override
    public void entityInit()
    {
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (isInWater())
        {
            fizzle();
            return;
        }
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        if (this.world.isRemote) this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D);
        this.motionY -= 0.03999999910593033D;
        float var2 = 0.98F;
        if (this.onGround)
        {
            var2 = 0.58800006F;
            IBlockState blockState = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ)));
            if (blockState.isTopSolid()) var2 = blockState.getBlock().slipperiness * 0.98F;
        }
        this.motionX *= var2;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= var2;
        if (FMLLaunchHandler.side().isClient() && this.getRidingEntity() == null) setVelocity(this.motionX, this.motionY, this.motionZ);
        if (!this.primed)
        {
            this.primed = true;
            this.world.playSound(null, this.getPosition(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        this.fuse--;
        if (this.fuse < 10)
        {
            this.pulse++;
        }
        else
        {
            this.pulse += 0.1D;
        }
        if (this.fuse <= 0 || isBurning())
        {
            detonate();
        }
        else if (this.getRidingEntity() instanceof EntityArrow)
        {
            EntityArrow ea = (EntityArrow) this.getRidingEntity();
            this.motionX = ea.motionX;
            this.motionY = ea.motionY;
            this.motionZ = ea.motionZ;
            setPosition(ea.posX, ea.posY, ea.posZ);
            this.prevPosX = ea.prevPosX;
            this.prevPosY = ea.prevPosY;
            this.prevPosZ = ea.prevPosZ;
            this.lastTickPosX = ea.lastTickPosX;
            this.lastTickPosY = ea.lastTickPosY;
            this.lastTickPosZ = ea.lastTickPosZ;
            if (ea.arrowShake != 0) detonate();
            this.wasOnArrow = true;
        }
        else if (this.wasOnArrow)
        {
            detonate();
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox()
    {
        return this.canBeCollidedWith() ? this.getEntityBoundingBox() : null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()
    {
        int pulsePhase = (int) this.pulse % 2;
        if (pulsePhase == 1) return 240;
        return super.getBrightnessForRender();
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead && this.getRidingEntity() == null && this.onGround;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        this.fuse = compound.getInteger("fuse");
        this.unblastable = compound.getBoolean("unblastable");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setInteger("fuse", this.fuse);
        compound.setBoolean("unblastable", this.unblastable);
    }
}
