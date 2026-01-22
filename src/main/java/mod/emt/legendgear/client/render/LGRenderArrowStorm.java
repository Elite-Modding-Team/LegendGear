package mod.emt.legendgear.client.render;

import mod.emt.legendgear.entity.LGEntityArrowStorm;
import mod.emt.legendgear.entity.LGEntityQuake;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class LGRenderArrowStorm extends Render<LGEntityArrowStorm>
{
    protected LGRenderArrowStorm(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityArrowStorm entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (entity.lifetime == 0)
        {
            for (int i = 0; i < 15; i++)
            {
                entity.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, entity.posX + entity.world.rand.nextGaussian() * 0.3D, entity.posY, entity.posZ + entity.world.rand.nextGaussian() * 0.3D, 0.0D, entity.world.rand.nextDouble(), 0.0D);
            }
        }
        for (int i = 0; i < 5; i++)
        {
            entity.world.spawnParticle(EnumParticleTypes.CLOUD, entity.posX + entity.world.rand.nextGaussian() * LGEntityArrowStorm.RADIUS * 0.5D, entity.posY + 3.0D, entity.posZ + entity.world.rand.nextGaussian() * LGEntityArrowStorm.RADIUS * 0.5D, 0.0D, -0.1D, 0.0D);
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(LGEntityArrowStorm entity)
    {
        return null;
    }

    public static class Factory implements IRenderFactory<LGEntityArrowStorm>
    {
        @Override
        public Render<? super LGEntityArrowStorm> createRenderFor(RenderManager manager)
        {
            return new LGRenderArrowStorm(manager);
        }
    }
}
