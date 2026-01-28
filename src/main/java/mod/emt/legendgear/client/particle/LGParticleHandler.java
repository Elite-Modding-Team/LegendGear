package mod.emt.legendgear.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LGParticleHandler
{
    public static void spawnSparkleFX(World world, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, float scale)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new LGParticleSparkleFX(world, xCoord, yCoord + 0.5D, zCoord, xSpeed, ySpeed, zSpeed, scale));
    }
}
