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

import mod.emt.legendgear.entity.LGEntityWindMedallion;
import mod.emt.legendgear.init.LGItems;

@SideOnly(Side.CLIENT)
public class LGRenderWindMedallion extends RenderSnowball<LGEntityWindMedallion>
{
    public LGRenderWindMedallion(RenderManager renderManagerIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn, LGItems.ENDER_MEDALLION, itemRendererIn);
    }

    @Override
    public void doRender(LGEntityWindMedallion entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    public ItemStack getStackToRender(LGEntityWindMedallion entityIn)
    {
        return LGItems.WIND_MEDALLION.getDefaultInstance();
    }

    public static class Factory implements IRenderFactory<LGEntityWindMedallion>
    {
        @Override
        public Render<? super LGEntityWindMedallion> createRenderFor(RenderManager manager)
        {
            return new LGRenderWindMedallion(manager, Minecraft.getMinecraft().getRenderItem());
        }
    }
}
