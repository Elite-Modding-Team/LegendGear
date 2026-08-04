package mod.emt.legendgear.client.render;

import mod.emt.legendgear.entity.LGEntityWhirlwind;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class LGRenderWhirlwind extends Render<LGEntityWhirlwind>
{
    protected LGRenderWhirlwind(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityWhirlwind entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(LGEntityWhirlwind entity)
    {
        return null;
    }

    public static class Factory implements IRenderFactory<LGEntityWhirlwind>
    {
        @Override
        public Render<? super LGEntityWhirlwind> createRenderFor(RenderManager manager)
        {
            return new LGRenderWhirlwind(manager);
        }
    }
}
