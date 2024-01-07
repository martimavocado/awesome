package at.martimavocado.awesome.features

import at.martimavocado.awesome.utils.OtherUtils
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText

object FakeBan {
    fun showBanScreen(banID: String = "34528287", reason: String = "Cheating through the use of unfair game advantages") {
            val component = ChatComponentText("§cYou are temporarily banned for §f29d 23h 59m 59s§c from this server!")
            component.appendText("\n")
            component.appendText("\n§7Reason: §f$reason")
            component.appendText("\n§7Find out more: §b§nhttps://www.hypixel.net/appeal")
            component.appendText("\n")
            component.appendText("\n§7Ban ID: §7#$banID") //i would generate a random one but
            component.appendText("\n§7Sharing your Ban ID may affect the processing of your appeal!")
            Minecraft.getMinecraft().netHandler.networkManager.closeChannel(component)
    }
}