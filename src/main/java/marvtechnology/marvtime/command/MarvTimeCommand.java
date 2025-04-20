package marvtechnology.marvtime.command;

import marvtechnology.marvtime.Marvtime;
import marvtechnology.marvtime.lang.LanguageManager;
import marvtechnology.marvtime.data.StorageProvider;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MarvTimeCommand implements CommandExecutor {

    private final World world;

    public MarvTimeCommand(World world) {
        this.world = world;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command is for players only.");
            return true;
        }

        if (!player.hasPermission("marvtime.view")) {
            player.sendMessage("§cYou do not have permission.");
            return true;
        }

        long ticks = world.getFullTime();
        long totalDays = ticks / 24000;
        long year = totalDays / 365 + 1;
        long remainingDays = totalDays % 365;
        long month = remainingDays / 30 + 1;
        long day = remainingDays % 30 + 1;
        long timeTicks = world.getTime();
        long hour = (timeTicks / 1000 + 6) % 24;
        long minute = (timeTicks % 1000) * 60 / 1000;

        Map<String, String> placeholders = createPlaceholders(year, month, day, hour, minute);
        String msg = LanguageManager.get(player, "current-time-message", placeholders);
        player.sendMessage(msg);

        StorageProvider storage = Marvtime.getStorage();
        storage.saveTime(year, totalDays);

        return true;
    }

    private Map<String, String> createPlaceholders(long year, long month, long day, long hour, long minute) {
        return Map.of(
                "year", String.valueOf(year),
                "month", String.valueOf(month),
                "day", String.valueOf(day),
                "hour", String.format("%02d", hour),
                "minute", String.format("%02d", minute)
        );
    }
}

