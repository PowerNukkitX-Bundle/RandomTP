package org.powernukkitx.randomtp;

import org.powernukkitx.Player;
import org.powernukkitx.command.CommandResult;
import org.powernukkitx.command.PluginCommand;
import org.powernukkitx.command.SenderType;
import org.powernukkitx.command.route.RouteTree;
import org.powernukkitx.level.Level;
import org.powernukkitx.level.Location;
import org.powernukkitx.level.Position;
import org.powernukkitx.level.format.IChunk;
import org.powernukkitx.level.generator.biome.BiomePicker;
import org.powernukkitx.plugin.annotation.CommandDefinition;
import org.powernukkitx.registry.BiomeRegistry;
import org.powernukkitx.registry.Registries;
import org.powernukkitx.tags.BiomeTags;
import org.powernukkitx.utils.Config;
import org.powernukkitx.utils.Utils;

import static org.powernukkitx.level.generator.stages.normal.NormalTerrainStage.SEA_LEVEL;
import static org.powernukkitx.randomtp.RTPListener.checkCooldown;

@CommandDefinition(
        name = "randompt",
        aliases = {"rtp"},
        permission = "randomtp.command"
)
public class RTPCommand extends PluginCommand<RandomTP> {
    public RTPCommand() {
        super("randomtp", RandomTP.get());
    }

    @Override
    protected void buildCommandTree(RouteTree tree) {
        tree.getRoot()
                .senderType(SenderType.PLAYER)
                .exec(commandContext -> {
                    Player player = commandContext.getSender().asPlayer();
                    if (!player.hasPermission("randomtp.bypasscooldown")) {
                        int cooldown = checkCooldown(player);
                        if (cooldown > 0) {
                            return CommandResult.fail(tr(player, "randomtp.error.cooldown", (cooldown / 20)));
                        }
                    }
                    Config config = RandomTP.get().getConfig();
                    var worlds = config.getStringList("worlds");
                    Level level = player.getLevel();
                    Position spawn = level.getSpawnLocation();
                    if(!worlds.isEmpty() && worlds.contains(level.getName())) {
                        return CommandResult.fail(tr(player, "randomtp.error.world_not_allowed"));
                    }
                    int min = config.getInt("radius.min");
                    int max = config.getInt("radius.max");
                    while (true) {
                        int rad = Utils.rand(min, max);
                        int vec = Utils.rand(0, 360);
                        double angle = Math.toRadians(vec);
                        int x = (int) (rad * Math.cos(angle));
                        int z = (int) (rad * Math.sin(angle));
                        int destX = (int) (x + spawn.x);
                        int destZ = (int) (z + spawn.z);
                        BiomePicker biomePicker = level.getBiomePicker();
                        if(biomePicker != null) {
                            int biomeId = biomePicker.pick(x, SEA_LEVEL, z).getBiomeId();
                            BiomeRegistry registry = Registries.BIOME;
                            if (registry.containsTag(BiomeTags.OCEAN, biomeId) ||
                                registry.containsTag(BiomeTags.RIVER, biomeId)) continue;
                        }
                        int chunkX = destX >> 4;
                        int chunkZ = destZ >> 4;
                        IChunk chunk = level.getOrGenerateChunk(chunkX, chunkZ);
                        int localX = destX - (chunkX << 4);
                        int localZ = destZ - (chunkZ << 4);
                        int y = chunk.getRainHeight(localX, localZ) + 1;
                        Location dest = new Location(x, y, z, level);
                        player.teleport(dest);
                        return CommandResult.success();
                    }
                });
    }

    private String tr(Player player, String key, Object... args) {
        return getPlugin().getI18n().tr(player.getLanguageCode(), key, args);
    }
}
