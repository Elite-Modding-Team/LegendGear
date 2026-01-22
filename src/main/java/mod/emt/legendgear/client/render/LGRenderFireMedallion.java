package mod.emt.legendgear.client.render;

import mod.emt.legendgear.entity.LGEntityFireMedallion;
import mod.emt.legendgear.init.LGItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LGRenderFireMedallion extends RenderSnowball<LGEntityFireMedallion>
{
    public LGRenderFireMedallion(RenderManager renderManagerIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn, LGItems.FIRE_MEDALLION, itemRendererIn);
    }

    @Override
    public void doRender(LGEntityFireMedallion entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    public ItemStack getStackToRender(LGEntityFireMedallion entityIn)
    {
        return LGItems.FIRE_MEDALLION.getDefaultInstance();
    }

    public static class Factory implements IRenderFactory<LGEntityFireMedallion>
    {
        @Override
        public Render<? super LGEntityFireMedallion> createRenderFor(RenderManager manager)
        {
            return new LGRenderFireMedallion(manager, Minecraft.getMinecraft().getRenderItem());
        }
    }
}
