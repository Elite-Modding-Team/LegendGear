package mod.emt.legendgear.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import mod.emt.legendgear.client.particle.LGParticleHandler;
import mod.emt.legendgear.init.LGBlocks;
import mod.emt.legendgear.init.LGSoundEvents;

public class LGEntityGrindRail extends Entity implements IEntityAdditionalSpawnData
{
    public static final double MIN_GRIND_SPEED = 0.02D;
    public static final double START_GRIND_SPEED = 0.3D;
    public static final double DRAG = 0.002D;
    public static final double Y_OFF = 0.0625D;
    public static final int NODE_LINK_DISTANCE = 6;
    private static final DataParameter<String> COORDS = EntityDataManager.createKey(LGEntityGrindRail.class, DataSerializers.STRING);

    public static List<Vec3d> getNodesNearButNotAt(World world, int x, int y, int z, int radius)
    {
        List<Vec3d> nodes = new ArrayList<>();
        for (int ix = x - radius; ix <= x + radius; ix++)
        {
            for (int iy = y - radius; iy <= y + radius; iy++)
            {
                for (int iz = z - radius; iz <= z + radius; iz++)
                {
                    if (ix == x && iy == y && iz == z)
                    {
                        continue;
                    }
                    BlockPos pos = new BlockPos(ix, iy, iz);
                    if (world.getBlockState(pos).getBlock() == LGBlocks.STARBEAM_TORCH)
                    {
                        nodes.add(new Vec3d(ix + 0.5, iy + 0.5 + Y_OFF, iz + 0.5));
                    }
                }
            }
        }
        return nodes;
    }

    public static Vec3d findBestAlignedNode(World world, List<Vec3d> nodes, Vec3d pos, Vec3d dir, double minimumDot)
    {
        Vec3d best = null;
        double matchValue = minimumDot;
        dir = dir.normalize();

        for (Vec3d test : nodes)
        {
            Vec3d posToTest = test.subtract(pos).normalize();
            double dot = dir.dotProduct(posToTest);

            if (dot > matchValue && world.rayTraceBlocks(pos, test, false, true, false) == null)
            {
                matchValue = dot;
                best = test;
            }
        }

        return best;
    }

    public static LGEntityGrindRail tryMakingRail(World world, int x, int y, int z, EntityLivingBase rider)
    {
        Vec3d from = new Vec3d(x + 0.5D, y + 0.5D + Y_OFF, z + 0.5D);
        Vec3d dir = rider.getLookVec();
        List<Vec3d> nodeVectors = getNodesNearButNotAt(world, x, y, z, NODE_LINK_DISTANCE);

        Vec3d to = findBestAlignedNode(world, nodeVectors, from, dir, 0.0D);

        if (to == null)
        {
            return null;
        }

        Vec3d lineDir = to.subtract(from).normalize();
        double momentum = rider.motionX * lineDir.x + rider.motionY * lineDir.y + rider.motionZ * lineDir.z;

        return new LGEntityGrindRail(world, from, to, momentum);
    }

    private double fromX;
    private double fromY;
    private double fromZ;
    private double toX;
    private double toY;
    private double toZ;
    private double speed;

    public LGEntityGrindRail(World world, Vec3d startNode, Vec3d towardsNode, double speed)
    {
        super(world);
        this.fromX = startNode.x;
        this.fromY = startNode.y;
        this.fromZ = startNode.z;
        this.toX = towardsNode.x;
        this.toY = towardsNode.y;
        this.toZ = towardsNode.z;
        this.setPosition(startNode.x, startNode.y, startNode.z);
        this.speed = Math.max(START_GRIND_SPEED, speed);
        Vec3d dir = getLineDirection();
        this.speed = Math.max(this.speed, speed);
        this.motionX = dir.x * this.speed;
        this.motionY = dir.y * this.speed;
        this.motionZ = dir.z * this.speed;
        this.noClip = true;
        this.setSize(0.25F, 0.25F);
    }

    public LGEntityGrindRail(World world)
    {
        super(world);
        this.noClip = true;
        this.setSize(0.25F, 0.25F);
        this.speed = 0.3D;
    }

    public boolean isPast(Vec3d node)
    {
        Vec3d vel = new Vec3d(this.motionX, this.motionY, this.motionZ);
        Vec3d tonode = node.subtract(this.posX, this.posY, this.posZ);
        return vel.dotProduct(tonode) <= 0.0D;
    }

    public void snapOn()
    {
        Vec3d fromToPos = new Vec3d(this.posX - this.fromX, this.posY - this.fromY, this.posZ - this.fromZ);
        Vec3d snapDir = new Vec3d(this.toX - this.fromX, this.toY - this.fromY, this.toZ - this.fromZ).normalize();

        double snapDist = snapDir.dotProduct(fromToPos);

        this.posX = this.fromX + snapDir.x * snapDist;
        this.posY = this.fromY + snapDir.y * snapDist;
        this.posZ = this.fromZ + snapDir.z * snapDist;
    }

    public boolean nodeHop()
    {
        int newOldX = (int) Math.floor(this.toX);
        int newOldY = (int) Math.floor(this.toY);
        int newOldZ = (int) Math.floor(this.toZ);

        List<Vec3d> candidates = getNodesNearButNotAt(this.world, newOldX, newOldY, newOldZ, NODE_LINK_DISTANCE);
        Vec3d dir = getLineDirection();

        this.fromX = this.toX;
        this.fromY = this.toY;
        this.fromZ = this.toZ;

        Vec3d toNode = findBestAlignedNode(this.world, candidates, new Vec3d(this.fromX, this.fromY, this.fromZ), dir, 0.8D);

        if (toNode == null)
        {
            boot();
            return false;
        }

        this.toX = toNode.x;
        this.toY = toNode.y;
        this.toZ = toNode.z;

        return true;
    }

    public void boot()
    {
        this.setDead();

        if (!this.getPassengers().isEmpty())
        {
            Entity e = this.getPassengers().get(0);
            e.dismountRidingEntity();
            e.motionX = this.motionX;
            e.motionY = this.motionY;
            e.motionZ = this.motionZ;
            e.setPosition(this.posX, this.posY + this.getMountedYOffset() + 0.1D, this.posZ);
            e.fallDistance = 0.0F;
        }
    }

    public Vec3d getLineDirection()
    {
        return new Vec3d(this.toX - this.fromX, this.toY - this.fromY, this.toZ - this.fromZ).normalize();
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        PacketBuffer packet = new PacketBuffer(buffer);
        packet.writeDouble(this.fromX);
        packet.writeDouble(this.fromY);
        packet.writeDouble(this.fromZ);
        packet.writeDouble(this.toX);
        packet.writeDouble(this.toY);
        packet.writeDouble(this.toZ);
        packet.writeDouble(this.speed);
    }

    @Override
    public void readSpawnData(ByteBuf buffer)
    {
        PacketBuffer packet = new PacketBuffer(buffer);
        this.fromX = packet.readDouble();
        this.fromY = packet.readDouble();
        this.fromZ = packet.readDouble();
        this.toX = packet.readDouble();
        this.toY = packet.readDouble();
        this.toZ = packet.readDouble();
        this.speed = packet.readDouble();
    }

    @Override
    protected void entityInit()
    {
        this.dataManager.register(COORDS, "0,0,0,0,0,0");
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        super.onUpdate();

        if (!this.world.isRemote)
        {
            if (this.getPassengers().isEmpty())
            {
                this.setDead();
                return;
            }

            Entity rider = this.getPassengers().get(0);

            if (this.world.getBlockState(new BlockPos(this.fromX, this.fromY, this.fromZ)).getBlock() != LGBlocks.STARBEAM_TORCH || this.world.getBlockState(new BlockPos(this.toX, this.toY, this.toZ)).getBlock() != LGBlocks.STARBEAM_TORCH)
            {
                boot();
                return;
            }

            if (rider.getEntityData().getBoolean("justJumped"))
            {
                boot();
                rider.motionY += 0.5D;
                rider.getEntityData().setBoolean("justJumped", false);
                return;
            }

            Vec3d dir = getLineDirection();
            this.speed = new Vec3d(this.motionX, this.motionY, this.motionZ).length();

            Vec3d motionVec = new Vec3d(this.motionX, this.motionY, this.motionZ);
            this.speed = motionVec.length();

            this.speed += dir.y * -0.02D;

            if (rider.isSprinting())
            {
                this.speed += dir.y * -0.04D;

                if (dir.y == 0)
                {
                    this.speed *= 0.9D;
                }
            }

            this.speed -= DRAG;

            if (this.speed < MIN_GRIND_SPEED)
            {
                if (dir.y >= 0)
                {
                    boot();
                    return;
                }
                else
                {
                    this.speed = MIN_GRIND_SPEED;
                }
            }

            this.motionX = this.speed * dir.x;
            this.motionY = this.speed * dir.y;
            this.motionZ = this.speed * dir.z;
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;

        if (this.ticksExisted % 5 == 0)
        {
            float speedLevel = (float) ((this.speed - 0.02D) / 0.98D);
            this.world.playSound(null, this.getPosition(), LGSoundEvents.ENTITY_MAGIC_BOOMERANG_FLY.getSoundEvent(), SoundCategory.BLOCKS, 0.3F, 0.5F + speedLevel * 1.5F);
        }

        if (!this.world.isRemote && isPast(new Vec3d(this.toX, this.toY, this.toZ)) && nodeHop())
        {
            snapOn();
        }

        if (FMLLaunchHandler.side().isClient() && this.world.isRemote)
        {
            LGParticleHandler.spawnSparkleFX(this.world, this.posX, this.posY, this.posZ, this.rand.nextGaussian() * 0.1D, this.rand.nextGaussian() * 0.1D, this.rand.nextGaussian() * 0.1D, 3.0F);
            LGParticleHandler.spawnMagicRuneFX(this.world, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, 2.0F);
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.fromX = compound.getDouble("fromX");
        this.fromY = compound.getDouble("fromY");
        this.fromZ = compound.getDouble("fromZ");
        this.toX = compound.getDouble("toX");
        this.toY = compound.getDouble("toY");
        this.toZ = compound.getDouble("toZ");
        this.speed = compound.getDouble("speed");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setDouble("fromX", this.fromX);
        compound.setDouble("fromY", this.fromY);
        compound.setDouble("fromZ", this.fromZ);
        compound.setDouble("toX", this.toX);
        compound.setDouble("toY", this.toY);
        compound.setDouble("toZ", this.toZ);
        compound.setDouble("speed", this.speed);
    }

    @Override
    public double getMountedYOffset()
    {
        return 0.5D;
    }

    @Override
    public boolean shouldRiderSit()
    {
        return false;
    }
}
