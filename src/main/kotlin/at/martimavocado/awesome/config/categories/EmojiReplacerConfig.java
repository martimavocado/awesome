package at.martimavocado.awesome.config.categories;

import com.google.gson.annotations.Expose;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDropdown;
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption;

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
    @ConfigEditorDropdown
    public emojiRanksGifted giftedRanks = emojiRanksGifted.ZERO;

    public enum emojiRanksGifted {
        ZERO("None", 0),
        FIVE("5 Gifted Ranks", 5),
        TWENTY("20 Gifted Ranks", 20),
        FIFTY("50 Gifted Ranks", 50),
        ONE_HUNDRED("100 Gifted Ranks", 100),
        TWO_HUNDRED("200 Gifted Ranks", 200),
        ;

        private final String str;
        private final int amount;

        emojiRanksGifted(String str, int amount) {
            this.str = str;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return str;
        }

        public int getGifts() { return amount; }
    }
}
