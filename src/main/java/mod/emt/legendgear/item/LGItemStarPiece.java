package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;
import mod.emt.legendgear.client.particle.LGParticleHandler;
import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.init.LGSoundEvents;

// TODO: Fix particle XYZ positions
public class LGItemStarPiece extends Item
{
    // The amount of experience levels required to infuse our star piece here
    private static final int INFUSE_LEVEL = 3;

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        // Have the correct amount of levels? Start infusing the star piece
        if (this.equals(LGItems.STAR_PIECE) && player.experienceLevel >= INFUSE_LEVEL)
        {
            player.setActiveHand(hand);

            // Already infused? Play alternate sound and spawn sparkle particles
        }
        else if (this.equals(LGItems.INFUSED_STAR_PIECE))
        {
            final Random rand = player.world.rand;
            final Vec3d forward = player.getLookVec();
            final Vec3d right = forward.crossProduct(new Vec3d(0.0D, 1.0D, 0.0D)).normalize();
            final float fs = 0.3F;
            final float rs = 0.5F;
            final float rands = 0.1F;
            final double x = player.posX + fs * forward.x + rs * right.x + rands * rand.nextGaussian();
            final double y = player.posY + fs * forward.y + rs * right.y - 0.2D;
            final double z = player.posZ + fs * forward.z + rs * right.z + rands * rand.nextGaussian();

            LGParticleHandler.spawnSparkleFX(player.world, x, y + 0.5D, z, rands * rand.nextGaussian(), rands * rand.nextGaussian(), rands * rand.nextGaussian(), 1.0F);
            world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_STAR_PIECE_SPARKLE.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 1.0F + world.rand.nextFloat() * 0.2F);

            // If insufficient levels, warn the player
        }
        else if (player.experienceLevel < INFUSE_LEVEL)
        {
            player.sendStatusMessage(new TextComponentTranslation("message.legendgear.no_levels"), true);
            world.playSound(null, player.getPosition(), LGSoundEvents.RANDOM_MYSTERY_SPARKLE.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 1.0F + world.rand.nextFloat() * 0.2F);
        }

        return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;

            // Give us our infused star piece!
            if (this.equals(LGItems.STAR_PIECE) && player.experienceLevel >= INFUSE_LEVEL)
            {
                if (!world.isRemote)
                {
                    stack.shrink(1);
                    world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_STAR_PIECE_CHARGE_END.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 1.0F);
                    player.addExperienceLevel(-INFUSE_LEVEL);

                    for (int i = 0; i < 20; ++i)
                    {
                        final Random rand = world.rand;
                        final Vec3d forward = player.getLookVec();
                        final Vec3d right = forward.crossProduct(new Vec3d(0.0D, 1.0D, 0.0D)).normalize();
                        final float fs = 0.3F;
                        final float rs = 0.5F;
                        final float rands = 0.1F;
                        final double x = player.posX + fs * forward.x + rs * right.x;
                        final double y = player.posY + fs * forward.y + rs * right.y - 0.3D;
                        final double z = player.posZ + fs * forward.z + rs * right.z;

                        LGParticleHandler.spawnSparkleFX(player.world, x, y + 0.5D, z, rands * rand.nextGaussian(), rands * rand.nextGaussian(), rands * rand.nextGaussian(), 1.0F);
                    }

                    // Convert to infused star piece otherwise just add it to the inventory if the star pieces are stacked
                    if (stack.getCount() <= 0)
                    {
                        player.getCooldownTracker().setCooldown(this, 20);
                        return new ItemStack(LGItems.INFUSED_STAR_PIECE);
                    }
                    else
                    {
                        player.getCooldownTracker().setCooldown(this, 20);
                        player.addItemStackToInventory(new ItemStack(LGItems.INFUSED_STAR_PIECE));
                    }
                }
            }
        }

        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 64;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        if (this.equals(LGItems.STAR_PIECE))
        {
            tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.star_piece", INFUSE_LEVEL));
        }
        else if (this.equals(LGItems.INFUSED_STAR_PIECE))
        {
            tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.infused_star_piece"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return this.equals(LGItems.INFUSED_STAR_PIECE);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return this.equals(LGItems.INFUSED_STAR_PIECE) ? EnumRarity.EPIC : EnumRarity.RARE;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
    {
        // Start charging, play sound at the start and start sparkle particles
        if (!player.world.isRemote)
        {
            if (count == 56)
            {
                player.world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_STAR_PIECE_CHARGE_START.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
        }
        else if (count <= 56)
        {
            Random rand = player.world.rand;
            Vec3d forward = player.getLookVec();
            Vec3d right = forward.crossProduct(new Vec3d(0.0D, 1.0D, 0.0D)).normalize();
            float fs = 0.3F;
            float rs = 0.5F;
            float rands = 0.1F;
            double x = player.posX + fs * forward.x + rs * right.x + rands * rand.nextGaussian();
            double y = player.posY + fs * forward.y + rs * right.y - 0.2D;
            double z = player.posZ + fs * forward.z + rs * right.z + rands * rand.nextGaussian();

            LGParticleHandler.spawnSparkleFX(player.world, x, y + 0.5D, z, 0.0D, 0.05D, 0.0D, 1.0F);
        }
    }
}
