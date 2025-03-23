package mod.emt.legendgear.client.render;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.entity.LGEntityBomb;

public class LGRenderBomb extends Render<LGEntityBomb>
{
    private static final ResourceLocation REGULAR_TEXTURE = new ResourceLocation(LegendGear.MOD_ID, "textures/items/bomb.png");
    private static final ResourceLocation FLASH_TEXTURE = new ResourceLocation(LegendGear.MOD_ID, "textures/items/bomb_flash.png");

    public LGRenderBomb(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityBomb entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 0.1F, (float) z);

        if (entity.getRidingEntity() != null)
        {
            GlStateManager.translate(0.0F, -0.6F, 0.0F);
        }

        if (entity.fuse > 10)
        {
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
        }
        else
        {
            float scale = 0.25F - 0.25F * entity.fuse / 10.0F;
            GlStateManager.scale(0.5F + scale, 0.5F + scale, 0.5F + scale);
        }

        int pulsePhase = (int) entity.pulse % 2;
        ResourceLocation texture = pulsePhase == 0 ? REGULAR_TEXTURE : FLASH_TEXTURE;
        this.bindTexture(texture);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        this.drawBomb(tessellator, buffer);

        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(LGEntityBomb entity)
    {
        int pulsePhase = (int) entity.pulse % 2;
        return pulsePhase == 0 ? REGULAR_TEXTURE : FLASH_TEXTURE;
    }

    private void drawBomb(Tessellator tessellator, BufferBuilder buffer)
    {
        float scale = 1.0F;
        float halfWidth = 0.5F;
        float halfHeight = 0.25F;

        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
        buffer.pos(0.0F - halfWidth, 0.0F - halfHeight, 0.0).tex(0, 1).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(scale - halfWidth, 0.0F - halfHeight, 0.0).tex(1, 1).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(scale - halfWidth, scale - halfHeight, 0.0).tex(1, 0).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(0.0F - halfWidth, scale - halfHeight, 0.0).tex(0, 0).normal(0.0F, 1.0F, 0.0F).endVertex();
        tessellator.draw();
    }

    public static class Factory implements IRenderFactory<LGEntityBomb>
    {
        @Override
        public Render<? super LGEntityBomb> createRenderFor(RenderManager manager)
        {
            return new LGRenderBomb(manager);
        }
    }
}
