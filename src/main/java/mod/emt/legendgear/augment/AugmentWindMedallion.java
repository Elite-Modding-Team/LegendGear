package mod.emt.legendgear.augment;

import mod.emt.legendgear.entity.LGEntityWhirlwind;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;

public class AugmentWindMedallion implements IMedallionAugment
{
    @Override
    public void executeAbility(EntityPlayer player, EnumHand hand, ItemStack tool) {
        if (player.world.isRemote)
        {
            return;
        }

        player.world.playSound(null, player.posX, player.posY, player.posZ, LGSoundEvents.ITEM_AUGMENT_WHIRLWIND.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 0.8F + player.world.rand.nextFloat() * 0.4F);
        player.world.spawnEntity(new LGEntityWhirlwind(player.world, player));
    }

    @Override
    public int getChargeTime()
    {
        return 20;
    }

    @Override
    public int getActivationWindow()
    {
        return 20;
    }
}