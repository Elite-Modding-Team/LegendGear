package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;
import mod.emt.legendgear.init.LGSoundEvents;
import mod.emt.legendgear.util.TooltipHelper;

public class LGItemReedPipes extends Item
{
    public static int[] notes = {0, 3, 7, 9, 12};
    public static int[] altNotes = {-1, 2, 5, 8, 11};

    public static float getNotePitch(int note)
    {
        note += 2;
        return (float) Math.pow(2.0D, (double) (note - 12) / 12.0D);
    }

    public static int getNoteFromSpan(double span, int[] notearray)
    {
        int which = (int) (span * (notearray.length - 1) + 0.5);

        if (which < 0)
        {
            which = 0;
        }

        if (which >= notearray.length)
        {
            which = notearray.length - 1;
        }

        return notearray[which];
    }


    public LGItemReedPipes()
    {
        super();
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        if (!player.isHandActive())
        {
            player.setActiveHand(hand);

            int note = getNoteFromSpan((180 - (player.rotationPitch + 90)) / 180, notes);

            if (player.isSneaking())
            {
                note = getNoteFromSpan((180 - (player.rotationPitch + 90)) / 180, altNotes);
            }

            note++;

            // Mainly for testing purposes ignore the commented out print
            //System.out.println(player.rotationPitch);

            world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_FLUTE_ATTACK.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, getNotePitch(note));
            player.getEntityData().setInteger("flute_note", note);
        }

        return new ActionResult<ItemStack>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase player, int time)
    {
        player.getEntityData().setFloat("flute_note_time", 0);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags)
    {
        TooltipHelper.addWrappedTooltip(tooltip, TextFormatting.GRAY, I18n.format("tooltip.legendgear.reed_pipes"));
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
    {
        float noteTime = player.getEntityData().getFloat("flute_note_time");

        int savednote = player.getEntityData().getInteger("flute_note");
        int anglenote = getNoteFromSpan((180 - (player.rotationPitch + 90)) / 180, notes);

        if (player.isSneaking())
        {
            anglenote = getNoteFromSpan((180 - (player.rotationPitch + 90)) / 180, altNotes);
        }

        anglenote++;

        float pitch = getNotePitch(anglenote);

        if (anglenote != savednote)
        {
            player.getEntityData().setInteger("flute_note", anglenote);
            noteTime = 0;
            player.world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_FLUTE_ATTACK.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, pitch);
        }
        else
        {
            noteTime += pitch;
        }

        if (noteTime >= 5)
        {
            noteTime -= 5;
            player.world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_FLUTE_SUSTAIN.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, pitch);
        }

        player.getEntityData().setFloat("flute_note_time", noteTime);
    }
}
