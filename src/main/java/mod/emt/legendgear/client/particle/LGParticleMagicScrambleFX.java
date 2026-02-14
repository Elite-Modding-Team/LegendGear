package mod.emt.legendgear.client.particle;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LGParticleMagicScrambleFX extends LGParticleMagicRuneFX
{
    double hue;

    public LGParticleMagicScrambleFX(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, float scale)
    {
        super(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, scale);
        this.particleMaxAge = 15;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        this.setParticleTextureIndex((int) (Math.random() * 26.0D + 1.0D + 224.0D));
        float freshness = 1.0F - (float) this.particleAge / this.particleMaxAge;

        this.particleGreen = Math.min(freshness * 2.0F, 1.0F);
        this.particleRed = Math.max(freshness * 2.0F - 1.0F, 0.0F);
        this.particleBlue = 1.0F;
    }
}
