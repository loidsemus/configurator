package me.loidsemus.configurator.gui.input;

import com.github.stefvanschie.inventoryframework.Gui;
import me.loidsemus.configurator.messages.LangKey;
import me.loidsemus.pluginlib.Messages;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;

/**
 * Cancels the input if message equals !c, and opens a gui if not null
 */
public class MenuConversationCanceller implements ConversationCanceller {

    private final Gui gui;

    public MenuConversationCanceller(Gui gui) {
        this.gui = gui;
    }

    @Override
    public boolean cancelBasedOnInput(ConversationContext context, String input) {
        if (!input.equalsIgnoreCase("!c")) {
            return false;
        }

        Player player = (Player) context.getForWhom();

        if (gui != null) {
            gui.show(player);
        }

        player.sendMessage(Messages.get(LangKey.PROMPT_CANCELED, true));
        return true;
    }

    @Override
    public void setConversation(Conversation conversation) {}

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public ConversationCanceller clone() {
        return new MenuConversationCanceller(gui);
    }
}
