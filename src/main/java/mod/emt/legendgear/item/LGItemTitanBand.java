package mod.emt.legendgear.item;

import java.util.List;

import javax.annotation.Nullable;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

// TODO: Instead of durability and being able to break, it will recharge with extra earth medallions
public class LGItemTitanBand extends Item implements IBauble {
    public LGItemTitanBand() {
        super();
        setMaxStackSize(1);
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entity) {
        int carry = entity.getEntityData().getInteger("carryingTime");
        World world = entity.getEntityWorld();

        if (entity instanceof EntityPlayer) {
            if (entity.isBeingRidden()) {
                ++carry;

                if (world.isRemote) {
                    //LegendGear.proxy.addRuneParticle(par2World, par3Entity.riddenByEntity.posX, par3Entity.riddenByEntity.posY, par3Entity.riddenByEntity.posZ, ItemTitanBand.itemRand.nextGaussian() * 0.1, ItemTitanBand.itemRand.nextGaussian() * 0.1, ItemTitanBand.itemRand.nextGaussian() * 0.1, 1.0f);
                    world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, entity.posX, entity.posY + 2.0D, entity.posZ, world.rand.nextGaussian() * 0.1D, world.rand.nextGaussian() * 0.1D, world.rand.nextGaussian() * 0.1D);
                }

                if ((entity.getPassengers().get(0) instanceof EntityChicken || entity.getPassengers().get(0) instanceof EntityBat) && !entity.onGround && entity.fallDistance > 0.0F) {
                    entity.motionY *= 0.6D;
                    entity.fallDistance = 0.0F;
                }
            } else {
                carry = 0;
            }
        }

        entity.getEntityData().setInteger("carryingTime", carry);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaubleType.TRINKET;
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase player) {
        player.playSound(LGSoundEvents.ITEM_BAUBLE_EQUIP.getSoundEvent(), 1.0F, 1.0F);
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase player) {
        player.playSound(LGSoundEvents.ITEM_BAUBLE_UNEQUIP.getSoundEvent(), 1.0F, 1.0F);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.titan_band"));
    }
}