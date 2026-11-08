package org.powernukkitx.randomtp;

import org.powernukkitx.permission.Permission;
import org.powernukkitx.lang.PluginI18n;
import org.powernukkitx.lang.PluginI18nManager;
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
    private PluginI18n i18n;

    public void onEnable() {
        INSTANCE = this;
        getServer().getPluginManager().addPermission(new Permission("randomtp.admin", "Allow the usage of the rtp command", Permission.DEFAULT_TRUE));
        this.saveDefaultConfig();
        this.i18n = PluginI18nManager.register(this);
    }

    public static RandomTP get() {
        return INSTANCE;
    }

    public PluginI18n getI18n() {
        return i18n;
    }
}
