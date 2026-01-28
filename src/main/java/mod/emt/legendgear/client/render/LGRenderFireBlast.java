package mod.emt.legendgear.client.render;

import javax.annotation.Nullable;

import mod.emt.legendgear.client.particle.LGParticleHandler;
import mod.emt.legendgear.entity.LGEntityFireBlast;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LGRenderFireBlast extends Render<LGEntityFireBlast>
{
    protected LGRenderFireBlast(RenderManager renderManager)
    {
        super(renderManager);
    }

    @Override
    public void doRender(LGEntityFireBlast entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (entity.lifetime == 0)
        {
            for (int i = 0; i < 15; i++)
            {
                entity.world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, entity.posX + entity.world.rand.nextGaussian() * 0.3D, entity.posY, entity.posZ + entity.world.rand.nextGaussian() * 0.3D, 0.0D, entity.world.rand.nextDouble(), 0.0D);
            }
        }
        if (entity.lifetime < LGEntityFireBlast.DETONATION_TIME)
        {
            double progress = 1.0D * entity.lifetime / LGEntityFireBlast.DETONATION_TIME;
            double decel = 1.0D - progress;
            decel = decel * decel * decel;
            decel = 1.0D - decel;
            double r = LGEntityFireBlast.DETONATION_RADIUS * decel;
            double theta = decel * Math.PI * 4.0D;
            for (int i = 0; i < 3; i++)
            {
                double ox = Math.cos(theta) * r;
                double oz = Math.sin(theta) * r;
                LGParticleHandler.spawnFireSwirlFX(entity.world, entity.posX + ox, entity.posY, entity.posZ + oz, 0.0D, 0.05D, 0.0D, 3.0F);
                theta += 2.0943951023931953D;
            }
        }
        if (entity.lifetime > LGEntityFireBlast.DETONATION_TIME)
        {
            for (int i = 0; i < 5; i++)
            {
                LGParticleHandler.spawnFireSwirlFX(entity.world, entity.posX + entity.world.rand.nextGaussian() * LGEntityFireBlast.DETONATION_RADIUS * 0.5D, entity.posY, entity.posZ + entity.world.rand.nextGaussian() * LGEntityFireBlast.DETONATION_RADIUS * 0.5D, 0.0D, 0.5D, 0.0D, 10.0F);
                entity.world.spawnParticle(EnumParticleTypes.LAVA, entity.posX + entity.world.rand.nextGaussian() * LGEntityFireBlast.DETONATION_RADIUS * 0.5D, entity.posY, entity.posZ + entity.world.rand.nextGaussian() * LGEntityFireBlast.DETONATION_RADIUS * 0.5D, 0.0D, 0.5D, 0.0D);
            }
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(LGEntityFireBlast entity)
    {
        return null;
    }

    public static class Factory implements IRenderFactory<LGEntityFireBlast>
    {
        @Override
        public Render<? super LGEntityFireBlast> createRenderFor(RenderManager manager)
        {
            return new LGRenderFireBlast(manager);
        }
    }
}
