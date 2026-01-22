package mod.emt.legendgear.client.render;

import javax.annotation.Nullable;

import mod.emt.legendgear.entity.LGEntityEnderBomb;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LGRenderEnderBomb extends Render<LGEntityEnderBomb>
{
    protected LGRenderEnderBomb(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityEnderBomb entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (entity.lifespan_timer == 0)
        {
            for (int i = 0; i < 15; i++)
            {
                entity.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, entity.posX + entity.world.rand.nextGaussian() * 0.3D, entity.posY, entity.posZ + entity.world.rand.nextGaussian() * 0.3D, 0.0D, entity.world.rand.nextDouble(), 0.0D);
            }
        }
        if (entity.portal)
        {
            entity.world.spawnParticle(EnumParticleTypes.PORTAL, entity.xCoord, entity.yCoord, entity.zCoord, entity.xSpeed, entity.ySpeed, entity.zSpeed);
        }
        if (entity.lifespan_timer < entity.EXPAND_TIME)
        {
            for (int i = 0; i < 15; i++)
            {
                double theta = i * Math.PI * entity.radius;
                double ox = Math.cos(theta) * entity.radius;
                double oz = Math.sin(theta) * entity.radius;
                entity.world.spawnParticle(EnumParticleTypes.PORTAL, entity.posX + ox, entity.posY, entity.posZ + oz, 0.0D, 0.05D, 0.0D);
            }
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(LGEntityEnderBomb entity)
    {
        return null;
    }

    public static class Factory implements IRenderFactory<LGEntityEnderBomb>
    {
        @Override
        public Render<? super LGEntityEnderBomb> createRenderFor(RenderManager manager)
        {
            return new LGRenderEnderBomb(manager);
        }
    }
}
