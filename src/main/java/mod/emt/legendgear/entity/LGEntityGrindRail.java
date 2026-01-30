package mod.emt.legendgear.entity;

import io.netty.buffer.ByteBuf;
import mod.emt.legendgear.client.particle.LGParticleHandler;
import mod.emt.legendgear.init.LGBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import java.util.ArrayList;
import java.util.List;

// TODO: Clean up everything
public class LGEntityGrindRail extends Entity implements IEntityAdditionalSpawnData {
    private static final DataParameter<String> COORDS = EntityDataManager.createKey(LGEntityGrindRail.class, DataSerializers.STRING);

    public double fromX;
    public double fromY;
    public double fromZ;
    public double toX;
    public double toY;
    public double toZ;
    public Vec3d nextNode;
    public static final double min_grind_speed = 0.02D;
    public static final double max_grind_speed = 1.0D;
    public static final double start_grind_speed = 0.3D;
    public static final double drag = 0.002D;
    public static final double YOFF = 0.0625D;
    public double speed;
    public static final int NODE_LINK_DISTANCE = 6;

    public LGEntityGrindRail(World world, Vec3d startNode, Vec3d towardsNode, double speed) {
        super(world);
        this.fromX = startNode.x;
        this.fromY = startNode.y;
        this.fromZ = startNode.z;
        this.toX = towardsNode.x;
        this.toY = towardsNode.y;
        this.toZ = towardsNode.z;
        this.setPosition(startNode.x, startNode.y, startNode.z);
        this.speed = Math.max(0.3D, speed);
        Vec3d dir = getLineDirection();
        this.speed = Math.max(this.speed, speed);
        this.motionX = dir.x * this.speed;
        this.motionY = dir.y * this.speed;
        this.motionZ = dir.z * this.speed;
        this.noClip = true;
        this.setSize(0.25F, 0.25F);
        //this.dataWatcher.addObject(24, new Integer((int) (this.speed * 1000000.0D)));
        //this.dataWatcher.addObject(25, "");
        saveCoordsToData();
    }

    public LGEntityGrindRail(World world) {
        super(world);
        this.noClip = true;
        this.setSize(0.25F, 0.25F);
        this.speed = 0.3D;
        //this.dataWatcher.addObject(24, new Integer((int) (this.speed * 1000000.0D)));
        //this.dataWatcher.addObject(25, "");
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(COORDS, "0,0,0,0,0,0");
    }

    public boolean isPast(Vec3d node) {
        Vec3d vel = new Vec3d(this.motionX, this.motionY, this.motionZ);
        Vec3d tonode = node.subtract(this.posX, this.posY, this.posZ);
        return vel.dotProduct(tonode) <= 0.0D;
    }

    public void snapOn() {
        Vec3d fromToPos = new Vec3d(this.posX - this.fromX, this.posY - this.fromY, this.posZ - this.fromZ);
        Vec3d snapDir = new Vec3d(this.toX - this.fromX, this.toY - this.fromY, this.toZ - this.fromZ).normalize();

        double snapdist = snapDir.dotProduct(fromToPos);

        this.posX = this.fromX + snapDir.x * snapdist;
        this.posY = this.fromY + snapDir.y * snapdist;
        this.posZ = this.fromZ + snapDir.z * snapdist;
    }

    public static List getNodesNearButNotAt(World world, int x, int y, int z, int radius) {
        List<BlockPos> nodes = new ArrayList<>();
        Iterable<BlockPos> positions = BlockPos.getAllInBox(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);

        for (BlockPos pos : positions) {
            continue;
        }
        return nodes;
    }

    public boolean nodeHop() {
        int newOldX = (int) Math.floor(this.toX);
        int newOldY = (int) Math.floor(this.toY);
        int newOldZ = (int) Math.floor(this.toZ);

        List candidates = getNodesNearButNotAt(this.world, newOldX, newOldY, newOldZ, 6);
        Vec3d dir = getLineDirection();

        this.fromX = this.toX;
        this.fromY = this.toY;
        this.fromZ = this.toZ;

        Vec3d toNode = findBestAlignedNode(this.world, candidates, new Vec3d(this.fromX, this.fromY, this.fromZ), dir, 0.8D);

        if (toNode == null) {
            boot();
            return false;
        }

        this.toX = toNode.x;
        this.toY = toNode.y;
        this.toZ = toNode.z;

        Vec3d newdir = getLineDirection();

        boolean isSneaking = false;
        if (!this.getPassengers().isEmpty()) {
            isSneaking = this.getPassengers().get(0).isSneaking();
        }

        float speedlevel = (float) ((this.speed - 0.02D) / 0.98D);
        return true;
    }

    public void boot() {
        this.setDead();

        if (!this.getPassengers().isEmpty()) {
            Entity e = this.getPassengers().get(0);
            e.dismountRidingEntity();
            e.motionX = this.motionX;
            e.motionY = this.motionY;
            e.motionZ = this.motionZ;
            e.setPosition(this.posX, this.posY + this.getMountedYOffset() + 0.1D, this.posZ);
            e.fallDistance = 0.0F;
        }
    }

    public void onUpdate() {
        super.onUpdate();
        loadCoordsFromData();
        Vec3d dir = getLineDirection();

        if (this.getPassengers().isEmpty()) {
            this.setDead();
            return;
        }

        Entity rider = this.getPassengers().get(0);
        BlockPos fromPos = new BlockPos(this.fromX, this.fromY, this.fromZ);
        BlockPos toPos = new BlockPos(this.toX, this.toY, this.toZ);

        if (this.world.getBlockState(fromPos).getBlock() != LGBlocks.STARBEAM_TORCH) {
            boot();
            return;
        }

        if (this.world.getBlockState(toPos).getBlock() != LGBlocks.STARBEAM_TORCH) {
            boot();
            return;
        }

        if (rider.getEntityData().getBoolean("justJumped")) {
            boot();
            rider.motionY += 0.5D;
            rider.getEntityData().setBoolean("justJumped", false);
            return;
        }

        Vec3d motionVec = new Vec3d(this.motionX, this.motionY, this.motionZ);
        this.speed = motionVec.length();

        this.speed += dir.y * -0.02D;
        if (rider.isSneaking()) {
            this.speed += dir.y * -0.04D;
        }
        this.speed -= 0.002D;

        if (this.speed < 0.02D && !this.world.isRemote) {
            boot();
            return;
        }

        this.speed = Math.min(this.speed, 1.0D);
        this.motionX = this.speed * dir.x;
        this.motionY = this.speed * dir.y;
        this.motionZ = this.speed * dir.z;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;

        if (isPast(new Vec3d(this.toX, this.toY, this.toZ))) {
            if (nodeHop()) {
                snapOn();
            }
        }

        if (this.world.isRemote) {
            LGParticleHandler.spawnSparkleFX(this.world, this.posX, this.posY, this.posZ, this.rand.nextGaussian() * 0.1D, this.rand.nextGaussian() * 0.1D, this.rand.nextGaussian() * 0.1D, 3.0F);
            LGParticleHandler.spawnMagicRuneFX(this.world, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, 2.0F);
        } else if (this.ticksExisted % 5 == 0) {
            float speedlevel = (float) ((this.speed - 0.02D) / 0.98D);
            //this.worldObj.playSoundAtEntity(this, "assets.gah", 0.3F, 0.5F + speedlevel * 1.5F);
        }

        saveCoordsToData();
    }

    public void loadCoordsFromData() {
        String data = this.dataManager.get(COORDS);
        String[] parts = data.split(",");

        if (parts.length != 6) {
            return;
        }

        this.fromX = Integer.parseInt(parts[0]) + 0.5D;
        this.fromY = Integer.parseInt(parts[1]) + 0.5D + 0.0625D;
        this.fromZ = Integer.parseInt(parts[2]) + 0.5D;

        this.toX = Integer.parseInt(parts[3]) + 0.5D;
        this.toY = Integer.parseInt(parts[4]) + 0.5D + 0.0625D;
        this.toZ = Integer.parseInt(parts[5]) + 0.5D;
    }

    public void saveCoordsToData() {
        String out = (int) Math.floor(this.fromX) + "," +
                (int) Math.floor(this.fromY) + "," +
                (int) Math.floor(this.fromZ) + "," +
                (int) Math.floor(this.toX) + "," +
                (int) Math.floor(this.toY) + "," +
                (int) Math.floor(this.toZ);

        this.dataManager.set(COORDS, out);
    }

    public static Vec3d findBestAlignedNode(World world, List<Vec3d> nodes, Vec3d pos, Vec3d dir) {
        return findBestAlignedNode(world, nodes, pos, dir, 0.0D);
    }

    public static Vec3d findBestAlignedNode(World world, List<Vec3d> nodes, Vec3d pos, Vec3d dir, double minimumDot) {
        Vec3d best = null;
        double matchvalue = minimumDot;
        dir = dir.normalize();

        for (Vec3d test : nodes) {
            Vec3d posToTest = test.subtract(pos).normalize();
            double dot = dir.dotProduct(posToTest);

            if (dot > matchvalue && world.rayTraceBlocks(pos, test, false, true, false) == null) {
                matchvalue = dot;
                best = test;
            }
        }

        return best;
    }

    public Vec3d getLineDirection() {
        return new Vec3d(this.toX - this.fromX, this.toY - this.fromY, this.toZ - this.fromZ).normalize();
    }

    public static LGEntityGrindRail tryMakingRail(World world, int x, int y, int z, EntityLivingBase rider) {
        Vec3d from = new Vec3d(x + 0.5D, y + 0.5D + 0.0625D, z + 0.5D);
        Vec3d dir = rider.getLookVec();
        List<BlockPos> nodes = getNodesNearButNotAt(world, x, y, z, 6);
        List<Vec3d> nodeVectors = new ArrayList<>();

        for (BlockPos p : nodes) {
            nodeVectors.add(new Vec3d(p.getX() + 0.5D, p.getY() + 0.5625D, p.getZ() + 0.5D));
        }

        Vec3d to = findBestAlignedNode(world, nodeVectors, from, dir, 0.0D);

        if (to == null) {
            return null;
        }

        Vec3d linedir = to.subtract(from).normalize();
        double momentum = rider.motionX * linedir.x + rider.motionY * linedir.y + rider.motionZ * linedir.z;

        return new LGEntityGrindRail(world, from, to, momentum);
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public double getMountedYOffset() {
        return 0.5D;
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        this.fromX = compound.getDouble("fromX");
        this.fromY = compound.getDouble("fromY");
        this.fromZ = compound.getDouble("fromZ");
        this.toX = compound.getDouble("toX");
        this.toY = compound.getDouble("toY");
        this.toZ = compound.getDouble("toZ");
        this.speed = compound.getDouble("speed");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setDouble("fromX", this.fromX);
        compound.setDouble("fromY", this.fromY);
        compound.setDouble("fromZ", this.fromZ);
        compound.setDouble("toX", this.toX);
        compound.setDouble("toY", this.toY);
        compound.setDouble("toZ", this.toZ);
        compound.setDouble("speed", this.speed);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
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
    public void readSpawnData(ByteBuf buffer) {
        PacketBuffer packet = new PacketBuffer(buffer);
        this.fromX = packet.readDouble();
        this.fromY = packet.readDouble();
        this.fromZ = packet.readDouble();
        this.toX = packet.readDouble();
        this.toY = packet.readDouble();
        this.toZ = packet.readDouble();
        this.speed = packet.readDouble();
        saveCoordsToData();
    }
}
