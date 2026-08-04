package mod.emt.legendgear.network;

import mod.emt.legendgear.LegendGear;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class LGPacketHandler
{
    public static SimpleNetworkWrapper instance;

    public static void init()
    {
        int id = 0;
        instance = NetworkRegistry.INSTANCE.newSimpleChannel(LegendGear.MOD_ID);
        instance.registerMessage(PacketEnderBeam.Handler.class, PacketEnderBeam.class, id++, Side.CLIENT);
        instance.registerMessage(PacketEnderTeleport.Handler.class, PacketEnderTeleport.class, id++, Side.CLIENT);
        instance.registerMessage(PacketAugmentActivate.Handler.class, PacketAugmentActivate.class, id++, Side.SERVER);
        instance.registerMessage(PacketAugmentCharge.Handler.class, PacketAugmentCharge.class, id++, Side.SERVER);
        instance.registerMessage(PacketAugmentState.Handler.class, PacketAugmentState.class, id++, Side.CLIENT);
    }
}