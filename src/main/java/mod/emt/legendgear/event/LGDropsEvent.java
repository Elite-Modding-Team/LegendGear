package mod.emt.legendgear.event;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.block.LGBlockMysticShrub;
import mod.emt.legendgear.entity.LGEntityItemNoStacking;
import mod.emt.legendgear.init.LGItems;
import mod.emt.legendgear.init.LGLootTables;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGDropsEvent
{
    @SubscribeEvent
    public static void onBlockDrops(BlockEvent.HarvestDropsEvent event)
    {
        EntityPlayer player = event.getHarvester();
        if (player == null)
        {
            return;
        }
        World world = event.getWorld();
        if (world.isRemote || !(world instanceof WorldServer))
        {
            return;
        }
        IBlockState state = event.getState();
        Block block = state.getBlock();
        if (!(block instanceof LGBlockMysticShrub) || state.getValue(LGBlockMysticShrub.TYPE) == LGBlockMysticShrub.EnumType.CUT || event.isSilkTouching())
        {
            return;
        }
        BlockPos pos = event.getPos();
        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.5D;
        double z = pos.getZ() + 0.5D;
        boolean charged = state.getValue(LGBlockMysticShrub.TYPE) == LGBlockMysticShrub.EnumType.CHARGED;
        for (ItemStack stack : generateDrops((WorldServer) world, charged ? LGLootTables.MYSTIC_SHRUB_CHARGED : LGLootTables.MYSTIC_SHRUB, player, charged ? event.getFortuneLevel() : event.getFortuneLevel() * 0.5F, null))
        {
            world.spawnEntity(createItemDrop(stack, world, x, y, z));
        }
    }

    @SubscribeEvent
    public static void onMobDrops(LivingDropsEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.world.isRemote || !(entity.world instanceof WorldServer))
        {
            return;
        }
        DamageSource dmgSrc = event.getSource();
        if (!event.isRecentlyHit() || !(dmgSrc.getTrueSource() instanceof EntityPlayer))
        {
            return;
        }
        if (entity instanceof EntityCreeper && event.getSource().isExplosion())
        {
            event.getDrops().add(createItemDrop(new ItemStack(LGItems.BOMB, 3), entity));
        }
        if (entity instanceof EntityEnderman || entity instanceof EntityBlaze || entity instanceof EntityGhast || entity instanceof EntityPigZombie || entity instanceof EntitySkeleton || entity instanceof EntityCreeper)
        {
            addMobDrops(event.getDrops(), entity, LGLootTables.MOBS_SPECIAL, dmgSrc, event.getLootingLevel());
            return;
        }
        addMobDrops(event.getDrops(), entity, LGLootTables.MOBS_REGULAR, dmgSrc, event.getLootingLevel());
    }

    private static List<ItemStack> generateDrops(WorldServer world, ResourceLocation lootTable, EntityPlayer player, float bonusLuck, @Nullable DamageSource dmgSrc)
    {
        LootContext.Builder lootCtxBuilder = new LootContext.Builder(world).withPlayer(player).withLuck(player.getLuck() + bonusLuck);
        if (dmgSrc != null)
        {
            lootCtxBuilder.withDamageSource(dmgSrc);
        }
        return new ArrayList<>(world.getLootTableManager().getLootTableFromLocation(lootTable).generateLootForPools(world.rand, lootCtxBuilder.build()));
    }

    private static EntityItem createItemDrop(ItemStack stack, Entity sourceEntity)
    {
        return createItemDrop(stack, sourceEntity.world, sourceEntity.posX, sourceEntity.posY, sourceEntity.posZ);
    }

    private static EntityItem createItemDrop(ItemStack stack, World world, double x, double y, double z)
    {
        EntityItem itemEntity = new LGEntityItemNoStacking(world, x, y, z, stack);
        itemEntity.motionX = world.rand.nextGaussian() * 0.07D;
        itemEntity.motionY = world.rand.nextGaussian() * 0.07D + 0.2D;
        itemEntity.motionZ = world.rand.nextGaussian() * 0.07D;
        itemEntity.setDefaultPickupDelay();
        return itemEntity;
    }

    private static void addMobDrops(List<EntityItem> drops, EntityLivingBase entity, ResourceLocation table, DamageSource dmgSrc, int looting)
    {
        for (ItemStack dropStack : generateDrops((WorldServer) entity.world, table, (EntityPlayer) Objects.requireNonNull(dmgSrc.getTrueSource()), looting * 0.5F, dmgSrc))
        {
            drops.add(createItemDrop(dropStack, entity));
        }
    }
}
