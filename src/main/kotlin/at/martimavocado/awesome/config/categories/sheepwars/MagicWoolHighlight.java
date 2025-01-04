package at.martimavocado.awesome.config.categories.sheepwars;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorColour;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class MagicWoolHighlight {
    @Expose
    @ConfigOption(name = "Enabled", desc = "Highlights Magic Wool spawns")
    @ConfigEditorBoolean
    public boolean enabled = true;

    @Expose
    @ConfigOption(name = "Beacon", desc = "Adds a beacon effect")
    @ConfigEditorBoolean
    public boolean beacon = true;

    @Expose
    @ConfigOption(name = "Color Match", desc = "Makes the highlight be the same color as the current wool")
    @ConfigEditorBoolean
    public boolean colorMatch = true;

    @Expose
    @ConfigOption(name = "Color", desc = "Makes the highlight be a specific color\n" + "Requires Color Match to be disabled.")
    @ConfigEditorColour
    public String color = "0:0:0:0";
}
