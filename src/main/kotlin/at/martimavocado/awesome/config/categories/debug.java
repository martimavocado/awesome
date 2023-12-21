package at.martimavocado.awesome.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class debug {
    @Expose
    @ConfigOption(name = "raw chat", desc = "prints a copy of raw messages to stdout")
    @ConfigEditorBoolean
    public boolean rawChat = false;

    @Expose
    @ConfigOption(name = "command logger", desc = "logs commands sent")
    @ConfigEditorBoolean
    public boolean commandLogs = false;

    @Expose
    @ConfigOption(name = "replace color code thing with &", desc = "helps with debugging sometimes :shrug:")
    @ConfigEditorBoolean
    public boolean colorCodes = false;
}
