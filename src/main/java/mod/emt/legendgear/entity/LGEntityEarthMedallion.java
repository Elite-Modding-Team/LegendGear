package mod.emt.legendgear.entity;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.config.LGConfig;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.client.particle.LGParticleHandler;
import mod.emt.legendgear.init.LGItems;

public class LGEntityEarthMedallion extends EntityThrowable implements IEntityAdditionalSpawnData
{
    public LGEntityEarthMedallion(World world, EntityLivingBase thrower)
    {
        super(world, thrower);
    }

    public LGEntityEarthMedallion(World world)
    {
        super(world);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (world.isRemote)
            LGParticleHandler.spawnMagicRuneFX(this.world, this.posX + this.rand.nextGaussian() * 0.1D, this.posY + this.rand.nextGaussian() * 0.1D, this.posZ + this.rand.nextGaussian() * 0.1D, this.rand.nextGaussian() * 0.03D, this.rand.nextGaussian() * 0.03D, this.rand.nextGaussian() * 0.03D, 1.0F);
    }

    @Override
    public void onImpact(RayTraceResult result)
    {
        if (world.isRemote) return;
        world.spawnEntity(new LGEntityQuake(world, posX, posY, posZ, getThrower(), LGConfig.GENERAL_SETTINGS.earthMedallionDamage, LGConfig.GENERAL_SETTINGS.earthMedallionVerticalKnockback, LGConfig.GENERAL_SETTINGS.earthMedallionHorizontalKnockback));
        world.playSound(null, getPosition(), SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.NEUTRAL, 2.0F, 0.3F);
        world.playSound(null, getPosition(), LGSoundEvents.ITEM_MEDALLION_SHATTER.getSoundEvent(), SoundCategory.NEUTRAL, 2.0F, 0.7F);
        world.setEntityState(this, (byte) 3);
        setDead();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 3)
        {
            for (int i = 0; i < 8; ++i)
            {
                world.spawnParticle(EnumParticleTypes.ITEM_CRACK, posX, posY, posZ, (rand.nextFloat() - 0.5D) * 0.08D, (rand.nextFloat() - 0.5D) * 0.08D, (rand.nextFloat() - 0.5D) * 0.08D, Item.getIdFromItem(LGItems.EARTH_MEDALLION));
            }

            for (int i = 0; i < 15; ++i)
            {
                LGParticleHandler.spawnMagicRuneFX(this.world, this.posX + this.rand.nextGaussian() * 0.3D, this.posY, this.posZ + this.rand.nextGaussian() * 0.3D, 0.0D, this.rand.nextDouble(), 0.0D, 1.5F);
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
