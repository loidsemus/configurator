package me.loidsemus.configurator.gui.input;

import me.loidsemus.configurator.messages.LangKey;
import me.loidsemus.configurator.messages.Messages;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;

import java.util.function.Consumer;

public class NumberPrompt extends NumericPrompt {

    private final Consumer<Number> consumer;

    public NumberPrompt(Consumer<Number> consumer) {
        this.consumer = consumer;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return Messages.get(LangKey.PROMPT_ENTER_NUMBER, true);
    }

    @Override
    protected String getFailedValidationText(ConversationContext context, String invalidInput) {
        return Messages.get(LangKey.PROMPT_FAILED_NUMBER_VALIDATION, true, invalidInput);
    }

    @Override
    protected String getInputNotNumericText(ConversationContext context, String invalidInput) {
        return Messages.get(LangKey.PROMPT_FAILED_NUMBER_VALIDATION, true, invalidInput);
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
        consumer.accept(input);
        return Prompt.END_OF_CONVERSATION;
    }
}
