package mod.emt.legendgear.item;

import mod.emt.legendgear.entity.LGEntityPing;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;


public class LGItemSpottingScope extends Item {
    public LGItemSpottingScope() {
        super();
        setMaxStackSize(1);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 60000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        player.setActiveHand(hand);

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase player, int time) {
        if (!world.isRemote && this.getMaxItemUseDuration(stack) - time <= 5) {
            final float range = 192.0F;
            final Vec3d from = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
            final Vec3d direction = player.getLookVec().normalize();
            final Vec3d scaledDirection = direction.scale(range);
            final Vec3d target = from.add(scaledDirection);
            final RayTraceResult trace = world.rayTraceBlocks(from, target);
            Vec3d hit = null;

            if (trace != null) {
                hit = trace.hitVec;
            }

            if (hit != null) {
                world.spawnEntity(new LGEntityPing(world, hit.x, hit.y, hit.z, 500, 0));
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.spotting_scope"));
    }
}
