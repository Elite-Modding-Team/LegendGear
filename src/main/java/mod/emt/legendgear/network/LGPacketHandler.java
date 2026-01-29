package mod.emt.legendgear.network;

import mod.emt.legendgear.LegendGear;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class LGPacketHandler
{
    public static SimpleNetworkWrapper INSTANCE;

    public static void init()
    {
        int id = 0;
        INSTANCE.registerMessage(LGPacketJump.class, LGPacketJump.class, id++, Side.SERVER);
    }

    static
    {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LegendGear.MOD_ID);
    }
}
