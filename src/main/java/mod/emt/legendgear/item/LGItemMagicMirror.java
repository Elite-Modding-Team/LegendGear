package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.client.particle.LGParticleHandler;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.util.TooltipHelper;

public class LGItemMagicMirror extends Item
{
    public float status;

    public LGItemMagicMirror()
    {
        super();
        setMaxStackSize(1);
        status = 0.0F;
        addPropertyOverride(new ResourceLocation(LegendGear.MOD_ID, "status"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
            {
                if (stack.getItem() instanceof LGItemMagicMirror)
                {
                    return ((LGItemMagicMirror) stack.getItem()).status;
                }
                else return 0.0F;
            }
        });
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (!isNBTOutdoors() && stack.hasTagCompound() && stack.getTagCompound().hasKey("lastSkyX"))
        {
            player.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        else world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_MAGIC_MIRROR_INACTIVE.getSoundEvent(), SoundCategory.PLAYERS, 0.8F, 1.0F);
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;
            if (!isOutdoors(player, world) && world.provider.canRespawnHere())
            {
                entity.fallDistance = 0.0F;
                NBTTagCompound nbt = stack.getTagCompound();
                double tx = nbt.getDouble("lastSkyX");
                double ty = nbt.getDouble("lastSkyY");
                double tz = nbt.getDouble("lastSkyZ");
                while (!world.isSideSolid(new BlockPos(tx, ty - 1, tz), EnumFacing.UP)) ty--;
                if (world.isRemote) world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, entity.posX, entity.posY, entity.posZ, 0, 0, 0);
                player.setPositionAndUpdate(tx, ty, tz);
                entity.stopActiveHand();
                world.playSound(player, entity.getPosition(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.PLAYERS, 0.6F, 1.0F);
                if (world.isRemote) world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, tx, ty, tz, 0, 0, 0);
                else player.getCooldownTracker().setCooldown(this, 60);
            }
        }
        return stack;
    }

    @Override
    public boolean isDamageable()
    {
        return false;
    }

    @Override
    public boolean shouldRotateAroundWhenRendering()
    {
        return false;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
        if (entity.ticksExisted % 20 == 0 && entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;
            if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
            NBTTagCompound nbt = stack.getTagCompound();
            if (isOutdoors(player, world))
            {
                nbt.setDouble("lastSkyX", player.posX);
                nbt.setDouble("lastSkyY", player.posY);
                nbt.setDouble("lastSkyZ", player.posZ);
                status = 0.0F;
            }
            else if (!player.isActiveItemStackBlocking()) status = 1.0F;
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BLOCK;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 70;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        TooltipHelper.addWrappedTooltip(tooltip, TextFormatting.GRAY, I18n.format("tooltip.legendgear.magic_mirror"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return false;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase entity, int time)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;
            if (isOutdoors(player, player.world)) return;
            status = 2.0F;
            int dur = stack.getMaxItemUseDuration() - time;
            float progress = dur * 1.0f / stack.getMaxItemUseDuration();
            if (FMLLaunchHandler.side().isClient() && player.world.isRemote)
            {
                if (player instanceof EntityPlayerSP) ((EntityPlayerSP) player).timeInPortal = Math.max(progress * 0.95f, ((EntityPlayerSP) player).timeInPortal);
                double theta = Math.PI * 6 * progress;
                double r = 2 * (1 - progress);
                for (int i = 0; i < 3; i++)
                {
                    LGParticleHandler.spawnMagicRuneFX(player.world, player.posX + Math.cos(theta) * r, player.posY, player.posZ + Math.sin(theta) * r, 0.0D, 0.1D, 0.0D, 1.5F);
                    theta += Math.PI * 2 / 3;
                }
            }
            int soundOften = 8;
            if (dur % soundOften == 0)
            {
                player.world.playSound(player, player.getPosition(), LGSoundEvents.ITEM_MAGIC_MIRROR_USE.getSoundEvent(), SoundCategory.PLAYERS, 0.3F, 0.2F * (2 + (float) dur / soundOften));
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
    {
        return false;
    }

    private boolean isOutdoors(EntityPlayer player, World world)
    {
        return world.canSeeSky(player.getPosition().add(0.0D, 2.1D, 0.0D));
    }

    private boolean isNBTOutdoors()
    {
        return status == 0.0F;
    }
}
