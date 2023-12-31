package at.martimavocado.awesome.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigEditorDropdown;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class EmojiReplacerConfig {

    @Expose
    @ConfigOption(name = "Enabled", desc = "")
    @ConfigEditorBoolean
    public boolean enabled = true;

    @Expose
    @ConfigOption(name = "MVP++", desc = "")
    @ConfigEditorBoolean
    public boolean mvp = true;

    @Expose
    @ConfigOption(name = "Gifted Ranks", desc = "Determines which emojis will be replaced. " +
            "For the best results, choose the amount of gifted ranks you have.")
    @ConfigEditorDropdown()
    public emojiRanksGifted giftedRanks = emojiRanksGifted.ZERO;

    public enum emojiRanksGifted {
        ZERO("None"),
        FIVE("5 Gifted Ranks"),
        TWENTY("20 Gifted Ranks"),
        FIFTY("50 Gifted Ranks"),
        ONE_HUNDRED("100 Gifted Ranks"),
        TWO_HUNDRED("200 Gifted Ranks");

        private final String str;

        emojiRanksGifted(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return str;
        }
    }
}
