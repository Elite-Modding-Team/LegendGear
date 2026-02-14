package mod.emt.legendgear.client.render;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
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

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.config.LGConfig;
import mod.emt.legendgear.entity.LGEntityFallingStar;
import mod.emt.legendgear.util.Rainbow;

@SideOnly(Side.CLIENT)
public class LGRenderFallingStar extends Render<LGEntityFallingStar>
{
    private static final ResourceLocation STAR_TEXTURE = new ResourceLocation(LegendGear.MOD_ID, "textures/misc/star.png");
    private static final ResourceLocation BEAM_TEXTURE = new ResourceLocation(LegendGear.MOD_ID, "textures/misc/beam.png");

    public LGRenderFallingStar(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityFallingStar entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 0.5F, (float) z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);
        GlStateManager.depthMask(false);

        bindTexture(BEAM_TEXTURE);

        float phase = (Minecraft.getSystemTime() % 1000) / 1000f;
        float r = Rainbow.r(phase);
        float g = Rainbow.g(phase);
        float b = Rainbow.b(phase);
        float scale = entity.dwindleTimer * 3.0f / LGConfig.GENERAL_SETTINGS.fallenStarLifetime;

        if (entity.dwindleTimer > LGConfig.GENERAL_SETTINGS.fallenStarLifetime - 10)
        {
            GlStateManager.pushMatrix();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            float sc = entity.dwindleTimer - (LGConfig.GENERAL_SETTINGS.fallenStarLifetime - 10);
            sc *= 0.1f;
            GlStateManager.scale(sc, sc, sc);

            buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            buffer.pos(-scale * 0.3, 30, 0).tex(0, 0).color(r, g, b, 1.0F).endVertex();
            buffer.pos(scale * 0.3, 30, 0).tex(1, 0).color(r, g, b, 1.0F).endVertex();
            buffer.pos(scale * 0.3, 0, 0).tex(1, 1).color(r, g, b, 1.0F).endVertex();
            buffer.pos(-scale * 0.3, 0, 0).tex(0, 1).color(r, g, b, 1.0F).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        bindTexture(STAR_TEXTURE);

        GlStateManager.scale(scale, scale, scale);
        renderBillboard(r, g, b, phase, 180);

        GlStateManager.scale(0.8f, 0.8f, 0.8f);
        renderBillboard(g, b, r, phase, -270);

        GlStateManager.scale(0.8f, 0.8f, 0.8f);
        renderBillboard(b, r, g, phase, 90);

        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(LGEntityFallingStar entity)
    {
        return STAR_TEXTURE;
    }

    private void renderBillboard(float r, float g, float b, float phase, float angle)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        GlStateManager.pushMatrix();

        GlStateManager.rotate(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(angle * phase, 0.0F, 0.0F, 1.0F);

        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(-0.5F, -0.5F, 0.0F).tex(0, 1).color(r, g, b, 1.0F).endVertex();
        buffer.pos(0.5F, -0.5F, 0.0F).tex(1, 1).color(r, g, b, 1.0F).endVertex();
        buffer.pos(0.5F, 0.5F, 0.0F).tex(1, 0).color(r, g, b, 1.0F).endVertex();
        buffer.pos(-0.5F, 0.5F, 0.0F).tex(0, 0).color(r, g, b, 1.0F).endVertex();
        tessellator.draw();

        GlStateManager.popMatrix();
    }

    public static class Factory implements IRenderFactory<LGEntityFallingStar>
    {
        @Override
        public Render<? super LGEntityFallingStar> createRenderFor(RenderManager manager)
        {
            return new LGRenderFallingStar(manager);
        }
    }
}
