package mod.emt.legendgear.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.init.LGBlocks;

public class LGTileEntitySwordPedestal extends LGTileEntityClayUrn implements ITickable
{
    public static final int CHARGE_PER_POINT = 6000;
    private long lastActiveWorldTime;
    private long storedCharge;

    public LGTileEntitySwordPedestal()
    {
        this.lastActiveWorldTime = 0;
        this.storedCharge = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        lastActiveWorldTime = compound.getLong("lastWorldTime");
        if (lastActiveWorldTime == 0L)
        {
            lastActiveWorldTime = world.getTotalWorldTime();
        }
        storedCharge = compound.getLong("storedCharge");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setLong("lastWorldTime", lastActiveWorldTime);
        compound.setLong("storedCharge", storedCharge);
        return compound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return new SPacketUpdateTileEntity(pos, 4, tag);
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
    {
        return oldState.getBlock() != newState.getBlock();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(pos).grow(1.5);
    }

    @Override
    public void update()
    {
        if (world.isRemote) return;

        long worldTime = world.getTotalWorldTime();

        BlockPos skybeamPos = pos.down(2);
        boolean skybeamPowered = world.getBlockState(skybeamPos).getBlock() == LGBlocks.SKYBEAM_BLOCK && world.isBlockPowered(pos);

        if (skybeamPowered && !getContents().isEmpty())
        {
            long delta = worldTime - lastActiveWorldTime;
            if (delta > 2)
            {
                storedCharge += delta;
            }
            else if (world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 128.0D, false) == null)
            {
                storedCharge++;
            }
        }

        lastActiveWorldTime = worldTime;

        int chargePoints = (int) (storedCharge / CHARGE_PER_POINT);
        storedCharge -= (long) chargePoints * CHARGE_PER_POINT;

        if (chargePoints > 0 && !getContents().isEmpty() && getContents().isItemStackDamageable())
        {
            getContents().setItemDamage(Math.max(0, getContents().getItemDamage() - chargePoints));
            markDirty();
        }
    }
}
