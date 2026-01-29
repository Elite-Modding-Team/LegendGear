package mod.emt.legendgear.client.render;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.entity.LGEntityQuake;

@SideOnly(Side.CLIENT)
public class LGRenderQuake extends Render<LGEntityQuake>
{
    protected LGRenderQuake(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityQuake entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(LGEntityQuake entity)
    {
        return null;
    }

    public static class Factory implements IRenderFactory<LGEntityQuake>
    {
        @Override
        public Render<? super LGEntityQuake> createRenderFor(RenderManager manager)
        {
            return new LGRenderQuake(manager);
        }
    }
}
