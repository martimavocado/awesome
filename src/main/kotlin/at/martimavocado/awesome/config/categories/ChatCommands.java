package at.martimavocado.awesome.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class ChatCommands {
    @Expose
    @ConfigOption(name = "main toggle", desc = "this is the main toggle")
    @ConfigEditorBoolean
    public boolean enabled = true;

    @Expose
    @ConfigOption(name = "party warping", desc = "enables ?aw warp")
    @ConfigEditorBoolean
    public boolean warping = true;

    @Expose
    @ConfigOption(name = "party transfering", desc = "enables ?aw transfer")
    @ConfigEditorBoolean
    public boolean transfer = true;

    @Expose
    @ConfigOption(name = "chat", desc = "enables ?aw say something")
    @ConfigEditorBoolean
    public boolean say = true;

    @Expose
    @ConfigOption(name = "ban", desc = "enables being banned")
    @ConfigEditorBoolean
    public boolean ban = true;
}
