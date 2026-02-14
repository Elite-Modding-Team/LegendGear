package mod.emt.legendgear.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LGParticleSparkleFX extends Particle
{
    double hue;
    float baseScale;

    public LGParticleSparkleFX(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, float scale)
    {
        super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
        this.motionX = xSpeed;
        this.motionY = ySpeed;
        this.motionZ = zSpeed;
        this.posX = xCoord;
        this.posY = yCoord;
        this.posZ = zCoord;

        this.setParticleTextureIndex(149);

        this.canCollide = false;
        this.particleMaxAge = 6;
        this.particleAge = 0;
        this.particleScale = 1.5F;
        this.baseScale = scale;
        this.hue = Math.random() * Math.PI * 2.0D;
        this.onUpdate();
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        this.move(this.motionX, this.motionY, this.motionZ);

        this.hue += 0.3D;
        float freshness = 1.0F - (float) this.particleAge / (float) this.particleMaxAge;

        this.particleAlpha = 1.0F;
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = freshness;
        this.particleScale = freshness * this.baseScale;
    }

    @Override
    public int getBrightnessForRender(float partialTick)
    {
        return 15728880;
    }
}
