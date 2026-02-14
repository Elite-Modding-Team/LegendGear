package mod.emt.legendgear.block;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Random;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.tileentity.LGTileEntityInfusedStarstone;

public class LGBlockInfusedStarstone extends LGBlockStarstone implements ITileEntityProvider
{
    private static final ResourceLocation INFUSED_STARSTONE = new ResourceLocation(LegendGear.MOD_ID, "infused_starstone");

    public LGBlockInfusedStarstone(float resistanceAmount)
    {
        super(resistanceAmount);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new LGTileEntityInfusedStarstone();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return LGItems.INFUSED_STARSTONE;
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state)
    {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(INFUSED_STARSTONE));
    }
}
