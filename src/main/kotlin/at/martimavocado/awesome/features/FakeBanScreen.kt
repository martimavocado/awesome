package at.martimavocado.awesome.features

import at.martimavocado.awesome.Awesome
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatComponentText
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent


class FakeBanScreen {
    @SubscribeEvent
    fun chatReceive(event: ClientChatReceivedEvent) {
        if (event.message.unformattedText.contains("?ban") && Awesome.config.ban) {
            if (checkIGN("Iciing")) {
                val component = ChatComponentText("§cYou are temporarily banned for §f29d 23h 59m 59s§c from this server!")
                component.appendText("\n")
                component.appendText("\n§7Reason: §fCheating through the use of unfair game advantages")
                component.appendText("\n§7Find out more: §b§nhttps://www.hypixel.net/appeal")
                component.appendText("\n")
                component.appendText("\n§7Ban ID: §7#34528287") //i would generate a random one but
                component.appendText("\n§7Sharing your Ban ID may affect the processing of your appeal!")
                Minecraft.getMinecraft().netHandler.networkManager.closeChannel(component)
                return
            }
        }
    }

    private fun checkIGN(string: String): Boolean {
        return Minecraft.getMinecraft().thePlayer.name == string
    }
}
