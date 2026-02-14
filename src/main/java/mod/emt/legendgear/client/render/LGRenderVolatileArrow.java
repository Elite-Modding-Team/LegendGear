package mod.emt.legendgear.client.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.entity.LGEntityVolatileArrow;

@SideOnly(Side.CLIENT)
public class LGRenderVolatileArrow extends RenderArrow<LGEntityVolatileArrow>
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/projectiles/arrow.png");

    public LGRenderVolatileArrow(RenderManager manager)
    {
        super(manager);
    }

    public ResourceLocation getEntityTexture(LGEntityVolatileArrow entity)
    {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<LGEntityVolatileArrow>
    {
        @Override
        public Render<? super LGEntityVolatileArrow> createRenderFor(RenderManager manager)
        {
            return new LGRenderVolatileArrow(manager);
        }
    }
}
