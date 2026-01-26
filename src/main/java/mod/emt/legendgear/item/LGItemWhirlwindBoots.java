package mod.emt.legendgear.item;

import java.util.List;

import javax.annotation.Nullable;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGSoundEvents;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

// TODO: Instead of durability and being able to break, it will recharge with extra wind medallions
public class LGItemWhirlwindBoots extends ItemArmor
{
    protected static final String TEXTURE = new ResourceLocation(LegendGear.MOD_ID, "textures/models/armor/whirlwind_boots.png").toString();

    public LGItemWhirlwindBoots(ArmorMaterial material, int renderIndex)
    {
        super(material, renderIndex, EntityEquipmentSlot.FEET);
        this.setMaxDamage(6000);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack)
    {
        super.onArmorTick(world, player, stack);

        if (player.isSprinting())
        {
            player.moveRelative(0.0F, 0.0F, 0.3F, 0.15F);
            final int blockY = (int) Math.floor(player.posY - 0.0625D - player.getYOffset());
            final int dashTicks = player.getEntityData().getInteger("dashTicks");

            if (!player.onGround && player.isInWater() && player.motionY <= 0.0D && player.motionY >= -0.2D)
            {
                player.motionY = 0.0D;
                player.onGround = true;
                player.posY = blockY + player.getYOffset() + 1.03D;
                player.fallDistance = 0.0F;
            }

            if (world.rand.nextInt(99) == 0)
            {
                stack.damageItem(1, player);
            }

            if (player.onGround)
            {
                if (world.isRemote)
                {
                    world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, player.posX, player.posY - player.getYOffset(), player.posZ, 0.0D, 0.0D, 0.0D);
                }
                else if (dashTicks % 3 == 0)
                {
                    world.playSound(null, player.getPosition(), LGSoundEvents.RANDOM_DASH.getSoundEvent(), SoundCategory.PLAYERS, 0.2F, 0.9F + world.rand.nextFloat() * 0.2F);
                }
            }

            player.getEntityData().setInteger("dashTicks", dashTicks + 1);
        }
        else
        {
            player.getEntityData().setInteger("dashTicks", 0);
        }
    }

    @Override
    public boolean isDamageable()
    {
        return false;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type)
    {
        return TEXTURE;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.legendgear.whirlwind_boots"));
    }
}
