package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
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

public class LGItemMagicBoomerang extends Item
{
    private final int maxThrowTime;
    private final float speed;
    private final Item repairItem;

    public LGItemMagicBoomerang(int durability, int maxThrowTime, float speed, Item repairItem)
    {
        super();
        setMaxStackSize(1);
        setMaxDamage(durability);
        this.maxThrowTime = maxThrowTime;
        this.speed = speed;
        this.repairItem = repairItem;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        if (!world.isRemote)
        {
            LGEntityMagicBoomerang emb = new LGEntityMagicBoomerang(world, player, player.getHeldItem(hand).copy(), maxThrowTime, speed);
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
        return material.getItem() == repairItem;
    }
}
