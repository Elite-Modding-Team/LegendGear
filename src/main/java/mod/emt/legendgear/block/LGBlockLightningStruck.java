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

public class LGBlockLightningStruck extends Block
{
    private final Block blockDrop;
    private final MapColor mapColor;
    private final int metadata;

    public LGBlockLightningStruck(Material material, SoundType soundType, MapColor mapColor, Block blockDrop, int metadata)
    {
        super(material);
        this.blockDrop = blockDrop;
        this.mapColor = mapColor;
        this.metadata = metadata;
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

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        drops.add(new ItemStack(blockDrop, 1, metadata));
    }
}
