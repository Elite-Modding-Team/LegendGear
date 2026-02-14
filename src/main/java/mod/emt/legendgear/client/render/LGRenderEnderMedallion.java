package mod.emt.legendgear.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.entity.LGEntityEnderMedallion;
import mod.emt.legendgear.init.LGItems;

@SideOnly(Side.CLIENT)
public class LGRenderEnderMedallion extends RenderSnowball<LGEntityEnderMedallion>
{
    public LGRenderEnderMedallion(RenderManager renderManagerIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn, LGItems.ENDER_MEDALLION, itemRendererIn);
    }

    @Override
    public void doRender(LGEntityEnderMedallion entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    public ItemStack getStackToRender(LGEntityEnderMedallion entityIn)
    {
        return LGItems.ENDER_MEDALLION.getDefaultInstance();
    }

    public static class Factory implements IRenderFactory<LGEntityEnderMedallion>
    {
        @Override
        public Render<? super LGEntityEnderMedallion> createRenderFor(RenderManager manager)
        {
            return new LGRenderEnderMedallion(manager, Minecraft.getMinecraft().getRenderItem());
        }
    }
}
