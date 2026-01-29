package mod.emt.legendgear.client.render;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.entity.LGEntityArrowStorm;

@SideOnly(Side.CLIENT)
public class LGRenderArrowStorm extends Render<LGEntityArrowStorm>
{
    protected LGRenderArrowStorm(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityArrowStorm entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(LGEntityArrowStorm entity)
    {
        return null;
    }

    public static class Factory implements IRenderFactory<LGEntityArrowStorm>
    {
        @Override
        public Render<? super LGEntityArrowStorm> createRenderFor(RenderManager manager)
        {
            return new LGRenderArrowStorm(manager);
        }
    }
}
