package mod.emt.legendgear.entity;

import mod.emt.legendgear.init.LGItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LGEntityFireMedallion extends EntityThrowable
{
    public LGEntityFireMedallion(World world, EntityLivingBase thrower)
    {
        super(world, thrower);
    }

    public LGEntityFireMedallion(World world)
    {
        super(world);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (world.isRemote)
            world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, posX + rand.nextGaussian() * 0.1D, posY + rand.nextGaussian() * 0.1D, posZ + rand.nextGaussian() * 0.1D, rand.nextGaussian() * 0.03D, rand.nextGaussian() * 0.03D, rand.nextGaussian() * 0.03D);
    }

    @Override
    public void onImpact(RayTraceResult result)
    {
        if (world.isRemote) return;
        world.spawnEntity(new LGEntityFireBlast(world, posX, posY, posZ, getThrower()));
        world.playSound(null, getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.NEUTRAL, 2.0F, 0.7F);
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
                world.spawnParticle(EnumParticleTypes.ITEM_CRACK, posX, posY, posZ, (rand.nextFloat() - 0.5D) * 0.08D, (rand.nextFloat() - 0.5D) * 0.08D, (rand.nextFloat() - 0.5D) * 0.08D, Item.getIdFromItem(LGItems.FIRE_MEDALLION));
            }
        }
    }
}
