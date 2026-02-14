package mod.emt.legendgear.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mod.emt.legendgear.tileentity.LGTileEntitySkybeam;

public class LGBlockSkybeam extends Block implements ITileEntityProvider
{
    public LGBlockSkybeam()
    {
        super(Material.ROCK, MapColor.BLACK);
        this.setHardness(50.0F);
        this.setResistance(2000.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new LGTileEntitySkybeam();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof LGTileEntitySkybeam)
            ((LGTileEntitySkybeam) te).setActive(isPoweredFromSides(world, pos));
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof LGTileEntitySkybeam)
            ((LGTileEntitySkybeam) tile).setActive(isPoweredFromSides(world, pos));
    }

    private boolean isPoweredFromSides(World world, BlockPos pos)
    {
        if (world.getStrongPower(pos) > 0)
            return true;
        for (EnumFacing side : EnumFacing.values())
        {
            BlockPos neighbor = pos.offset(side);
            IBlockState neighborState = world.getBlockState(neighbor);
            if (neighborState.canProvidePower() &&
                neighborState.getWeakPower(world, neighbor, side.getOpposite()) > 0)
                return true;
        }
        return false;
    }
}
