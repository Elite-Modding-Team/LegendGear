package mod.emt.legendgear.worldgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;
import mod.emt.legendgear.block.LGBlockMysticShrub;
import mod.emt.legendgear.init.LGBlocks;

public class LGShrubClusterMaker extends WorldGenerator
{
    public LGShrubClusterMaker()
    {
    }

    public void putShrub(World world, int x, int y, int z)
    {
        putShrub(world, new BlockPos(x, y, z));
    }

    public void putShrub(World world, int x, int y, int z, boolean charged)
    {
        putShrub(world, new BlockPos(x, y, z), charged);
    }

    public void putShrub(World world, BlockPos pos)
    {
        if (LGBlocks.MYSTIC_SHRUB.canPlaceBlockAt(world, pos) && LGBlocks.MYSTIC_SHRUB.canBlockStay(world, pos, LGBlocks.MYSTIC_SHRUB.getDefaultState()))
        {
            this.setBlockAndNotifyAdequately(world, pos, LGBlocks.MYSTIC_SHRUB.getDefaultState());
        }
    }

    public void putShrub(World world, BlockPos pos, boolean charged)
    {
        if (LGBlocks.MYSTIC_SHRUB.canPlaceBlockAt(world, pos) && LGBlocks.MYSTIC_SHRUB.canBlockStay(world, pos, LGBlocks.MYSTIC_SHRUB.getDefaultState()))
        {
            this.setBlockAndNotifyAdequately(world, pos, charged ? LGBlocks.MYSTIC_SHRUB.getDefaultState().withProperty(LGBlockMysticShrub.TYPE, LGBlockMysticShrub.EnumType.CHARGED) : LGBlocks.MYSTIC_SHRUB.getDefaultState());
        }
    }

    public void buildShrubStar(World world, int x, int y, int z, boolean charged)
    {
        putShrub(world, x - 1, y, z, charged);
        putShrub(world, x, y, z - 1, charged);
        putShrub(world, x + 1, y, z, charged);
        putShrub(world, x, y, z + 1, charged);
        putShrub(world, x - 3, y, z, charged);
        putShrub(world, x, y, z - 3, charged);
        putShrub(world, x + 3, y, z, charged);
        putShrub(world, x, y, z + 3, charged);
        putShrub(world, x - 2, y, z - 2, charged);
        putShrub(world, x + 2, y, z - 2, charged);
        putShrub(world, x + 2, y, z + 2, charged);
        putShrub(world, x - 2, y, z + 2, charged);
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos position)
    {
        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();
        if (rand.nextDouble() < 0.3D)
        {
            buildShrubStar(world, x, y, z, false);
        }
        else
        {
            putShrub(world, x, y, z + 1);
            putShrub(world, x + 1, y, z + 1);
            for (int i = -1; i < 3; i++) putShrub(world, x + i, y, z);
            for (int i = -1; i < 3; i++) putShrub(world, x + i, y, z - 1);
            putShrub(world, x, y, z - 2);
            putShrub(world, x + 1, y, z - 2);
        }
        world.notifyLightSet(position);
        return true;
    }
}
