package mod.emt.legendgear.worldgen;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;
import mod.emt.legendgear.config.LGConfig;
import mod.emt.legendgear.init.LGBlocks;

public class LGAzuriteGenerator implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.provider.getDimension() == 0)
        {
            int blockX = chunkX * 16 + 8;
            int blockZ = chunkZ * 16 + 8;

            for (int i = 0; i < LGConfig.WORLD_GEN_SETTINGS.azuriteOreFrequency; i++)
            {
                int y = random.nextInt(LGConfig.WORLD_GEN_SETTINGS.azuriteOreAltitudeMax + 1 - LGConfig.WORLD_GEN_SETTINGS.azuriteOreAltitudeMin) + LGConfig.WORLD_GEN_SETTINGS.azuriteOreAltitudeMin;

                int localX = random.nextInt(16);
                for (int localZ = 0; localZ < 16; localZ++)
                {
                    BlockPos pos = new BlockPos(blockX + localX, y, blockZ + localZ);
                    BlockPos pos2 = pos.south();

                    if (world.getBlockState(pos).getBlock() instanceof BlockStone && world.isAirBlock(pos2))
                    {
                        generateOres(random, world, pos, EnumFacing.SOUTH);
                    }
                    if (world.isAirBlock(pos) && world.getBlockState(pos2).getBlock() instanceof BlockStone)
                    {
                        generateOres(random, world, pos2, EnumFacing.NORTH);
                    }
                }

                int localZ = random.nextInt(16);
                for (int lx = 0; lx < 16; lx++)
                {
                    BlockPos pos = new BlockPos(blockX + lx, y, blockZ + localZ);
                    BlockPos pos2 = pos.east();

                    if (world.getBlockState(pos).getBlock() instanceof BlockStone && world.isAirBlock(pos2))
                    {
                        generateOres(random, world, pos, EnumFacing.EAST);
                    }
                    if (world.isAirBlock(pos) && world.getBlockState(pos2).getBlock() instanceof BlockStone)
                    {
                        generateOres(random, world, pos2, EnumFacing.WEST);
                    }
                }
            }
        }
    }

    private void generateOres(Random random, World world, BlockPos core, EnumFacing airDirection)
    {
        IBlockState azuriteOre = LGBlocks.AZURITE_ORE.getDefaultState();
        int r = 2;
        int h = 2;

        if (airDirection.getAxis() == EnumFacing.Axis.X)
        {
            for (int yOff = -h; yOff <= h; yOff++)
            {
                for (int zOff = -r; zOff <= r; zOff++)
                {
                    BlockPos orePos = core.add(0, yOff, zOff);
                    BlockPos crystalPos = orePos.offset(airDirection);

                    if (random.nextFloat() < LGConfig.WORLD_GEN_SETTINGS.azuriteOreDensity && world.getBlockState(orePos).getBlock() instanceof BlockStone && world.isAirBlock(crystalPos))
                    {
                        world.setBlockState(orePos, azuriteOre, 2);
                    }
                }
            }
        }
        else if (airDirection.getAxis() == EnumFacing.Axis.Z)
        {
            for (int yOff = -h; yOff <= h; yOff++)
            {
                for (int xOff = -r; xOff <= r; xOff++)
                {
                    BlockPos orePos = core.add(xOff, yOff, 0);
                    BlockPos crystalPos = orePos.offset(airDirection);

                    if (random.nextFloat() < LGConfig.WORLD_GEN_SETTINGS.azuriteOreDensity && world.getBlockState(orePos).getBlock() instanceof BlockStone && world.isAirBlock(crystalPos))
                    {
                        world.setBlockState(orePos, azuriteOre, 2);
                    }
                }
            }
        }
    }
}
