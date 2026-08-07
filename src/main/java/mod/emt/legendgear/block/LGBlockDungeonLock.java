package mod.emt.legendgear.block;

import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.item.LGItemDungeonKey;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import java.util.Random;

public class LGBlockDungeonLock extends Block {
    public static final PropertyEnum<LockType> LOCK = PropertyEnum.create("lock", LockType.class);

    public LGBlockDungeonLock()
    {
        super(Material.ROCK);
        this.setBlockUnbreakable();
        this.setResistance(6000000.0F);
        this.setSoundType(SoundType.METAL);
        this.setTickRandomly(true);
        this.setDefaultState(blockState.getBaseState().withProperty(LOCK, LockType.IRON));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LOCK);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(LOCK).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        LockType[] values = LockType.values();
        if (meta < 0 || meta >= values.length)
        {
            meta = 0;
        }
        return getDefaultState().withProperty(LOCK, values[meta]);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase entity, EnumHand hand)
    {
        return getStateFromMeta(meta);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(LOCK).ordinal();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack stack = player.getHeldItem(hand);
        LockType required = state.getValue(LOCK);

        if (required == LockType.UNLOCKING_IRON || required == LockType.UNLOCKING_GOLD || required == LockType.UNLOCKING_DIAMOND
                || required == LockType.UNLOCKING_EMERALD || required == LockType.UNLOCKING_OBSIDIAN || required == LockType.UNLOCKING_BOSS)
        {
            return false;
        }

        if (stack.isEmpty())
        {
            if (!world.isRemote)
            {
                player.sendStatusMessage(new TextComponentTranslation("message.legendgear.locked"), true);
                world.playSound(null, pos, LGSoundEvents.BLOCK_LOCKED.getSoundEvent(), SoundCategory.BLOCKS, 1.0F, 1.5F);
            }

            return true;
        }

        if (!(stack.getItem() instanceof LGItemDungeonKey))
        {
            return false;
        }

        if (!matchesKey(stack, required))
        {
            if (!world.isRemote)
            {
                player.sendStatusMessage(new TextComponentTranslation("message.legendgear.wrong_key"), true);
                world.playSound(null, pos, LGSoundEvents.BLOCK_LOCKED.getSoundEvent(), SoundCategory.BLOCKS, 1.0F, 1.5F);
            }
            return true;
        }

        if (!world.isRemote)
        {
            LockType unlocking;
            switch (required)
            {
                case GOLD:
                    unlocking = LockType.UNLOCKING_GOLD;
                    break;
                case DIAMOND:
                    unlocking = LockType.UNLOCKING_DIAMOND;
                    break;
                case EMERALD:
                    unlocking = LockType.UNLOCKING_EMERALD;
                    break;
                case OBSIDIAN:
                    unlocking = LockType.UNLOCKING_OBSIDIAN;
                    break;
                case BOSS:
                    unlocking = LockType.UNLOCKING_BOSS;
                    break;
                default:
                    unlocking = LockType.UNLOCKING_IRON;
                    break;
            }

            world.setBlockState(pos, state.withProperty(LOCK, unlocking), 3);
            world.scheduleUpdate(pos, this, tickRate(world));
            world.playSound(null, pos, LGSoundEvents.BLOCK_UNLOCK.getSoundEvent(), SoundCategory.BLOCKS, 1.0F, 1.0F);

            if (!player.capabilities.isCreativeMode)
            {
                stack.shrink(1);
            }
        }

        return true;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
    {
        LockType lockType = state.getValue(LOCK);
        if (lockType == LockType.UNLOCKING_IRON || lockType == LockType.UNLOCKING_GOLD || lockType == LockType.UNLOCKING_DIAMOND
                || lockType == LockType.UNLOCKING_EMERALD || lockType == LockType.UNLOCKING_OBSIDIAN || lockType == LockType.UNLOCKING_BOSS)
        {
            return;
        }

        LockType requiredUnlocking;
        switch (lockType)
        {
            case GOLD:
                requiredUnlocking = LockType.UNLOCKING_GOLD;
                break;
            case DIAMOND:
                requiredUnlocking = LockType.UNLOCKING_DIAMOND;
                break;
            case EMERALD:
                requiredUnlocking = LockType.UNLOCKING_EMERALD;
                break;
            case OBSIDIAN:
                requiredUnlocking = LockType.UNLOCKING_OBSIDIAN;
                break;
            case BOSS:
                requiredUnlocking = LockType.UNLOCKING_BOSS;
                break;
            default:
                requiredUnlocking = LockType.UNLOCKING_IRON;
                break;
        }

        for (EnumFacing face : EnumFacing.values())
        {
            BlockPos other = pos.offset(face);
            IBlockState neighbor = world.getBlockState(other);

            if (neighbor.getBlock() != this)
            {
                continue;
            }

            if (neighbor.getValue(LOCK) != requiredUnlocking)
            {
                continue;
            }

            world.setBlockState(pos, state.withProperty(LOCK, requiredUnlocking), 3);
            world.playSound(null, pos, LGSoundEvents.BLOCK_UNLOCK.getSoundEvent(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.scheduleUpdate(pos, this, tickRate(world));
            return;
        }
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        LockType lockType = state.getValue(LOCK);
        if (lockType == LockType.UNLOCKING_IRON || lockType == LockType.UNLOCKING_GOLD || lockType == LockType.UNLOCKING_DIAMOND || lockType == LockType.UNLOCKING_EMERALD
                || lockType == LockType.UNLOCKING_OBSIDIAN || lockType == LockType.UNLOCKING_BOSS)
        {
            world.setBlockToAir(pos);
            world.notifyNeighborsOfStateChange(pos, this, false);
        }
    }

    private boolean matchesKey(ItemStack stack, LockType lock)
    {
        if (!(stack.getItem() instanceof LGItemDungeonKey))
        {
            return false;
        }
        LGItemDungeonKey key = (LGItemDungeonKey) stack.getItem();
        return key.getType(stack) == toKeyType(lock);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this, 1, LockType.IRON.ordinal()));
        items.add(new ItemStack(this, 1, LockType.GOLD.ordinal()));
        items.add(new ItemStack(this, 1, LockType.DIAMOND.ordinal()));
        items.add(new ItemStack(this, 1, LockType.EMERALD.ordinal()));
        items.add(new ItemStack(this, 1, LockType.OBSIDIAN.ordinal()));
        items.add(new ItemStack(this, 1, LockType.BOSS.ordinal()));
    }

    private LGItemDungeonKey.KeyType toKeyType(LockType lockType)
    {
        switch (lockType)
        {
            case GOLD:
                return LGItemDungeonKey.KeyType.GOLD;
            case DIAMOND:
                return LGItemDungeonKey.KeyType.DIAMOND;
            case EMERALD:
                return LGItemDungeonKey.KeyType.EMERALD;
            case OBSIDIAN:
                return LGItemDungeonKey.KeyType.OBSIDIAN;
            case BOSS:
                return LGItemDungeonKey.KeyType.BOSS;
            default:
                return LGItemDungeonKey.KeyType.IRON;
        }
    }

    public enum LockType implements IStringSerializable
    {
        IRON("iron"),
        GOLD("gold"),
        DIAMOND("diamond"),
        EMERALD("emerald"),
        OBSIDIAN("obsidian"),
        BOSS("boss"),
        UNLOCKING_IRON("unlocking_iron"),
        UNLOCKING_GOLD("unlocking_gold"),
        UNLOCKING_DIAMOND("unlocking_diamond"),
        UNLOCKING_EMERALD("unlocking_emerald"),
        UNLOCKING_OBSIDIAN("unlocking_obsidian"),
        UNLOCKING_BOSS("unlocking_boss");

        private final String name;
        LockType(String name)
        {
            this.name = name;
        }

        @Override
        public String getName()
        {
            return name;
        }
    }
}
