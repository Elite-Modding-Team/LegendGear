package mod.emt.legendgear.util.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntityDamageSourceIndirect;

import javax.annotation.Nullable;

public class DamageSourceFirestorm extends EntityDamageSourceIndirect
{
    public DamageSourceFirestorm(String damageTypeIn, Entity source, @Nullable Entity indirectEntityIn)
    {
        super(damageTypeIn, source, indirectEntityIn);
        setDamageBypassesArmor();
        setFireDamage();
        setMagicDamage();
    }
}
