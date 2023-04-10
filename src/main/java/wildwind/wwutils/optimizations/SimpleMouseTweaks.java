package wildwind.wwutils.optimizations;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class SimpleMouseTweaks {
    public SimpleMouseTweaks() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderTick(final TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }
        Minecraft minecraft = Minecraft.getMinecraft();
        if (!(minecraft.currentScreen instanceof GuiContainer)) {
            return;
        }
        GuiContainer guiContainer = (GuiContainer) minecraft.currentScreen;
        if (Mouse.isButtonDown(0) && Keyboard.isKeyDown(42)) {
            Slot currentSlot = guiContainer.getSlotUnderMouse();
            if (currentSlot == null) {
                return;
            }
            minecraft.playerController.windowClick(guiContainer.inventorySlots.windowId,
                    currentSlot.slotNumber, 0, 1, minecraft.thePlayer);
        }
    }
}
