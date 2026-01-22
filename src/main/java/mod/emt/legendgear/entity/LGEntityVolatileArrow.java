package mod.emt.legendgear.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class LGEntityVolatileArrow extends EntityArrow {
    public LGEntityVolatileArrow(World worldIn) {
        super(worldIn);
    }

    public LGEntityVolatileArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public LGEntityVolatileArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (ticksExisted > 140) {
            setDead();
            world.playSound(null, getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.NEUTRAL, 0.1F, 1.0F / (rand.nextFloat() * 0.4F + 1.2F) + 0.5F);
            world.spawnParticle(EnumParticleTypes.CLOUD, posX, posY, posZ, 0.0D, 0.1D, 0.0D);
        }
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }
}
