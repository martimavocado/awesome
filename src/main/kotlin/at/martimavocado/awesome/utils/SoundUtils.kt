package at.martimavocado.awesome.utils

object SoundUtils {
    fun playSound(
        sound: String,
        volume: Float,
        pitch: Float,
    ) {
        val player = PlayerUtils.getPlayer() ?: return

        player.playSound(sound, volume, pitch)
    }
}
