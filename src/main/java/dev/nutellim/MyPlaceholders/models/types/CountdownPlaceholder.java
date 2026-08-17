package dev.nutellim.MyPlaceholders.models.types;

import dev.nutellim.MyPlaceholders.models.Placeholder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.TimeUnit;

public class CountdownPlaceholder extends Placeholder {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final long targetMillis;
    private final boolean validTarget;
    private final String format;
    private final String expiredMessage;

    public CountdownPlaceholder(String id, ConfigurationSection section) {
        super(id, section);
        this.format = section.getString("format", "%days%d %hours%h %minutes%m %seconds%s");
        this.expiredMessage = section.getString("expired", "Expired");

        String targetStr = section.getString("target", "");
        long parsedMillis;
        boolean parsedOk;
        try {
            LocalDateTime target = LocalDateTime.parse(targetStr, FORMATTER);
            parsedMillis = target.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            parsedOk = true;
        } catch (DateTimeParseException e) {
            parsedMillis = 0;
            parsedOk = false;
        }
        this.targetMillis = parsedMillis;
        this.validTarget = parsedOk;
    }

    @Override
    public String process(Player player) {
        if (!validTarget) return applyDecorations("Invalid countdown target for '" + id + "'");

        long remaining = targetMillis - System.currentTimeMillis();
        if (remaining <= 0) return applyDecorations(expiredMessage);

        long days    = TimeUnit.MILLISECONDS.toDays(remaining);
        long hours   = TimeUnit.MILLISECONDS.toHours(remaining) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remaining) % 60;

        String result = format
                .replace("%days%", String.valueOf(days))
                .replace("%hours%", String.valueOf(hours))
                .replace("%minutes%", String.valueOf(minutes))
                .replace("%seconds%", String.valueOf(seconds));

        return applyDecorations(result);
    }
}
