package mod.emt.legendgear.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

import mod.emt.legendgear.LegendGear;

public class LGPacketHandler
{
    public static SimpleNetworkWrapper instance;

    public static void init()
    {
        int id = 0;
        instance = NetworkRegistry.INSTANCE.newSimpleChannel(LegendGear.MOD_ID);

        // Client packets
        if (FMLLaunchHandler.side().isClient())
        {
        }
        // Server packets
        else
        {

        }
    }
}
