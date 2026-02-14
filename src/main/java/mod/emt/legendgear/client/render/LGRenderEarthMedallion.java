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

import mod.emt.legendgear.entity.LGEntityEarthMedallion;
import mod.emt.legendgear.init.LGItems;

@SideOnly(Side.CLIENT)
public class LGRenderEarthMedallion extends RenderSnowball<LGEntityEarthMedallion>
{
    public LGRenderEarthMedallion(RenderManager renderManagerIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn, LGItems.EARTH_MEDALLION, itemRendererIn);
    }

    @Override
    public void doRender(LGEntityEarthMedallion entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    public ItemStack getStackToRender(LGEntityEarthMedallion entityIn)
    {
        return LGItems.EARTH_MEDALLION.getDefaultInstance();
    }

    public static class Factory implements IRenderFactory<LGEntityEarthMedallion>
    {
        @Override
        public Render<? super LGEntityEarthMedallion> createRenderFor(RenderManager manager)
        {
            return new LGRenderEarthMedallion(manager, Minecraft.getMinecraft().getRenderItem());
        }
    }
}
