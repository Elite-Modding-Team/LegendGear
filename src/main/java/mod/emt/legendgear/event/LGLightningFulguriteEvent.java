package mod.emt.legendgear.event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockSand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.block.LGBlockLightningStruck;
import mod.emt.legendgear.block.LGBlockLightningStruckFalling;
import mod.emt.legendgear.config.LGConfig;
import mod.emt.legendgear.init.LGBlocks;
import mod.emt.legendgear.init.LGItems;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID)
public class LGLightningFulguriteEvent
{
    @SubscribeEvent
    public static void onBlockStruckByLightning(EntityJoinWorldEvent event)
    {
        if (!event.getWorld().isRemote && event.getEntity() instanceof EntityLightningBolt && LGConfig.GENERAL_SETTINGS.lightningStruckBlocks)
        {
            World world = event.getWorld();
            EntityLightningBolt lightning = (EntityLightningBolt) event.getEntity();
            BlockPos hitPos = world.getTopSolidOrLiquidBlock(lightning.getPosition());
            IBlockState hitBlockState = world.getBlockState(hitPos);
            Block hitBlock = hitBlockState.getBlock();
            // Try block below when hitting foliage or shallow water
            if (!hitBlockState.isSideSolid(world, hitPos, EnumFacing.UP))
            {
                hitPos = hitPos.down();
                hitBlockState = world.getBlockState(hitPos);
                hitBlock = hitBlockState.getBlock();
            }
            if (hitBlock instanceof BlockGrass)
            {
                world.setBlockState(hitPos, LGBlocks.LIGHTNING_STRUCK_DIRT.getDefaultState());
            }
            else if (hitBlock instanceof BlockDirt)
            {
                if (hitBlockState.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.COARSE_DIRT)
                {
                    world.setBlockState(hitPos, LGBlocks.LIGHTNING_STRUCK_COARSE_DIRT.getDefaultState());
                }
                else
                {
                    world.setBlockState(hitPos, LGBlocks.LIGHTNING_STRUCK_DIRT.getDefaultState());
                }
            }
            else if (hitBlock instanceof BlockSand)
            {
                if (hitBlockState.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND)
                {
                    world.setBlockState(hitPos, LGBlocks.LIGHTNING_STRUCK_RED_SAND.getDefaultState());
                }
                else
                {
                    world.setBlockState(hitPos, LGBlocks.LIGHTNING_STRUCK_SAND.getDefaultState());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onFulguriteHarvest(BlockEvent.HarvestDropsEvent event)
    {
        // Light-struck blocks dug by shovels will drop Fulgurite
        if (event.getHarvester() != null && (event.getHarvester().getHeldItemMainhand().getItem().getToolClasses(event.getHarvester().getHeldItemMainhand()).contains("shovel") && !event.isSilkTouching()))
        {
            if (event.getState().getBlock() instanceof LGBlockLightningStruck || event.getState().getBlock() instanceof LGBlockLightningStruckFalling)
            {
                List<ItemStack> toBeRemoved = new ArrayList<>();
                List<ItemStack> toBeAdded = new ArrayList<>();
                toBeAdded.add(new ItemStack(LGItems.FULGURITE));
                // If block drops something remove it in favor of the new item
                if (!event.getDrops().isEmpty())
                {
                    toBeRemoved.addAll(event.getDrops());
                }
                event.getDrops().addAll(toBeAdded);
                event.getDrops().removeAll(toBeRemoved);
            }
        }
    }
}
