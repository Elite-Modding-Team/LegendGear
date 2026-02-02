package mod.emt.legendgear.block;

import javax.annotation.Nullable;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import mod.emt.legendgear.init.LGBlocks;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.tileentity.LGTileEntitySwordPedestal;

public class LGBlockSwordPedestal extends BlockContainer
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyBool HAS_SWORD = PropertyBool.create("has_sword");
    private static final AxisAlignedBB AABB_NS = new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 0.5D, 0.75D);
    private static final AxisAlignedBB AABB_EW = new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 0.5D, 1.0D);

    public LGBlockSwordPedestal()
    {
        super(Material.ROCK);
        this.setHardness(4.0F);
        this.setSoundType(SoundType.STONE);
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HAS_SWORD, false));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        LGTileEntitySwordPedestal te = (LGTileEntitySwordPedestal) world.getTileEntity(pos);
        if (te != null && !te.getContents().isEmpty() && !world.isRemote)
        {
            EntityItem drop = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, te.getContents());
            drop.setDefaultPickupDelay();
            world.spawnEntity(drop);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta & 3)).withProperty(HAS_SWORD, (meta & 8) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = state.getValue(FACING).getHorizontalIndex();
        if (state.getValue(HAS_SWORD)) meta |= 8;
        return meta;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing facing = state.getValue(FACING);
        return (facing == EnumFacing.NORTH || facing == EnumFacing.SOUTH) ? AABB_NS : AABB_EW;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos)
    {
        return getBoundingBox(blockState, world, pos);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote || hand != EnumHand.MAIN_HAND) return false;

        LGTileEntitySwordPedestal te = (LGTileEntitySwordPedestal) world.getTileEntity(pos);
        if (te == null) return false;

        ItemStack held = player.getHeldItem(hand);

        // Take sword
        if (held.isEmpty() && !te.getContents().isEmpty())
        {
            player.setHeldItem(hand, te.getContents());
            te.setContents(ItemStack.EMPTY);
            world.playSound(null, pos, LGSoundEvents.BLOCK_SWORD_PEDESTAL_TAKE.getSoundEvent(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.setBlockState(pos, state.withProperty(HAS_SWORD, false));
            world.setBlockToAir(pos.up());
            world.notifyNeighborsOfStateChange(pos.down(), this, false);
            return true;
        }

        // Place sword
        if (!held.isEmpty() && te.getContents().isEmpty() && isSword(held) && (world.isAirBlock(pos.up()) || world.getBlockState(pos.up()).getBlock() == LGBlocks.SWORD_PEDESTAL_TECHNICAL))
        {
            te.setContents(held);
            player.setHeldItem(hand, ItemStack.EMPTY);
            world.playSound(null, pos, LGSoundEvents.BLOCK_SWORD_PEDESTAL_PLACE.getSoundEvent(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.setBlockState(pos, state.withProperty(HAS_SWORD, true));
            world.setBlockState(pos.up(), LGBlocks.SWORD_PEDESTAL_TECHNICAL.getDefaultState().withProperty(LGBlockSwordPedestalTechnical.FACING, state.getValue(FACING)));
            world.notifyNeighborsOfStateChange(pos.down(), this, false);
            return true;
        }

        return false;
    }

    @Override
    public int getWeakPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return state.getValue(HAS_SWORD) && side == EnumFacing.UP ? 15 : 0;
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    public int getStrongPower(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return getWeakPower(state, blockAccess, pos, side);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, HAS_SWORD);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        return new ArrayList<>();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new LGTileEntitySwordPedestal();
    }

    private boolean isSword(ItemStack stack)
    {
        return !stack.isEmpty() && stack.getItem() instanceof ItemSword;
    }
}
