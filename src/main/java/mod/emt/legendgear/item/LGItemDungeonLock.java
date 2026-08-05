package mod.emt.legendgear.item;

import mod.emt.legendgear.block.LGBlockDungeonLock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class LGItemDungeonLock extends ItemBlock
{
    public LGItemDungeonLock(Block block)
    {
        super(block);

        setHasSubtypes(true);
        setMaxDamage(0);
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        switch (LGBlockDungeonLock.LockType.values()[stack.getMetadata()])
        {
            case IRON:
                return "tile.legendgear.dungeon_lock.iron";
            case GOLD:
                return "tile.legendgear.dungeon_lock.gold";
            case DIAMOND:
                return "tile.legendgear.dungeon_lock.diamond";
            case UNLOCKING_IRON:
                return "tile.legendgear.dungeon_lock.unlocking_iron";
            case UNLOCKING_GOLD:
                return "tile.legendgear.dungeon_lock.unlocking_gold";
            case UNLOCKING_DIAMOND:
                return "tile.legendgear.dungeon_lock.unlocking_diamond";
            default:
                return "tile.legendgear.dungeon_lock";
        }
    }
}