package com.example.config.categories;

import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.annotations.ConfigEditorBoolean;
import io.github.moulberry.moulconfig.annotations.ConfigOption;

public class FirstCategory {

    @Expose
    @ConfigOption(name = "First Toggle", desc = "Enable this toggle to activate a feature.")
    @ConfigEditorBoolean
    public boolean firstToggle = false;

    @Expose
    @ConfigOption(name = "Second Toggle", desc = "Enable this toggle to activate a different feature.")
    @ConfigEditorBoolean
    public boolean secondToggle = false;
}
