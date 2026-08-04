package mod.emt.legendgear.client.render;

import mod.emt.legendgear.entity.LGEntityFireBolt;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class LGRenderFireBolt extends Render<LGEntityFireBolt>
{
    protected LGRenderFireBolt(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityFireBolt entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(LGEntityFireBolt entity)
    {
        return null;
    }

    public static class Factory implements IRenderFactory<LGEntityFireBolt>
    {
        @Override
        public Render<? super LGEntityFireBolt> createRenderFor(RenderManager manager)
        {
            return new LGRenderFireBolt(manager);
        }
    }
}
