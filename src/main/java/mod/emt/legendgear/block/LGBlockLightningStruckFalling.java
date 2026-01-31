package mod.emt.legendgear.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

public class LGBlockLightningStruckFalling extends LGBlockFalling
{
    public LGBlockLightningStruckFalling(Material material, SoundType soundType, MapColor mapColor, int dustColor)
    {
        super(material, soundType, mapColor, dustColor);
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
    {
        return false;
    }
}
