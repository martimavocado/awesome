package at.martimavocado.awesome.config.categories;

import at.martimavocado.awesome.awesome;
import com.google.gson.annotations.Expose;
import io.github.moulberry.moulconfig.Config;
import io.github.moulberry.moulconfig.annotations.Category;

public class ExampleModConfig extends Config {

    @Override
    public String getTitle() {
        return "awesome " + awesome.getVersion() + " by §cmartimavocado§r, config by §5Moulberry §rand §5nea89";
    }

    @Override
    public void saveNow() {
        awesome.configManager.save();
    }

    @Expose
    @Category(name = "chatter", desc = "this is where we chat.")
    public chatter chatter = new chatter();

    @Expose
    @Category(name = "Second Category", desc = "This is another category.")
    public SecondCategory secondCategory = new SecondCategory();
}
