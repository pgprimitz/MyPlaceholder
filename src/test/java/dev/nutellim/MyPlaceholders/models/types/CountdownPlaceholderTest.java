package dev.nutellim.MyPlaceholders.models.types;

import org.bukkit.configuration.ConfigurationSection;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CountdownPlaceholderTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ConfigurationSection section(String target, String format, String expired) {
        ConfigurationSection section = mock(ConfigurationSection.class);
        when(section.getString("value")).thenReturn(null);
        when(section.getBoolean("bold", false)).thenReturn(false);
        when(section.getBoolean("italic", false)).thenReturn(false);
        when(section.getBoolean("underline", false)).thenReturn(false);
        when(section.getBoolean("strikethrough", false)).thenReturn(false);
        when(section.getBoolean("obfuscate", false)).thenReturn(false);
        when(section.getBoolean("locked", false)).thenReturn(false);
        when(section.getString("target", "")).thenReturn(target);
        when(section.getString("format", "%days%d %hours%h %minutes%m %seconds%s")).thenReturn(format);
        when(section.getString("expired", "Expired")).thenReturn(expired);
        return section;
    }

    @Test
    void formatsRemainingTimeForAFutureTarget() {
        // +30s padding avoids flakiness: parsing drops sub-second precision from "now",
        // which would otherwise occasionally floor the minutes field down by one.
        LocalDateTime target = LocalDateTime.now().plusDays(2).plusHours(3).plusMinutes(4).plusSeconds(30);
        ConfigurationSection section = section(target.format(FORMATTER), "%days%d %hours%h %minutes%m", "Expired");

        CountdownPlaceholder placeholder = new CountdownPlaceholder("test", section);
        String result = placeholder.process(null);

        assertEquals("2d 3h 4m", result);
    }

    @Test
    void showsExpiredMessageOncePastTheTarget() {
        LocalDateTime target = LocalDateTime.now().minusMinutes(5);
        ConfigurationSection section = section(target.format(FORMATTER), "%days%d %hours%h", "Event finished!");

        CountdownPlaceholder placeholder = new CountdownPlaceholder("test", section);

        assertEquals("Event finished!", placeholder.process(null));
    }

    @Test
    void reportsAnInvalidTargetInsteadOfCrashing() {
        ConfigurationSection section = section("not-a-date", "%days%d", "Expired");

        CountdownPlaceholder placeholder = new CountdownPlaceholder("xmas", section);
        String result = placeholder.process(null);

        assertTrue(result.contains("Invalid countdown target"));
        assertTrue(result.contains("xmas"));
    }
}
