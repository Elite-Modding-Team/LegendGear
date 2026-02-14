package mod.emt.legendgear.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LGTileEntityInfusedStarstone extends TileEntity
{
    @Override
    public double getMaxRenderDistanceSquared()
    {
        // Method name remains the same, but ensure the @Override is present.
        return 65536.0D;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRenderInPass(int pass)
    {
        return pass == 1;
    }
}
