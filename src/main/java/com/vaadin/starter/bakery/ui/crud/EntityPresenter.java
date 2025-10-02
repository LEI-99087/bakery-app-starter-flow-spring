package com.vaadin.starter.bakery.ui.crud;

import java.util.function.UnaryOperator;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.data.entity.util.EntityUtil;
import com.vaadin.starter.bakery.backend.service.CrudService;
import com.vaadin.starter.bakery.backend.service.UserFriendlyDataException;
import com.vaadin.starter.bakery.ui.utils.messages.CrudErrorMessage;
import com.vaadin.starter.bakery.ui.utils.messages.Message;
import com.vaadin.starter.bakery.ui.views.EntityView;

/**
 * Presenter class for managing entity operations in the UI.
 * Handles business logic for CRUD operations, validation, and user interactions
 * for entity views.
 *
 * @param <T> the type of entity this presenter handles, must extend AbstractEntity
 * @param <V> the type of view this presenter manages, must extend EntityView
 */
public class EntityPresenter<T extends AbstractEntity, V extends EntityView<T>>
        implements HasLogger {

    private CrudService<T> crudService;

    private CurrentUser currentUser;

    private V view;

    private EntityPresenterState<T> state = new EntityPresenterState<T>();

    /**
     * Constructs a new EntityPresenter with the specified dependencies.
     *
     * @param crudService the service used for entity operations
     * @param currentUser the currently authenticated user
     */
    public EntityPresenter(
            CrudService<T> crudService, CurrentUser currentUser) {
        this.crudService = crudService;
        this.currentUser = currentUser;
    }

    /**
     * Sets the view that this presenter manages.
     *
     * @param view the view instance
     */
    public void setView(V view) {
        this.view = view;
    }

    /**
     * Gets the view managed by this presenter.
     *
     * @return the view instance
     */
    public V getView() {
        return view;
    }

    /**
     * Deletes the current entity after confirmation if necessary.
     *
     * @param onSuccess callback to execute after successful deletion
     */
    public void delete(CrudOperationListener<T> onSuccess) {
        Message CONFIRM_DELETE = Message.CONFIRM_DELETE.createMessage();
        confirmIfNecessaryAndExecute(true, CONFIRM_DELETE, () -> {
            if (executeOperation(() -> crudService.delete(currentUser.getUser(),
                    state.getEntity()))) {
                onSuccess.execute(state.getEntity());
            }
        }, () -> {
        });
    }

    /**
     * Saves the current entity.
     *
     * @param onSuccess callback to execute after successful save
     */
    public void save(CrudOperationListener<T> onSuccess) {
        if (executeOperation(() -> saveEntity())) {
            onSuccess.execute(state.getEntity());
        }
    }

    /**
     * Executes an update operation on the current entity.
     *
     * @param updater function that takes the current entity and returns the updated entity
     * @return true if the update was successful, false otherwise
     */
    public boolean executeUpdate(UnaryOperator<T> updater) {
        return executeOperation(() -> {
            state.updateEntity(updater.apply(getEntity()), isNew());
        });
    }

    /**
     * Executes a CRUD operation and handles any exceptions that may occur.
     *
     * @param operation the operation to execute
     * @return true if the operation completed successfully, false otherwise
     */
    private boolean executeOperation(Runnable operation) {
        try {
            operation.run();
            return true;
        }
        catch (UserFriendlyDataException e) {
            // Commit failed because of application-level data constraints
            consumeError(e, e.getMessage(), true);
        }
        catch (DataIntegrityViolationException e) {
            // Commit failed because of validation errors
            consumeError(
                    e, CrudErrorMessage.OPERATION_PREVENTED_BY_REFERENCES, true);
        }
        catch (OptimisticLockingFailureException e) {
            consumeError(e, CrudErrorMessage.CONCURRENT_UPDATE, true);
        }
        catch (EntityNotFoundException e) {
            consumeError(e, CrudErrorMessage.ENTITY_NOT_FOUND, false);
        }
        catch (ConstraintViolationException e) {
            consumeError(e, CrudErrorMessage.REQUIRED_FIELDS_MISSING, false);
        }
        return false;
    }

    /**
     * Handles errors by logging them and showing user-friendly error messages.
     *
     * @param e the exception that occurred
     * @param message the user-friendly error message to display
     * @param isPersistent whether the error message should be persistent
     */
    private void consumeError(
            Exception e, String message, boolean isPersistent) {
        getLogger().debug(message, e);
        view.showError(message, isPersistent);
    }

    /**
     * Saves the current entity using the CRUD service.
     */
    private void saveEntity() {
        state.updateEntity(
                crudService.save(currentUser.getUser(), state.getEntity()),
                isNew());
    }

    /**
     * Writes the entity data to the view.
     *
     * @return true if the write operation was successful, false otherwise
     */
    public boolean writeEntity() {
        try {
            view.write(state.getEntity());
            return true;
        }
        catch (ValidationException e) {
            view.showError(CrudErrorMessage.REQUIRED_FIELDS_MISSING, false);
            return false;
        }
        catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Closes the current entity view and clears the state.
     */
    public void close() {
        state.clear();
        view.clear();
    }

    /**
     * Cancels the current operation, with confirmation if there are unsaved changes.
     *
     * @param onConfirmed callback to execute if cancellation is confirmed
     * @param onCancelled callback to execute if cancellation is cancelled by user
     */
    public void cancel(Runnable onConfirmed, Runnable onCancelled) {
        confirmIfNecessaryAndExecute(
                view.isDirty(),
                Message.UNSAVED_CHANGES.createMessage(state.getEntityName()),
                () -> {
                    view.clear();
                    onConfirmed.run();
                }, onCancelled);
    }

    /**
     * Executes an operation with confirmation if necessary.
     *
     * @param needsConfirmation whether confirmation is required
     * @param message the confirmation message to display
     * @param onConfirmed callback to execute if operation is confirmed
     * @param onCancelled callback to execute if operation is cancelled
     */
    private void confirmIfNecessaryAndExecute(
            boolean needsConfirmation, Message message, Runnable onConfirmed,
            Runnable onCancelled) {
        if (needsConfirmation) {
            showConfirmationRequest(message, onConfirmed, onCancelled);
        }
        else {
            onConfirmed.run();
        }
    }

    /**
     * Shows a confirmation dialog to the user.
     *
     * @param message the message to display in the confirmation dialog
     * @param onOk callback to execute if user confirms
     * @param onCancel callback to execute if user cancels
     */
    private void showConfirmationRequest(
            Message message, Runnable onOk, Runnable onCancel) {
        view.getConfirmDialog().setText(message.getMessage());
        view.getConfirmDialog().setHeader(message.getCaption());
        view.getConfirmDialog().setCancelText(message.getCancelText());
        view.getConfirmDialog().setConfirmText(message.getOkText());
        view.getConfirmDialog().setOpened(true);

        final Registration okRegistration =
                view.getConfirmDialog().addConfirmListener(e -> onOk.run());
        final Registration cancelRegistration =
                view.getConfirmDialog().addCancelListener(e -> onCancel.run());
        state.updateRegistration(okRegistration, cancelRegistration);
    }

    /**
     * Loads an entity by ID.
     *
     * @param id the ID of the entity to load
     * @param onSuccess callback to execute when entity is successfully loaded
     * @return true if the entity was loaded successfully, false otherwise
     */
    public boolean loadEntity(Long id, CrudOperationListener<T> onSuccess) {
        return executeOperation(() -> {
            state.updateEntity(crudService.load(id), false);
            onSuccess.execute(state.getEntity());
        });
    }

    /**
     * Creates a new entity instance.
     *
     * @return the newly created entity
     */
    public T createNew() {
        state.updateEntity(crudService.createNew(currentUser.getUser()), true);
        return state.getEntity();
    }

    /**
     * Gets the current entity.
     *
     * @return the current entity
     */
    public T getEntity() {
        return state.getEntity();
    }

    /**
     * Checks if the current entity is new (not yet persisted).
     *
     * @return true if the entity is new, false if it exists in the database
     */
    public boolean isNew() {
        return state.isNew();
    }

    /**
     * Functional interface for CRUD operation listeners.
     *
     * @param <T> the type of entity
     */
    @FunctionalInterface
    public interface CrudOperationListener<T> {

        /**
         * Executes the operation with the given entity.
         *
         * @param entity the entity to operate on
         */
        void execute(T entity);
    }

}

/**
 * Holds the state variables for the EntityPresenter that can change during operations.
 *
 * @param <T> the type of entity
 */
class EntityPresenterState<T extends AbstractEntity> {

    private T entity;
    private String entityName;
    private Registration okRegistration;
    private Registration cancelRegistration;
    private boolean isNew = false;

    /**
     * Updates the current entity and its state.
     *
     * @param entity the entity to set as current
     * @param isNew whether the entity is new (not persisted)
     */
    void updateEntity(T entity, boolean isNew) {
        this.entity = entity;
        this.entityName = EntityUtil.getName(this.entity.getClass());
        this.isNew = isNew;
    }

    /**
     * Updates the dialog registration listeners.
     *
     * @param okRegistration registration for the OK button listener
     * @param cancelRegistration registration for the cancel button listener
     */
    void updateRegistration(
            Registration okRegistration, Registration cancelRegistration) {
        clearRegistration(this.okRegistration);
        clearRegistration(this.cancelRegistration);
        this.okRegistration = okRegistration;
        this.cancelRegistration = cancelRegistration;
    }

    /**
     * Clears the current state, removing registrations and resetting entity data.
     */
    void clear() {
        this.entity = null;
        this.entityName = null;
        this.isNew = false;
        updateRegistration(null, null);
    }

    /**
     * Clears a registration if it exists.
     *
     * @param registration the registration to clear
     */
    private void clearRegistration(Registration registration) {
        if (registration != null) {
            registration.remove();
        }
    }

    /**
     * Gets the current entity.
     *
     * @return the current entity
     */
    public T getEntity() {
        return entity;
    }

    /**
     * Gets the entity name.
     *
     * @return the entity name
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * Checks if the current entity is new.
     *
     * @return true if the entity is new, false otherwise
     */
    public boolean isNew() {
        return isNew;
    }

}