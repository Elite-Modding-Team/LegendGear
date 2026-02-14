package mod.emt.legendgear.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import mod.emt.legendgear.LegendGear;
import mod.emt.legendgear.init.LGItems;

@Mod.EventBusSubscriber(modid = LegendGear.MOD_ID, value = Side.CLIENT)
public class LGSpottingScopeEvent
{
    private static final ResourceLocation VIGNETTE_TEXTURE = new ResourceLocation("textures/misc/vignette.png");
    private static boolean smoothCameraReset;

    @SubscribeEvent
    public static void scopeFOV(FOVUpdateEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = event.getEntity();
        if (player.isHandActive())
        {
            ItemStack stack = player.getHeldItem(player.getActiveHand());
            if (player.getItemInUseCount() != 0 && player.getItemInUseCount() < stack.getMaxItemUseDuration() - 5 && !stack.isEmpty() && stack.getItem() == LGItems.SPOTTING_SCOPE)
            {
                event.setNewfov(event.getFov() * 0.15F);
                if (!mc.gameSettings.smoothCamera)
                {
                    mc.gameSettings.smoothCamera = true;
                    smoothCameraReset = true;
                }
            }
        }
        else if (smoothCameraReset)
        {
            mc.gameSettings.smoothCamera = false;
            smoothCameraReset = false;
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HELMET)
        {
            return;
        }
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        ItemStack stack = player.getHeldItemMainhand();
        if (player.getItemInUseCount() != 0 && player.getItemInUseCount() < stack.getMaxItemUseDuration() - 5 && !stack.isEmpty() && stack.getItem() == LGItems.SPOTTING_SCOPE)
        {
            ScaledResolution scaledResolution = new ScaledResolution(mc);
            int screenWidth = scaledResolution.getScaledWidth();
            int screenHeight = scaledResolution.getScaledHeight();
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(VIGNETTE_TEXTURE);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(0.0D, screenHeight, -90.0D).tex(0.0D, 1.0D).endVertex();
            buffer.pos(screenWidth, screenHeight, -90.0D).tex(1.0D, 1.0D).endVertex();
            buffer.pos(screenWidth, 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
            buffer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }
    }
}
