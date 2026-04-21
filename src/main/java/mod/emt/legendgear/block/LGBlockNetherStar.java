package mod.emt.legendgear.block;

import mod.emt.legendgear.tileentity.LGTileEntityNetherStarBlock;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class LGBlockNetherStar extends Block implements ITileEntityProvider
{
    public float resistance;

    public LGBlockNetherStar(float resistanceAmount)
    {
        super(Material.ROCK, MapColor.SILVER);
        this.setLightLevel(1.0F);
        this.setHardness(5.0F);
        this.setResistance(resistance);
        resistance = resistanceAmount;
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta)
    {
        return new LGTileEntityNetherStarBlock();
    }

    @Override
    public boolean isBeaconBase(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull BlockPos beacon)
    {
        return true;
    }
}
