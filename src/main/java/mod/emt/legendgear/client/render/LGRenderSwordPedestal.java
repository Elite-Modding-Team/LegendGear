package mod.emt.legendgear.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.block.LGBlockSwordPedestal;
import mod.emt.legendgear.tileentity.LGTileEntitySwordPedestal;

@SideOnly(Side.CLIENT)
public class LGRenderSwordPedestal extends TileEntitySpecialRenderer<LGTileEntitySwordPedestal>
{
    @Override
    public void render(LGTileEntitySwordPedestal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te.getContents().isEmpty()) return;

        GlStateManager.pushMatrix();

        GlStateManager.translate(x + 0.5D, y + 0.9D, z + 0.5D);
        EnumFacing facing = getWorld().getBlockState(te.getPos()).getValue(LGBlockSwordPedestal.FACING);
        if (facing == EnumFacing.EAST || facing == EnumFacing.WEST)
        {
            GlStateManager.rotate(90.0F, 0, 1, 0);
        }
        GlStateManager.rotate(225.0F, 0, 0, 1);

        Minecraft.getMinecraft().getRenderItem().renderItem(te.getContents(), ItemCameraTransforms.TransformType.NONE);

        GlStateManager.popMatrix();
    }
}
