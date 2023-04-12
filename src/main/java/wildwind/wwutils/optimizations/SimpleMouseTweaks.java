package wildwind.wwutils.optimizations;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.HashMap;

public class SimpleMouseTweaks {
    private final HashMap<Integer, Long> moved = new HashMap<>();

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
            final int slotId = currentSlot.slotNumber;
            if (moved.containsKey(slotId) && moved.get(slotId) + 400 > System.currentTimeMillis()) {
                return;
            }
            minecraft.playerController.windowClick(guiContainer.inventorySlots.windowId,
                    slotId, 0, 1, minecraft.thePlayer);
            moved.put(slotId, System.currentTimeMillis());
        }
    }
}
