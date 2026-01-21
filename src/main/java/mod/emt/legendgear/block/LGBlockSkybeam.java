package mod.emt.legendgear.block;

import mod.emt.legendgear.tileentity.LGTileEntitySkybeam;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class LGBlockSkybeam extends Block implements ITileEntityProvider
{

    public LGBlockSkybeam()
    {
        super(Material.ROCK);
        setHardness(50.0F);
        setResistance(2000.0F);
    }

    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new LGTileEntitySkybeam();
    }

    private boolean isPoweredFromSides(World world, BlockPos pos) {
        if (world.getStrongPower(pos) > 0)
            return true;
        for (EnumFacing side : EnumFacing.values())
        {
            BlockPos neighbor = pos.offset(side);
            IBlockState neighborState = world.getBlockState(neighbor);
            if (neighborState.canProvidePower() &&
                    neighborState.getWeakPower((IBlockAccess) world, neighbor, side.getOpposite()) > 0)
                return true;
        }
        return false;
    }

    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof LGTileEntitySkybeam)
            ((LGTileEntitySkybeam) te).setActive(isPoweredFromSides(world, pos));
    }

    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof LGTileEntitySkybeam)
            ((LGTileEntitySkybeam) tile).setActive(isPoweredFromSides(world, pos));
    }

    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
}
