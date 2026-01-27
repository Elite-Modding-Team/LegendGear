package mod.emt.legendgear.client.render;

import mod.emt.legendgear.tileentity.LGTileEntitySkybeam;
import mod.emt.legendgear.util.Rainbow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class LGRenderSkybeam extends TileEntitySpecialRenderer<LGTileEntitySkybeam>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("legendgear:textures/misc/beam.png");

    private static boolean isOptifineInstalled;

    // TODO: See what this actually does
    static
    {
        try
        {
            Class.forName("Config");
            isOptifineInstalled = true;
        } catch (ClassNotFoundException e)
        {
            try
            {
                Class.forName("net.optifine.Config");
                isOptifineInstalled = true;
            } catch (ClassNotFoundException e2)
            {
                isOptifineInstalled = false;
            }
        }
    }

    public boolean isGlobalRenderer(LGTileEntitySkybeam tile)
    {
        return true;
    }

    @Override
    public void render(LGTileEntitySkybeam tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (tile == null)
            return;
        float phase = (float) (Minecraft.getSystemTime() % 2500L) / 2500.0F;
        float currentTick = tile.tick + partialTicks;
        float ramp = tile.getRamp();
        float power = ramp;
        float maxBeamHeight = tile.currentBeamHeight;
        GlStateManager.pushMatrix();
        float rotationSpeed = 0.5F;
        GlStateManager.translate((float) x + 0.5F, (float) y + 0.0F, (float) z + 0.5F);
        GlStateManager.rotate(currentTick * rotationSpeed, 0.0F, 1.0F, 0.0F);
        bindTexture(TEXTURE);
        char c0 = '';
        int j = c0 % 65536;
        int k = c0 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
        GlStateManager.enableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        int beams = 8;
        int passesToRun = isOptifineInstalled ? 1 : 3;
        float brightnessCap = isOptifineInstalled ? 0.6F : 1.0F;
        for (int pass = 0; pass < passesToRun; pass++)
        {
            float tempPhase = phase;
            for (int i = 0; i < beams; i++)
            {
                GlStateManager.pushMatrix();
                double angle = 2.356194490192345D * i;
                GlStateManager.translate((float) Math.cos(angle) * 0.5F * power, 0.0F, (float) Math.sin(angle) * 0.5F * power);
                GlStateManager.rotate(-(currentTick * rotationSpeed), 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(-(Minecraft.getMinecraft().getRenderManager()).playerViewY, 0.0F, 1.0F, 0.0F);
                float r = Rainbow.r(tempPhase * 2.0F);
                float g = Rainbow.g(tempPhase * 2.0F);
                float b = Rainbow.b(tempPhase * 2.0F);
                float a = brightnessCap * (1.0F - tempPhase);
                if (a < 0.001F)
                    a = 0.001F;
                float w = 2.0F * power;
                float h = maxBeamHeight * tempPhase * power;
                buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                buffer.pos(-w, h, 0.0D).tex(0.0D, 0.0D).color(r, g, b, a).endVertex();
                buffer.pos(w, h, 0.0D).tex(1.0D, 0.0D).color(r, g, b, a).endVertex();
                buffer.pos(w, 0.0D, 0.0D).tex(1.0D, 1.0D).color(r, g, b, a).endVertex();
                buffer.pos(-w, 0.0D, 0.0D).tex(0.0D, 1.0D).color(r, g, b, a).endVertex();
                tessellator.draw();
                tempPhase += 0.125F;
                if (tempPhase > 1.0F)
                    tempPhase--;
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.disableBlend();
        GlStateManager.enableFog();
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }
}
