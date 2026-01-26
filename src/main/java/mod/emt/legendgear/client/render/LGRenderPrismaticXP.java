package mod.emt.legendgear.client.render;

import mod.emt.legendgear.util.Rainbow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LGRenderPrismaticXP extends Render<EntityXPOrb>
{
    private static final ResourceLocation prismaticLegendOrbs = new ResourceLocation("legendgear:textures/misc/xp_orb.png");

    public LGRenderPrismaticXP(RenderManager renderManager)
    {
        super(renderManager);
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    protected ResourceLocation getEntityTexture(EntityXPOrb entity)
    {
        return prismaticLegendOrbs;
    }

    public void renderTheXPOrb(EntityXPOrb orb, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 0.1F, (float) z);
        int frame = orb.getTextureByXP();
        this.bindTexture(prismaticLegendOrbs);
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();
        float uMin = (float) (frame % 4 * 16 + 0) / 64.0F;
        float uMax = (float) (frame % 4 * 16 + 16) / 64.0F;
        float vMin = (float) (frame / 4 * 16 + 0) / 64.0F;
        float vMax = (float) (frame / 4 * 16 + 16) / 64.0F;
        float var16 = 1.0F;
        float var17 = 0.5F;
        float var18 = 0.25F;
        int light = 240;
        int lightU = light % 65536;
        int lightV = light / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lightU / 1.0F, (float) lightV / 1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float var26 = 255.0F;

        float time = (Minecraft.getSystemTime() % 1000000) / 50f;
        float phase = (float) ((Minecraft.getSystemTime() % 1000 / 1000f) + (orb.posX + orb.posZ) * 0.05);
        float r = Rainbow.r(phase);
        float g = Rainbow.g(phase);
        float b = Rainbow.b(phase);

        lightV = (int) (r * var26);
        int var22 = (int) (g * var26);
        int var23 = (int) (b * var26);
        int var24 = lightV << 16 | var22 << 8 | var23;
        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        float var25 = 0.3F;
        GlStateManager.scale(var25, var25, var25);
        GlStateManager.disableLighting();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);

        buffer.pos((double) (0.0F - var17), (double) (0.0F - var18), 0.0D)
                .tex((double) uMin, (double) vMax)
                .color(r, g, b, 1.0F)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();

        buffer.pos((double) (var16 - var17), (double) (0.0F - var18), 0.0D)
                .tex((double) uMax, (double) vMax)
                .color(r, g, b, 1.0F)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();

        buffer.pos((double) (var16 - var17), (double) (1.0F - var18), 0.0D)
                .tex((double) uMax, (double) vMin)
                .color(r, g, b, 1.0F)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();

        buffer.pos((double) (0.0F - var17), (double) (1.0F - var18), 0.0D)
                .tex((double) uMin, (double) vMin)
                .color(r, g, b, 1.0F)
                .normal(0.0F, 1.0F, 0.0F)
                .endVertex();

        tess.draw();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    public void doRender(EntityXPOrb entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        renderTheXPOrb(entity, x, y, z, entityYaw, partialTicks);
    }
}
