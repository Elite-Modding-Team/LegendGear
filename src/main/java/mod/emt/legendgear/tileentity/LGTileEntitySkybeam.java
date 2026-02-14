package mod.emt.legendgear.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class LGTileEntitySkybeam extends TileEntity implements ITickable
{
    public boolean active = false;

    public int tick = 0;

    public int fade = 0;

    public float currentBeamHeight = 133.0F;

    public void update()
    {
        if (!this.world.isRemote)
            return;
        int oldFade = this.fade;
        if (this.active)
        {
            if (this.fade < 20)
                this.fade++;
        }
        else if (this.fade > 0)
        {
            this.fade--;
        }
        if (this.active || this.fade > 0)
        {
            this.tick++;
            if (this.tick % 20 == 0)
                calculateBeamHeight();
            if (oldFade != this.fade)
                this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
        }
    }

    public float getRamp()
    {
        return Math.min(this.fade / 20.0F, 1.0F);
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.active = nbt.getBoolean("Active");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setBoolean("Active", this.active);
        return nbt;
    }

    public double getMaxRenderDistanceSquared()
    {
        return 65536.0D;
    }

    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new SPacketUpdateTileEntity(this.pos, 1, nbt);
    }

    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbt = super.getUpdateTag();
        writeToNBT(nbt);
        return nbt;
    }

    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.getNbtCompound());
    }

    public void handleUpdateTag(NBTTagCompound tag)
    {
        super.handleUpdateTag(tag);
        readFromNBT(tag);
    }

    public boolean shouldRenderInPass(int pass)
    {
        return (pass == 1);
    }

    public AxisAlignedBB getRenderBoundingBox()
    {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    public void setActive(boolean powered)
    {
        if (this.active != powered)
        {
            this.active = powered;
            markDirty();
            if (this.world != null)
            {
                this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
                if (!this.world.isRemote)
                    this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
            }
        }
    }

    public float getRenderTicks(float partialTicks)
    {
        return this.tick + partialTicks;
    }

    private void calculateBeamHeight()
    {
        int maxCheckHeight = 256;
        int consecutiveSolidBlocks = 0;
        int obstructionStartY = -1;
        for (int y = this.pos.getY() + 1; y < maxCheckHeight; y++)
        {
            BlockPos checkPos = new BlockPos(this.pos.getX(), y, this.pos.getZ());
            IBlockState state = this.world.getBlockState(checkPos);
            if (state.isFullCube() && state.isOpaqueCube() && !(state.getBlock() instanceof net.minecraft.block.BlockSlab))
            {
                if (consecutiveSolidBlocks == 0)
                    obstructionStartY = y;
                consecutiveSolidBlocks++;
                if (consecutiveSolidBlocks >= 3)
                {
                    this.currentBeamHeight = (obstructionStartY - this.pos.getY()) - 0.5F + 13.0F;
                    return;
                }
            }
            else
            {
                consecutiveSolidBlocks = 0;
                obstructionStartY = -1;
            }
        }
        this.currentBeamHeight = 133.0F;
    }
}
