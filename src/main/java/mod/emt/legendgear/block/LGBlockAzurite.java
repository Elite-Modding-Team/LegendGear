package mod.emt.legendgear.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class LGBlockAzurite extends Block
{
    public LGBlockAzurite()
    {
        super(Material.IRON, MapColor.SILVER);
        this.setHardness(4.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.GLASS);
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon)
    {
        return true;
    }
}
