package me.loidsemus.configurator.gui.input;

import me.loidsemus.configurator.messages.LangKey;
import me.loidsemus.configurator.messages.Messages;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

import java.util.function.Consumer;

public class TextPrompt extends org.bukkit.conversations.StringPrompt {

    private final Consumer<String> consumer;

    public TextPrompt(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return Messages.get(LangKey.PROMPT_ENTER_TEXT, true);
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        consumer.accept(input);
        return Prompt.END_OF_CONVERSATION;
    }


}
