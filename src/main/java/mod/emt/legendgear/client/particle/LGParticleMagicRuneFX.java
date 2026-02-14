package mod.emt.legendgear.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LGParticleMagicRuneFX extends Particle
{
    double hue;

    public LGParticleMagicRuneFX(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, float scale)
    {
        super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
        this.motionX = xSpeed;
        this.motionY = ySpeed;
        this.motionZ = zSpeed;
        this.posX = xCoord;
        this.posY = yCoord;
        this.posZ = zCoord;

        this.setParticleTextureIndex((int) (Math.random() * 26.0D + 1.0D + 224.0D));

        this.canCollide = false;
        this.particleMaxAge = 30;
        this.particleAge = 0;
        this.particleScale = 1.5F;
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
        float r = (float) Math.sin(this.hue) / 2.0F + 0.5F;
        float g = (float) Math.sin(this.hue + 2.0943951023931953D) / 2.0F + 0.5F;
        float b = (float) Math.sin(this.hue - 2.0943951023931953D) / 2.0F + 0.5F;
        float freshness = 1.0F - (float) this.particleAge / (float) this.particleMaxAge;

        this.particleAlpha = freshness;
        this.particleRed = freshness + (1.0F - freshness) * r;
        this.particleGreen = freshness + (1.0F - freshness) * g;
        this.particleBlue = freshness + (1.0F - freshness) * b;
    }

    @Override
    public int getBrightnessForRender(float partialTick)
    {
        return 15728880;
    }
}
