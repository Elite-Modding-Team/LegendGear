package mod.emt.legendgear.client.render;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.entity.LGEntityHookshot;
import mod.emt.legendgear.item.LGItemHookshot;

@SideOnly(Side.CLIENT)
public class LGRenderHookshot extends Render<LGEntityHookshot>
{
    private static final ResourceLocation BARB_TEXTURE = new ResourceLocation(LegendGear.MOD_ID, "textures/items/hookshot_barb.png");

    public LGRenderHookshot(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityHookshot entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y + 0.25D, z);
        GlStateManager.scale(0.8F, 0.8F, 0.8F);
        drawBarb();
        GlStateManager.popMatrix();

        EntityPlayer player = entity.shooter;
        if (player != null)
        {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            int k = player.getPrimaryHand() == EnumHandSide.RIGHT ? 1 : -1;
            ItemStack itemstack = player.getHeldItemMainhand();

            if (!(itemstack.getItem() instanceof LGItemHookshot))
            {
                k = -k;
            }

            float f7 = player.getSwingProgress(partialTicks);
            float f8 = MathHelper.sin(MathHelper.sqrt(f7) * (float) Math.PI);
            float f9 = (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks) * 0.017453292F;
            double d0 = MathHelper.sin(f9);
            double d1 = MathHelper.cos(f9);
            double d2 = k * 0.35D;
            double d3;
            double d4;
            double d5;
            double d6;

            if ((this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0) && player == Minecraft.getMinecraft().player)
            {
                float f10 = this.renderManager.options.fovSetting;
                f10 = f10 / 100.0F;
                Vec3d vec3d = new Vec3d(k * -0.5D * f10, -0.1D * f10, 0.4D);
                vec3d = vec3d.rotatePitch(-(player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks) * 0.017453292F);
                vec3d = vec3d.rotateYaw(-(player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks) * 0.017453292F);
                vec3d = vec3d.rotateYaw(f8 * 0.5F);
                vec3d = vec3d.rotatePitch(-f8 * 0.7F);
                d3 = player.prevPosX + (player.posX - player.prevPosX) * partialTicks + vec3d.x;
                d4 = player.prevPosY + (player.posY - player.prevPosY) * partialTicks + vec3d.y;
                d5 = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks + vec3d.z;
                d6 = player.getEyeHeight();
            }
            else
            {
                d3 = player.prevPosX + (player.posX - player.prevPosX) * partialTicks - d1 * d2 - d0 * 0.8D;
                d4 = player.prevPosY + player.getEyeHeight() + (player.posY - player.prevPosY) * (double) partialTicks - 0.8D;
                d5 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) partialTicks - d0 * d2 + d1 * 0.8D;
                d6 = player.isSneaking() ? -0.1875D : 0.0D;
            }

            double d13 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            double d8 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + 0.25D;
            double d9 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
            double d10 = ((float) (d3 - d13));
            double d11 = ((float) (d4 - d8)) + d6;
            double d12 = ((float) (d5 - d9));
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            buffer.begin(3, DefaultVertexFormats.POSITION_COLOR);

            for (int i1 = 0; i1 <= 1; ++i1)
            {
                buffer.pos(x + d10 * i1, y + d11 * (i1 * i1 + i1) * 0.5D + 0.25D, z + d12 * i1).color(0, 0, 0, 255).endVertex();
            }

            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();

            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(LGEntityHookshot entity)
    {
        return BARB_TEXTURE;
    }

    private void drawBarb()
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(BARB_TEXTURE);

        float minU = 0.0F;
        float maxU = 1.0F;
        float minV = 0.0F;
        float maxV = 1.0F;

        GlStateManager.rotate(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        float size = 0.5F;
        buffer.pos(-size, -size, 0).tex(maxU, maxV).endVertex();
        buffer.pos(size, -size, 0).tex(minU, maxV).endVertex();
        buffer.pos(size, size, 0).tex(minU, minV).endVertex();
        buffer.pos(-size, size, 0).tex(maxU, minV).endVertex();

        tessellator.draw();
    }

    public static class Factory implements IRenderFactory<LGEntityHookshot>
    {
        @Override
        public Render<? super LGEntityHookshot> createRenderFor(RenderManager manager)
        {
            return new LGRenderHookshot(manager);
        }
    }
}
