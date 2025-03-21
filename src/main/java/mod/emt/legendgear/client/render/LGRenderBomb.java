package mod.emt.legendgear.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.entity.LGEntityBomb;
import mod.emt.legendgear.init.LGItems;

@SideOnly(Side.CLIENT)
public class LGRenderBomb extends RenderSnowball<LGEntityBomb>
{
    public LGRenderBomb(RenderManager renderManager, RenderItem itemRenderer)
    {
        super(renderManager, LGItems.BOMB, itemRenderer);
    }

    @Override
    public void doRender(LGEntityBomb entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    public ItemStack getStackToRender(LGEntityBomb entity)
    {
        return LGItems.BOMB.getDefaultInstance();
    }

    public static class Factory implements IRenderFactory<LGEntityBomb>
    {
        @Override
        public Render<? super LGEntityBomb> createRenderFor(RenderManager manager)
        {
            return new LGRenderBomb(manager, Minecraft.getMinecraft().getRenderItem());
        }
    }
}
