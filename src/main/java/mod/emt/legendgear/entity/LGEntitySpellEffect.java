package mod.emt.legendgear.entity;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.spell.LGSpell;
import mod.emt.legendgear.spell.LGSpellRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import java.util.*;

public class LGEntitySpellEffect extends Entity implements IEntityAdditionalSpawnData {
    public EntityPlayer caster;
    public double radius;
    public double power;
    public boolean isCrit;
    public LGSpell spell;
    public int lifeTicks;
    private static final float BLOCK_RADIUS_FUDGE = 0.25F;

    public final List<BlockPos> blocksAffected = new ArrayList<>();
    private final List<Runnable> pendingBlockActions = new ArrayList<>();

    public LGEntitySpellEffect(World world)
    {
        super(world);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.setSize(0.0F, 0.0F);
    }

    public LGEntitySpellEffect(World world, EntityPlayer caster, LGSpell spell, Vec3d location, double radius, double power, boolean crit)
    {
        this(world);
        this.spell = spell;
        this.caster = caster;
        this.radius = radius;
        this.power = power;
        this.isCrit = crit;
        setPosition(location.x, location.y, location.z);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (!world.isRemote)
        {
            tryHittingEntities();
            affectBlocks();
        }

        if (lifeTicks == 0)
        {
            if (!world.isRemote)
            {
                spawnDecorator();
            }
            else
            {
                playSpellSound();
            }
        }

        lifeTicks++;

        if (lifeTicks >= spell.getSpellLife())
        {
            setDead();
        }
    }

    public void queueBlockAction(Runnable action)
    {
        pendingBlockActions.add(action);
    }

    public void processEntityHit(Entity target)
    {
        spell.onNonLivingEntityHit(this, target);

        if (target instanceof EntityLivingBase)
        {
            EntityLivingBase living = (EntityLivingBase) target;
            boolean affected = true;

            if (power > 0.0D)
            {
                affected = living.attackEntityFrom(spell.getDamageSource(this), (float) power);
            }

            if (affected)
            {
                spell.onLivingEntityHit(this, living);
                applyKnockback(living);
            }
        }
    }

    public void tryHittingEntities()
    {
        AxisAlignedBB bounds = new AxisAlignedBB(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius);
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, bounds);
        for (Entity entity : entities)
        {
            AxisAlignedBB box = entity.getEntityBoundingBox();

            if (box == null || (entity == caster && !spell.canAffectCaster()))
            {
                continue;
            }

            double cx = Math.min(box.maxX, Math.max(posX, box.minX));
            double cy = Math.min(box.maxY, Math.max(posY, box.minY));
            double cz = Math.min(box.maxZ, Math.max(posZ, box.minZ));
            double dsq = (cx - posX) * (cx - posX) + (cy - posY) * (cy - posY) + (cz - posZ) * (cz - posZ);

            if (dsq <= radius * radius)
            {
                processEntityHit(entity);
            }
        }
    }

    private void affectBlocks()
    {
        blocksAffected.clear();
        double adjustedRadius = radius + BLOCK_RADIUS_FUDGE;

        AxisAlignedBB bounds = new AxisAlignedBB(Math.floor(posX - adjustedRadius), Math.floor(posY - adjustedRadius), Math.floor(posZ - adjustedRadius), Math.ceil(posX + adjustedRadius), Math.ceil(posY + adjustedRadius), Math.ceil(posZ + adjustedRadius));
        Vec3d center = new Vec3d(posX, posY, posZ);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int x = (int) bounds.minX; x < (int) bounds.maxX; x++)
        {
            for (int y = (int) bounds.minY; y < (int) bounds.maxY; y++)
            {
                for (int z = (int) bounds.minZ; z < (int) bounds.maxZ; z++)
                {
                    pos.setPos(x, y, z);
                    IBlockState state = world.getBlockState(pos);
                    if (state.getBlock().isAir(state, world, pos))
                    {
                        continue;
                    }
                    Vec3d blockCenter = new Vec3d(x + 0.5, y + 0.5, z + 0.5);
                    if (blockCenter.squareDistanceTo(center) <= adjustedRadius * adjustedRadius)
                    {
                        onBlockHit(pos.toImmutable());
                    }
                }
            }
        }

        for (Runnable action : pendingBlockActions)
        {
            action.run();
        }

        pendingBlockActions.clear();
    }

    private void applyKnockback(EntityLivingBase target)
    {
        if (caster == null)
        {
            return;
        }

        float strength = (float) spell.getKnockback(isCrit);

        if (strength <= 0.0F)
        {
            return;
        }

        target.knockBack(caster, strength, MathHelper.sin(caster.rotationYaw * 0.017453292F), -MathHelper.cos(caster.rotationYaw * 0.017453292F));
    }

    private void onBlockHit(BlockPos pos)
    {
        spell.onBlockHit(this, pos);
    }

    private void spawnDecorator()
    {
        world.spawnEntity(new LGEntitySpellDecorator(this));
    }

    private void playSpellSound()
    {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tag)
    {
        tag.setString("spell", spell.getId());
        tag.setDouble("radius", radius);
        tag.setDouble("power", power);
        tag.setBoolean("isCrit", isCrit);
        tag.setInteger("lifeTicks", lifeTicks);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tag)
    {
        spell = LGSpellRegistry.get(tag.getString("spell"));
        radius = tag.getDouble("radius");
        power = tag.getDouble("power");
        isCrit = tag.getBoolean("isCrit");
        lifeTicks = tag.getInteger("lifeTicks");
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        ByteBufUtils.writeUTF8String(buffer, spell.getId());
        buffer.writeDouble(radius);
        buffer.writeDouble(power);
        buffer.writeBoolean(isCrit);
        buffer.writeInt(lifeTicks);
    }

    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        spell = LGSpellRegistry.get(ByteBufUtils.readUTF8String(buffer));
        radius = buffer.readDouble();
        power = buffer.readDouble();
        isCrit = buffer.readBoolean();
        lifeTicks = buffer.readInt();
    }
}
