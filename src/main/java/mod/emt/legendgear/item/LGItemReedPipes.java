package mod.emt.legendgear.item;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
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
    private final int[] notes = {0, 3, 7, 9, 12};
    private final int[] altNotes = {-1, 2, 5, 8, 11};

    public LGItemReedPipes()
    {
        super();
        setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        if (!player.isHandActive() && player.hurtTime == 0)
        {
            player.setActiveHand(hand);

            int note = getNoteFromSpan((180 - (player.rotationPitch + 90)) / 180, notes);

            if (player.isSneaking())
            {
                note = getNoteFromSpan((180 - (player.rotationPitch + 90)) / 180, altNotes);
            }

            note++;

            world.playSound(null, player.getPosition(), LGSoundEvents.ITEM_FLUTE_ATTACK.getSoundEvent(), SoundCategory.PLAYERS, 1.0F, getNotePitch(note));
            player.getEntityData().setInteger("flute_note", note);
        }

        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BLOCK;
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
        if (player.hurtTime > 0) return;

        float noteTime = player.getEntityData().getFloat("flute_note_time");

        int savedNote = player.getEntityData().getInteger("flute_note");
        int angleNote = getNoteFromSpan((180 - (player.rotationPitch + 90)) / 180, notes);

        if (player.isSneaking())
        {
            angleNote = getNoteFromSpan((180 - (player.rotationPitch + 90)) / 180, altNotes);
        }

        angleNote++;

        float pitch = getNotePitch(angleNote);

        if (angleNote != savedNote)
        {
            player.getEntityData().setInteger("flute_note", angleNote);
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

    private float getNotePitch(int note)
    {
        note += 2;
        return (float) Math.pow(2.0D, (note - 12) / 12.0D);
    }

    private int getNoteFromSpan(double span, int[] noteArray)
    {
        int which = (int) (span * (noteArray.length - 1) + 0.5);

        if (which < 0)
        {
            which = 0;
        }

        if (which >= noteArray.length)
        {
            which = noteArray.length - 1;
        }

        return noteArray[which];
    }
}
