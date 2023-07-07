package wildwind.wwutils.mixins;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wildwind.wwutils.optimizations.CopyableSignTexts;

import java.io.IOException;

import static wildwind.wwutils.optimizations.CopyableSignTexts.selectAll;

@Mixin(GuiEditSign.class)
public class MixinGuiEditSign extends GuiScreen {
    /**
     * Copyable sign texts
     */
    @Shadow
    private TileEntitySign tileSign;
    @Shadow
    private int editLine;

    private String StringBeforeEdit;

    @Inject(method = "func_73869_a", at = @At("HEAD"), remap = false, cancellable = true)
    protected void keyTyped(char typedChar, int keyCode, CallbackInfo ci) throws IOException {
        if (GuiScreen.isKeyComboCtrlA(keyCode)) {
            selectAll = true;
            return;
        }
        if (GuiScreen.isKeyComboCtrlC(keyCode) && selectAll) {
            GuiScreen.setClipboardString(tileSign.signText[editLine].getUnformattedText());
            selectAll = false;
            return;
        }
        if (GuiScreen.isKeyComboCtrlX(keyCode) && selectAll) {
            GuiScreen.setClipboardString(tileSign.signText[editLine].getUnformattedText());
            tileSign.signText[editLine] = new ChatComponentText("");
            selectAll = false;
            return;
        }
        if (selectAll && keyCode == 14) {
            selectAll = false;
            tileSign.signText[editLine] = new ChatComponentText("");
        }
        if (GuiScreen.isKeyComboCtrlV(keyCode)) {
            String s = tileSign.signText[editLine].getUnformattedText();
            char[] chars = GuiScreen.getClipboardString().toCharArray();
            if (selectAll) {
                s = "";
                selectAll = false;
            }
            for (char c : chars) {
                if (ChatAllowedCharacters.isAllowedCharacter(c) && this.fontRendererObj.getStringWidth(s + c) <= 90) {
                    s = s + c;
                } else {
                    break;
                }
            }
            tileSign.signText[editLine] = new ChatComponentText(s);
        }
        if (!(GuiScreen.isAltKeyDown() || GuiScreen.isCtrlKeyDown() || GuiScreen.isShiftKeyDown())) {
            selectAll = false;
        }
    }

    @Inject(method = "func_73869_a", at = @At("RETURN"), remap = false, cancellable = true)
    protected void updateLine(char typedChar, int keyCode, CallbackInfo ci) throws IOException {
        CopyableSignTexts.editLine = editLine;
    }

    @Inject(method = "func_146281_b", at = @At("HEAD"), remap = false, cancellable = true)
    public void onGuiClosed(CallbackInfo ci) {
        selectAll = false;
    }
}
