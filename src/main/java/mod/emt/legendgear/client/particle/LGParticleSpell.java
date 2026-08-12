package mod.emt.legendgear.client.particle;

import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LGParticleSpell
{
    public double x;
    public double y;
    public double z;

    public double vx;
    public double vy;
    public double vz;

    public double ax;
    public double ay;
    public double az;

    public int hibernateTime;
    public int maxLife = 20;
    public int lifeTicks;
    public double renderAge;

    public double age;
    public double size = 1.0D;
    public double drag = 1.0D;
    public double uniqueness;

    public LGParticleSpell()
    {
    }

    public LGParticleSpell(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LGParticleSpell(double x, double y, double z, double vx, double vy, double vz)
    {
        this(x, y, z);
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public LGParticleSpell(double x, double y, double z, double vx, double vy, double vz, double ax, double ay, double az)
    {
        this(x, y, z, vx, vy, vz);
        this.ax = ax;
        this.ay = ay;
        this.az = az;
    }

    public static LGParticleSpell radialParticle(Random rand, double spread, double velScale, double accScale)
    {
        double gx = rand.nextGaussian();
        double gy = rand.nextGaussian();
        double gz = rand.nextGaussian();
        Vec3d outward = new Vec3d(gx, gy, gz).normalize();
        gx = outward.x * rand.nextDouble() * spread;
        gy = outward.y * rand.nextDouble() * spread;
        gz = outward.z * rand.nextDouble() * spread;
        LGParticleSpell particle = new LGParticleSpell();
        particle.x = gx;
        particle.y = gy;
        particle.z = gz;
        particle.vx = gx * velScale;
        particle.vy = gy * velScale;
        particle.vz = gz * velScale;
        particle.ax = gx * accScale;
        particle.ay = gy * accScale;
        particle.az = gz * accScale;
        particle.uniqueness = rand.nextDouble();
        return particle;
    }

    public boolean tick()
    {
        if (hibernateTime-- > 0)
        {
            return true;
        }
        vx += ax;
        vy += ay;
        vz += az;
        vx *= drag;
        vy *= drag;
        vz *= drag;
        x += vx;
        y += vy;
        z += vz;
        lifeTicks++;
        age  = (double) lifeTicks / (double) maxLife;
        return age  < 1.0D;
    }

    public static List<LGParticleSpell> updateParticles(List<LGParticleSpell> input)
    {
        List<LGParticleSpell> output = new ArrayList<>(input.size());

        for (LGParticleSpell particle : input)
        {
            if (particle.tick())
            {
                output.add(particle);
            }
        }

        return output;
    }
}