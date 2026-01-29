package mod.emt.legendgear.client.render;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;
import mod.emt.legendgear.entity.LGEntityEnderBomb;

// TODO: Ring vertices have a closing animation in 1.5.2, this currently doesn't occur in our port
@SideOnly(Side.CLIENT)
public class LGRenderEnderBomb extends Render<LGEntityEnderBomb>
{
    Random rand = new Random();

    protected LGRenderEnderBomb(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityEnderBomb entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        // Stop rendering as soon as the timer is out
        if (entity.lifespan_timer >= entity.EXPAND_TIME || entity.isDead)
        {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();

        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tess.getBuffer();

        drawRingVertices(entity, bufferbuilder, 30, 13, 53); // #1E0D35
        drawRingVertices(entity, bufferbuilder, 78, 32, 138); // #4E208A
        drawRingVertices(entity, bufferbuilder, 144, 64, 255); // #9040FF

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(LGEntityEnderBomb entity)
    {
        return null;
    }

    private void drawRingVertices(LGEntityEnderBomb entity, BufferBuilder buffer, int r, int g, int b)
    {
        // Return as soon as radius is at 0
        if (entity.radius <= 0) return;

        // Fade out effect
        float fadeDuration = 4.0F;
        float ticksRemaining = (float) entity.EXPAND_TIME - (float) entity.lifespan_timer;
        float alphaPercent;

        if (ticksRemaining > fadeDuration)
        {
            alphaPercent = 1.0F;
        }
        else
        {
            alphaPercent = ticksRemaining / fadeDuration;
        }

        int alpha = (int) (alphaPercent * 255);
        if (alpha <= 0) return;

        buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);

        int segments = 12;
        double fuzz = 0.05D * entity.radius;
        double firstX = 0;
        double firstY = 0;
        double firstZ = 0;

        for (int i = 0; i < segments; i++)
        {
            double th = 2.0D * Math.PI * i / segments + entity.radius;
            double x = Math.cos(th) * entity.radius + (rand.nextGaussian() * fuzz);
            double y = 0.0D + (rand.nextGaussian() * fuzz);
            double z = Math.sin(th) * entity.radius + (rand.nextGaussian() * fuzz);
            x += rand.nextGaussian() * fuzz;
            y += rand.nextGaussian() * fuzz;
            z += rand.nextGaussian() * fuzz;

            if (i == 0)
            {
                firstX = x;
                firstY = y;
                firstZ = z;
            }

            buffer.pos(x, y, z).color(r, g, b, alpha).endVertex();
        }

        buffer.pos(firstX, firstY, firstZ).color(r, g, b, alpha).endVertex();
        Tessellator.getInstance().draw();
    }

    public static class Factory implements IRenderFactory<LGEntityEnderBomb>
    {
        @Override
        public Render<? super LGEntityEnderBomb> createRenderFor(RenderManager manager)
        {
            return new LGRenderEnderBomb(manager);
        }
    }
}
