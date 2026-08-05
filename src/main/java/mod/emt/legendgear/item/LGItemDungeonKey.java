package mod.emt.legendgear.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class LGItemDungeonKey extends Item {
    public LGItemDungeonKey()
    {
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
        switch (KeyType.byMeta(stack.getMetadata()))
        {
            case GOLD:
                return super.getTranslationKey() + ".gold";
            case DIAMOND:
                return super.getTranslationKey() + ".diamond";
            case BOSS:
                return super.getTranslationKey() + ".boss";
            default:
                return super.getTranslationKey() + ".iron";
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (!isInCreativeTab(tab))
        {
            return;
        }

        for (KeyType type : KeyType.values())
        {
            items.add(new ItemStack(this, 1, type.ordinal()));
        }
    }

    public KeyType getType(ItemStack stack)
    {
        return KeyType.byMeta(stack.getMetadata());
    }

    public enum KeyType
    {
        IRON("iron"),
        GOLD("gold"),
        DIAMOND("diamond"),
        BOSS("boss");

        private final String name;

        KeyType(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return name;
        }

        public static KeyType byMeta(int meta)
        {
            KeyType[] values = values();

            if (meta < 0 || meta >= values.length)
            {
                return IRON;
            }

            return values[meta];
        }
    }
}
