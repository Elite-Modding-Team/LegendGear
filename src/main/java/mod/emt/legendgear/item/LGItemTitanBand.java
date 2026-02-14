package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import java.util.List;
import mod.emt.legendgear.client.particle.LGParticleHandler;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.util.TooltipHelper;

// TODO: Instead of durability and being able to break, it will recharge with extra earth medallions
// TODO: Currently carried mobs sit right on top of the player unlike 1.5.2 where they are floating. Maybe there is another way to get around this?
public class LGItemTitanBand extends Item implements IBauble
{
    public LGItemTitanBand()
    {
        super();
        setMaxStackSize(1);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        TooltipHelper.addWrappedTooltip(tooltip, TextFormatting.GRAY, I18n.format("tooltip.legendgear.titan_band"));
    }

    @Override
    public BaubleType getBaubleType(ItemStack itemStack)
    {
        return BaubleType.TRINKET;
    }

    @Override
    public void onWornTick(ItemStack stack, EntityLivingBase entity)
    {
        int carry = entity.getEntityData().getInteger("carryingTime");
        World world = entity.getEntityWorld();

        if (entity instanceof EntityPlayer)
        {
            if (entity.isBeingRidden())
            {
                ++carry;

                if (FMLLaunchHandler.side().isClient() && world.isRemote)
                {
                    // TODO: Particle position varies depending on mob currently
                    LGParticleHandler.spawnMagicRuneFX(world, entity.posX, entity.posY + 2.0D, entity.posZ, world.rand.nextGaussian() * 0.1D, world.rand.nextGaussian() * 0.1D, world.rand.nextGaussian() * 0.1D, 1.0F);
                }

                if ((entity.getPassengers().get(0) instanceof EntityChicken || entity.getPassengers().get(0) instanceof EntityBat) && !entity.onGround && entity.fallDistance > 0.0F)
                {
                    entity.motionY *= 0.6D;
                    entity.fallDistance = 0.0F;
                }
            }
            else
            {
                carry = 0;
            }
        }

        entity.getEntityData().setInteger("carryingTime", carry);
    }

    @Override
    public void onEquipped(ItemStack stack, EntityLivingBase player)
    {
        player.playSound(LGSoundEvents.ITEM_BAUBLE_EQUIP.getSoundEvent(), 1.0F, 1.0F);
    }

    @Override
    public void onUnequipped(ItemStack stack, EntityLivingBase player)
    {
        player.playSound(LGSoundEvents.ITEM_BAUBLE_UNEQUIP.getSoundEvent(), 1.0F, 1.0F);
    }
}