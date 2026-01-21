package mod.emt.legendgear.util;

import net.minecraft.util.math.MathHelper;

public class Rainbow
{
    public static float r(float phase) {
        phase = (float) (phase * 6.283185307179586D);
        float r = (MathHelper.sin(phase + 0.0F) + 1.0F) * 0.5F;
        float g = (MathHelper.sin(phase + 2.0943952F) + 1.0F) * 0.5F;
        float b = (MathHelper.sin(phase + 4.1887903F) + 1.0F) * 0.5F;
        float resat = Math.min(r, Math.min(g, b));
        r -= resat;
        g -= resat;
        b -= resat;
        float scaler = 1.0F / Math.max(r, Math.max(g, b));
        r = Math.min(scaler * r, 1.0F);
        return r;
    }

    public static float g(float phase)
    {
        phase = (float) (phase * 6.283185307179586D);
        float r = (MathHelper.sin(phase + 0.0F) + 1.0F) * 0.5F;
        float g = (MathHelper.sin(phase + 2.0943952F) + 1.0F) * 0.5F;
        float b = (MathHelper.sin(phase + 4.1887903F) + 1.0F) * 0.5F;
        float resat = Math.min(r, Math.min(g, b));
        r -= resat;
        g -= resat;
        b -= resat;
        float scaler = 1.0F / Math.max(r, Math.max(g, b));
        g = Math.min(scaler * g * 0.5F + 0.5F, 1.0F);
        return g;
    }

    public static float b(float phase)
    {
        phase = (float) (phase * 6.283185307179586D);
        float r = (MathHelper.sin(phase + 0.0F) + 1.0F) * 0.5F;
        float g = (MathHelper.sin(phase + 2.0943952F) + 1.0F) * 0.5F;
        float b = (MathHelper.sin(phase + 4.1887903F) + 1.0F) * 0.5F;
        float resat = Math.min(r, Math.min(g, b));
        r -= resat;
        g -= resat;
        b -= resat;
        float scaler = 1.0F / Math.max(r, Math.max(g, b));
        b = Math.min(scaler * b * 0.5F + 0.5F, 1.0F);
        return b;
    }
}
