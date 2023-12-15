package at.martimavocado.awesome.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class debug {
    @Expose
    @ConfigOption(name = "raw chat", desc = "prints a copy of raw messages to stdout")
    @ConfigEditorBoolean
    public boolean rawChat = false;
}
