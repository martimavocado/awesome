package at.martimavocado.awesome.mixins.transformers;

import at.martimavocado.awesome.events.entity.DataWatcherUpdatedEvent;
import java.util.List;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DataWatcher.class)
public class MixinDataWatcher {
    @Shadow
    @Final
    private Entity owner;

    @Inject(method = "updateWatchedObjectsFromList", at = @At("TAIL"))
    public void onWhatever(List<DataWatcher.WatchableObject> list, CallbackInfo ci) {
        Event event = new DataWatcherUpdatedEvent(owner, list);

        MinecraftForge.EVENT_BUS.post(event);
    }
}

