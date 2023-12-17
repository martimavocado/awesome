package at.martimavocado.awesome.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class EmojiReplacerConfig {
    @Expose
    @ConfigOption(name = "MVP++", desc = "")
    @ConfigEditorBoolean
    public boolean mvp = true;

    @Expose
    @ConfigOption(name = "5 Gifted Ranks", desc = "")
    @ConfigEditorBoolean
    public boolean five = true;

    @Expose
    @ConfigOption(name = "20 Gifted Ranks", desc = "")
    @ConfigEditorBoolean
    public boolean twenty = true;

    @Expose
    @ConfigOption(name = "50 Gifted Ranks", desc = "")
    @ConfigEditorBoolean
    public boolean fifty = true;

    @Expose
    @ConfigOption(name = "100 Gifted Ranks", desc = "")
    @ConfigEditorBoolean
    public boolean hundred = true;

    @Expose
    @ConfigOption(name = "200 Gifted Ranks", desc = "")
    @ConfigEditorBoolean
    public boolean twoHundred = true;
}
