package mod.emt.legendgear.client.render;

import mod.emt.legendgear.client.particle.LGParticleSpell;
import mod.emt.legendgear.entity.LGEntitySpellDecorator;
import mod.emt.legendgear.spell.LGSpell;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jetbrains.annotations.NotNull;

public class LGRenderSpellDecorator extends Render<LGEntitySpellDecorator> {
    public LGRenderSpellDecorator(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(@NotNull LGEntitySpellDecorator entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        this.renderSpellEffect(entity, partialTicks);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    private void renderSpellEffect(LGEntitySpellDecorator decorator, float partialTicks)
    {
        if (decorator.particles == null)
        {
            return;
        }

        LGSpell spell = decorator.spell;
        double particleScale = spell.getParticleScale();

        for (LGParticleSpell particle : decorator.particles)
        {
            if (particle.hibernateTime <= 0)
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate(particle.x + particle.vx * partialTicks, particle.y + particle.vy * partialTicks, particle.z + particle.vz * partialTicks);
                particle.renderAge = particle.age + (double) partialTicks / particle.maxLife;
                spell.renderParticle(this, particle, partialTicks, particleScale);
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(@NotNull LGEntitySpellDecorator entity)
    {
        return null;
    }

    public static class Factory implements IRenderFactory<LGEntitySpellDecorator>
    {
        @Override
        public Render<? super LGEntitySpellDecorator> createRenderFor(RenderManager manager)
        {
            return new LGRenderSpellDecorator(manager);
        }
    }
}