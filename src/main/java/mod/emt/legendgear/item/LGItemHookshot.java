package mod.emt.legendgear.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.entity.LGEntityHookshot;

public class LGItemHookshot extends Item
{
    private static final String NBT_CAST = "cast";
    private static final ResourceLocation RL_CAST = new ResourceLocation(LegendGear.MOD_ID, NBT_CAST);

    public static boolean isCast(ItemStack stack)
    {
        NBTTagCompound nbt = stack.getTagCompound();
        return nbt != null && nbt.getBoolean(NBT_CAST);
    }

    public static void setCast(ItemStack stack, boolean cast)
    {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null)
        {
            nbt = new NBTTagCompound();
            stack.setTagCompound(nbt);
        }
        nbt.setBoolean(NBT_CAST, cast);
    }

    public LGItemHookshot()
    {
        this.setMaxDamage(256);
        this.setMaxStackSize(1);
        this.setFull3D();
        this.addPropertyOverride(RL_CAST, (stack, world, entity) -> isCast(stack) ? 1.0F : 0.0F);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        if (hand == EnumHand.OFF_HAND) return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));

        ItemStack stack = player.getHeldItem(hand);
        if (isCast(stack))
        {
            if (!world.isRemote)
            {
                stack.damageItem(1, player);
            }
            setCast(stack, false);
        }
        else
        {
            if (!world.isRemote)
            {
                LGEntityHookshot hook = new LGEntityHookshot(world, player);
                world.spawnEntity(hook);
                hook.shootHookshot(player, player.rotationPitch, player.rotationYaw, 1.5F, 1.0F);
                world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            setCast(stack, true);
            player.getCooldownTracker().setCooldown(this, 40);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRotateAroundWhenRendering()
    {
        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (!isSelected)
        {
            setCast(stack, false);
        }
    }
}
