package com.example.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.Accordion;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class SecondCategory {


    @ConfigOption(name = "Chat Category", desc = "A sub-category for chat features")
    @Accordion
    @Expose
    public ChatSubCategory chat = new ChatSubCategory();

    public static class ChatSubCategory {
        @Expose
        @ConfigOption(name = "Duplicate Chat", desc = "Duplicate all chat messages.")
        @ConfigEditorBoolean
        public boolean duplicateChatMessage = false;
    }
}
