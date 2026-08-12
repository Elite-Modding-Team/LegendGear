package mod.emt.legendgear.client.render;

import mod.emt.legendgear.entity.LGEntitySpellDecorator;
import mod.emt.legendgear.item.LGItemSpellTome;
import mod.emt.legendgear.spell.LGSpell;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class LGRenderSpellReticle {
    public static void drawHorizontalRing(double x, double y, double z, double radius, int segments) {
        drawHorizontalRing(x, y, z, radius, segments, 0.0F);
    }

    public static void drawHorizontalRing(double x, double y, double z, double radius, int segments, float twist) {
        drawHorizontalRing(x, y, z, radius, segments, twist, 1);
    }

    public static void drawHorizontalRing(double x, double y, double z, double radius, int segments, float twist, int stride) {
        GlStateManager.glBegin(GL11.GL_LINE_LOOP);

        for (int i = 0; i < segments; ++i) {
            double theta = (Math.PI * 2D) / (double)segments * (double)(i * stride) + (Math.PI * 2D) * ((double)twist + 0.125D);
            double dx = Math.cos(theta) * radius;
            double dz = Math.sin(theta) * radius;
            GL11.glVertex3d(x + dx, y, z + dz);
        }

        GlStateManager.glEnd();
    }

    private static void billboardTransform()
    {
        Minecraft mc = Minecraft.getMinecraft();
        RenderManager rm = mc.getRenderManager();
        GlStateManager.rotate(180.0F - rm.playerViewY, 0.0F, 1.0F, 0.0F);
        if (mc.gameSettings.thirdPersonView == 2)
        {
            GlStateManager.rotate(rm.playerViewX, 1.0F, 0.0F, 0.0F);
        }
        else
        {
            GlStateManager.rotate(-rm.playerViewX, 1.0F, 0.0F, 0.0F);
        }
    }

    public static void drawFancyReticle(EntityPlayer player, Vec3d pos, double radius, float progress, boolean tint) {
        float phase = (float)(Minecraft.getSystemTime() % 1000L) / 1000.0F;
        GlStateManager.alphaFunc(516, 0.02F);
        GlStateManager.glLineWidth(1.0F);
        GlStateManager.depthFunc(515);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 64.0F / 255.0F);
        drawHorizontalRing(pos.x, pos.y + radius * 0.866D, pos.z, radius / 2.0D, 8, 0.0F, 5);
        drawHorizontalRing(pos.x, pos.y - radius * 0.866D, pos.z, radius / 2.0D, 8, 0.0F, 5);

        GlStateManager.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(pos.x, pos.y, pos.z);
        GL11.glVertex3d(pos.x, pos.y - radius, pos.z);
        GlStateManager.glEnd();

        if (!player.isHandActive()) {
            GlStateManager.glLineWidth(2.0F);
            GlStateManager.depthFunc(515);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 128.0F / 255.0F);
            if (tint) {
                GlStateManager.color(1.0F, 1.0F, 128.0F / 255.0F, 128.0F / 255.0F);
            }

            drawHorizontalRing(pos.x, pos.y, pos.z, radius, 8);
            GlStateManager.depthFunc(516);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 32.0F / 255.0F);
            if (tint) {
                GlStateManager.color(1.0F, 1.0F, 128.0F / 255.0F, 32.0F / 255.0F);
            }

            drawHorizontalRing(pos.x, pos.y, pos.z, radius, 8);
        } else if (progress < 1.0F) {
            GlStateManager.glLineWidth(3.0F);
            GlStateManager.depthFunc(515);
            GlStateManager.color(1.0F, progress, 0.0F, 1.0F);
            drawHorizontalRing(pos.x, pos.y, pos.z, radius, 8);
            GlStateManager.depthFunc(516);
            GlStateManager.color(1.0F, progress, 0.0F, 0.3F);
            drawHorizontalRing(pos.x, pos.y, pos.z, radius, 8);
            GlStateManager.glLineWidth(2.0F);
            GlStateManager.depthFunc(515);
            GlStateManager.color(1.0F, progress, 0.0F, 1.0F);
            drawHorizontalRing(pos.x, pos.y, pos.z, radius * (double)progress, 8, -progress);
            GlStateManager.depthFunc(516);
            GlStateManager.color(1.0F, progress, 0.0F, 0.3F);
            drawHorizontalRing(pos.x, pos.y, pos.z, radius * (double)progress, 8, -progress);

            double flux1 = 1.0D - Math.cos((double)progress * Math.PI / 2.0D);
            double flux2 = 1.0D - Math.cos((double)progress * Math.PI * 3.0D / 2.0D);
            double flux3 = 1.0D - Math.cos((double)progress * Math.PI * 5.0D / 2.0D);

            GlStateManager.glLineWidth(2.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F * progress);
            GlStateManager.depthFunc(515);
            drawHorizontalRing(pos.x, pos.y, pos.z, radius * flux1, 8, progress / 2.0F, 5);
            drawHorizontalRing(pos.x, pos.y, pos.z, radius * flux2, 8, progress, 5);
            drawHorizontalRing(pos.x, pos.y, pos.z, radius * flux3, 8, -progress / 2.0F, 5);
        } else {
            phase = (float)Math.sin((double)phase * Math.PI * 2.0D * 10.0D) / 2.0F + 0.5F;
            GlStateManager.glLineWidth(3.0F);
            GlStateManager.depthFunc(515);
            GlStateManager.color(1.0F, phase * 0.5F + 0.5F, (1.0F - phase) * 0.5F + 0.5F, 1.0F);
            drawHorizontalRing(pos.x, pos.y, pos.z, radius, 8);
            GlStateManager.glLineWidth(2.0F);
            drawHorizontalRing(pos.x, pos.y, pos.z, radius, 8, 0.0F, 5);
            GlStateManager.depthFunc(516);
            GlStateManager.color(1.0F, phase * 0.5F + 0.5F, (1.0F - phase) * 0.5F + 0.5F, 0.3F);
            GlStateManager.glLineWidth(3.0F);
            drawHorizontalRing(pos.x, pos.y, pos.z, radius, 8);
        }
    }

    @SubscribeEvent
    public void renderWorldLastEvent(RenderWorldLastEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;

        if (player == null)
        {
            return;
        }

        double playerX = player.prevPosX + (player.posX - player.prevPosX) * event.getPartialTicks();
        double playerY = player.prevPosY + (player.posY - player.prevPosY) * event.getPartialTicks();
        double playerZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.getPartialTicks();

        ItemStack weapon = player.getHeldItemMainhand();
        if (!weapon.isEmpty() && weapon.getItem() instanceof LGItemSpellTome)
        {
            LGItemSpellTome tome = (LGItemSpellTome) weapon.getItem();
            LGSpell spell = tome.getSpell();

            if (!(tome.hideIdleReticle() && !player.isHandActive()))
            {
                Vec3d playerLook = player.getLookVec();
                Vec3d playerEye = player.getPositionEyes(event.getPartialTicks());
                double castRange = spell.getBaseCastRange();
                double castRadius = spell.getBaseCastRadius();
                boolean rayHit = false;
                Vec3d targetPos = LGItemSpellTome.getRayTargetResult(playerEye, playerLook, castRange, player.world, spell.hitsWater());

                if (targetPos.distanceTo(playerEye) < castRange)
                {
                    rayHit = true;
                }

                float retreat = -0.015625F;
                targetPos = targetPos.add(playerLook.x * retreat, playerLook.y * retreat, playerLook.z * retreat);
                GlStateManager.pushMatrix();
                GlStateManager.translate(-playerX, -playerY, -playerZ);
                GlStateManager.disableLighting();
                GlStateManager.disableTexture2D();
                GlStateManager.depthMask(false);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                drawFancyReticle(player, targetPos, castRadius, tome.getCastingProgress(weapon, player, event.getPartialTicks()), rayHit);
                GlStateManager.depthFunc(GL11.GL_LEQUAL);
                GlStateManager.enableTexture2D();
                GlStateManager.enableLighting();
                GlStateManager.depthMask(true);
                GlStateManager.popMatrix();
            }
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(-playerX, -playerY, -playerZ);
        GlStateManager.disableLighting();
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.enableBlend();

        for (Entity entity : player.world.loadedEntityList)
        {
            if (!(entity instanceof LGEntitySpellDecorator))
            {
                continue;
            }

            LGEntitySpellDecorator dec = (LGEntitySpellDecorator) entity;
            if (dec.isCrit) {
                float burstTime = (float) dec.age + event.getPartialTicks();
                float phase = burstTime / 10.0F;
                if (phase <= 1.0F) {
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F - phase);
                    GlStateManager.glLineWidth(2.0F);
                    double x = dec.prevPosX + (dec.posX - dec.prevPosX) * event.getPartialTicks();
                    double y = dec.prevPosY + (dec.posY - dec.prevPosY) * event.getPartialTicks();
                    double z = dec.prevPosZ + (dec.posZ - dec.prevPosZ) * event.getPartialTicks();
                    LGRenderSpellReticle.drawHorizontalRing(x, y + phase * 0.3D, z, dec.radius, 8);
                    LGRenderSpellReticle.drawHorizontalRing(x, y - phase * 0.3D, z, dec.radius, 8);
                    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                }
            }
        }

        GlStateManager.disableBlend();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    public static void drawPinchDiamond(double phase, double size, double tilt) {
        GlStateManager.pushMatrix();
        billboardTransform();
        double sy = phase * phase;
        double sx = (1.0D - phase);
        GlStateManager.scale(size, size, size);
        GlStateManager.rotate((float)tilt, 0.0F, 0.0F, 1.0F);
        GlStateManager.glBegin(GL11.GL_QUADS);
        GL11.glVertex3d(0.0D, sy, 0.0D);
        GL11.glVertex3d(-sx, 0.0D, 0.0D);
        GL11.glVertex3d(0.0D, -sy, 0.0D);
        GL11.glVertex3d(sx, 0.0D, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
    }

    public static void drawDiamondCrescent(double phase, double size, double tilt) {
        GlStateManager.pushMatrix();
        billboardTransform();
        double top = 1.0D;
        if (phase < 0.5D)
        {
            top = -Math.cos(phase * 2.0D * Math.PI);
        }

        double bottom = -1.0D;
        if (phase > 0.5D)
        {
            bottom = -Math.cos((phase - 0.5D) * 2.0D * Math.PI);
        }

        GlStateManager.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex3d(0.0D, bottom * size, 0.0D);
        GL11.glVertex3d(size, 0.0D, 0.0D);
        GL11.glVertex3d(0.0D, top * size, 0.0D);
        GL11.glVertex3d(0.0D, top * size, 0.0D);
        GL11.glVertex3d(-size, 0.0D, 0.0D);
        GL11.glVertex3d(0.0D, bottom * size, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
    }

    public static void drawCross(int spikes, double size, double tilt) {
        GlStateManager.pushMatrix();
        billboardTransform();
        GlStateManager.glBegin(GL11.GL_LINES);
        double dTh = Math.PI / (double)spikes;

        for (int i = 0; i < spikes; ++i)
        {
            double theta = (double)i * dTh + tilt;
            double x = Math.cos(theta) * size;
            double y = Math.sin(theta) * size;
            GL11.glVertex3d(x, y, 0.0D);
            GL11.glVertex3d(-x, -y, 0.0D);
        }

        GlStateManager.glEnd();
        GlStateManager.popMatrix();
    }

    public static void drawPolySolid(int spikes, double size, double tilt) {
        GlStateManager.pushMatrix();
        billboardTransform();
        GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex3d(0.0D, 0.0D, 0.0D);
        double dTh = (Math.PI * 2D) / (double)spikes;
        for (int i = 0; i <= spikes; i++)
        {
            double theta = i * dTh + tilt;
            GL11.glVertex3d(Math.cos(theta) * size, Math.sin(theta) * size, 0.0D);
        }
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
    }

    public static void drawPolyOutline(int spikes, double size, double tilt) {
        GlStateManager.pushMatrix();
        billboardTransform();
        GlStateManager.glBegin(GL11.GL_LINE_LOOP);
        double dTh = (Math.PI * 2D) / (double)spikes;
        for (int i = 0; i < spikes; ++i) {
            double theta = (double)i * dTh + tilt;
            double x = Math.cos(theta) * size;
            double y = Math.sin(theta) * size;
            GL11.glVertex3d(x, y, 0.0D);
        }
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
    }

    public static void drawStar(int spikes, double tightness, double size, double tilt) {
        GlStateManager.pushMatrix();
        billboardTransform();
        GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex3d(0.0D, 0.0D, 0.0D);
        double dTh = (Math.PI * 2D) / (double)spikes;
        for (int i = 0; i < spikes; ++i) {
            double theta = (double)i * dTh + tilt;
            double x = Math.cos(theta) * size;
            double y = Math.sin(theta) * size;
            GL11.glVertex3d(x, y, 0.0D);
            theta += dTh * 0.5D;
            x = Math.cos(theta) * size * tightness;
            y = Math.sin(theta) * size * tightness;
            GL11.glVertex3d(x, y, 0.0D);
        }
        double x = Math.cos(tilt) * size;
        double y = Math.sin(tilt) * size;
        GL11.glVertex3d(x, y, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
    }

    public static void drawScythe(double phase, double size, double tilt) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(size, size, size);
        GlStateManager.rotate((float)tilt, 0.0F, 1.0F, 0.0F);
        GlStateManager.disableCull();
        GlStateManager.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex3d(1.0D, 0.0D, 0.0D);
        GL11.glVertex3d(0.7D, 0.0D, 0.7D);
        GL11.glVertex3d(1.0D - phase * 0.3D, 0.0D, 0.0D);
        GL11.glVertex3d(1.0D - phase * 0.3D, 0.0D, 0.0D);
        GL11.glVertex3d(0.7D, 0.0D, -0.7D);
        GL11.glVertex3d(1.0D, 0.0D, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    public static void diamondZig(double w, double h, double phase) {
        GlStateManager.pushMatrix();
        billboardTransform();
        GlStateManager.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3d(0.0D, (1.0D - Math.abs(phase)) * h, 0.0D);
        GL11.glVertex3d(-phase * w, 0.0D, 0.0D);
        GL11.glVertex3d(phase * w, 0.0D, 0.0D);
        GL11.glVertex3d(0.0D, -(1.0D - Math.abs(phase)) * h, 0.0D);
        GlStateManager.glEnd();
        GlStateManager.popMatrix();
    }
}