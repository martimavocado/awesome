package at.martimavocado.awesome.events.chat

import net.minecraft.util.IChatComponent
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
open class PlayerChatEvent(
    val message: String,
    val author: String,
    var chatComponent: IChatComponent,
): Event() {
    @Cancelable
    open class Normal(
        message: String,
        author: String,
        chatComponent: IChatComponent
    ) : PlayerChatEvent(message, author, chatComponent)
    @Cancelable
    open class Party(
        message: String,
        author: String,
        chatComponent: IChatComponent
    ) : PlayerChatEvent(message, author, chatComponent)
    @Cancelable
    open class Guild(
        message: String,
        author: String,
        chatComponent: IChatComponent
    ) : PlayerChatEvent(message, author, chatComponent)
    @Cancelable
    open class Officer(
        message: String,
        author: String,
        chatComponent: IChatComponent
    ) : PlayerChatEvent(message, author, chatComponent)
    @Cancelable
    open class SkyblockCoop(
        message: String,
        author: String,
        chatComponent: IChatComponent
    ) : PlayerChatEvent(message, author, chatComponent)
    @Cancelable
    open class DirectMessage(
        message: String,
        author: String,
        chatComponent: IChatComponent,
        isSending: Boolean
    ) : PlayerChatEvent(message, author, chatComponent)
}