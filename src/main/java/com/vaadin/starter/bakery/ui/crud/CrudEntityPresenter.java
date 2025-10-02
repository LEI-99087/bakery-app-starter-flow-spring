package com.vaadin.starter.bakery.ui.crud;

import com.vaadin.starter.bakery.app.HasLogger;
import com.vaadin.starter.bakery.app.security.CurrentUser;
import com.vaadin.starter.bakery.backend.data.entity.AbstractEntity;
import com.vaadin.starter.bakery.backend.service.CrudService;
import com.vaadin.starter.bakery.backend.service.UserFriendlyDataException;
import com.vaadin.starter.bakery.ui.utils.messages.CrudErrorMessage;
import com.vaadin.starter.bakery.ui.views.HasNotifications;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.function.Consumer;

/**
 * Presenter class for handling CRUD operations on entities.
 * Manages business logic for entity operations and handles various types of exceptions
 * with user-friendly error messages.
 *
 * @param <E> the type of entity this presenter handles, must extend AbstractEntity
 */
public class CrudEntityPresenter<E extends AbstractEntity>	implements HasLogger {

    private final CrudService<E> crudService;

    private final CurrentUser currentUser;

    private final HasNotifications view;

    /**
     * Constructs a new CrudEntityPresenter with the specified dependencies.
     *
     * @param crudService the service used for entity operations
     * @param currentUser the currently authenticated user
     * @param view the view that will display notifications
     */
    public CrudEntityPresenter(CrudService<E> crudService, CurrentUser currentUser, HasNotifications view) {
        this.crudService = crudService;
        this.currentUser = currentUser;
        this.view = view;
    }

    /**
     * Deletes an entity and handles the result with success or failure callbacks.
     *
     * @param entity the entity to delete
     * @param onSuccess callback invoked when deletion is successful
     * @param onFail callback invoked when deletion fails
     */
    public void delete(E entity, Consumer<E> onSuccess, Consumer<E> onFail) {
        if (executeOperation(() -> crudService.delete(currentUser.getUser(), entity))) {
            onSuccess.accept(entity);
        } else {
            onFail.accept(entity);
        }
    }

    /**
     * Saves an entity and handles the result with success or failure callbacks.
     *
     * @param entity the entity to save
     * @param onSuccess callback invoked when save is successful
     * @param onFail callback invoked when save fails
     */
    public void save(E entity, Consumer<E> onSuccess, Consumer<E> onFail) {
        if (executeOperation(() -> saveEntity(entity))) {
            onSuccess.accept(entity);
        } else {
            onFail.accept(entity);
        }
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
        } catch (UserFriendlyDataException e) {
            // Commit failed because of application-level data constraints
            consumeError(e, e.getMessage(), true);
        } catch (DataIntegrityViolationException e) {
            // Commit failed because of validation errors
            consumeError(
                    e, CrudErrorMessage.OPERATION_PREVENTED_BY_REFERENCES, true);
        } catch (OptimisticLockingFailureException e) {
            consumeError(e, CrudErrorMessage.CONCURRENT_UPDATE, true);
        } catch (EntityNotFoundException e) {
            consumeError(e, CrudErrorMessage.ENTITY_NOT_FOUND, false);
        } catch (ConstraintViolationException e) {
            consumeError(e, CrudErrorMessage.REQUIRED_FIELDS_MISSING, false);
        }
        return false;
    }

    /**
     * Handles errors by logging them and showing user-friendly notifications.
     *
     * @param e the exception that occurred
     * @param message the user-friendly error message to display
     * @param isPersistent whether the notification should be persistent
     */
    private void consumeError(Exception e, String message, boolean isPersistent) {
        getLogger().debug(message, e);
        view.showNotification(message, isPersistent);
    }

    /**
     * Saves an entity using the CRUD service.
     *
     * @param entity the entity to save
     */
    private void saveEntity(E entity) {
        crudService.save(currentUser.getUser(), entity);
    }

    /**
     * Loads an entity by ID and handles the result with a success callback.
     *
     * @param id the ID of the entity to load
     * @param onSuccess callback invoked when the entity is successfully loaded
     * @return true if the entity was loaded successfully, false otherwise
     */
    public boolean loadEntity(Long id, Consumer<E> onSuccess) {
        return executeOperation(() -> onSuccess.accept(crudService.load(id)));
    }
}