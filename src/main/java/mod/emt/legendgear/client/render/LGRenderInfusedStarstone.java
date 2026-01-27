package mod.emt.legendgear.client.render;

import mod.emt.legendgear.tileentity.LGTileEntityInfusedStarstone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class LGRenderInfusedStarstone extends TileEntitySpecialRenderer<LGTileEntityInfusedStarstone>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("legendgear:textures/misc/chaos_rainbow.png");

    @Override
    public void render(LGTileEntityInfusedStarstone entity, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        float phase = (float) (Minecraft.getSystemTime() % 8000L) / 8000.0F;
        float uPhase = (float) (Minecraft.getSystemTime() % 11000L) / 11000.0F;
        int sp = 64;
        float u = (((entity.getPos().getX() + entity.getPos().getZ() + entity.getPos().getY()) % sp + sp) % sp) * sp - uPhase;
        float v = (((entity.getPos().getX() + entity.getPos().getZ() + entity.getPos().getY()) % sp + sp) % sp) * sp + phase;
        float ds = 0.015625F;
        float dsu = 0.03125F;

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        bindTexture(TEXTURE);
        GlStateManager.depthMask(false);
        GlStateManager.enableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.enablePolygonOffset();
        GlStateManager.doPolygonOffset(-2.0F, -5.0F);

        // Initialize buffer
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        float r = 0.6F;
        float g = 0.6F;
        float b = 0.6F;
        float a = 0.3F;

        if (!getWorld().getBlockState(entity.getPos().up()).isOpaqueCube())
        {
            buffer.pos(0.0D, 1.0D, 0.0D).tex(u + dsu, v + ds).color(r, g, b, a).endVertex();
            buffer.pos(0.0D, 1.0D, 1.0D).tex(u + dsu * 2.0F, v + ds * 2.0F).color(r, g, b, a).endVertex();
            buffer.pos(1.0D, 1.0D, 1.0D).tex(u + dsu * 3.0F, v + ds * 3.0F).color(r, g, b, a).endVertex();
            buffer.pos(1.0D, 1.0D, 0.0D).tex(u + dsu * 2.0F, v + ds * 2.0F).color(r, g, b, a).endVertex();
        }

        if (!getWorld().getBlockState(entity.getPos().down()).isOpaqueCube())
        {
            buffer.pos(0.0D, 0.0D, 0.0D).tex(u, v).color(r, g, b, a).endVertex();
            buffer.pos(1.0D, 0.0D, 0.0D).tex(u + dsu, v + ds).color(r, g, b, a).endVertex();
            buffer.pos(1.0D, 0.0D, 1.0D).tex(u + dsu * 2.0F, v + ds * 2.0F).color(r, g, b, a).endVertex();
            buffer.pos(0.0D, 0.0D, 1.0D).tex(u + dsu, v + ds).color(r, g, b, a).endVertex();
        }

        if (!getWorld().getBlockState(entity.getPos().west()).isOpaqueCube())
        {
            buffer.pos(0.0D, 0.0D, 1.0D).tex(u + dsu, v + ds).color(r, g, b, a).endVertex();
            buffer.pos(0.0D, 1.0D, 1.0D).tex(u + dsu * 2.0F, v + ds * 2.0F).color(r, g, b, a).endVertex();
            buffer.pos(0.0D, 1.0D, 0.0D).tex(u + dsu, v + ds).color(r, g, b, a).endVertex();
            buffer.pos(0.0D, 0.0D, 0.0D).tex(u, v).color(r, g, b, a).endVertex();
        }

        if (!getWorld().getBlockState(entity.getPos().east()).isOpaqueCube())
        {
            buffer.pos(1.0D, 1.0D, 0.0D).tex(u + dsu * 2.0F, v + ds * 2.0F).color(r, g, b, a).endVertex();
            buffer.pos(1.0D, 1.0D, 1.0D).tex(u + dsu * 3.0F, v + ds * 3.0F).color(r, g, b, a).endVertex();
            buffer.pos(1.0D, 0.0D, 1.0D).tex(u + dsu * 2.0F, v + ds * 2.0F).color(r, g, b, a).endVertex();
            buffer.pos(1.0D, 0.0D, 0.0D).tex(u + dsu, v + ds).color(r, g, b, a).endVertex();
        }

        if (!getWorld().getBlockState(entity.getPos().south()).isOpaqueCube())
        {
            buffer.pos(1.0D, 0.0D, 1.0D).tex(u + dsu * 2.0F, v + ds * 2.0F).color(r, g, b, a).endVertex();
            buffer.pos(1.0D, 1.0D, 1.0D).tex(u + dsu * 3.0F, v + ds * 3.0F).color(r, g, b, a).endVertex();
            buffer.pos(0.0D, 1.0D, 1.0D).tex(u + dsu * 2.0F, v + ds * 2.0F).color(r, g, b, a).endVertex();
            buffer.pos(0.0D, 0.0D, 1.0D).tex(u + dsu, v + ds).color(r, g, b, a).endVertex();
        }

        if (!getWorld().getBlockState(entity.getPos().north()).isOpaqueCube())
        {
            buffer.pos(0.0D, 1.0D, 0.0D).tex(u + dsu, v + ds).color(r, g, b, a).endVertex();
            buffer.pos(1.0D, 1.0D, 0.0D).tex(u + dsu * 2.0F, v + ds * 2.0F).color(r, g, b, a).endVertex();
            buffer.pos(1.0D, 0.0D, 0.0D).tex(u + dsu, v + ds).color(r, g, b, a).endVertex();
            buffer.pos(0.0D, 0.0D, 0.0D).tex(u, v).color(r, g, b, a).endVertex();
        }

        tess.draw();
        GlStateManager.disablePolygonOffset();
        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }
}
