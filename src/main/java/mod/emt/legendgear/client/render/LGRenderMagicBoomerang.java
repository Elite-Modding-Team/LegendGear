package mod.emt.legendgear.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.entity.LGEntityMagicBoomerang;
import mod.emt.legendgear.init.LGItems;

@SideOnly(Side.CLIENT)
public class LGRenderMagicBoomerang extends Render<LGEntityMagicBoomerang>
{
    private final RenderItem itemRenderer;

    public LGRenderMagicBoomerang(RenderManager renderManager, RenderItem itemRenderer)
    {
        super(renderManager);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void doRender(LGEntityMagicBoomerang entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        ItemStack stack = entity.getItem();
        if (stack.isEmpty()) return;

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + (entity.rotationYaw < 0 ? -90.0F : 90.0F), 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(LGEntityMagicBoomerang entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    public static class Factory implements IRenderFactory<LGEntityMagicBoomerang>
    {
        @Override
        public Render<? super LGEntityMagicBoomerang> createRenderFor(RenderManager manager)
        {
            return new LGRenderMagicBoomerang(manager, Minecraft.getMinecraft().getRenderItem());
        }
    }
}
