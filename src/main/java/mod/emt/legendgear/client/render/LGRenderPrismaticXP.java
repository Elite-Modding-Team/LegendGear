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
    }

    protected ResourceLocation getEntityTexture(EntityXPOrb entity)
    {
        return prismaticLegendOrbs;
    }

    public void renderTheXPOrb(EntityXPOrb orb, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        int frame = orb.getTextureByXP();
        bindTexture(prismaticLegendOrbs);
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();
        float uMin = (frame % 4 * 16) / 64.0F;
        float uMax = (frame % 4 * 16 + 16) / 64.0F;
        float vMin = (frame / 4 * 16) / 64.0F;
        float vMax = (frame / 4 * 16 + 16) / 64.0F;
        float size = 0.3F;
        int light = 240;
        int lightU = light % 65536;
        int lightV = light / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightU, lightV);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float phase = (float) ((Minecraft.getSystemTime() % 1000L) / 1000.0D + (orb.posX + orb.posZ) * 0.05D);
        float r = Rainbow.r(phase);
        float g = Rainbow.g(phase);
        float b = Rainbow.b(phase);
        int rgba = (int) (r * 255.0F) << 16 | (int) (g * 255.0F) << 8 | (int) (b * 255.0F);
        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(((this.renderManager.options.thirdPersonView == 2) ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(size, size, size);
        GlStateManager.disableLighting();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(-0.5D, -0.25D, 0.0D).tex(uMin, vMax).color(r, g, b, 1.0F).endVertex();
        buffer.pos(0.5D, -0.25D, 0.0D).tex(uMax, vMax).color(r, g, b, 1.0F).endVertex();
        buffer.pos(0.5D, 0.75D, 0.0D).tex(uMax, vMin).color(r, g, b, 1.0F).endVertex();
        buffer.pos(-0.5D, 0.75D, 0.0D).tex(uMin, vMin).color(r, g, b, 1.0F).endVertex();
        tess.draw();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    public void doRender(EntityXPOrb entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        renderTheXPOrb(entity, x, y, z, entityYaw, partialTicks);
    }
}
