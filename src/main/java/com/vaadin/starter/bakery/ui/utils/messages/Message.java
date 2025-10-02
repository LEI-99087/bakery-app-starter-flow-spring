package com.vaadin.starter.bakery.ui.utils.messages;

/**
 * Represents a message with caption, button texts, and content.
 * Used for confirmation dialogs and user notifications throughout the application.
 */
public class Message {

    /** Default caption for delete confirmation dialogs. */
    public static final String CONFIRM_CAPTION_DELETE = "Confirm Delete";

    /** Default message for delete confirmation dialogs. */
    public static final String CONFIRM_MESSAGE_DELETE = "Are you sure you want to delete the selected Item? This action cannot be undone.";

    /** Default text for delete confirmation button. */
    public static final String BUTTON_CAPTION_DELETE = "Delete";

    /** Default text for cancel button. */
    public static final String BUTTON_CAPTION_CANCEL = "Cancel";

    /** Message supplier for unsaved changes confirmation dialog. */
    public static final MessageSupplier UNSAVED_CHANGES = createMessage("Unsaved Changes", "Discard", "Continue Editing",
            "There are unsaved modifications to the %s. Discard changes?");

    /** Message supplier for delete confirmation dialog. */
    public static final MessageSupplier CONFIRM_DELETE = createMessage(CONFIRM_CAPTION_DELETE, BUTTON_CAPTION_DELETE,
            BUTTON_CAPTION_CANCEL, CONFIRM_MESSAGE_DELETE);

    private final String caption;
    private final String okText;
    private final String cancelText;
    private final String message;

    /**
     * Constructs a new Message with the specified properties.
     *
     * @param caption the dialog caption/title
     * @param okText the text for the confirm/ok button
     * @param cancelText the text for the cancel button
     * @param message the message content to display
     */
    public Message(String caption, String okText, String cancelText, String message) {
        this.caption = caption;
        this.okText = okText;
        this.cancelText = cancelText;
        this.message = message;
    }

    /**
     * Creates a MessageSupplier with the specified template properties.
     *
     * @param caption the dialog caption/title
     * @param okText the text for the confirm/ok button
     * @param cancelText the text for the cancel button
     * @param message the message template (can include format specifiers)
     * @return a MessageSupplier that creates Messages with the given template
     */
    private static MessageSupplier createMessage(String caption, String okText, String cancelText, String message) {
        return (parameters) -> new Message(caption, okText, cancelText, String.format(message, parameters));
    }

    /**
     * Gets the dialog caption/title.
     *
     * @return the caption text
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Gets the confirm/ok button text.
     *
     * @return the ok button text
     */
    public String getOkText() {
        return okText;
    }

    /**
     * Gets the cancel button text.
     *
     * @return the cancel button text
     */
    public String getCancelText() {
        return cancelText;
    }

    /**
     * Gets the message content.
     *
     * @return the message text
     */
    public String getMessage() {
        return message;
    }

    /**
     * Functional interface for creating parameterized messages.
     * Allows dynamic creation of messages with formatted content.
     */
    @FunctionalInterface
    public interface MessageSupplier {

        /**
         * Creates a Message with the specified parameters.
         *
         * @param parameters the parameters to format into the message template
         * @return a new Message instance with formatted content
         */
        Message createMessage(Object... parameters);
    }

}