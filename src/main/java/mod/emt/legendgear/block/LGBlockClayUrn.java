package mod.emt.legendgear.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import mod.emt.legendgear.init.LGRegistry;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.tileentity.LGTileEntityClayUrn;

public class LGBlockClayUrn extends BlockContainer
{
    private static final AxisAlignedBB URN_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 1.0D, 0.9375D);

    public LGBlockClayUrn()
    {
        super(Material.CLAY);
        this.setHardness(0.2F);
        this.setSoundType(LGRegistry.URN);
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return URN_AABB;
    }

    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos)
    {
        return URN_AABB;
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
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity)
    {
        if (!world.isRemote)
        {
            succItem(world, pos, entity);

            if (entity instanceof EntityArrow && world.setBlockToAir(pos))
            {
                world.playEvent(2001, pos, Block.getStateId(state));
            }
        }
    }

    @Override
    public void onLanded(World world, Entity entity)
    {
        super.onLanded(world, entity);
        if (!world.isRemote)
        {
            succItem(world, entity.getPosition().down(), entity);
        }
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        return new ArrayList<>();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        LGTileEntityClayUrn urn = (LGTileEntityClayUrn) world.getTileEntity(pos);
        if (urn != null && urn.getContents() != null && !world.isRemote)
        {
            EntityItem drop = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, urn.getContents());
            drop.setDefaultPickupDelay();
            world.spawnEntity(drop);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new LGTileEntityClayUrn();
    }

    private void succItem(World world, BlockPos pos, Entity entity)
    {
        if (entity instanceof EntityItem)
        {
            EntityItem itemEntity = (EntityItem) entity;
            LGTileEntityClayUrn urn = (LGTileEntityClayUrn) world.getTileEntity(pos);
            if (urn != null && !itemEntity.isDead)
            {
                ItemStack itemStack = itemEntity.getItem();
                ItemStack contents = urn.getContents();
                if (contents.isEmpty())
                {
                    urn.setContents(itemStack);
                    itemEntity.setDead();
                    world.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 0.3F, 0.5F + world.rand.nextFloat() * 0.2F);
                    world.playSound(null, pos, LGSoundEvents.BLOCK_URN_STEP.getSoundEvent(), SoundCategory.BLOCKS, 0.3F, 0.5F + world.rand.nextFloat() * 0.2F);
                }
                else if (contents.getItem() == itemStack.getItem() && contents.getCount() < contents.getMaxStackSize())
                {
                    int count = Math.min(itemStack.getCount(), contents.getMaxStackSize() - contents.getCount());
                    itemStack.shrink(count);
                    contents.grow(count);
                    if (itemStack.isEmpty())
                    {
                        itemEntity.setDead();
                    }
                    world.playSound(null, pos, SoundEvents.ENTITY_CHICKEN_EGG, SoundCategory.BLOCKS, 0.3F, 0.5F + world.rand.nextFloat() * 0.2F);
                    world.playSound(null, pos, LGSoundEvents.BLOCK_URN_STEP.getSoundEvent(), SoundCategory.BLOCKS, 0.3F, 0.5F + world.rand.nextFloat() * 0.2F);
                }
            }
        }
    }
}
