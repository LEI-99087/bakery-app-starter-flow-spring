package com.vaadin.starter.bakery.ui.views;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;

/**
 * Interface for views that require confirmation dialog functionality.
 * Provides methods to set and get confirmation dialogs for user interactions.
 */
public interface HasConfirmation {

    /**
     * Sets the confirmation dialog for the view.
     *
     * @param confirmDialog the ConfirmDialog instance to use for confirmations
     */
    void setConfirmDialog(ConfirmDialog confirmDialog);

    /**
     * Gets the confirmation dialog used by the view.
     *
     * @return the ConfirmDialog instance
     */
    ConfirmDialog getConfirmDialog();
}