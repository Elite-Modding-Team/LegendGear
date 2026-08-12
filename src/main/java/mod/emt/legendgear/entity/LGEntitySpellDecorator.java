package mod.emt.legendgear.entity;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.client.particle.LGParticleSpell;
import mod.emt.legendgear.spell.LGSpell;
import mod.emt.legendgear.spell.LGSpellRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LGEntitySpellDecorator extends Entity implements IEntityAdditionalSpawnData
{
    public LGSpell spell;
    public List<LGParticleSpell> particles;
    public double radius;
    public double power;
    public boolean isCrit;
    public int age;

    public LGEntitySpellDecorator(World world)
    {
        super(world);
        this.ignoreFrustumCheck = true;
    }

    public LGEntitySpellDecorator(LGEntitySpellEffect effect)
    {
        this(effect.world);
        this.spell  = effect.spell ;
        this.radius = effect.radius;
        this.power = effect.power;
        this.isCrit = effect.isCrit;
        setPosition(effect.posX, effect.posY, effect.posZ);
        setSize((float) radius * 2.0F, (float) radius * 2.0F);
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (particles == null)
        {
            particles = new ArrayList<>();

            if (age == 0)
            {
                spell.generateParticles(this);
            }

            if (particles.isEmpty())
            {
                setDead();
                return;
            }
        }

        if (world.isRemote)
        {
            particles = LGParticleSpell.updateParticles(particles);

            if (particles.isEmpty())
            {
                setDead();
                return;
            }
        }

        age++;

        if (age >= spell.getParticleLife())
        {
            setDead();
        }
    }

    @Override
    protected void writeEntityToNBT(@NotNull NBTTagCompound compound)
    {
        compound.setString("spell", spell.getId());
        compound.setDouble("radius", radius);
        compound.setDouble("power", power);
        compound.setBoolean("isCrit", isCrit);
        compound.setInteger("age", age);
    }

    @Override
    protected void readEntityFromNBT(@NotNull NBTTagCompound compound)
    {
        spell = LGSpellRegistry.get(compound.getString("spell"));
        radius = compound.getDouble("radius");
        power = compound.getDouble("power");
        isCrit = compound.getBoolean("isCrit");
        age = compound.getInteger("age");
        setSize((float) radius * 2.0F, (float) radius * 2.0F);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        ByteBufUtils.writeUTF8String(buffer, spell.getId());
        buffer.writeDouble(radius);
        buffer.writeDouble(power);
        buffer.writeBoolean(isCrit);
        buffer.writeInt(age);
    }

    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        spell = LGSpellRegistry.get(ByteBufUtils.readUTF8String(buffer));
        radius = buffer.readDouble();
        power = buffer.readDouble();
        isCrit = buffer.readBoolean();
        age = buffer.readInt();
        setSize((float) radius * 2.0F, (float) radius * 2.0F);
    }
}
