package at.martimavocado.awesome.config.categories.sheepwars;

import at.martimavocado.awesome.features.sheepwars.SheepWarsPowerUp;
import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorColour;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

import java.util.ArrayList;
import java.util.List;

public class MagicWoolPerk {
    @Expose
    @ConfigOption(name = "Show GUI", desc = "Shows a GUI with the current wool's perk")
    @ConfigEditorBoolean
    public boolean showGUI = true;

    @Expose
    @ConfigOption(name = "Ping on shoot", desc = "Pings when the wool has a good perk")
    @ConfigEditorBoolean
    public boolean shootPing = false;

    @Expose
    @ConfigOption(name = "Good Perks", desc = "Makes the highlight be the same color as the current wool")
    @ConfigEditorBoolean
    public List<SheepWarsPowerUp> goodPerks = new ArrayList<>();

    @Expose
    @ConfigOption(name = "Color", desc = "Makes the highlight be a specific color\n" + "Requires Color Match to be disabled.")
    @ConfigEditorColour
    public String color = "0:0:0:0";
}
