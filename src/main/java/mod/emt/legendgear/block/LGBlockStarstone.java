package mod.emt.legendgear.block;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGBlocks;
import mod.emt.legendgear.init.LGItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Random;

public class LGBlockStarstone extends Block
{
    private static final ResourceLocation INFUSED_STARSTONE = new ResourceLocation(LegendGear.MOD_ID, "infused_starstone");
    private static final ResourceLocation STARSTONE = new ResourceLocation(LegendGear.MOD_ID, "starstone");
    public float resistance;

    public LGBlockStarstone(float resistanceAmount)
    {
        super(Material.ROCK, MapColor.SILVER);
        this.setLightLevel(1.0F);
        this.setHardness(5.0F);
        this.setResistance(resistance);
        resistance = resistanceAmount;
    }

    @Override
    public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon)
    {
        return true;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 1;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        if (this.equals(LGBlocks.INFUSED_STARSTONE_BLOCK)) {
            return LGItems.INFUSED_STARSTONE;
        }

        return LGItems.STARSTONE;
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state)
    {
        if (this.equals(LGBlocks.INFUSED_STARSTONE_BLOCK)) {
            return new ItemStack(ForgeRegistries.ITEMS.getValue(INFUSED_STARSTONE));
        }

        return new ItemStack(ForgeRegistries.ITEMS.getValue(STARSTONE));
    }
}
