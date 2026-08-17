package dev.nutellim.MyPlaceholders.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

class CommandListenerTest {

    private CommandListener listener;
    private MockedStatic<Bukkit> bukkitMock;
    private MockedStatic<PlaceholderAPI> placeholderApiMock;

    @BeforeEach
    void setUp() {
        listener = new CommandListener();
        bukkitMock = mockStatic(Bukkit.class);
        placeholderApiMock = mockStatic(PlaceholderAPI.class);
    }

    @AfterEach
    void tearDown() {
        bukkitMock.close();
        placeholderApiMock.close();
    }

    // --- PlayerCommandPreprocessEvent -----------------------------------------------------

    @Test
    void playerCommand_resolvesUsingTheSenderNotAMentionedPlayer() {
        Player sender = mock(Player.class);
        Player mentioned = mock(Player.class); // e.g. an admin whose name the attacker types to fake a match

        PlayerCommandPreprocessEvent event = mock(PlayerCommandPreprocessEvent.class);
        when(event.getMessage()).thenReturn("/op Attacker %mp_x%");
        when(event.getPlayer()).thenReturn(sender);

        placeholderApiMock.when(() -> PlaceholderAPI.setPlaceholders(sender, "op Attacker %mp_x%"))
                .thenReturn("op Attacker RESOLVED");

        listener.onPlayerCommand(event);

        // Resolution must be scoped to whoever actually typed the command...
        placeholderApiMock.verify(() -> PlaceholderAPI.setPlaceholders(sender, "op Attacker %mp_x%"));
        // ...never to some other player just because their name appears in the text.
        placeholderApiMock.verify(() -> PlaceholderAPI.setPlaceholders(mentioned, "op Attacker %mp_x%"), never());

        verify(event).setMessage("/op Attacker RESOLVED");
    }

    @Test
    void playerCommand_neverEscalatesToConsole() {
        // Regression test for the privilege-escalation bug: a low-privilege player
        // must not be able to get their command executed as the console sender.
        Player sender = mock(Player.class);

        PlayerCommandPreprocessEvent event = mock(PlayerCommandPreprocessEvent.class);
        when(event.getMessage()).thenReturn("/op Attacker %mp_x%");
        when(event.getPlayer()).thenReturn(sender);

        placeholderApiMock.when(() -> PlaceholderAPI.setPlaceholders(any(Player.class), anyString()))
                .thenReturn("op Attacker %mp_x%");

        listener.onPlayerCommand(event);

        verify(event, never()).setCancelled(anyBoolean());
        bukkitMock.verify(() -> Bukkit.dispatchCommand(any(ConsoleCommandSender.class), anyString()), never());
    }

    @Test
    void playerCommand_withoutPlaceholder_isLeftUntouched() {
        PlayerCommandPreprocessEvent event = mock(PlayerCommandPreprocessEvent.class);
        when(event.getMessage()).thenReturn("/help");

        listener.onPlayerCommand(event);

        verify(event, never()).setMessage(anyString());
        placeholderApiMock.verifyNoInteractions();
    }

    @Test
    void playerCommand_skipsPapiPrefixedCommands() {
        PlayerCommandPreprocessEvent event = mock(PlayerCommandPreprocessEvent.class);
        when(event.getMessage()).thenReturn("/papi parse me %mp_x%");

        listener.onPlayerCommand(event);

        verify(event, never()).setMessage(anyString());
    }

    @Test
    void playerCommand_skipsItsOwnMpSubcommands() {
        PlayerCommandPreprocessEvent event = mock(PlayerCommandPreprocessEvent.class);
        when(event.getMessage()).thenReturn("/mp reload %mp_x%");

        listener.onPlayerCommand(event);

        verify(event, never()).setMessage(anyString());
    }

    // --- ServerCommandEvent -----------------------------------------------------------------

    @Test
    void serverCommand_resolvesRelativeToAMentionedPlayer_andStaysOnConsole() {
        Player steve = mock(Player.class);

        ServerCommandEvent event = mock(ServerCommandEvent.class);
        when(event.getCommand()).thenReturn("give Steve %mp_bonus%");

        bukkitMock.when(() -> Bukkit.getPlayer("give")).thenReturn(null);
        bukkitMock.when(() -> Bukkit.getPlayer("Steve")).thenReturn(steve);
        placeholderApiMock.when(() -> PlaceholderAPI.setPlaceholders(steve, "give Steve %mp_bonus%"))
                .thenReturn("give Steve 100");

        listener.onServerCommand(event);

        verify(event).setCommand("give Steve 100");
        bukkitMock.verify(() -> Bukkit.dispatchCommand(any(ConsoleCommandSender.class), anyString()), never());
    }

    @Test
    void serverCommand_withNoMatchingPlayer_isLeftUntouched() {
        ServerCommandEvent event = mock(ServerCommandEvent.class);
        when(event.getCommand()).thenReturn("give Ghost %mp_bonus%");

        bukkitMock.when(() -> Bukkit.getPlayer(anyString())).thenReturn(null);

        listener.onServerCommand(event);

        verify(event, never()).setCommand(anyString());
    }
}
