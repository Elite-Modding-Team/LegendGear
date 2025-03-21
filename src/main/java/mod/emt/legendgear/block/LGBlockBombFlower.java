package mod.emt.legendgear.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import mod.emt.legendgear.entity.LGEntityBomb;

@SuppressWarnings("deprecation")
public class LGBlockBombFlower extends Block implements IShearable
{
    public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);
    public static final AxisAlignedBB BOMB_FLOWER_NORMAL_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.875F, 1.0F);
    public static final AxisAlignedBB BOMB_FLOWER_CUT_AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);

    public LGBlockBombFlower()
    {
        super(Material.PLANTS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.NORMAL));
        this.setTickRandomly(true);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(TYPE, EnumType.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(TYPE).getMeta();
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos)
    {
        if (blockState.getValue(TYPE) == EnumType.CUT) return 1.0F;
        return 0.0F;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return state.getValue(TYPE) == EnumType.CUT ? BOMB_FLOWER_CUT_AABB : BOMB_FLOWER_NORMAL_AABB;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos)
    {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        checkFlowerChange(world, pos);
        if (rand.nextInt(5) == 0 && state.getValue(TYPE) == EnumType.CUT)
            world.setBlockState(pos, state.withProperty(TYPE, EnumType.NORMAL), 2);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
    {
        super.neighborChanged(state, world, pos, block, fromPos);
        checkFlowerChange(world, pos);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        return super.canPlaceBlockAt(world, pos) && canBlockStay(world, pos);
    }

    @Override
    public BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.bomb_flower"));
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (state.getValue(TYPE) == EnumType.NORMAL)
        {
            world.setBlockState(pos, state.withProperty(TYPE, EnumType.CUT), 2);
            spawnBomb(world, pos);
            return true;
        }
        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
    {
        if (world.getBlockState(pos).getValue(TYPE) == EnumType.NORMAL)
        {
            world.setBlockState(pos, getDefaultState().withProperty(TYPE, EnumType.CUT), 2);
            spawnBomb(world, pos);
        }
    }

    public boolean canBlockStay(World world, BlockPos pos)
    {
        int radius = 1;
        boolean hasSurface = false;
        boolean hasLava = false;
        boolean hasAir = true;
        if (world.getBlockState(pos.down()).getBlock() instanceof BlockStone || world.getBlockState(pos.down()).getBlock() instanceof BlockNetherrack)
            hasSurface = true;
        if (!(world.getBlockState(pos.up()).getBlock() instanceof BlockAir)) hasAir = false;
        for (int x = pos.getX() - radius; x <= pos.getX() + radius; x++)
        {
            for (int z = pos.getZ() - radius; z <= pos.getZ() + radius; z++)
            {
                if (world.getBlockState(new BlockPos(x, pos.down().getY(), z)).getBlock() == Blocks.LAVA)
                    hasLava = true;
            }
        }
        return hasSurface && hasLava && hasAir;
    }

    @Override
    public boolean isShearable(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos)
    {
        return item.getItem() instanceof ItemShears && world.getBlockState(pos).getValue(TYPE) == EnumType.NORMAL;
    }

    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        stacks.add(new ItemStack(this));
        return stacks;
    }

    public void checkFlowerChange(World world, BlockPos pos)
    {
        if (!canBlockStay(world, pos))
        {
            if (world.getBlockState(pos).getValue(TYPE) == EnumType.NORMAL) spawnBomb(world, pos);
            world.setBlockToAir(pos);
        }
    }

    public void spawnBomb(World world, BlockPos pos)
    {
        if (!world.isRemote)
        {
            LGEntityBomb eb = new LGEntityBomb(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 80);
            eb.fuse -= world.rand.nextInt(5);
            eb.motionY = 0.1D;
            world.spawnEntity(eb);
        }
    }

    public enum EnumType implements IStringSerializable
    {
        NORMAL(0, "normal"), CUT(1, "cut");

        private static final EnumType[] META_LOOKUP = new EnumType[values().length];

        static
        {
            for (EnumType type : values())
            {
                META_LOOKUP[type.getMeta()] = type;
            }
        }

        public static EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length) meta = 0;
            return META_LOOKUP[meta];
        }

        private final int meta;
        private final String name;

        EnumType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
    }
}
