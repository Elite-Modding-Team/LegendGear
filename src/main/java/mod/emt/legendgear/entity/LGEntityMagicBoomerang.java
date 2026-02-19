package mod.emt.legendgear.entity;

import mod.emt.legendgear.init.LGItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import io.netty.buffer.ByteBuf;
import java.util.List;
import mod.emt.legendgear.block.LGBlockMysticShrub;
import mod.emt.legendgear.config.LGConfig;
import mod.emt.legendgear.init.LGSoundEvents;

public class LGEntityMagicBoomerang extends EntityThrowable implements IEntityAdditionalSpawnData
{
    private static final DataParameter<ItemStack> ITEM = EntityDataManager.createKey(LGEntityMagicBoomerang.class, DataSerializers.ITEM_STACK);

    public int thrownFromSlot;
    private int maxThrowTime;
    private float speed;
    private int returnTime;
    private ItemStack boomerangItem;
    private Entity owner;

    public LGEntityMagicBoomerang(World world, EntityLivingBase entity, ItemStack boomerangThrown, int maxThrowTime, float speed)
    {
        super(world, entity);
        this.maxThrowTime = maxThrowTime;
        this.speed = speed;
        this.returnTime = maxThrowTime;
        this.boomerangItem = boomerangThrown;
        this.owner = entity;
        this.noClip = false;
        this.setItem(boomerangThrown);
    }

    public LGEntityMagicBoomerang(World world)
    {
        super(world);
    }

    @Override
    public void writeSpawnData(ByteBuf data)
    {
        data.writeInt(thrower != null ? thrower.getEntityId() : -1);
    }

    @Override
    public void readSpawnData(ByteBuf data)
    {
        owner = world.getEntityByID(data.readInt());

        if (owner instanceof EntityLivingBase)
        {
            this.thrower = (EntityLivingBase) owner;
        }
    }

    @Override
    protected void entityInit()
    {
        this.dataManager.register(ITEM, new ItemStack(LGItems.WIND_MEDALLION));
    }

    public void setItem(ItemStack stack) {
        this.dataManager.set(ITEM, stack);
    }

    public ItemStack getItem()
    {
        return this.dataManager.get(ITEM);
    }

    @Override
    public void onUpdate()
    {
        if (!world.isRemote)
        {
            EntityPlayer thrower = null;
            if (owner instanceof EntityPlayer) thrower = (EntityPlayer) owner;
            if (thrower != null)
            {
                if (thrower.capabilities.isCreativeMode)
                {
                    ItemStack hand = thrower.inventory.mainInventory.get(thrower.inventory.currentItem);
                    if (boomerangItem.isItemEqual(hand))
                    {
                        thrower.inventory.mainInventory.set(thrower.inventory.currentItem, new ItemStack(Items.AIR));
                    }
                }
                if (returnTime == maxThrowTime)
                {
                    thrower.swingArm(EnumHand.MAIN_HAND);
                    shoot(thrower, thrower.rotationPitch, thrower.rotationYaw, 0.0F, speed, 0.0F);
                }
                if (returnTime % 3 == 0)
                {
                    world.playSound(null, getPosition(), LGSoundEvents.ENTITY_MAGIC_BOOMERANG_FLY.getSoundEvent(), SoundCategory.PLAYERS, 2.0F, 1.0F + 1.0F / (1.0F + getDistance(thrower) / 16));
                }
                if (--returnTime <= 0)
                {
                    float currentHeading = (float) Math.atan2(motionZ, motionX);
                    float headingToThrower = (float) Math.atan2(thrower.posZ - posZ, thrower.posX - posX);
                    float curveScale = -returnTime * 0.007f;
                    float newHeading = updateRotationRadians(currentHeading, headingToThrower, curveScale);
                    double currentPitch = Math.atan2(motionY, Math.sqrt(motionX * motionX + motionZ * motionZ));
                    double targetPitch = Math.atan2(thrower.posY + (double) thrower.getEyeHeight() - posY, Math.sqrt((thrower.posX - posX) * (thrower.posX - posX) + (thrower.posZ - posZ) * (thrower.posZ - posZ)));
                    float newPitch = updateRotationRadians((float) currentPitch, (float) targetPitch, curveScale * 0.3F);
                    motionX = Math.cos(newHeading) * Math.cos(newPitch);
                    motionZ = Math.sin(newHeading) * Math.cos(newPitch);
                    motionY = Math.sin(newPitch);
                    shoot(motionX, motionY, motionZ, speed, 0.0F);
                }
            }
            if (getRidingEntity() == null)
            {
                List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().grow(1.0D, 1.0D, 1.0D));
                for (Entity e : entities)
                {
                    if (e instanceof EntityItem)
                    {
                        e.startRiding(this);
                        break;
                    }
                }
            }
            if (returnTime < -100)
            {
                world.spawnEntity(new EntityItem(world, posX, posY, posZ, boomerangItem));
                setDead();
            }
            rotationPitch = returnTime * 50;
            prevRotationPitch = (returnTime + 1) * 50;
        }
        else
        {
            for (int i = 0; i < 4; i++)
            {
                world.spawnParticle(EnumParticleTypes.CRIT, posX + motionX * (double) i / 4.0D, posY + motionY * (double) i / 4.0D, posZ + motionZ * (double) i / 4.0D, -motionX, -motionY + 0.2D, -motionZ);
            }
        }
        super.onUpdate();
    }

    @Override
    protected float getGravityVelocity()
    {
        return 0.0F;
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (world.isRemote) return;
        if (result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit instanceof EntityLivingBase)
        {
            if (result.entityHit == getThrower() && returnTime <= 0 && result.entityHit instanceof EntityPlayer)
            { //caught by thrower
                EntityPlayer ep = (EntityPlayer) result.entityHit;
                ItemStack crowdedItem = ep.inventory.getStackInSlot(thrownFromSlot);
                ep.inventory.mainInventory.set(thrownFromSlot, boomerangItem);
                boolean success = ep.inventory.addItemStackToInventory(crowdedItem);
                if (!success)
                {
                    ep.dropItem(crowdedItem, true);
                }
                if (getRidingEntity() != null)
                {
                    Entity passenger = getRidingEntity();
                    passenger.startRiding(this);
                    passenger.posX = ep.posX;
                    passenger.posY = ep.posY;
                    passenger.posZ = ep.posZ;
                }
                if (!isDead)
                {
                    playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((rand.nextFloat() - rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    EntityTracker entitytracker = ((WorldServer) world).getEntityTracker();
                    entitytracker.sendToTracking(this, new SPacketCollectItem(getEntityId(), getThrower().getEntityId(), 1));
                }
                setDead();
            }
            if (result.entityHit != getThrower() && getThrower() != null)
            {
                EntityLivingBase el = (EntityLivingBase) result.entityHit;
                boomerangItem.damageItem(1, getThrower());
                if (boomerangItem.getCount() == 0)
                {
                    world.playSound(null, getPosition(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, 0.8F);
                    setDead();
                }
                el.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), (float) LGConfig.GENERAL_SETTINGS.magicBoomerangDamage);
            }
        }
        else
        {
            BlockPos blockPos = result.getBlockPos();
            IBlockState blockState = world.getBlockState(blockPos);
            Material mat = blockState.getMaterial();
            boolean solid = blockState.getCollisionBoundingBox(world, blockPos) != null;
            if (blockState.getBlockHardness(world, blockPos) == 0.0F && (mat == Material.PLANTS || mat == Material.GLASS || mat == Material.VINE))
            {
                if (blockState.getBlock() instanceof LGBlockMysticShrub)
                {
                    blockState.getBlock().harvestBlock(world, (EntityPlayer) thrower, blockPos, blockState, null, boomerangItem);
                    world.destroyBlock(blockPos, false);
                    world.setBlockState(blockPos, blockState.withProperty(LGBlockMysticShrub.TYPE, LGBlockMysticShrub.EnumType.CUT), 2);
                }
                else world.destroyBlock(blockPos, true);
                if (getThrower() != null && boomerangItem.getCount() == 0)
                {
                    world.playSound(null, getPosition(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS, 1.0F, 0.8F);
                    setDead();
                }
            }
            else if (!noClip && solid)
            {
                returnTime = Math.min(returnTime, 0);
                noClip = true;
                motionX *= -1;
                motionY *= -1;
                motionZ *= -1;
            }
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setTag("Item", boomerangItem.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        super.readEntityFromNBT(tagCompound);
        boomerangItem = new ItemStack(tagCompound.getCompoundTag("Item"));
    }

    private float updateRotationRadians(float par1, float par2, float par3)
    {
        float var4 = (par2 - par1);
        var4 %= (float) Math.PI * 2;
        if (var4 >= Math.PI) var4 -= (float) Math.PI * 2;
        if (var4 < -Math.PI) var4 += (float) Math.PI * 2;
        if (var4 > par3) var4 = par3;
        if (var4 < -par3) var4 = -par3;
        return par1 + var4;
    }
}
