package mod.emt.legendgear.network;

import mod.emt.legendgear.LegendGear;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class LGPacketHandler
{
    public static SimpleNetworkWrapper INSTANCE;

    public static void init() {
        int id = 0;
        INSTANCE.registerMessage(PacketEnderBeam.Handler.class, PacketEnderBeam.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(PacketEnderTeleport.Handler.class, PacketEnderTeleport.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(PacketAugmentActivate.Handler.class, PacketAugmentActivate.class, id++, Side.SERVER);
        INSTANCE.registerMessage(PacketAugmentCharge.Handler.class, PacketAugmentCharge.class, id++, Side.SERVER);
        INSTANCE.registerMessage(PacketAugmentState.Handler.class, PacketAugmentState.class, id++, Side.CLIENT);
    }

    static {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(LegendGear.MOD_ID);
    }
}