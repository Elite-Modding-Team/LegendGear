package mod.emt.legendgear.worldgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class LGShrubGenerator implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.getWorldType() != WorldType.FLAT && world.provider.canRespawnHere() && random.nextInt(8) == 0)
        {
            int x = (chunkX * 16) + 8 + random.nextInt(8);
            int z = (chunkZ * 16) + 8 + random.nextInt(8);
            BlockPos pos = new BlockPos(x, 0, z);
            int y = world.getHeight(pos).getY();
            pos = pos.add(0, y, 0);
            new LGShrubClusterMaker().generate(world, random, pos);
        }
    }
}
