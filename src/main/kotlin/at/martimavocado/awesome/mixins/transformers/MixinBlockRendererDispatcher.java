package at.martimavocado.awesome.mixins.transformers;

import at.martimavocado.awesome.features.sheepwars.EarthquakeEffectHider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockRendererDispatcher.class)
public abstract class MixinBlockRendererDispatcher {

    @Inject(method = "renderBlockDamage", at = @At("HEAD"), cancellable = true)
    private void renderBlockDamage_inject(IBlockState state, BlockPos pos, TextureAtlasSprite texture, IBlockAccess blockAccess, CallbackInfo ci) {
        boolean bl = EarthquakeEffectHider.INSTANCE.shouldCancelOverlay(pos);

        if (bl) ci.cancel();
    }
}
