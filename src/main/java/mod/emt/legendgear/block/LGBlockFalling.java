package mod.emt.legendgear.block;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LGBlockFalling extends BlockFalling
{
    private final int dustColor;
    private final MapColor mapColor;

    public LGBlockFalling(MapColor mapColor, int dustColor)
    {
        super(Material.GROUND);
        this.dustColor = dustColor;
        this.mapColor = mapColor;
        this.setHardness(0.5F);
        this.setSoundType(SoundType.SAND);
        this.setHarvestLevel("shovel", 0);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return mapColor;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getDustColor(IBlockState state)
    {
        return dustColor;
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
    {
        IBlockState plant = plantable.getPlant(world, pos.offset(direction));

        if (plant.getBlock() == Blocks.CACTUS || plant.getBlock() == Blocks.DEADBUSH)
        {
            return this instanceof LGBlockFalling;
        }

        return super.canSustainPlant(state, world, pos, direction, plantable);
    }
}
