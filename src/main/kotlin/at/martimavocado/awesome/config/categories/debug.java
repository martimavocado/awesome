package at.martimavocado.awesome.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class debug {
    @Expose
    @ConfigOption(name = "raw messages", desc = "prints a copy of raw messages to stdout")
    @ConfigEditorBoolean
    public boolean rawMessages = false;

    @Expose
    @ConfigOption(name = "only chat", desc = "makes it so raw messages only works in chat")
    @ConfigEditorBoolean
    public boolean onlyChat = true;

    @Expose
    @ConfigOption(name = "command logger", desc = "logs commands sent")
    @ConfigEditorBoolean
    public boolean commandLogs = false;

    @Expose
    @ConfigOption(name = "replace color code thing with &", desc = "helps with debugging sometimes :shrug:")
    @ConfigEditorBoolean
    public boolean colorCodes = false;

    @Expose
    @ConfigOption(name = "substring color", desc = "i hate minecraft colro code")
    @ConfigEditorBoolean
    public boolean debugColors = false;

    @Expose
    @ConfigOption(name = "store titles", desc = "prints titles/subtitles to stdout")
    @ConfigEditorBoolean
    public boolean logTitles = false;


    @Expose
    @ConfigOption(name = "debug commands", desc = "shows messages that weren't commands")
    @ConfigEditorBoolean
    public boolean figureOutCommands = false;
}
