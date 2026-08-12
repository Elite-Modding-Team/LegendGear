package mod.emt.legendgear.item;

import mod.emt.legendgear.entity.LGEntitySpellEffect;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.item.base.LGItemBase;
import mod.emt.legendgear.spell.LGSpell;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class LGItemSpellTome extends LGItemBase {
    private final LGSpell spell;

    public LGItemSpellTome(EnumRarity rarity, LGSpell spell) {
        super(rarity, "");
        this.spell = spell;
    }

    public LGSpell getSpell()
    {
        return spell;
    }

    public boolean hideIdleReticle()
    {
        return false;
    }

    @Override
    public int getMaxItemUseDuration(@NotNull ItemStack stack)
    {
        return 65535;
    }

    @Override
    public @NotNull EnumAction getItemUseAction(@NotNull ItemStack stack)
    {
        return EnumAction.BOW;
    }

    @Override
    public @NotNull ActionResult<ItemStack> onItemRightClick(@NotNull World world, EntityPlayer player, @NotNull EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onUsingTick(@NotNull ItemStack stack, @NotNull EntityLivingBase entity, int count)
    {
        if (!(entity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;

        if (player.world.isRemote)
        {
            return;
        }

        int ticks = getMaxItemUseDuration(stack) - count;
        int castTicks = getCastingTicks(stack, player);

        if (ticks == castTicks / 3)
        {
            playChargeSound(player.world, player, 0.5F);
        }

        if (ticks == castTicks * 2 / 3)
        {
            playChargeSound(player.world, player, 0.75F);
        }

        if (ticks == castTicks)
        {
            playChargeSound(player.world, player, 1.0F);
        }
    }

    @Override
    public void onPlayerStoppedUsing(@NotNull ItemStack stack, @NotNull World world, @NotNull EntityLivingBase entity, int timeLeft)
    {
        if (!(entity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;
        int usedTicks = getMaxItemUseDuration(stack) - timeLeft;
        int castTicks = getCastingTicks(stack, player);

        if (usedTicks >= castTicks)
        {
            boolean crit = (usedTicks - castTicks) <= 5;
            castSpell(player, stack, crit);
        }
    }

    protected void castSpell(EntityPlayer player, ItemStack stack, boolean crit)
    {
        double radius = spell.getBaseCastRadius();
        double power = spell.getBaseArcanePower();

        if (crit)
        {
            power += spell.getBaseCritBonus();

            if (!player.world.isRemote)
            {
                playCritSound(player.world, player);
            }
        }

        Vec3d location = getRayTargetResult(player.getPositionEyes(1.0F), player.getLookVec(), spell.getBaseCastRange(), player.world, spell.hitsWater());
        if (!player.world.isRemote)
        {
            cast(player, location, radius, power, crit);
        }
        player.swingArm(EnumHand.MAIN_HAND);
    }

    protected void cast(EntityPlayer caster, Vec3d location, double radius, double power, boolean crit)
    {
        if (!caster.world.isRemote)
        {
            playCastSound(caster.world, location);
            caster.world.spawnEntity(new LGEntitySpellEffect(caster.world, caster, spell, location, radius, power, crit));
        }
    }

    public int getCastingTicks(ItemStack stack, EntityPlayer player)
    {
        return (int)(spell.getBaseCastTime() * 20.0D);
    }

    public float getCastingProgress(ItemStack stack, EntityPlayer player, float partialTicks)
    {
        return Math.min((player.getItemInUseMaxCount() + partialTicks) / (float) getCastingTicks(stack, player), 1.0F);
    }

    public static Vec3d getRayTargetResult(Vec3d from, Vec3d direction, double maxDistance, World world, boolean hitLiquids)
    {
        Vec3d end = from.add(direction.scale(maxDistance));
        RayTraceResult result = world.rayTraceBlocks(from, end, hitLiquids, false, false);
        if (result != null && result.hitVec != null)
        {
            if (result.hitVec.distanceTo(from) < maxDistance)
            {
                return result.hitVec;
            }
        }
        return end;
    }

    public SoundEvent getChargeSound()
    {
        return LGSoundEvents.ITEM_SPELL_CHARGE.getSoundEvent();
    }

    public SoundEvent getCritSound()
    {
        return LGSoundEvents.ITEM_SPELL_CRITICAL.getSoundEvent();
    }

    protected void playChargeSound(World world, EntityPlayer player, float pitch)
    {
        world.playSound(null, player.posX, player.posY, player.posZ, getChargeSound(), SoundCategory.PLAYERS, 0.4F, pitch);
    }

    protected void playCritSound(World world, EntityPlayer player)
    {
        world.playSound(null, player.posX, player.posY, player.posZ, getCritSound(), SoundCategory.PLAYERS, 0.4F, 1.0F);
    }

    protected void playCastSound(World world, Vec3d pos)
    {
        world.playSound(null, pos.x, pos.y, pos.z, spell.getCastSound(), SoundCategory.PLAYERS, 2.0F, 1.0F);
    }
}
