package com.vaadin.starter.bakery.ui.views;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.starter.bakery.ui.utils.BakeryConst;

/**
 * Interface for views showing notifications to users.
 * Provides methods to display both transient and persistent notifications.
 */
public interface HasNotifications extends HasElement {

    /**
     * Shows a transient notification with the specified message.
     * The notification will automatically disappear after a short duration.
     *
     * @param message the message to display in the notification
     */
    default void showNotification(String message) {
        showNotification(message, false);
    }

    /**
     * Shows a notification with the specified message and persistence setting.
     *
     * @param message the message to display in the notification
     * @param persistent if true, the notification will remain visible until manually closed;
     *                   if false, it will automatically disappear after a short duration
     */
    default void showNotification(String message, boolean persistent) {
        if (persistent) {
            Button close = new Button("Close");
            close.getElement().setAttribute("theme", "tertiary small error");
            Notification notification = new Notification(new Text(message), close);
            notification.setPosition(Position.BOTTOM_START);
            notification.setDuration(0);
            close.addClickListener(event -> notification.close());
            notification.open();
        } else {
            Notification.show(message, BakeryConst.NOTIFICATION_DURATION, Position.BOTTOM_STRETCH);
        }
    }
}