package mod.emt.legendgear.recipe;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;

import mod.emt.legendgear.util.MedallionAugmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeMedallionAugment extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
    private static final Map<Item, String> MEDALLION_ITEMS = new HashMap<>();
    private static final Set<Item> REMOVAL_ITEMS = new HashSet<>();

    public static void registerAugmentItem(Item item, String key)
    {
        MEDALLION_ITEMS.put(item, key);
    }

    public static void registerRemovalItem(Item item)
    {
        REMOVAL_ITEMS.add(item);
    }

    public RecipeMedallionAugment(String modid)
    {
        setRegistryName(modid, "apply_medallion");
    }

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn)
    {
        boolean foundMedallion = false;
        boolean foundRemoval = false;
        boolean foundTool = false;

        ItemStack foundToolStack = ItemStack.EMPTY;

        for (int index = 0; index < inv.getSizeInventory(); index++)
        {
            ItemStack stack = inv.getStackInSlot(index);

            if (stack.isEmpty())
            {
                continue;
            }

            if (MEDALLION_ITEMS.containsKey(stack.getItem()) && !foundMedallion)
            {
                // Medallion item must not be damaged
                if (stack.getItemDamage() != 0)
                {
                    return false;
                }

                foundMedallion = true;
            }
            else if (REMOVAL_ITEMS.contains(stack.getItem()) && !foundRemoval)
            {
                foundRemoval = true;
            }
            else if (isAcceptableTarget(stack) && !foundTool)
            {
                foundTool = true;
                foundToolStack = stack;
            }
            else
            {
                return false;
            }
        }

        if (!foundTool)
        {
            return false;
        }

        String existing = MedallionAugmentHelper.getAugment(foundToolStack);

        if (foundMedallion)
        {
            return existing == null;
        }

        if (foundRemoval)
        {
            return existing != null;
        }

        return false;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv)
    {
        ItemStack toolToModify = ItemStack.EMPTY;
        String medallionKey = null;
        boolean removing = false;

        for (int index = 0; index < inv.getSizeInventory(); index++)
        {
            ItemStack stack = inv.getStackInSlot(index);

            if (stack.isEmpty())
            {
                continue;
            }

            if (MEDALLION_ITEMS.containsKey(stack.getItem()) && medallionKey == null)
            {
                // Medallion item must not be damaged
                if (stack.getItemDamage() != 0)
                {
                    return ItemStack.EMPTY;
                }

                medallionKey = MEDALLION_ITEMS.get(stack.getItem());
            }
            else if (REMOVAL_ITEMS.contains(stack.getItem()) && !removing)
            {
                removing = true;
            }
            else if (toolToModify.isEmpty() && isAcceptableTarget(stack))
            {
                toolToModify = stack.copy();
            }
            else
            {
                return ItemStack.EMPTY;
            }
        }

        if (toolToModify.isEmpty())
        {
            return ItemStack.EMPTY;
        }

        toolToModify.setCount(1);

        if (removing)
        {
            if (MedallionAugmentHelper.getAugment(toolToModify) == null)
            {
                return ItemStack.EMPTY;
            }

            MedallionAugmentHelper.removeAugment(toolToModify);
            return toolToModify;
        }

        if (medallionKey != null)
        {
            if (MedallionAugmentHelper.getAugment(toolToModify) != null)
            {
                return ItemStack.EMPTY;
            }

            MedallionAugmentHelper.applyAugment(toolToModify, medallionKey);
            return toolToModify;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height)
    {
        return width * height >= 2;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput()
    {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isDynamic()
    {
        return true;
    }

    private boolean isAcceptableTarget(ItemStack stack)
    {
        Item item = stack.getItem();
        return item instanceof ItemTool || item instanceof ItemSword || stack.isItemStackDamageable();
    }
}