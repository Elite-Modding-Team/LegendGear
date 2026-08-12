package mod.emt.legendgear.client.render;

import mod.emt.legendgear.entity.LGEntitySpellEffect;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class LGRenderSpellEffect extends Render<LGEntitySpellEffect>
{
    protected LGRenderSpellEffect(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(@NotNull LGEntitySpellEffect entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@NotNull LGEntitySpellEffect entity)
    {
        return null;
    }

    public static class Factory implements IRenderFactory<LGEntitySpellEffect>
    {
        @Override
        public Render<? super LGEntitySpellEffect> createRenderFor(RenderManager manager)
        {
            return new LGRenderSpellEffect(manager);
        }
    }
}
