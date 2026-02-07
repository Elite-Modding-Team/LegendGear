package mod.emt.legendgear.item;

import mod.emt.legendgear.client.particle.LGParticleHandler;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import mod.emt.legendgear.block.LGBlockMysticShrub;
import mod.emt.legendgear.init.LGBlocks;

public class LGItemMagicPowder extends Item
{
    public LGItemMagicPowder()
    {
        this.setMaxStackSize(16);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand)
    {
        boolean success = false;

        if (target instanceof EntityAgeable)
        {
            EntityAgeable ageable = (EntityAgeable) target;
            if (!target.world.isRemote)
            {
                if (ageable.isChild())
                {
                    ageable.setGrowingAge(0);
                }
                else
                {
                    ageable.setGrowingAge(-20 * 300);
                }
            }
            else
            {
                spawnParticles(target.world, target.posX, target.posY + target.height / 2, target.posZ);
            }
            success = true;
        }

        if (target instanceof EntitySheep)
        {
            EntitySheep sheep = (EntitySheep) target;
            if (!target.world.isRemote)
            {
                sheep.setFleeceColor(EnumDyeColor.values()[target.world.rand.nextInt(EnumDyeColor.values().length)]);
            }
            success = true;
        }

        if (target instanceof EntityCreeper)
        {
            EntityCreeper creeper = (EntityCreeper) target;
            if (!target.world.isRemote)
            {
                creeper.onStruckByLightning(new EntityLightningBolt(creeper.world, creeper.posX, creeper.posY, creeper.posZ, true));
                creeper.extinguish();
            }
            success = true;
        }

        if (target instanceof EntityZombie)
        {
            EntityZombie zombie = (EntityZombie) target;
            ItemStack helmet = zombie.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (helmet.isEmpty())
            {
                if (!target.world.isRemote)
                {
                    zombie.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Blocks.PUMPKIN));
                    zombie.getEntityData().setBoolean("pumpkined", true);
                }
                success = true;
            }
            else if (zombie.getEntityData().getBoolean("pumpkined") && !zombie.getEntityData().getBoolean("meloned"))
            {
                if (!target.world.isRemote)
                {
                    zombie.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Blocks.MELON_BLOCK));
                    zombie.getEntityData().setBoolean("meloned", true);
                    zombie.getEntityData().setBoolean("pumpkined", false);
                }
                success = true;
            }
            else if (zombie.getEntityData().getBoolean("meloned") && !zombie.getEntityData().getBoolean("pumpkined"))
            {
                if (!target.world.isRemote)
                {
                    zombie.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Blocks.PUMPKIN));
                    zombie.getEntityData().setBoolean("meloned", false);
                    zombie.getEntityData().setBoolean("pumpkined", true);
                }
                success = true;
            }
        }

        if (success)
        {
            if (!player.capabilities.isCreativeMode)
            {
                stack.shrink(1);
            }

            if (target.world.isRemote)
            {
                spawnParticles(target.world, target.posX, target.posY + target.height / 2, target.posZ);
            }
            else
            {
                target.world.playSound(null, target.getPosition(), LGSoundEvents.ITEM_MAGIC_POWDER_SPRINKLE.getSoundEvent(), SoundCategory.AMBIENT, 0.2F, 1.0F);
                target.world.playSound(null, target.getPosition(), LGSoundEvents.ITEM_MAGIC_POWDER_TRANSFORM.getSoundEvent(), SoundCategory.AMBIENT, 1.0F, 0.7F + target.world.rand.nextFloat() * 0.5F);
            }
            return true;
        }

        return super.itemInteractionForEntity(stack, player, target, hand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        int meta = block.getMetaFromState(state);
        boolean success = false;

        if (block == Blocks.BROWN_MUSHROOM)
        {
            world.setBlockState(pos, Blocks.RED_MUSHROOM.getDefaultState());
            success = true;
        }
        else if (block == Blocks.RED_MUSHROOM)
        {
            world.setBlockState(pos, Blocks.BROWN_MUSHROOM.getDefaultState());
            success = true;
        }

        if (block == Blocks.RED_FLOWER)
        {
            world.setBlockState(pos, Blocks.YELLOW_FLOWER.getDefaultState());
            success = true;
        }
        else if (block == Blocks.YELLOW_FLOWER)
        {
            world.setBlockState(pos, Blocks.RED_FLOWER.getDefaultState());
            success = true;
        }

        if (block == Blocks.PUMPKIN)
        {
            world.setBlockState(pos, Blocks.MELON_BLOCK.getDefaultState());
            success = true;
        }
        else if (block == Blocks.MELON_BLOCK)
        {
            world.setBlockState(pos, Blocks.PUMPKIN.getDefaultState());
            success = true;
        }

        if (block == LGBlocks.MYSTIC_SHRUB && meta == 2)
        {
            world.setBlockState(pos, LGBlocks.MYSTIC_SHRUB.getDefaultState().withProperty(LGBlockMysticShrub.TYPE, LGBlockMysticShrub.EnumType.PLAIN));
            success = true;
        }

        if (block == Blocks.STONEBRICK)
        {
            int newMeta = (meta + 1) % 4;
            world.setBlockState(pos, state.withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.byMetadata(newMeta)));
            success = true;
        }

        if (block == Blocks.SANDSTONE)
        {
            int newMeta = (meta + 1) % 3;
            world.setBlockState(pos, state.withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.byMetadata(newMeta)));
            success = true;
        }

        if (block == Blocks.COBBLESTONE)
        {
            world.setBlockState(pos, Blocks.MOSSY_COBBLESTONE.getDefaultState());
            success = true;
        }
        else if (block == Blocks.MOSSY_COBBLESTONE)
        {
            world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
            success = true;
        }

        if (block == Blocks.SAPLING)
        {
            int newMeta = (meta + 1) % 6;
            world.setBlockState(pos, state.withProperty(BlockSapling.TYPE, BlockPlanks.EnumType.byMetadata(newMeta)));
            success = true;
        }

        if (block == Blocks.WOOL)
        {
            int newMeta = (meta + 1) % 16;
            world.setBlockState(pos, state.withProperty(BlockColored.COLOR, EnumDyeColor.byMetadata(newMeta)));
            success = true;
        }

        if (success)
        {
            if (!player.capabilities.isCreativeMode)
            {
                stack.shrink(1);
            }

            if (world.isRemote)
            {
                world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
                for (int i = 0; i < 20; i++)
                {
                    world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, pos.getX() + 0.5 + world.rand.nextGaussian() * 0.5, pos.getY() + 0.5 + world.rand.nextGaussian() * 0.5, pos.getZ() + 0.5 + world.rand.nextGaussian() * 0.5, 0, -0.02, 0);
                }
            }
            else
            {
                world.playSound(null, pos, LGSoundEvents.ITEM_MAGIC_POWDER_SPRINKLE.getSoundEvent(), SoundCategory.AMBIENT, 0.2F, 1.0F);
                world.playSound(null, pos, LGSoundEvents.ITEM_MAGIC_POWDER_TRANSFORM.getSoundEvent(), SoundCategory.AMBIENT, 1.0F, 0.7F + world.rand.nextFloat() * 0.5F);
            }
        }

        return success ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
    }

    private void spawnParticles(World world, double x, double y, double z)
    {
        world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, x, y, z, 0, 0, 0);
        for (int i = 0; i < 20; i++)
        {
            LGParticleHandler.spawnSparkleFX(world, x + world.rand.nextGaussian() * 0.5D, y + world.rand.nextGaussian() * 0.5D, z + world.rand.nextGaussian() * 0.5D, 0.0D, -0.02D, 0.0D, 1.0F);
        }
    }
}
