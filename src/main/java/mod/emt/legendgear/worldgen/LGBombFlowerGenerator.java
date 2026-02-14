package mod.emt.legendgear.worldgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;
import mod.emt.legendgear.config.LGConfig;
import mod.emt.legendgear.init.LGBlocks;

public class LGBombFlowerGenerator implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        final int spread = 9;
        final float spreadChance = 0.5F;
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int i = 0; i < LGConfig.WORLD_GEN_SETTINGS.bombFlowerFrequency; i++)
        {
            int x = (chunkX * 16) + 8 + random.nextInt(8);
            int z = (chunkZ * 16) + 8 + random.nextInt(8);
            int y = 1;

            pos.setPos(x, y, z);
            while (y < world.getHeight(x, z) && !world.isAirBlock(pos))
            {
                y++;
                pos.setPos(x, y, z);
            }

            if (LGBlocks.BOMB_FLOWER.canPlaceBlockAt(world, pos))
            {
                for (int xo = 0; xo < spread; xo++)
                {
                    for (int zo = 0; zo < spread; zo++)
                    {
                        if (random.nextFloat() < spreadChance)
                        {
                            int nx = x + xo - spread / 2;
                            int nz = z + zo - spread / 2;
                            pos.setPos(nx, y, nz);
                            if (LGBlocks.BOMB_FLOWER.canPlaceBlockAt(world, pos))
                            {
                                world.setBlockState(pos, LGBlocks.BOMB_FLOWER.getDefaultState());
                            }
                        }
                    }
                }
            }
        }
    }
}
