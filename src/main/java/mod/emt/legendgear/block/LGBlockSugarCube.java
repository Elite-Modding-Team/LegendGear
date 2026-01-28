package mod.emt.legendgear.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class LGBlockSugarCube extends Block
{
    public LGBlockSugarCube()
    {
        super(Material.SAND, MapColor.SNOW);
        this.setLightOpacity(5);
        this.setHardness(0.25F);
        this.setTickRandomly(true);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        drops.add(new ItemStack(Items.SUGAR, 9));
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
    {
        checkForDissolve(world, pos, state);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        checkForDissolve(world, pos, state);
    }

    private void checkForDissolve(World world, BlockPos pos, IBlockState state)
    {
        boolean shouldDissolve = false;

        for (EnumFacing side : EnumFacing.values()) {
            if (world.getBlockState(pos.offset(side)).getMaterial() == Material.WATER)
            {
                shouldDissolve = true;
                break;
            }
        }

        if (shouldDissolve)
        {
            world.playEvent(2001, pos, Block.getStateId(world.getBlockState(pos)));
            world.setBlockToAir(pos);

            if (!world.isRemote)
            {
                this.dropBlockAsItem(world, pos, state, 0);
            }
        }
    }

    @Override
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (world.isRainingAt(pos.up())) {
            if (random.nextInt(3) == 0) {
                world.playEvent(2001, pos, Block.getStateId(state));
                world.setBlockToAir(pos);

                if (!world.isRemote) {
                    this.dropBlockAsItem(world, pos, state, 0);
                }
            }
        }
    }
}
