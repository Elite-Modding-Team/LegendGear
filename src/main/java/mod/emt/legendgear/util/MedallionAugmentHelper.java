package mod.emt.legendgear.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public final class MedallionAugmentHelper
{
    private static final String TAG_AUGMENT = "MedallionAugment";
    private static final String TAG_CHARGE = "Charge";
    private static final String TAG_DATA = "MedallionData";
    private static final String TAG_ENERGY = "Energy";
    private static final String TAG_HOLDING = "HoldingCharge";
    private static final String TAG_PORTAL_TIMER = "PortalTimer";
    private static final String TAG_RECALL_TIME = "RecallTime";
    private static final String TAG_RECALL_X = "RecallX";
    private static final String TAG_RECALL_Y = "RecallY";
    private static final String TAG_RECALL_Z = "RecallZ";
    private static final String TAG_STATE = "State";

    private final NBTTagCompound tag;

    public enum State
    {
        IDLE,
        CHARGING,
        CHARGED
    }

    private MedallionAugmentHelper(NBTTagCompound tag)
    {
        this.tag = tag;
    }

    /**
     * Applies a medallion augment to a tool.
     */
    public static void applyAugment(ItemStack tool, String medallionKey) {
        if (!tool.hasTagCompound())
        {
            tool.setTagCompound(new NBTTagCompound());
        }

        tool.getTagCompound().setString(TAG_AUGMENT, medallionKey);
    }

    /**
     * Returns the medallion augment stored on a tool.
     */
    public static String getAugment(ItemStack tool) {
        if (tool.hasTagCompound() && tool.getTagCompound().hasKey(TAG_AUGMENT))
        {
            return tool.getTagCompound().getString(TAG_AUGMENT);
        }

        return null;
    }

    public static void removeAugment(ItemStack tool)
    {
        if (!tool.hasTagCompound())
        {
            return;
        }

        NBTTagCompound tag = tool.getTagCompound();
        tag.removeTag(TAG_AUGMENT);

        if (tag.getKeySet().isEmpty())
        {
            tool.setTagCompound(null);
        }
    }

    public static MedallionAugmentHelper getPlayer(EntityPlayer player)
    {
        NBTTagCompound data = player.getEntityData();

        if (!data.hasKey(TAG_DATA))
        {
            data.setTag(TAG_DATA, new NBTTagCompound());
        }

        return new MedallionAugmentHelper(data.getCompoundTag(TAG_DATA));
    }

    public void beginCharging()
    {
        setState(State.CHARGING);
        setCharge(0);
        setEnergy(0);
        setHoldingCharge(false);
    }

    public void becomeCharged(int activationWindow)
    {
        setState(State.CHARGED);
        setCharge(0);
        setEnergy(activationWindow);
        setHoldingCharge(true);
    }

    public void beginRecall(EntityPlayer player)
    {
        setRecallX(player.posX);
        setRecallY(player.posY);
        setRecallZ(player.posZ);
        setRecallTime(50);
    }

    public void clearRecall()
    {
        setRecallTime(0);
        setRecallX(0.0D);
        setRecallY(0.0D);
        setRecallZ(0.0D);
    }


    public void reset()
    {
        setState(State.IDLE);
        setCharge(0);
        setEnergy(0);
        setHoldingCharge(false);
    }

    public boolean isCharging()
    {
        return getState() == State.CHARGING;
    }

    public boolean isCharged()
    {
        return getState() == State.CHARGED;
    }

    public boolean isHoldingCharge()
    {
        return tag.getBoolean(TAG_HOLDING);
    }

    public boolean isIdle()
    {
        return getState() == State.IDLE;
    }

    public void decrementEnergy()
    {
        if (getEnergy() > 0)
        {
            setEnergy(getEnergy() - 1);
        }
    }

    public double getCharge()
    {
        return tag.getDouble(TAG_CHARGE);
    }

    public int getEnergy()
    {
        return tag.getInteger(TAG_ENERGY);
    }

    public int getPortalTimer()
    {
        return tag.getInteger(TAG_PORTAL_TIMER);
    }

    public int getRecallTime()
    {
        return tag.getInteger(TAG_RECALL_TIME);
    }

    public double getRecallX()
    {
        return tag.getDouble(TAG_RECALL_X);
    }

    public double getRecallY()
    {
        return tag.getDouble(TAG_RECALL_Y);
    }

    public double getRecallZ()
    {
        return tag.getDouble(TAG_RECALL_Z);
    }

    public State getState()
    {
        int ordinal = tag.getInteger(TAG_STATE);

        if (ordinal < 0 || ordinal >= State.values().length)
        {
            return State.IDLE;
        }

        return State.values()[ordinal];
    }

    public void incrementCharge(double amount)
    {
        setCharge(getCharge() + amount);
    }

    public void setCharge(double charge)
    {
        tag.setDouble(TAG_CHARGE, charge);
    }

    public void setCharging(boolean charging)
    {
        if (charging)
        {
            if (isIdle()) beginCharging();
        } else
        {
            reset();
        }
    }

    public void setEnergy(int energy)
    {
        tag.setInteger(TAG_ENERGY, energy);
    }

    public void setHoldingCharge(boolean holding)
    {
        tag.setBoolean(TAG_HOLDING, holding);
    }

    public void setPortalTimer(int ticks)
    {
        tag.setInteger(TAG_PORTAL_TIMER, ticks);
    }

    public void setRecallTime(int time)
    {
        tag.setInteger(TAG_RECALL_TIME, time);
    }

    public void setRecallX(double x)
    {
        tag.setDouble(TAG_RECALL_X, x);
    }

    public void setRecallY(double y)
    {
        tag.setDouble(TAG_RECALL_Y, y);
    }

    public void setRecallZ(double z)
    {
        tag.setDouble(TAG_RECALL_Z, z);
    }

    public void setState(State state)
    {
        tag.setInteger(TAG_STATE, state.ordinal());
    }
}