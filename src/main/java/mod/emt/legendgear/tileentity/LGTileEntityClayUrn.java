package mod.emt.legendgear.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class LGTileEntityClayUrn extends TileEntity
{
    private ItemStack contents = ItemStack.EMPTY;

    public ItemStack getContents()
    {
        return contents;
    }

    public void setContents(ItemStack contents)
    {
        this.contents = contents;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (compound.hasKey("Item", 10))
        {
            contents = new ItemStack(compound.getCompoundTag("Item"));
        }
        else
        {
            contents = ItemStack.EMPTY;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (!contents.isEmpty())
        {
            compound.setTag("Item", contents.writeToNBT(new NBTTagCompound()));
        }
        return compound;
    }
}
