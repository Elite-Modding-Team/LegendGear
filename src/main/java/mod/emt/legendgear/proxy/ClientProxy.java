package mod.emt.legendgear.proxy;

import mod.emt.legendgear.client.render.LGRenderSpellReticle;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
    public void preInit()
    {
        super.preInit();
    }

    public void init()
    {
        super.init();
        MinecraftForge.EVENT_BUS.register(new LGRenderSpellReticle());
    }

    public void postInit()
    {
        super.postInit();
    }
}
