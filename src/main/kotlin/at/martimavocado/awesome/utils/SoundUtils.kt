package at.martimavocado.awesome.utils

object SoundUtils {
    fun playSound(
        sound: String,
        volume: Float = 1f,
        pitch: Float = 1f,
    ) {
        val player = PlayerUtils.getPlayer() ?: return

        player.playSound(sound, volume, pitch)
    }

    fun playDing() = playSound("note.pling", 1f, 1f)
}
