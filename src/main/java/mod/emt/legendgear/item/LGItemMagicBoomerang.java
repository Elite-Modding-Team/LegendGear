package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import mod.emt.legendgear.entity.LGEntityMagicBoomerang;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.util.TooltipHelper;

//TODO: Let's not hardcode repair time, damage, speed, and throw time. We should make these customizable to be able to make more boomerang variants
public class LGItemMagicBoomerang extends Item
{
    public LGItemMagicBoomerang()
    {
        super();
        setMaxStackSize(1);
        setMaxDamage(1500);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        if (!world.isRemote)
        {
            LGEntityMagicBoomerang emb = new LGEntityMagicBoomerang(world, player, player.getHeldItem(hand).copy());
            emb.thrownFromSlot = player.inventory.currentItem;
            world.spawnEntity(emb);
            world.playSound(null, player.getPosition(), LGSoundEvents.ENTITY_MAGIC_BOOMERANG_FLY.getSoundEvent(), SoundCategory.PLAYERS, 3.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
            player.getHeldItem(hand).setCount(0);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        TooltipHelper.addWrappedTooltip(tooltip, TextFormatting.GRAY, I18n.format("tooltip.legendgear.magic_boomerang"));
    }

    @Override
    public boolean getIsRepairable(ItemStack stack, ItemStack material)
    {
        return material.getItem() == Items.GOLD_INGOT;
    }
}
