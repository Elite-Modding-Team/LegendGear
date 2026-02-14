package mod.emt.legendgear.client.render;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.entity.LGEntityGrindRail;

@SideOnly(Side.CLIENT)
public class LGRenderGrindRail extends Render<LGEntityGrindRail>
{
    public LGRenderGrindRail(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityGrindRail entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        float time = (System.currentTimeMillis() % 1000000.0F) / 50F;
        float var27 = time * 0.4F;
        float r = (MathHelper.sin(var27 + 0.0F) + 1.0F) * 0.5F;
        float g = (MathHelper.sin(var27 + 2.1F) + 1.0F) * 0.5F;
        float b = (MathHelper.sin(var27 + 4.2F) + 1.0F) * 0.5F;
        float resat = Math.min(r, Math.min(g, b));
        r -= resat;
        g -= resat;
        b -= resat;
        float scaler = 1.0f / Math.max(r, Math.max(g, b));
        r = Math.min(scaler * r * 0.5F + 0.5F, 1.0F);
        g = Math.min(scaler * g * 0.5F + 0.5F, 1.0F);
        b = Math.min(scaler * b * 0.5F + 0.5F, 1.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        bufferbuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);

        bufferbuilder.pos(x + entity.fromX - entity.posX, y + entity.fromY - entity.posY, z + entity.fromZ - entity.posZ)
            .color(r, g, b, 1.0F)
            .endVertex();

        bufferbuilder.pos(x + entity.toX - entity.posX, y + entity.toY - entity.posY, z + entity.toZ - entity.posZ)
            .color(r, g, b, 1.0F)
            .endVertex();

        tessellator.draw();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(LGEntityGrindRail entity)
    {
        return null;
    }

    public static class Factory implements IRenderFactory<LGEntityGrindRail>
    {
        @Override
        public Render<? super LGEntityGrindRail> createRenderFor(RenderManager manager)
        {
            return new LGRenderGrindRail(manager);
        }
    }
}
