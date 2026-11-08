package org.powernukkitx.randomtp;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import org.powernukkitx.Player;
import org.powernukkitx.Server;
import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.player.PlayerQuitEvent;
import org.powernukkitx.plugin.annotation.EventListener;

@EventListener
public class RTPListener implements Listener {

    private final static Object2IntArrayMap<Player> cooldown = new Object2IntArrayMap<>();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        cooldown.remove(event.getPlayer());
    }

    public static int checkCooldown(Player player) {
        int tick = Server.getInstance().getTick();
        if (cooldown.containsKey(player)) {
            int lastRtp = cooldown.getInt(player);
            int delta = tick - lastRtp;
            int minDelta = RandomTP.get().getConfig().getInt("cooldown");
            if (delta < minDelta) return minDelta - delta;
        }
        cooldown.put(player, tick);
        return 0;
    }

}
