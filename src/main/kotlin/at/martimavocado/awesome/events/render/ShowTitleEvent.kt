package at.martimavocado.awesome.events.render

import net.minecraftforge.fml.common.eventhandler.Event

class ShowTitleEvent (val title: String, val subtitle: String) : Event()