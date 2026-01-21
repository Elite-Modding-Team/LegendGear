package mod.emt.legendgear.item;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class LGItemDimensionalCatalyst extends Item
{
    @Override
    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.UNCOMMON;
    }

    // TODO: Port functionality
    /*public boolean onItemUseFirst(final ItemStack stack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ) {
        final Block block = world.getBlock(x, y, z);
        final int meta = world.getBlockMetadata(x, y, z);
        if (player.capabilities.allowEdit && (block.canHarvestBlock(player, meta) || block == LegendGear2.azuriteOreBlock) && world.canMineBlock(player, x, y, z) && !block.hasTileEntity(meta) && block.getMobilityFlag() == 0 && !world.isRemote) {
            if (block == LegendGear2.azuriteOreBlock) {
                final int dx = EnumFacing.values()[side].getFrontOffsetX();
                final int dy = EnumFacing.values()[side].getFrontOffsetY();
                final int dz = EnumFacing.values()[side].getFrontOffsetZ();
                world.setBlock(x, y, z, Blocks.stone, 0, 3);
                for (int j = 0; j < 3; ++j) {
                    final EntityItem item = new EntityItem(world, x + 0.5 + dx, y + 0.5 + dy, z + 0.5 + dz, new ItemStack((Item)LegendGear2.azurite, 1, 1));
                    world.spawnEntityInWorld((Entity)item);
                }
                world.playSoundEffect((double)x, (double)y, (double)z, "mob.endermen.portal", 1.0f, 1.0f);
                --stack.stackSize;
                player.addStat((StatBase)LegendGear2.achievementAzurite, 1);
                return true;
            }
            for (int i = 0; i < 6; ++i) {
                final int dx2 = world.rand.nextInt(3) - 1;
                final int dy2 = world.rand.nextInt(3) - 1;
                final int dz2 = world.rand.nextInt(3) - 1;
                final int px = dx2 + x;
                final int py = dy2 + y;
                final int pz = dz2 + z;
                if (block.canPlaceBlockAt(world, px, py, pz) && world.isAirBlock(px, py, pz)) {
                    world.setBlock(px, py, pz, block, meta, 3);
                    world.playSoundEffect((double)x, (double)y, (double)z, "mob.endermen.portal", 1.0f, 1.0f);
                    world.setBlockToAir(x, y, z);
                    --stack.stackSize;
                    return true;
                }
            }
        }
        return false;
    }*/
}
