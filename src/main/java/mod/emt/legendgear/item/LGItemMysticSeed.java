package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGBlocks;
import mod.emt.legendgear.worldgen.LGShrubClusterMaker;

public class LGItemMysticSeed extends ItemSeeds
{
    public float charged;

    public LGItemMysticSeed(Block crops, Block soil)
    {
        super(crops, soil);
        charged = 0.0F;
        addPropertyOverride(new ResourceLocation(LegendGear.MOD_ID, "charged"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
            {
                if (stack.getItem() instanceof LGItemMysticSeed)
                {
                    return ((LGItemMysticSeed) stack.getItem()).charged;
                }
                else return 0.0F;
            }
        });
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        EnumActionResult result = super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        if (result == EnumActionResult.SUCCESS)
        {
            if (!world.isRemote)
            {
                new LGShrubClusterMaker().buildShrubStar(world, pos.getX(), pos.getY() + 1, pos.getZ(), world.isThundering());
            }
            else
            {
                for (int i = 0; i < 12; i++)
                {
                    world.spawnParticle(EnumParticleTypes.CRIT, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, itemRand.nextGaussian(), 1, itemRand.nextGaussian());
                }
            }
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
        }
        return result;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
    {
        return EnumPlantType.Plains;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos)
    {
        return LGBlocks.MYSTIC_SHRUB.getDefaultState();
    }

    @Override
    public boolean isDamageable()
    {
        return false;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected)
    {
        charged = world.isThundering() ? 1.0F : 0.0F;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.mystic_seed"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
}
