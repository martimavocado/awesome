package at.martimavocado.awesome.config

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.config.categories.AwesomeConfig
import io.github.notenoughupdates.moulconfig.gui.GuiScreenElementWrapper
import io.github.notenoughupdates.moulconfig.gui.MoulConfigEditor

object ConfigGuiManager {
    var editor: MoulConfigEditor<AwesomeConfig>? = null

    fun getEditorInstance() = editor ?: MoulConfigEditor(Awesome.configManager.processor).also { editor = it }

    fun openConfigGui(search: String? = null) {
        val editor = getEditorInstance()

        if (search != null) {
            editor.search(search)
        }

        Awesome.openScreen(GuiScreenElementWrapper(editor))
    }

    fun onCommand(args: Array<String>) {
        if (args.isNotEmpty()) {
            openConfigGui(args.joinToString(" "))
        } else {
            openConfigGui()
        }
    }
}
