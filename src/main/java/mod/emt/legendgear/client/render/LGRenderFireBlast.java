package mod.emt.legendgear.client.render;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.entity.LGEntityFireBlast;

@SideOnly(Side.CLIENT)
public class LGRenderFireBlast extends Render<LGEntityFireBlast>
{
    protected LGRenderFireBlast(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityFireBlast entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(LGEntityFireBlast entity)
    {
        return null;
    }

    public static class Factory implements IRenderFactory<LGEntityFireBlast>
    {
        @Override
        public Render<? super LGEntityFireBlast> createRenderFor(RenderManager manager)
        {
            return new LGRenderFireBlast(manager);
        }
    }
}
