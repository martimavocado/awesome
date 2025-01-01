package at.martimavocado.awesome.config.categories.sheepwars;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.Category;

public class SheepWarsConfig {
    @Expose
    @Category(name = "Magic Wool Highlighter", desc = "")
    public MagicWoolHighlight magicWoolHighlight = new MagicWoolHighlight();

    @Expose
    @Category(name = "Magic Wool Perks", desc = "")
    public MagicWoolPerk magicWoolPerk = new MagicWoolPerk();
}
