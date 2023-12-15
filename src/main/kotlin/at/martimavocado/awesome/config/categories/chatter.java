package at.martimavocado.awesome.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class chatter {

    @Expose
    @ConfigOption(name = "Recolor Emojis", desc = "Recolors colorless emojis\n" +
        "Example: §r§f❤ §r§6➜§r §r§c❤§r")
    @ConfigEditorBoolean
    public boolean colorEmoji = true;

    @Expose
    @ConfigOption(name = "Shorten Channel Names", desc = "Shortens channel names\n" +
        "Example: §9Party §8> §r§6➜§r §9P §8>")
    @ConfigEditorBoolean
    public boolean shortChannels = false;
}
