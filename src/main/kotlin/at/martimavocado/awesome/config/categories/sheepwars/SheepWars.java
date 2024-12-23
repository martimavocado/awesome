package at.martimavocado.awesome.config.categories.sheepwars;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.Category;

public class SheepWars {
    @Expose
    @Category(name = "Magic Wool Highlighter", desc = "")
    public MagicWoolHighlight magicWool = new MagicWoolHighlight();
}
