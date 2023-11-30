package moe.nmkmn.jecon_leaderboard;

import jp.jyn.jecon.Jecon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public final class JeconLeaderboard extends JavaPlugin {
    private Jecon jecon;

    @Override
    public void onEnable() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Jecon");
        if (plugin == null || !plugin.isEnabled()) {
            getLogger().warning("Jecon is not available.");
        }

        this.jecon = (Jecon) plugin;

        new BukkitRunnable() {
            @Override
            public void run() {
                ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
                Scoreboard scoreboard = scoreboardManager.getMainScoreboard();
                Objective objective;

                if (scoreboard.getObjective("Jecon-Leaderboard") == null) {
                    objective = scoreboard.registerNewObjective("Jecon-Leaderboard", "dummy", "Jecon-Leaderboard");
                } else {
                    objective = scoreboard.getObjective("Jecon-Leaderboard");
                }

                for (Player player: Bukkit.getServer().getOnlinePlayers()) {
                    Optional<BigDecimal> value = jecon.getRepository().getDecimal(player.getUniqueId());

                    Score score = Objects.requireNonNull(objective).getScore(player.getName());
                    score.setScore(value.orElse(BigDecimal.valueOf(0.0)).intValue());
                }
            }
        }.runTaskTimer(this, 0, 5 * 60 * 20L);
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Disable");
    }
}
