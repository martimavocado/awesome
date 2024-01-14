package at.martimavocado.awesome.mixins.transformers;

import at.martimavocado.awesome.events.ClientMessageEvent;
import at.martimavocado.awesome.hooks.EntityPlayerSPHookKt;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {
    @Inject(method = "sendChatMessage", at = @At(value = "HEAD"), cancellable = true)
    private void sendChatMessage_inject(String message, CallbackInfo ci) {
        Event event = new ClientMessageEvent(message);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}