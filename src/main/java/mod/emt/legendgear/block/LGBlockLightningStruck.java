package mod.emt.legendgear.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

public class LGBlockLightningStruck extends Block
{
    private final MapColor mapColor;

    public LGBlockLightningStruck(Material material, SoundType soundType, MapColor mapColor)
    {
        super(material);
        this.mapColor = mapColor;
        this.setSoundType(soundType);
        this.setHarvestLevel("shovel", 0);
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return mapColor;
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
    {
        return false;
    }
}
