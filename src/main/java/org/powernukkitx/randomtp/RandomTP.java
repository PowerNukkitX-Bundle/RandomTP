package org.powernukkitx.randomtp;

import org.powernukkitx.plugin.PluginBase;
import org.powernukkitx.plugin.annotation.PluginMeta;

@PluginMeta(
        name = "RandomTP",
        version = "1.0.0",
        authors = {
                "Buddelbubi"
        },
        api = {
                "3.0.0"
        },
        website = "https://github.com/PowerNukkitX/RandomTP"
)
public class RandomTP extends PluginBase {

    private static RandomTP INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
    }

    public static RandomTP get() {
        return INSTANCE;
    }
}