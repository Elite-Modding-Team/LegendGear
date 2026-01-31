package mod.emt.legendgear.worldgen;

import mod.emt.legendgear.init.LGBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

// TODO: Clean up and make sure they're not too rare or too common
public class LGAzuriteGenerator implements IWorldGenerator
{
    public static final int attempts = 1;
    public static final int minAltitude = 100;
    public static final int maxAltitude = 110;
    public static final float density = 0.5F;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.provider.getDimension() == 0)
        {
            int blockX = chunkX * 16 + 8;
            int blockZ = chunkZ * 16 + 8;

            for (int i = 0; i < attempts; i++)
            {
                int y = random.nextInt(maxAltitude + 1 - minAltitude) + minAltitude;

                int localX = random.nextInt(16);
                for (int localZ = 0; localZ < 16; localZ++)
                {
                    BlockPos pos = new BlockPos(blockX + localX, y, blockZ + localZ);
                    BlockPos pos2 = pos.south();

                    if (world.getBlockState(pos).getBlock() == Blocks.STONE && world.isAirBlock(pos2))
                        generateOres(random, world, pos, EnumFacing.SOUTH);
                    if (world.isAirBlock(pos) && world.getBlockState(pos2).getBlock() == Blocks.STONE)
                        generateOres(random, world, pos2, EnumFacing.NORTH);
                }

                int localZ = random.nextInt(16);
                for (int lx = 0; lx < 16; lx++)
                {
                    BlockPos pos = new BlockPos(blockX + lx, y, blockZ + localZ);
                    BlockPos pos2 = pos.east();

                    if (world.getBlockState(pos).getBlock() == Blocks.STONE && world.isAirBlock(pos2))
                        generateOres(random, world, pos, EnumFacing.EAST);
                    if (world.isAirBlock(pos) && world.getBlockState(pos2).getBlock() == Blocks.STONE)
                        generateOres(random, world, pos2, EnumFacing.WEST);
                }
            }
        }
    }

    private void generateOres(Random random, World world, BlockPos core, EnumFacing airDirection)
    {
        IBlockState azuriteOre = LGBlocks.AZURITE_ORE.getDefaultState();
        //IBlockState azuriteCrystal = Blocks.AIR.getDefaultState(); // Disabled leftover placeholder from 1.7.10
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

                    if (random.nextFloat() < density && world.getBlockState(orePos).getBlock() == Blocks.STONE && world.isAirBlock(crystalPos))
                    {
                        world.setBlockState(orePos, azuriteOre, 2);
                        //world.setBlockState(crystalPos, azuriteCrystal, 2);
                    }
                }
            }
        } else if (airDirection.getAxis() == EnumFacing.Axis.Z)
        {
            for (int yOff = -h; yOff <= h; yOff++)
            {
                for (int xOff = -r; xOff <= r; xOff++)
                {
                    BlockPos orePos = core.add(xOff, yOff, 0);
                    BlockPos crystalPos = orePos.offset(airDirection);

                    if (random.nextFloat() < density && world.getBlockState(orePos).getBlock() == Blocks.STONE && world.isAirBlock(crystalPos))
                    {
                        world.setBlockState(orePos, azuriteOre, 2);
                        //world.setBlockState(crystalPos, azuriteCrystal, 2);
                    }
                }
            }
        }
    }
}
