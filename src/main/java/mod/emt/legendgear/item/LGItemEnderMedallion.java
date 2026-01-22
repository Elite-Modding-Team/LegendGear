package mod.emt.legendgear.item;

import mod.emt.legendgear.entity.LGEntityEnderMedallion;
import mod.emt.legendgear.item.base.LGItemMedallion;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class LGItemEnderMedallion extends LGItemMedallion
{
    public LGItemEnderMedallion()
    {
        super();
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;
            if (!player.capabilities.isCreativeMode) stack.shrink(1);
            world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
            if (!world.isRemote)
            {
                LGEntityEnderMedallion entityEnderMedallion = new LGEntityEnderMedallion(world, player);
                entityEnderMedallion.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
                world.spawnEntity(entityEnderMedallion);
            }
        }
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.ender_medallion"));
    }
}
