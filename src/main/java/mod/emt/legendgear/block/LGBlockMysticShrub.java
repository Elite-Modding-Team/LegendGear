package mod.emt.legendgear.block;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

@SuppressWarnings("deprecation")
public class LGBlockMysticShrub extends BlockBush
{
    public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);
    public static final AxisAlignedBB MYSTIC_SHRUB_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.8D, 0.9D);
    public static final AxisAlignedBB MYSTIC_SHRUB_CUT_AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.18D, 0.9D);

    public LGBlockMysticShrub()
    {
        super(Material.PLANTS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.PLAIN));
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    public int getLightValue(IBlockState state)
    {
        if (state.getValue(TYPE) == EnumType.PLAIN) return 5;
        else if (state.getValue(TYPE) == EnumType.CHARGED) return 8;
        else return 2;
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
    public boolean isReplaceable(IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World world, BlockPos pos)
    {
        return blockState.getValue(TYPE) == EnumType.CUT ? 1.0F : super.getBlockHardness(blockState, world, pos);
    }

    @Override
    public void onPlayerDestroy(World world, BlockPos pos, IBlockState state)
    {
        if (state.getValue(TYPE) != EnumType.CUT) world.setBlockState(pos, state.withProperty(TYPE, EnumType.CUT), 2);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return null;
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity)
    {
        if (entity instanceof EntityPlayer && state.getValue(TYPE) == EnumType.CHARGED)
        {
            world.addWeatherEffect(new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), false));
            world.setBlockState(pos, state.withProperty(TYPE, EnumType.PLAIN), 2);
        }
    }

    @Override
    public BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        if (state.getValue(TYPE) == EnumType.CUT && world.isRaining() ? rand.nextInt(4) == 0 : rand.nextInt(20) == 0)
        {
            world.setBlockState(pos, state.withProperty(TYPE, world.isThundering() ? EnumType.CHARGED : EnumType.PLAIN), 2);
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        if (state.getValue(TYPE) == EnumType.CUT) return MYSTIC_SHRUB_CUT_AABB;
        return MYSTIC_SHRUB_AABB;
    }

    public enum EnumType implements IStringSerializable
    {
        PLAIN(0, "plain"), CHARGED(1, "charged"), CUT(2, "cut");

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
