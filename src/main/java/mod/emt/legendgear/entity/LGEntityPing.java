package mod.emt.legendgear.entity;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LGEntityPing extends Entity implements IEntityAdditionalSpawnData {
    public int energy;
    public int color;
    public int age;

    public LGEntityPing(World world) {
        super(world);
        this.energy = 10;
        this.color = 0;
        this.age = 0;
        this.ignoreFrustumCheck = true;
    }

    public LGEntityPing(World world, double x, double y, double z, int charge, int color) {
        this(world);
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.energy = charge;
        this.color = color;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(this.color);
        buffer.writeInt(this.energy);
        buffer.writeInt(this.age);
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        this.color = buffer.readInt();
        this.energy = buffer.readInt();
        this.age = buffer.readInt();
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate() {
        if (this.age == 0) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, LGSoundEvents.ITEM_SPOTTING_SCOPE_MARK.getSoundEvent(), SoundCategory.NEUTRAL, 20.0F, 1.0F);
            this.world.playSound(null, this.posX, this.posY, this.posZ, LGSoundEvents.ITEM_SPOTTING_SCOPE_MARK.getSoundEvent(), SoundCategory.NEUTRAL, 20.0F, 1.5F);
        }
        this.age++;
        this.energy--;
        if (this.energy <= 0) setDead();
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag) {
        this.color = tag.getInteger("color");
        this.energy = tag.getInteger("energy");
        this.age = tag.getInteger("age");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag) {
        tag.setInteger("color", this.color);
        tag.setInteger("energy", this.energy);
        tag.setInteger("age", this.age);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBrightnessForRender() {
        return 15728880;
    }

    @Override
    public float getBrightness() {
        return 1.0F;
    }
}
