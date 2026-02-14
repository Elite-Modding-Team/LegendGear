package mod.emt.legendgear.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LGParticleFireSwirlFX extends Particle
{
    float baseScale;

    public LGParticleFireSwirlFX(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, float scale)
    {
        super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
        this.motionX = xSpeed;
        this.motionY = ySpeed;
        this.motionZ = zSpeed;
        this.posX = xCoord;
        this.posY = yCoord;
        this.posZ = zCoord;
        this.canCollide = false;
        this.particleMaxAge = 7;
        this.particleAge = 0;
        this.particleScale = 1.5F;
        this.baseScale = scale;
        this.onUpdate();
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.setParticleTextureIndex(7 - this.particleAge);

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        this.move(this.motionX, this.motionY, this.motionZ);

        float freshness = 1.0F - (float) this.particleAge / (float) this.particleMaxAge;
        float r = Math.min(freshness * 3.0F, 1.0F);
        float g = Math.max(Math.min(freshness * 3.0F - 1.0F, 1.0F), 0.0F);
        float b = Math.max(Math.min(freshness * 3.0F - 2.0F, 1.0F), 0.0F);
        this.particleAlpha = 1.0F;
        this.particleRed = r;
        this.particleGreen = g;
        this.particleBlue = b;
        this.particleScale = (1.5F - 0.0F * freshness) * this.baseScale;
    }

    @Override
    public int getBrightnessForRender(float partialTick)
    {
        return 15728880;
    }
}
