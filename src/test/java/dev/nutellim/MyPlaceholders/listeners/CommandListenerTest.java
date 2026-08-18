package dev.nutellim.MyPlaceholders.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.event.server.ServerCommandEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

class CommandListenerTest {

    private CommandListener listener;
    private MockedStatic<PlaceholderAPI> placeholderApiMock;

    @BeforeEach
    void setUp() {
        listener = new CommandListener();
        placeholderApiMock = mockStatic(PlaceholderAPI.class);
    }

    @AfterEach
    void tearDown() {
        placeholderApiMock.close();
    }

    // --- ServerCommandEvent -----------------------------------------------------------------
    // The listener only reacts to console-dispatched commands (e.g. other plugins calling
    // Bukkit.dispatchCommand(console, ...) with an admin-authored placeholder in their config).
    // It must never guess a player identity from the command text and must never touch
    // commands typed directly by a player (PlayerCommandPreprocessEvent is not intercepted).

    @Test
    void serverCommand_resolvesWithoutGuessingAPlayer() {
        ServerCommandEvent event = mock(ServerCommandEvent.class);
        when(event.getCommand()).thenReturn("give Steve %mp_bonus%");

        placeholderApiMock.when(() -> PlaceholderAPI.setPlaceholders(null, "give Steve %mp_bonus%"))
                .thenReturn("give Steve 100");

        listener.onServerCommand(event);

        // Resolution is always context-free: no player is ever looked up or matched.
        placeholderApiMock.verify(() -> PlaceholderAPI.setPlaceholders(null, "give Steve %mp_bonus%"));
        verify(event).setCommand("give Steve 100");
    }

    @Test
    void serverCommand_withoutPlaceholder_isLeftUntouched() {
        ServerCommandEvent event = mock(ServerCommandEvent.class);
        when(event.getCommand()).thenReturn("give Steve 100");

        listener.onServerCommand(event);

        verify(event, never()).setCommand(anyString());
        placeholderApiMock.verifyNoInteractions();
    }

    @Test
    void serverCommand_skipsPapiPrefixedCommands() {
        ServerCommandEvent event = mock(ServerCommandEvent.class);
        when(event.getCommand()).thenReturn("papi parse me %mp_x%");

        listener.onServerCommand(event);

        verify(event, never()).setCommand(anyString());
    }

    @Test
    void serverCommand_skipsItsOwnMpSubcommands() {
        ServerCommandEvent event = mock(ServerCommandEvent.class);
        when(event.getCommand()).thenReturn("mp reload %mp_x%");

        listener.onServerCommand(event);

        verify(event, never()).setCommand(anyString());
    }
}
