package mod.emt.legendgear.client.render;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.entity.LGEntityPing;
import mod.emt.legendgear.util.Rainbow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.lwjgl.opengl.GL11;

public class LGRenderPing extends Render<LGEntityPing> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(LegendGear.MOD_ID, "textures/misc/ripple.png");

    public LGRenderPing(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityPing entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 1.0D, z);
        GlStateManager.rotate(180 - Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.0F);

        bindTexture(TEXTURE);
        renderOuterRipple(entity, partialTicks);
        renderInnerRipple(entity, partialTicks);

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    private void renderOuterRipple(LGEntityPing entity, float partialTicks) {
        float scale = calculateScale(entity, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        float phase = (Minecraft.getSystemTime() % 1000L) / 1000.0F;
        float r = Rainbow.r(phase);
        float g = Rainbow.g(phase);
        float b = Rainbow.b(phase);

        buffer.pos(-0.5, -0.5, 0).tex(0, 0).color(r, g, b, 0.8F).endVertex();
        buffer.pos(0.5, -0.5, 0).tex(1, 0).color(r, g, b, 0.8F).endVertex();
        buffer.pos(0.5, 0.5, 0).tex(1, 1).color(r, g, b, 0.8F).endVertex();
        buffer.pos(-0.5, 0.5, 0).tex(0, 1).color(r, g, b, 0.8F).endVertex();

        tessellator.draw();
        GlStateManager.popMatrix();
    }

    private void renderInnerRipple(LGEntityPing entity, float partialTicks) {
        float phase = (Minecraft.getSystemTime() % 1000L) / 1000.0F;
        float innerScale = phase * calculateScale(entity, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.scale(innerScale, innerScale, innerScale);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        float r = Rainbow.r(1 - phase);
        float g = Rainbow.g(1 - phase);
        float b = Rainbow.b(1 - phase);

        buffer.pos(-0.5, -0.5, 0).tex(0, 0).color(r, g, b, 0.6F).endVertex();
        buffer.pos(0.5, -0.5, 0).tex(1, 0).color(r, g, b, 0.6F).endVertex();
        buffer.pos(0.5, 0.5, 0).tex(1, 1).color(r, g, b, 0.6F).endVertex();
        buffer.pos(-0.5, 0.5, 0).tex(0, 1).color(r, g, b, 0.6F).endVertex();

        tessellator.draw();
        GlStateManager.popMatrix();
    }

    private float calculateScale(LGEntityPing entity, float partialTicks) {
        float ageRatio = (8.0F - (entity.age + partialTicks)) / 8.0F;
        ageRatio = Math.max(ageRatio, 0.0F);

        Vec3d viewerPos = new Vec3d(renderManager.viewerPosX, renderManager.viewerPosY, renderManager.viewerPosZ);
        Vec3d entityPos = new Vec3d(entity.posX, entity.posY, entity.posZ);
        double distance = viewerPos.distanceTo(entityPos);

        float scale = 2.0F;
        if (scale < distance / 20.0) {
            scale = (float) distance / 20.0F;
        }

        return scale * (1.0F - ageRatio) * (1.0F + ageRatio * ageRatio * 12.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(LGEntityPing entity) {
        return TEXTURE;
    }

    public static class Factory implements IRenderFactory<LGEntityPing> {
        @Override
        public Render<? super LGEntityPing> createRenderFor(RenderManager manager) {
            return new LGRenderPing(manager);
        }
    }
}
