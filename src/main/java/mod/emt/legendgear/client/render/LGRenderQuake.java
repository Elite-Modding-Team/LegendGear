package mod.emt.legendgear.client.render;

import mod.emt.legendgear.entity.LGEntityQuake;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

// TODO: Particles don't seem to work?
@SideOnly(Side.CLIENT)
public class LGRenderQuake extends Render<LGEntityQuake>
{
    protected LGRenderQuake(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityQuake entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        double radius = entity.oneshot_radius;
        if (!entity.oneshot)
        {
            radius = LGEntityQuake.FIRST_PULSE_RADIUS + LGEntityQuake.RADIUS_PER_TICK * entity.lifetime;
        }
        if (entity.ticksExisted % 5 == 0)
        {
            for (int i = 0; i < 12; i++)
            {
                double th = i * Math.PI * 2.0D / 12.0D;
                double vx = Math.cos(th);
                double vz = Math.sin(th);
                entity.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, entity.posX + vx * radius, entity.posY, entity.posZ + vz * radius, vx * 0.05D, 0.2D, vz * 0.05D);
            }
        }
        if (entity.lifetime <= 1 && !entity.oneshot)
        {
            for (int i = 0; i < 15; i++)
            {
                entity.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, entity.posX + entity.world.rand.nextGaussian() * 0.3D, entity.posY, entity.posZ + entity.world.rand.nextGaussian() * 0.3D, 0.0D, entity.world.rand.nextDouble(), 0.0D);
            }
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(LGEntityQuake entity)
    {
        return null;
    }

    public static class Factory implements IRenderFactory<LGEntityQuake>
    {
        @Override
        public Render<? super LGEntityQuake> createRenderFor(RenderManager manager)
        {
            return new LGRenderQuake(manager);
        }
    }
}
