package com.example.config.categories;

import com.example.ExampleMod;
import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.Config;
import io.github.moulberry.moulconfig.annotations.Category;

public class ExampleModConfig extends Config {

    @Override
    public String getTitle() {
        return "Example Mod " + ExampleMod.getVersion() + " by §channibal2§r, config by §5Moulberry §rand §5nea89";
    }

    @Override
    public void saveNow() {
        ExampleMod.configManager.save();
    }

    @Expose
    @Category(name = "First Category", desc = "This is the first category.")
    public FirstCategory firstCategory = new FirstCategory();

    @Expose
    @Category(name = "Second Category", desc = "This is another category.")
    public SecondCategory secondCategory = new SecondCategory();
}
