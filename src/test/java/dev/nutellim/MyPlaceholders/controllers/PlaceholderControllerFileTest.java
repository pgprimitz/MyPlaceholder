package dev.nutellim.MyPlaceholders.controllers;

import dev.nutellim.MyPlaceholders.MyPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.File;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Regression tests for the path-traversal fix in
 * {@link PlaceholderController#getPlaceholderFile(String)}.
 */
class PlaceholderControllerFileTest {

    @TempDir
    File dataFolder;

    private MockedStatic<Bukkit> bukkitMock;
    private PlaceholderController controller;

    @BeforeEach
    void setUp() {
        bukkitMock = mockStaticBukkit();

        MyPlaceholder plugin = mock(MyPlaceholder.class);
        when(plugin.getDataFolder()).thenReturn(dataFolder);
        when(plugin.getLogger()).thenReturn(Logger.getLogger("MyPlaceholderTest"));

        controller = new PlaceholderController(plugin);
    }

    @AfterEach
    void tearDown() {
        bukkitMock.close();
    }

    private MockedStatic<Bukkit> mockStaticBukkit() {
        MockedStatic<Bukkit> mocked = org.mockito.Mockito.mockStatic(Bukkit.class);
        Server server = mock(Server.class);
        BukkitScheduler scheduler = mock(BukkitScheduler.class);
        when(scheduler.runTaskTimerAsynchronously(any(Plugin.class), any(BukkitRunnable.class), anyLong(), anyLong()))
                .thenReturn(mock(BukkitTask.class));
        mocked.when(Bukkit::getServer).thenReturn(server);
        mocked.when(Bukkit::getScheduler).thenReturn(scheduler);
        return mocked;
    }

    @Test
    void resolvesNormalCategoryInsidePlaceholdersFolder() throws Exception {
        File file = controller.getPlaceholderFile("example");

        assertEquals(
                new File(new File(dataFolder, "placeholders"), "example.yml").getCanonicalFile(),
                file.getCanonicalFile()
        );
    }

    @Test
    void resolvesNestedCategoryInsidePlaceholdersFolder() throws Exception {
        File file = controller.getPlaceholderFile("group/example");

        assertEquals(
                new File(new File(dataFolder, "placeholders"), "group/example.yml").getCanonicalFile(),
                file.getCanonicalFile()
        );
    }

    @Test
    void rejectsPathTraversalAboveThePlaceholdersFolder() {
        File file = controller.getPlaceholderFile("../../../../etc/passwd");

        assertNull(file, "categoryId escaping the placeholders folder must be rejected");
    }

    @Test
    void rejectsPathTraversalTargetingAnotherPluginConfig() {
        File file = controller.getPlaceholderFile("../otherplugin/config");

        assertNull(file, "categoryId escaping the placeholders folder must be rejected");
    }
}
