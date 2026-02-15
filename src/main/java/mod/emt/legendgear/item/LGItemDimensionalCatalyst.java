package mod.emt.legendgear.item;

import mod.emt.legendgear.config.LGConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mod.emt.legendgear.init.LGBlocks;
import mod.emt.legendgear.init.LGItems;

public class LGItemDimensionalCatalyst extends Item
{

    public LGItemDimensionalCatalyst()
    {
        super();
        setMaxStackSize(16);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        if (world.isRemote || !player.capabilities.allowEdit || !world.isBlockModifiable(player, pos))
        {
            return EnumActionResult.PASS;
        }

        ItemStack stack = player.getHeldItem(hand);
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        // Special behavior: Replace targeted azurite ore with stone, drop azurite
        if (block == LGBlocks.AZURITE_ORE && LGConfig.GENERAL_SETTINGS.dimensionalCatalystAzuriteExtraction)
        {
            int dx = side.getXOffset();
            int dy = side.getYOffset();
            int dz = side.getZOffset();
            world.destroyBlock(pos, false);
            world.setBlockState(pos, Blocks.STONE.getDefaultState());
            for (int j = 0; j < 3; j++)
            {
                EntityItem itemEntity = new EntityItem(world, pos.getX() + 0.5 + dx, pos.getY() + 0.5 + dy, pos.getZ() + 0.5 + dz, new ItemStack(LGItems.AZURITE, 1));
                world.spawnEntity(itemEntity);
            }
            world.playSound(null, pos, SoundEvents.ENTITY_ILLAGER_MIRROR_MOVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            if (!player.capabilities.isCreativeMode) stack.shrink(1);
            // TODO: Advancement
            return EnumActionResult.SUCCESS;
        }

        // Regular behavior: Teleport targeted block to random adjacent position
        for (int i = 0; i < 6; i++)
        {
            int dx = world.rand.nextInt(3) - 1;
            int dy = world.rand.nextInt(3) - 1;
            int dz = world.rand.nextInt(3) - 1;
            BlockPos target = pos.add(dx, dy, dz);
            if (world.isAirBlock(target) && block.canPlaceBlockAt(world, target))
            {
                world.setBlockState(target, state);
                world.playSound(null, pos, SoundEvents.ENTITY_ENDERMEN_TELEPORT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.setBlockToAir(pos);
                if (!player.capabilities.isCreativeMode) stack.shrink(1);
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }
}
