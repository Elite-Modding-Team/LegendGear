package mod.emt.legendgear.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LGEntityFallingStar extends Entity implements IEntityAdditionalSpawnData
{
    public static final int DWINDLE_TIME = 20 * 22;
    public int dwindleTimer;
    private boolean impact;

    public LGEntityFallingStar(World world)
    {
        super(world);
        this.impact = false;
        this.noClip = false;
        this.setSize(0.5f, 1.0f);
        this.dwindleTimer = DWINDLE_TIME;
    }

    public LGEntityFallingStar(EntityPlayer player)
    {
        this(player.world);
        double theta = rand.nextDouble() * Math.PI * 2;
        double r = 48;
        this.posX = player.posX + Math.cos(theta) * r;
        this.posY = player.posY + 150;
        this.posZ = player.posZ + Math.sin(theta) * r;
        this.setPosition(posX, posY, posZ);
        this.motionY = 0.0;
        this.motionX = rand.nextGaussian() * 0.0;
        this.motionZ = rand.nextGaussian() * 0.0;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeBoolean(this.impact);
        buffer.writeInt(this.dwindleTimer);
    }

    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        this.impact = buffer.readBoolean();
        this.dwindleTimer = buffer.readInt();
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    public void onUpdate()
    {
        this.motionY = -2.0D;
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

        if (this.ticksExisted == 2 && !this.world.isRemote && !this.impact)
        {
            this.world.playSound(null, this.getPosition(), LGSoundEvents.ENTITY_FALLING_STAR_FALL.getSoundEvent(), SoundCategory.AMBIENT, 100.0f, 1.0f);
        }

        AxisAlignedBB boundingBox = new AxisAlignedBB(this.getEntityBoundingBox().minX, this.getEntityBoundingBox().minY - 0.5, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().maxX, this.getEntityBoundingBox().minY, this.getEntityBoundingBox().maxZ);

        if (this.world.isMaterialInBB(boundingBox, Material.WATER))
        {
            handleWaterImpact();
        }

        if (this.collided && !this.impact)
        {
            handleGroundImpact();
        }

        if (!this.world.isRemote && this.impact && this.dwindleTimer % 25 == 0)
        {
            this.world.playSound(null, this.getPosition(), LGSoundEvents.ENTITY_FALLING_STAR_TWINKLE.getSoundEvent(), SoundCategory.AMBIENT, 2.0f, 1.0f);
        }

        if (this.impact && --this.dwindleTimer <= 0)
        {
            this.world.playSound(null, this.getPosition(), LGSoundEvents.ENTITY_FALLING_STAR_DESPAWN.getSoundEvent(), SoundCategory.AMBIENT, 2.0f, 1.0f);
            handleStarDeath();
        }

        super.onUpdate();
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player)
    {
        if (!this.world.isRemote && player.inventory.addItemStackToInventory(new ItemStack(LGItems.STAR_PIECE, 1)))
        {
            this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            this.world.playSound(null, this.getPosition(), LGSoundEvents.ENTITY_FALLING_STAR_CAUGHT.getSoundEvent(), SoundCategory.PLAYERS, 2.0f, 1.0f);

            int xp = 15;
            while (xp > 0)
            {
                int split = Math.min(xp, 1);
                xp -= split;
                this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, split));
            }

            if (!this.isDead)
            {
                ((WorldServer) this.world).getEntityTracker().sendToTracking(this, new SPacketCollectItem(this.getEntityId(), player.getEntityId(), 1));
            }

            this.setDead();
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.impact = compound.getBoolean("impact");
        this.dwindleTimer = compound.getInteger("dwindle");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setBoolean("impact", this.impact);
        compound.setInteger("dwindle", this.dwindleTimer);
    }

    private void handleWaterImpact()
    {
        if (!this.impact && !this.world.isRemote)
        {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 5.0f, false);
            this.world.playSound(null, this.getPosition(), LGSoundEvents.ENTITY_FALLING_STAR_LAND.getSoundEvent(), SoundCategory.PLAYERS, 10.0f, 1.0f);
            this.setSize(0.5f, 0.5f);
            //this.world.playSound(null, this.posX, this.posY, this.posZ, LegendGear2.SPLASH_SOUND, SoundCategory.NEUTRAL, 10.0f, 1.0f);
        }
        this.impact = true;
        this.motionY += 0.2;
        this.motionY *= 0.9;
    }

    private void handleGroundImpact()
    {
        if (!this.world.isRemote)
        {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 5.0f, false);
            this.world.playSound(null, this.getPosition(), LGSoundEvents.ENTITY_FALLING_STAR_LAND.getSoundEvent(), SoundCategory.PLAYERS, 10.0f, 1.0f);
            this.setSize(0.5f, 0.5f);
            //Vec3d pos = new Vec3d(this.posX, this.posY, this.posZ);
            //this.world.spawnEntity(new EntitySpellEffect(world, SpellID.StarImpact, null, pos, 2, 1, false));
        }
        this.impact = true;
    }

    private void handleStarDeath()
    {
        if (!this.world.isRemote)
        {
            this.entityDropItem(new ItemStack(LGItems.STARDUST), 0);
            int xp = 5;
            while (xp > 0)
            {
                int split = Math.min(xp, 1);
                xp -= split;
                this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, split));
            }
        }
        this.setDead();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isInRangeToRender3d(double x, double y, double z)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBrightnessForRender()
    {
        return 15728880;
    }

    @Override
    public float getBrightness()
    {
        return 1.0F;
    }
}
