package mod.emt.legendgear.event;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import mod.emt.legendgear.LegendGear;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID, value = Side.CLIENT)
public class LGTextureStitchEvent
{
    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event)
    {
        // Custom particles with textures go here
    }
}
