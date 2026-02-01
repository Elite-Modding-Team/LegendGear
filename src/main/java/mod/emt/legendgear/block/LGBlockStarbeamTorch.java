package mod.emt.legendgear.block;

import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;
import mod.emt.legendgear.client.particle.LGParticleHandler;
import mod.emt.legendgear.entity.LGEntityGrindRail;

public class LGBlockStarbeamTorch extends BlockTorch
{
    public LGBlockStarbeamTorch()
    {
        super();
        this.setLightLevel(1.0F);
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side)
    {
        return side == EnumFacing.UP && world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.UP);
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;

            if (!world.isRemote && player.fallDistance > 0.1D && player.getRidingEntity() == null)
            {
                LGEntityGrindRail rail = LGEntityGrindRail.tryMakingRail(world, pos.getX(), pos.getY(), pos.getZ(), player);

                if (rail != null)
                {
                    world.spawnEntity(rail);
                    player.startRiding(rail);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
    {
        int facing = state.getValue(FACING).getIndex();

        double posX = pos.getX() + 0.5D;
        double posY = pos.getY() + 0.7D;
        double posZ = pos.getZ() + 0.5D;

        double var13 = 0.22D;
        double var15 = 0.27D;

        double ox = rand.nextGaussian() * 0.08D;
        double oy = rand.nextGaussian() * 0.08D - 0.04D;
        double oz = rand.nextGaussian() * 0.08D;

        // TODO: Fix particle positions
        if (facing == 4)
        { // West
            LGParticleHandler.spawnSparkleFX(world, posX - var15 + ox, posY + var13 + oy, posZ + oz, 0.0D, 0.0D, 0.0D, 0.5F);
        }
        else if (facing == 5)
        { // East
            LGParticleHandler.spawnSparkleFX(world, posX + var15 + ox, posY + var13 + oy, posZ + oz, 0.0D, 0.0D, 0.0D, 0.5F);
        }
        else if (facing == 2)
        { // North
            LGParticleHandler.spawnSparkleFX(world, posX + ox, posY + var13 + oy, posZ - var15 + oz, 0.0D, 0.0D, 0.0D, 0.5F);
        }
        else if (facing == 3)
        { // South
            LGParticleHandler.spawnSparkleFX(world, posX + ox, posY + var13 + oy, posZ + var15 + oz, 0.0D, 0.0D, 0.0D, 0.5F);
        }
        else
        {
            LGParticleHandler.spawnSparkleFX(world, posX + ox, posY + oy, posZ + oz, 0.0D, 0.0D, 0.0D, 0.5F);
        }
    }
}
