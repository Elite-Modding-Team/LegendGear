package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.item.base.LGItemFood;
import mod.emt.legendgear.util.TooltipHelper;

public class LGItemRockCandy extends LGItemFood
{
    public LGItemRockCandy()
    {
        super(3, 0.3F, false, 24);
        this.setAlwaysEdible();
        this.setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
    {
        super.onItemUseFinish(stack, world, entity);

        if (!world.isRemote)
        {
            if (this.equals(LGItems.CHORUS_FRUIT_ROCK_CANDY))
            {
                double d0 = entity.posX;
                double d1 = entity.posY;
                double d2 = entity.posZ;

                for (int i = 0; i < 32; ++i)
                {
                    double d3 = entity.posX + (entity.getRNG().nextDouble() - 0.5D) * 32.0D;
                    double d4 = MathHelper.clamp(entity.posY + (double) (entity.getRNG().nextInt(32) - 16), 0.0D, world.getActualHeight() - 1);
                    double d5 = entity.posZ + (entity.getRNG().nextDouble() - 0.5D) * 32.0D;

                    if (entity.isRiding())
                    {
                        entity.dismountRidingEntity();
                    }

                    if (entity.attemptTeleport(d3, d4, d5))
                    {
                        world.playSound(null, d0, d1, d2, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                        entity.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                        break;
                    }
                }

                if (entity instanceof EntityPlayer)
                {
                    ((EntityPlayer) entity).getCooldownTracker().setCooldown(this, 20);
                }
            }
        }

        return new ItemStack(Items.STICK);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        TooltipHelper.addWrappedTooltip(tooltip, TextFormatting.GRAY, I18n.format("tooltip.legendgear.rock_candy"));
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player)
    {
        if (!world.isRemote)
        {
            player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 30 * 20, 2));

            if (stack.getItem() == LGItems.DIAMOND_ROCK_CANDY)
            {
                player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 30 * 20, 2));
                player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 30 * 20, 2));
            }
            else if (stack.getItem() == LGItems.EMERALD_ROCK_CANDY)
            {
                player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 30 * 20, 2));
            }
            else if (stack.getItem() == LGItems.GLOWSTONE_ROCK_CANDY)
            {
                player.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 30 * 20, 0));
            }
            else if (stack.getItem() == LGItems.LAPIS_LAZULI_ROCK_CANDY)
            {
                player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 30 * 20, 2));
            }
            else if (stack.getItem() == LGItems.QUARTZ_ROCK_CANDY)
            {
                player.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 30 * 20, 0));
            }
            else if (stack.getItem() == LGItems.REDSTONE_ROCK_CANDY)
            {
                player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 30 * 20, 0));
            }
        }

        super.onFoodEaten(stack, world, player);
    }
}