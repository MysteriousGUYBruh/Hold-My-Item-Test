package com.holdmyitem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class HandRenderHandler {

    @SubscribeEvent
    public void onRenderHand(RenderSpecificHandEvent event) {
        if (!HoldConfig.enabled) return;

        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        ItemStack stack = event.getItemStack();
        EnumHand hand = event.getHand();

        // Let vanilla handle empty hands and maps (it already draws arms there).
        if (stack.isEmpty() || stack.getItem() instanceof ItemMap) return;
        if (HoldConfig.hideWhileUsing && player.isHandActive() && player.getActiveHand() == hand) return;

        EnumHandSide side = hand == EnumHand.MAIN_HAND ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
        boolean right = side == EnumHandSide.RIGHT;
        float sign = right ? 1.0F : -1.0F;
        float swing = event.getSwingProgress();
        float equip = event.getEquipProgress();

        event.setCanceled(true);

        // ---------- Item (same transforms vanilla uses) ----------
        GlStateManager.pushMatrix();
        float sx = -0.4F * MathHelper.sin(MathHelper.sqrt(swing) * (float) Math.PI);
        float sy = 0.2F * MathHelper.sin(MathHelper.sqrt(swing) * (float) Math.PI * 2.0F);
        float sz = -0.2F * MathHelper.sin(swing * (float) Math.PI);
        GlStateManager.translate(sign * sx, sy, sz);
        GlStateManager.translate(sign * 0.56F, -0.52F + equip * -0.6F, -0.72F);
        float f = MathHelper.sin(swing * swing * (float) Math.PI);
        GlStateManager.rotate(sign * (45.0F + f * -20.0F), 0.0F, 1.0F, 0.0F);
        float f1 = MathHelper.sin(MathHelper.sqrt(swing) * (float) Math.PI);
        GlStateManager.rotate(sign * f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sign * -45.0F, 0.0F, 1.0F, 0.0F);
        applyContext(player, event.getPartialTicks(), sign);
        mc.getItemRenderer().renderItemSide(player, stack,
                right ? TransformType.FIRST_PERSON_RIGHT_HAND : TransformType.FIRST_PERSON_LEFT_HAND, !right);
        GlStateManager.popMatrix();

        // ---------- Arm (same transforms vanilla uses for an empty hand) ----------
        GlStateManager.pushMatrix();
        float r = MathHelper.sqrt(swing);
        float ax = -0.3F * MathHelper.sin(r * (float) Math.PI);
        float ay = 0.4F * MathHelper.sin(r * (float) Math.PI * 2.0F);
        float az = -0.4F * MathHelper.sin(swing * (float) Math.PI);
        GlStateManager.translate(sign * (ax + 0.64000005F), ay - 0.6F + equip * -0.6F, az - 0.71999997F);
        GlStateManager.rotate(sign * 45.0F, 0.0F, 1.0F, 0.0F);
        float a5 = MathHelper.sin(swing * swing * (float) Math.PI);
        float a6 = MathHelper.sin(r * (float) Math.PI);
        GlStateManager.rotate(sign * a6 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(sign * a5 * -20.0F, 0.0F, 0.0F, 1.0F);
        mc.getTextureManager().bindTexture(player.getLocationSkin());
        GlStateManager.translate(sign * -1.0F, 3.6F, 3.5F);
        GlStateManager.rotate(sign * 120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(sign * -135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(sign * 5.6F, 0.0F, 0.0F);
        // user offsets, applied in arm-model space
        GlStateManager.translate(sign * HoldConfig.armX, HoldConfig.armY, HoldConfig.armZ);
        RenderPlayer rp = (RenderPlayer) mc.getRenderManager().getEntityRenderObject(player);
        GlStateManager.disableCull();
        if (right) rp.renderRightArm(player); else rp.renderLeftArm(player);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

    /** Small contextual tweaks: ladder climbing, swimming, elytra. */
    private void applyContext(EntityPlayerSP p, float pt, float sign) {
        if (!HoldConfig.contextualAnimations) return;
        float t = (p.ticksExisted + pt);
        if (p.isOnLadder() && p.motionY != 0.0D && !p.onGround) {
            GlStateManager.translate(0.0F, 0.06F * MathHelper.sin(t * 0.5F * sign), 0.0F);
            GlStateManager.rotate(8.0F, 1.0F, 0.0F, 0.0F);
        } else if (p.isInWater() && p.isSprinting()) {
            GlStateManager.translate(0.0F, 0.04F * MathHelper.sin(t * 0.4F + (sign > 0 ? 0 : 3.14F)), 0.0F);
            GlStateManager.rotate(10.0F * MathHelper.sin(t * 0.4F + (sign > 0 ? 0 : 3.14F)), 1.0F, 0.0F, 0.0F);
        } else if (p.isElytraFlying()) {
            GlStateManager.translate(0.0F, -0.05F, 0.0F);
        }
    }
}
