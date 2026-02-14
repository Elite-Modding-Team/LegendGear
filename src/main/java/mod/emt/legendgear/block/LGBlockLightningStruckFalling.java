package mod.emt.legendgear.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

public class LGBlockLightningStruckFalling extends LGBlockFalling
{
    private final Block blockDrop;
    private final int metadata;

    public LGBlockLightningStruckFalling(Material material, SoundType soundType, MapColor mapColor, Block blockDrop, int metadata, int dustColor)
    {
        super(material, soundType, mapColor, dustColor);
        this.blockDrop = blockDrop;
        this.metadata = metadata;
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
    {
        return false;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        drops.add(new ItemStack(blockDrop, 1, metadata));
    }
}
