package wildwind.wwutils.mixins;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wildwind.wwutils.optimizations.CopyableSignTexts;

import java.util.List;

@Mixin(TileEntitySignRenderer.class)
public abstract class MixinTileEntitySignRenderer extends TileEntitySpecialRenderer<TileEntitySign> {
    /**
     * Copyable sign texts
     */
    @Shadow
    private static final ResourceLocation SIGN_TEXTURE = new ResourceLocation("textures/entity/sign.png");
    @Shadow
    private final ModelSign model = new ModelSign();

    @Inject(method = "renderTileEntityAt", at = @At("HEAD"), cancellable = true)
    public void renderTileEntityAt(TileEntitySign te, double x, double y, double z, float partialTicks, int destroyStage, CallbackInfo ci) {
        Block block = te.getBlockType();
        GlStateManager.pushMatrix();
        float f = 0.6666667F;

        if (block == Blocks.standing_sign) {
            GlStateManager.translate((float) x + 0.5F, (float) y + 0.75F * f, (float) z + 0.5F);
            float f1 = (float) (te.getBlockMetadata() * 360) / 16.0F;
            GlStateManager.rotate(-f1, 0.0F, 1.0F, 0.0F);
            this.model.signStick.showModel = true;
        } else {
            int k = te.getBlockMetadata();
            float f2 = 0.0F;

            if (k == 2) {
                f2 = 180.0F;
            }

            if (k == 4) {
                f2 = 90.0F;
            }

            if (k == 5) {
                f2 = -90.0F;
            }

            GlStateManager.translate((float) x + 0.5F, (float) y + 0.75F * f, (float) z + 0.5F);
            GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.3125F, -0.4375F);
            this.model.signStick.showModel = false;
        }

        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 2.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
            this.bindTexture(SIGN_TEXTURE);
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.scale(f, -f, -f);
        this.model.renderSign();
        GlStateManager.popMatrix();
        FontRenderer fontrenderer = this.getFontRenderer();
        float f3 = 0.015625F * f;
        GlStateManager.translate(0.0F, 0.5F * f, 0.07F * f);
        GlStateManager.scale(f3, -f3, f3);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F * f3);
        GlStateManager.depthMask(false);
        int i = 0;

        if (destroyStage < 0) {
            for (int j = 0; j < te.signText.length; ++j) {
                if (te.signText[j] != null) {
                    IChatComponent ichatcomponent = te.signText[j];
                    List<IChatComponent> list = GuiUtilRenderComponents.splitText(ichatcomponent, 90, fontrenderer, false, true);
                    String s = list.size() > 0 ? ((IChatComponent) list.get(0)).getFormattedText() : "";
                    //Edit starts here

                    s = s.replaceAll("§r","");
                    int stringY = j * 10 - te.signText.length * 5;
                    if (CopyableSignTexts.selectAll && j == CopyableSignTexts.editLine && !s.equals("")) {
                        GuiScreen.drawRect(-fontrenderer.getStringWidth(s) / 2 - 1, stringY - 1, fontrenderer.getStringWidth(s) / 2 + 1, stringY + fontrenderer.FONT_HEIGHT - 1, 0xFF3399FF);
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, stringY, 0xFFFFFFFF);
                    }else{
                        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, stringY, i);
                    }
                    if (j == te.lineBeingEdited) {
                        fontrenderer.drawString("> ", -fontrenderer.getStringWidth(s) / 2 - fontrenderer.getStringWidth("> "), stringY, i);
                        fontrenderer.drawString(" <", fontrenderer.getStringWidth(s) / 2, stringY, i);
                    }

                    //Edit ends here
                }
            }
        }

        GlStateManager.depthMask(true);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();

        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
        ci.cancel();
    }
}
